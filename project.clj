(defproject clj-factory "0.2.2-SNAPSHOT"
  :description "Library for producing test data from factories"
  :url "https://github.com/duck1123/clj-factory"
  :author "Daniel E. Renfer <duck@kronkltd.net>"
  :min-lein-version "2.0.0"
  :plugins [[codox "0.6.7"]
            [lein-midje "3.1.3"]]
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :profiles {:dev
             {:dependencies [[midje "1.6.3"]]}})
