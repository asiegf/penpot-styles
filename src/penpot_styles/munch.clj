(ns penpot-styles.munch
  (:require [clojure.string :as s]))

(defn- make-classes-fn
  [n]
  (fn [& props]
    (->> props 
         (map #(format ".%s-%s" (name %) n))
         (s/join ", "))))

(defn parse-colors
  [file]
  (reduce-kv (fn [m id {:keys [color name]}]
               (let [n (s/lower-case name)
                     var (format "var(--%s)" name)
                     ->classes (make-classes-fn name)]
                 (-> m 
                     (assoc-in [":root" (str "--" id)] color)
                     (assoc-in [":root" (str "--" n)] color)
                     (assoc (->classes :text-color)   {:color var}
                            (->classes :bg-color)     {:background-color var}
                            (->classes :border-color) {:border-color var}))))
             {}
             (get-in file [:data :colors])))


(defn parse-fonts
  [file]
  (->> (get-in file [:data :typographies])
       vals
       (map :font-family)
       set
       (map #(s/replace % " " "+"))
       (map #(format "@import url('https://fonts.googleapis.com/css2?family=%s');" %))))

(defn parse-typographies
  [file]
  (reduce-kv (fn [m id {:keys [line-height font-style text-transform font-size font-weight
                               name font-family]}]
               (let [->classes (make-classes-fn name)]
                 (assoc m
                        (->classes :font)
                        {:font-family (format "'%s'" font-family)
                         :font-weight font-weight
                         :font-size   (str font-size "px")})))
             {}
             (get-in file [:data :typographies])))


