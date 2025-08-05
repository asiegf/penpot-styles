(ns penpot-styles.digest
  (:refer-clojure :exclude [abs])
  (:require [clojure.string :as s]
            [garden.core :refer [css] :exclude [abs]]))

(defn css-minify 
  [css]
  (-> css
      (s/replace #"/\*.*?\*/" "")
      (s/replace #"\s+" " ")
      (s/replace #"\s*([:;])\s*" "$1")
      (s/replace #"\s*([{}])\s*" "$1")
      s/trim))

(defn spit-and-confirm
  [output s]
  (try 
    (do (spit output s)
        (str "file created: " output))
    (catch Exception _ (str "failed to write here: " output))))

(defn out
  [{:keys [minify output]} imports rules]
  (cond->> rules
    true   (map css)
    (not (empty? imports)) (concat imports)
    true   (s/join "\n")
    minify css-minify
    output (spit-and-confirm output)))
