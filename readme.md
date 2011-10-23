# clj-factory

Factories allow test data to be easily produced. The goal was to have
something similar to Factory Girl for Clojure.

## Sequences

Sequences are a way to quickly produce simple data values. When
invoked, the sequence function will be called with a different value
each time. By default, this is an ever-incrementing counter value. By
incorporating this value in your calculations, you will always get
different results.

### Example

    (defseq :word
      [n]
      (str "word" n))
    
    (fseq :word) => "word1"
    (fseq :word) => "word2"
    (fseq :word) => "word3"


## Factories

Factories are a basis for producing sample map data. Factories will
merge the provided data with the basis map to produce new data.

If the value of a factories keys is a function, that function will be
called with no arguments. (unless an override has been provided)

### Example

    (ns clj-factory.example
      (:use [clj-factory.core :only [deffactory defseq
                                     factory fseq]])) 
    
    (defseq :username [n] (str "user" n))
    (defseq :domain [n] (str "sub" n ".example.com"))
    (defseq :fname [n] "John")
    (defseq :lname [n] "Smith")
    (defseq :password [n] (str "hunter" n))
    
    (deffactory :user
      {:username   (fseq :username)
       :domain     (fseq :domain)
       :first-name (fseq :fname)
       :last-name  (fseq :lname)
       :password   (fseq :password)}))

And used like:

    (factory :user)
    => {:username "word1",
        :domain "sub1.example.com",
        :first-name "John",
        :last-name "Smith",
        :password "hunter1"}
    
    
    (factory :user {:name "Tom"})
    => {:username "word2",
        :domain "sub2.example.com",
        :name "John Smith",
        :first-name "Tom",
        :last-name "Smith",
        :password "hunter2"}

## TODO

- Add method to assign a create function that will process the factory
  data.

- Allow factories to use the values of previously defined fields in
  the calculation of factory data.

## License

Copyright (C) 2011 KRONK Ltd.

Distributed under the Eclipse Public License, the same as Clojure.
