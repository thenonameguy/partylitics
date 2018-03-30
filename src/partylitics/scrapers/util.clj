(ns partylitics.scrapers.util
  (:require [net.cgrand.enlive-html :as html]
            [clj-http.client :as http]))

(defn fetch
  ([url] (fetch url {}))
  ([url params]
   (-> url
       (http/get params)
       (:body)
       (html/html-snippet))))

(defn first-text [nodes]
  (-> nodes first html/text))

(defn select-first-text [& args]
  (first-text (apply html/select args)))

(defn parse-link [node]
  {:text (html/text node)
   :href (get-in node [:attrs :href])})

(defn parse-int [s]
  (Integer/parseInt s))
