(ns myblog.ns
  (:require [clj-http.client :as client]
  [clojure.xml :as xml]
  [clojure.zip :as zip]
  [clojure.contrib.zip-filter.xml :as zf]))

(defn get-rss-chanel [rss]
  (client/get rss))

(defn parse-rss [rss]
  (xml/parse rss))

(defn get-chanel-info [rss]
  (map #(println %) (parse-rss rss)))

(defn get-item [rss path]
  (if (nil? path)
    rss
  (let[tag (first path)]
    (println (first (:tag rss)))
    (get-item (map #(:content %) (filter #(= tag (:tag %)) rss)) (next path)))))