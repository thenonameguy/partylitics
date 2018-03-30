(ns partylitics.scrapers.esthu.venue
  (:require [net.cgrand.enlive-html :as html]
            [partylitics.scrapers.esthu.util :as eutil]
            [partylitics.scrapers.util :as util]
            [traversy.lens :as lens]))

(defn fetch-music-page []
  (eutil/get "http://est.hu/zene/"))

(defn ids [music-page]
  (->> [:select#zenekereso_hely :option]
       (html/select music-page)
       (map #(-> %
                 (get-in [:attrs :value])
                 (util/parse-int)))
       (drop 1)
       (set)))

(defn fetch [id]
  (let [page (eutil/get (str "http://est.hu/hely/" id))]
    (when-not (= "404" (util/select-first-text page [:h1]))
      page)))

(defn info [page]
  (let [info        (html/select page [:.locationadatlap])
        parse-first #(util/select-first-text info %)]
    (lens/update
     {:name     :.rovatcim
      :location :#lokacio_cim
      :phone    :#lokacio_tel
      :email    :#lokacio_email
      :url      :#lokacio_url
      :open     :#lokacio_url}
     lens/all-values
     #(parse-first [%]))))

(defn event-ids [page]
  (->> [:.talalat_program :a.url.summary]
       (html/select page)
       (map #(->> %
                  (util/parse-link)
                  (:href)
                  (re-find #".*\/(\d+)\/")
                  (second)
                  (util/parse-int)))
       (set)))

(defn parse-page [page]
  {:info   (info page)
   :events (event-ids page)})

(defn parse [id]
  (-> id fetch parse-page))

(comment
  (def music-page (fetch-music-page))

  (ids music-page)

  (def venue (-> music-page
                 ids
                 vec
                 rand-nth
                 fetch))

  (parse-page venue)

  (event-ids venue)

  (parse 3287))
