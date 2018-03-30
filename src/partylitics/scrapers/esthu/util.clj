(ns partylitics.scrapers.esthu.util
  (:refer-clojure :exclude [get])
  (:require [partylitics.scrapers.util :as util]))

(def charset "iso-8859-2")

(def get #(util/fetch % {:as charset}))
