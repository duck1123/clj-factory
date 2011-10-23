(defproject clj-factory "0.2.0-SNAPSHOT"
  :description "Library for producing test data from factories"
  :url "https://github.com/duck1123/clj-factory"
  :repositories {"jiksnu-internal" "http://build.jiksnu.com/repository/internal"
                 "jiksnu-snapshots" "http://build.jiksnu.com/repository/snapshots"}
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [midje "1.3-alpha4"]]
  :warn-on-reflection false)
