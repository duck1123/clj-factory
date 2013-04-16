(ns clj-factory.core)

;; # Counter functions

(def ^:dynamic *counters* (ref {}))

(defn set-counter!
  "Set the counter for the supplied key to the new value"
  [type value]
  (get
   (dosync
    (alter
     *counters*
     (fn [counter] (assoc counter type value))))
   type))

(defn reset-counter!
  "Reset the counter for the supplied key to 0"
  [type]
  (set-counter! type 0))

(defn get-counter
  "Get the counter for the supplied key"
  [type]
  (get @*counters* type 0))

(defn inc-counter!
  "Increment the counter for the supplied key by 1"
  [type]
  (dosync
   (alter
    *counters*
    (fn [counter]
      (assoc counter type (inc (get counter type 0)))))))

(defn next-counter!
  "Increment and return the next value for the supplied key"
  [type]
  (get (inc-counter! type) type))

;; # Multimethods

(defn dispatch-fn
  [type & _] type)

(defmulti
  ^{:doc "Returns a new data item for the provided key.

 The defined sequence function is called with the value of the next counter
 for that key."} fseq dispatch-fn)

(defmulti factory dispatch-fn)
(defmulti factory-data dispatch-fn)

(defn eval-keys
  [[k v]]
  [k (if (var? v)
       (v) v)])

(defn merge-result [args opts]
  (->> args
       (apply merge opts)
       (map eval-keys)
       (into {})))

(defmacro deffactory
  "Defines a new method for factory multimethod"
  [type & body]
  `(defmethod clj-factory.core/factory ~type
     [type# & args#]
     (let [{:as opts#} (do ~@body)]
       (merge-result args# opts#))))

(defmacro defrecordfactory
  "Defines a new method for factory multimethod"
  [type f & body]
  `(defmethod clj-factory.core/factory ~type
     [type# & args#]
     (let [{:as opts#} (do ~@body)]
       (~f (merge-result args# opts#)))))

(defmacro defseq
  "Defines a method for fseq multimethod associated with :type dispatch value.

  You can use :let-form and :result to create readable methods, such as:

    (defseq :email [user-id] (str \"user_\" user-id \"@sample.com\"))

  "
  [type let-form result]
  `(let [type# ~type]
     (defmethod clj-factory.core/fseq type#
       [type#]
       (let [~let-form [(next-counter! type#)]]
         ~result))))
