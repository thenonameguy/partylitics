(def project 'partylitics)
(def version "0.0.1-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "1.9.0"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [enlive "1.1.6"]
                            [clj-http "3.8.0"]
                            [traversy "0.5.0"]])

(task-options!
 aot {:namespace   #{'partylitics.core}}
 pom {:project     project
      :version     version
      :description "Nightlife analyitcs"
      :url         "https://kszabohome.ownip.net"
      :scm         {:url "https://github.com/kszabohome/partylitics"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main        'partylitics.core
      :file        (str "partylitics-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[partylitics.core :as app])
  (apply (resolve 'app/-main) args))

(require '[adzerk.boot-test :refer [test]])
