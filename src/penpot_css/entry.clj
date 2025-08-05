(ns penpot-css.entry
  (:require [clojure.string :as s]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.java.io :as io]
            [penpot-css.core :as core])
  (:gen-class))


(defn writable? [filepath]
  (let [file (java.io.File. filepath)]
    (if (.exists file)
      (.canWrite file)
      (let [parent (.getParentFile file)]
        (and parent (.canWrite parent))))))

(def cli-options
  [["-t" "--token TOKEN" "the penpot access token"
    :id :token
    :parse-fn str
    :missing "your penpot token access is required"
    :validate [string? "Must be a valid string"]]

   ["-f" "--file FILE_ID" "the penpot file-id"
    :id :file
    :missing "the penpot file-id is required"
    :parse-fn str
    :validate [string? "Must be a valid string"]]

   ["-o" "--output FILEPATH" "the output file"
    :id :output
    :parse-fn str
    :validate [writable? "Must be a valid filepath"]]

   ["-m" "--minify" "will minify css" :default true]

   ["-h" "--help"]])

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (s/join \newline errors)))

(defn usage
  [options-summary]
  (->> ["Running penpot-css, a handy way to generate a base css from penpot."
        nil
        "Usage: clojure -M -m [options]"
        nil
        "Options:"
        options-summary]
       (s/join \newline)))

(defn validate-args
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      ;; help => exit OK with usage summary
      (:help options) {:exit-message (usage summary) :ok? true}

      ;; errors => exit with description of errors
      errors {:exit-message (error-msg errors)}

      ;; valid access and output => running program
      (core/valid-access? (:token options) (:file options))
      {:exit-message (core/generate options), :ok? true}

      ;; failed custom validation => exit with usage summary
      :default {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [action options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (println "running ..."))))
