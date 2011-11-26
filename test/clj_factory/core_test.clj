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

(deftest test-set-counter
  (fact "Sets the counter with a given name to certain value"
        (set-counter! :test-type 5800)
    (is (= 5800 (:test-type (deref *counters*))))))

(deftest test-inc-counter!
  (testing "first increment, counter was not yet in use"
    (fact "should equal 1"
      (inc-counter! :test-inc-counter)
      (is (= 1 (:test-inc-counter (deref *counters*))))))
  (testing "subsequent increments"
    (fact "should increment an existing counter"
      (inc-counter! :test-inc-counter)
      (is (= 2 (:test-inc-counter (deref *counters*)))))
      (inc-counter! :test-inc-counter)
      (is (= 3 (:test-inc-counter (deref *counters*))))))

(deftest test-next-counter!
  (testing "first increment, counter was not yet in use"
    (fact "should equal 1"
      (is (= 1 (next-counter! :test-next-counter)))
  (testing "subsequent increments"
    (fact "should increment an existing counter"
      (are [expected result] (= expected result)
        2 (next-counter! :test-next-counter)
        3 (next-counter! :test-next-counter)))))))

(deftest test-defseq-and-fseq-with-dispatch-value
  (fact "Sequentially generates entities based on a given pattern"
    (defseq :username [id] (str "user" id))
    (are [expected result] (= expected result)
         "user1" (fseq :username)
         "user2" (fseq :username)
         "user3" (fseq :username)))
  (fact "Sequentially generates complex entities based on a given pattern"
    (defseq :user [user-id] { :username (str "user-" user-id) :email (str "user-" user-id "@example.com") })
    (are [expected result] (= expected result)
         { :username (str "user-1") :email (str "user-1@example.com") } (fseq :user)
         { :username (str "user-2") :email (str "user-2@example.com") } (fseq :user)
         { :username (str "user-3") :email (str "user-3@example.com") } (fseq :user))))

(deftest test-factory
  (fact "Generates entities based on given pattern"
    (deffactory :user { :username (str "username") :email "user-20@example.com" } )
    (is (= { :username (str "username") :email "user-20@example.com" } (factory :user))))

  (fact "Generates copmlex entities based on given pattern"
    (deffactory :user { :username (str "u" "s" "e" "r" "n" "a" "m" "e") :email (str "user-" (+ 15 5) "@example.com") } )
    (is (= { :username (str "username") :email "user-20@example.com" } (factory :user)))))

