(ns partylitics.scrapers.esthu.ajax
  (:require [clj-http.client :as http]
            [net.cgrand.enlive-html :as html]
            [partylitics.scrapers.util :as util]))

(defn ajax [url params]
  (-> url
      (http/post {:form-params params
                  :as :auto})
      (:body)
      (html/html-snippet)))

(defn parties [nodes]
  (html/select nodes [:div.talalat_program :table :tr]))

(defn parse-party [node]
  (let [[venue-td time-td event-td] (html/select node [:td.fo_cella])
        [venue-link event-link]     (->> [:.egyseg :a]
                                         (html/select [venue-td event-td])
                                         (map util/parse-link))]
    {:venue venue-link
     :event (assoc event-link :start-time (html/text time-td))}))

(comment
  (def budapest-city-id 298)

  (def bp-ajax
    (ajax
     "http://est.hu/ajax.php?i=zenekereso"
     {:dt "2018.03.30"
      :page 1
      :rend 1
      :varos budapest-city-id}))

  (def first-row
    (-> bp-ajax
        parties
        first))

  (parse-party first-row)

  (def parsed-parties
    (->> bp-ajax
         parties
         (map parse-party)
         (take 5)))

  (clojure.pprint/pprint parsed-parties))
