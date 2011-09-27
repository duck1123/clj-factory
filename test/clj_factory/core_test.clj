(ns clj-factory.core-test
  (:use clj-factory.core
        clojure.test
        midje.sweet))

(deftest test-get-counter
  (testing "when the type has been defined"
    (fact "should return a number"
      (let [type :foo]
        (reset-counter! type)
        (get-counter type) => 0))
    (testing "when it has been incremented"
      (fact "should return a larger number"
        (let [type :foo]
          (reset-counter! type)
          (let [response1 (get-counter type)]
            (inc-counter! type)
            (let [response2 (get-counter type)]
              (< response1 response2) => truthy)))))))

(deftest test-inc-counter!)

(deftest test-next-counter!)

(deftest test-fseq)

(deftest test-factory)

(deftest test-deffactory)

(deftest test-defseq)
