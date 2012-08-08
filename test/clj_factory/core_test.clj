(ns clj-factory.core-test
  (:use [clj-factory.core :only [*counters* deffactory defseq factory fseq
                                 get-counter inc-counter! reset-counter!
                                 set-counter! next-counter!]]
        [midje.sweet :only [contains fact =>]]))

(fact "#'get-counter"
  (fact "when the type has been defined"
    (fact "should return a number"
      (let [type :foo]
        (reset-counter! type)

        (get-counter type) => 0))

    (fact "when it has been incremented"
      (fact "should return a larger number"
        (let [type :foo]
          (reset-counter! type)

          (let [response1 (get-counter type)]
            (inc-counter! type)

            (get-counter type) => #(> % response1)))))))

(fact "#'set-counter"
  (fact "Sets the counter with a given name to certain value"
    (set-counter! :test-type 5800)

    @*counters* => (contains {:test-type 5800})))

(fact "#'inc-counter!"
  (fact "first increment, counter was not yet in use"
    (reset-counter! :test-inc-counter)

    (fact "should equal 1"
      (inc-counter! :test-inc-counter)

      (:test-inc-counter (deref *counters*)) => 1))

  (fact "subsequent increments"
    (fact "should increment an existing counter"
      (inc-counter! :test-inc-counter)

      @*counters* => (contains {:test-inc-counter 2})

      (inc-counter! :test-inc-counter)
      @*counters* => (contains {:test-inc-counter 3}))))

(fact "#'next-counter!"
  (fact "first increment, counter was not yet in use"
    (reset-counter! :test-next-counter)

    (fact "should equal 1"
      (next-counter! :test-next-counter) => 1

      (fact "subsequent increments"
        (fact "should increment an existing counter"
          (next-counter! :test-next-counter) => 2
          (next-counter! :test-next-counter) => 3)))))

(fact "#'defseq"
  (fact "and fseq with dispatch value"
    (fact "Sequentially generates entities based on a given pattern"
      (reset-counter! :username)

      (defseq :username [id]
        (str "user" id))

      (fseq :username) => "user1"
      (fseq :username) => "user2"
      (fseq :username) => "user3")
    (fact "Sequentially generates complex entities based on a given pattern"
      (reset-counter! :user)

      (defseq :user [user-id]
        { :username (str "user-" user-id) :email (str "user-" user-id "@example.com") })

      (fseq :user) => { :username (str "user-1") :email (str "user-1@example.com") }
      (fseq :user) => { :username (str "user-2") :email (str "user-2@example.com") }
      (fseq :user) => { :username (str "user-3") :email (str "user-3@example.com") })))

(fact "#'factory"
  (fact "Generates entities based on given pattern"

    (deffactory :user
      {:username (str "username")
       :email "user-20@example.com"})

    (factory :user) => {:username "username"
                        :email "user-20@example.com"})

  (fact "Generates complex entities based on given pattern"

    (deffactory :user
      {:username (str "u" "s" "e" "r" "n" "a" "m" "e")
       :email (str "user-" (+ 15 5) "@example.com")})

    (factory :user) => {:username "username"
                        :email "user-20@example.com"})

  (fact "Can include sequences to provide values for keys"
    (reset-counter! :username)

    (defseq :username [n] (str "user-" n))

    (deffactory :user
      (let [username (fseq :username)]
        {:username username
         :email (str username "@example.com")}))

    (factory :user) => {:username "user-1"
                        :email "user-1@example.com"}

    (factory :user
             {:email "bob@example.com"}) => {:username "user-2"
                                             :email "bob@example.com"})

  (fact "Generates entities that have nested hashes"
    (deffactory :user
      {:friends {:name (str "u" "s" "e" "r" "n" "a" "m" "e")}})

    (factory :user) => (contains {:friends {:name "username"}}))

  (fact "Generates entities that have nested vectors"
    (deffactory :user
      {:friends [(str "j" "o" "h" "n")
                (str "j" "o" "e")
                "marta"]})

    (:friends (factory :user)) => ["john" "joe" "marta"])

  (fact "Generates entities that have nested seqs"
    (deffactory :user
      {:friends (list (str "j" "o" "h" "n")
                      (str "j" "o" "e")
                      "marta")})

    (:friends (factory :user)) => '("john" "joe" "marta"))
  )
