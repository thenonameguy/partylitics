(ns partylitics.cli
  (:require [partylitics.core :as core])
  (:gen-class))

(defn -main
  ""
  [& args]
  (core/scrape))
