(ns partylitics.core-test
  (:require [clojure.test :refer :all]
            [partylitics.core :as sut]))

(deftest scrape
  (is (nil? (sut/scrape))))
