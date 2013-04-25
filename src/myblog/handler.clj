(ns myblog.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [myblog.model :as model]
            [myblog.rss :as rss]
            [myblog.view :as view]
            [ring.util.response :as resp]
            [noir.util.middleware :as noir]
            [noir.session :as session]
            ))

(defmacro forever [& body] 
  `(while true ~@body))

(defn pint [s-int]
  (Integer/parseInt s-int))


(defn show-article-list []
  (view/show-article-list (model/select-article)))

(defn show-article [id]
  (view/show-article (model/find-article (pint id))))



(defn edit-article [id]
  (view/edit-article (model/find-article (pint id))))


(defn delete-article [id]
  (model/delete-article (pint id))
  (resp/redirect "/articles"))


(defn update-article [id header content]
  (let [article {:id (pint id), :header header, :content content}]
    (model/update-article article)
    (view/show-article article)))


(defn show-new-article []
  (view/show-new-article))

(defn create-rss [rss]
  (if (< 0 (count(model/select-rss-chanel-url rss)))
    false
  (let[feed   (rss/parse-feed rss)]
    (model/insert-rss-chanel {:rss_url rss, :published-date (:published-date feed), :description (:description feed), :title (:title feed)})
    true)))

(defn create-article [article]
  (model/insert-rss-chanel article)
  (resp/redirect "/articles"))

(defn check-and-insert [rss]
  ( if (> (:cnt  (first (model/select-rss-dublicate-entry rss))) 0)
    false
    (model/insert-rss-entry rss)))

(defn update-entry [rss]
 (map (fn[x](map (fn[y](check-and-insert y)) (:entries (rss/parse-feed (:rss_url  x))))) (lazy-seq (rss))))


(defroutes app-routes
  
  (GET "/" [] (resp/redirect "/articles"))
  
  ;; Show articles list
  (GET "/articles" [] (show-article-list))

  ;; Show form for a new article
  (GET "/article/new" [] (show-new-article))

  ;; Create new article
  (POST "/article/create" req (create-article (:params req)))

  ;; Show article details
  (GET "/article/:id" [id] (show-article id))

  ;; Show form for editting article
  (GET "/article/edit/:id" [id] (edit-article id))

  ;; Update article
  (POST "/article/update/:id" [id header content] (update-article id header content))

  ;; Delete article
  (POST "/article/delete/:id" [id] (delete-article id))

  (route/resources "/") 
  (route/not-found "Not Found"))

(def app
  (->
   [(handler/site app-routes)]
   noir/app-handler
   noir/war-handler
   ))



(comment
  ;; Function for inspecting java objects
  (use 'clojure.pprint)
  (defn show-methods [obj]
    (-> obj .getClass .getMethods vec pprint))
  
  )


