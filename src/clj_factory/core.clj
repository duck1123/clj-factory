(ns clj-factory.core)

(def ^:dynamic *counters* (ref {}))

(defn set-counter!
  [type value]
  (get
   (dosync
    (alter
     *counters*
     (fn [m] (assoc m type value))))
   type))

(defn reset-counter!
  [type]
  (set-counter! type 0))

(defn get-counter
  [type]
  (get @*counters* type 0))

(defn inc-counter!
  [type]
  (dosync
   (alter
    *counters*
    (fn [m]
      (assoc m type (inc (get m type 0)))))))

(defn next-counter!
  [type]
  (get (inc-counter! type) type))

(defmulti fseq (fn [type & _] type))

(defmulti factory (fn [type & _] type))

(defn eval-keys
  [[k v]]
  [k (if (ifn? v)
       (v) v)])

(defmacro deffactory
  [type opts & body]
  `(defmethod clj-factory.core/factory ~type
     [type# & args#]
     (apply merge
            (if (= (class ~type) Class) (eval (list 'new ~type)))
            (into {} (map eval-keys ~opts))
            args#)))

(defmacro defseq
  [type let-form result]
  `(let [type# ~type]
     (defmethod clj-factory.core/fseq type#
       [type#]
       (let [~let-form [(next-counter! type#)]]
         ~result))))
