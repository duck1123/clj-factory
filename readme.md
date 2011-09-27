# clj-factory

Factories allow test data to be easily produced. The goal was to have
something similar to Factory Girl for Clojure.

### Example

    (defseq :word
      [n]
      (str "word" n))
    
    (deffactory User
      (let [password (fseq :word)]
        {:username (fseq :word)
         :domain (-> (config) :domain)
         :name (fseq :word)
         :first-name (fseq :word)
         :last-name (fseq :word)
         :password password
         :confirm-password password}))

And used like:

    (fseq :word)
    (factory User)
    (factory User {:name "tom"})

## License

Copyright (C) 2011 KRONK Ltd.

Distributed under the Eclipse Public License, the same as Clojure.
