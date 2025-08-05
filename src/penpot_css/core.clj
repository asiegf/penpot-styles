 (ns penpot-css.core
   (:require [org.httpkit.client :as http]
             [clojure.string :as s]
             [cognitect.transit :as transit]
             [clojure.tools.cli :refer [parse-opts]]
             [clojure.java.io :as io]
             [clojure.data.json :as json]
             [penpot-css.munch :as munch]
             [penpot-css.digest :as digest])
   (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(defn- parse-transit
  [ba]
  (let [in     (ByteArrayInputStream. ba)
        reader (transit/reader in :json)]
    (transit/read reader)))

(defn get-file
  [token file]
  (let [{:keys [status body], :as response}
        @(http/get "https://design.penpot.app/api/rpc/command/get-file"
                   {:headers {"Authorization" (str "Token " token)}
                    :query-params {:id file}
                    :as :byte-array})]
    (when (= 200 status) (parse-transit body))))

;; just to avoid calling it twice
(def get-file* (memoize get-file))

(defn valid-access? [token file] (some? (get-file* token file)))

(defn summary
  [{{:keys [colors typographies components]} :data}]

  (format "%d colors, %d typographies and %d components (totaling %d variants)"
          (count colors)
          (count typographies)
          (count (set (map :path (vals components))))
          (count components)))

(defn bytes->base64 [byte-array]
  (let [encoder (java.util.Base64/getEncoder)]
    (.encodeToString encoder byte-array)))

(defn generate
  [{:keys [token file], :as opts}]

  (let [file (get-file* token file)]
    (->> file
         ((juxt munch/parse-colors munch/parse-typographies))
         (apply merge-with merge)
         (digest/out opts (munch/parse-fonts file)))))

