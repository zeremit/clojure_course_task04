(ns myblog.model
  (:use [korma db core])
  (:require [clojure.xml :as xml])
  (:import  [java.io ByteArrayInputStream]))



(def default-conn {:classname "com.mysql.jdbc.Driver"
                   :subprotocol "mysql"
                   :user "root"
                   :password ""
                   :subname "//127.0.0.1:3306/rss?useUnicode=true&characterEncoding=utf8"
                   :delimiters "`"})

;; (def env (into {} (System/getenv)))

;; (def dbhost (get env "OPENSHIFT_MYSQL_DB_HOST"))
;; (def dbport (get env "OPENSHIFT_MYSQL_DB_PORT"))

;; (def default-conn {:classname "com.mysql.jdbc.Driver"
;;                    :subprotocol "mysql"
;;                    :user "user"
;;                    :password "password"
;;                    :subname (str "//" dbhost ":" dbport "/helloworld?useUnicode=true&characterEncoding=utf8")
;;                    :delimiters "`"})


(defdb korma-db default-conn)

(defentity article)

(defentity rss_chanel)
(defentity rss_entry)


(defn create-article [item]
  (insert article (values item)))

(defn select-article []
  (select article))

(defn find-article [id]
  (first (select article (where {:id id}))))

(defn insert-rss-chanel [item]
  (insert rss_chanel
          (values item)))

(defn insert-rss-entry [item]
  (insert rss_entry
          (values item)))

(defn select-rss-chanel []
  (select rss_chanel))

(defn update-article [item]
  (update article
          (set-fields item)
          (where {:id (:id item)})))

(defn delete-article [id]
  (delete article
          (where {:id id})))


