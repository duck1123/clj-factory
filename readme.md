# clj-factory
[![Continuous Integration status](https://secure.travis-ci.org/duck1123/clj-factory.png)](http://travis-ci.org/duck1123/clj-factory)

clj-factory is a simple library to help you easily create and
manipulate test data. clj-factory was inspired by the FactoryGirl
library for Ruby.

## Useage

Add the following to your project.clj file:

```clojure
[clj-factory "0.2.1-SNAPSHOT"]
```

## Sequences

A sequence is a pattern for creating test values. You define a
sequence using the `defseq` function and name it with a keyword.

Every time the factory sequence function is invoked (`fseq`), the
function defined in `defseq` will be called with an integer counter
value unique to that sequence. That counter can be incorporated into
the result, used as a seed for some calculation, or ignored
completely.

### Example

``` clojure

(defseq :word [n]
  (str "word" n))

(fseq :word) => "word1"
(fseq :word) => "word2"
(fseq :word) => "word3"

```

```clojure

(defseq :random-word [_]
  (rand-nth ["the" "a" "because" "foo"]))

(fseq :random-word) => "a"
(fseq :random-word) => "the"
(fseq :random-word) => "foo"

```

```clojure

(defseq :even-number [n]
  (* n 2))
  
(fseq :even-number) => 2
(fseq :even-number) => 4
(fseq :even-number) => 6

```

## Factories

Factories are a basis for producing sample map data. Factories will
merge the provided data with the basis map to produce new data.

If the value of a factories keys is a function, that function will be
called with no arguments. (unless an override has been provided)

### Example

``` clojure

(:use '[clj-factory.core :only [deffactory defseq factory fseq]])

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
```

And used like:

``` clojure
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
```

## TODO

- Add method to assign a create function that will process the factory
  data.

- Allow factories to use the values of previously defined fields in
  the calculation of factory data.

## License

Copyright (C) 2011 KRONK Ltd.

Distributed under the Eclipse Public License, the same as Clojure.
