(ns partylitics.scrapers.esthu.event
  (:require [net.cgrand.enlive-html :as html]
            [partylitics.scrapers.esthu.util :as eutil]
            [partylitics.scrapers.util :as util]))

(defn fetch [id]
  (let [page (eutil/get (str "http://est.hu/esemeny/" id))]
    (when (seq (util/select-first-text page [:.rovatcim]))
      page)))

(defn parse-page [page]
  (let [info        (html/select page [:.musor_talalat])
        parse-first #(util/select-first-text info %)]
    {:name (parse-first [:.rovatcim])}
    ))

(defn parse [id]
  (-> id fetch parse-page))

(comment
  (def bad-page (fetch 1))

  (def good-page (fetch 10843472))

  (parse-page good-page)

  )
