(ns de.sample.todoapp.backend.config
  "Load config file from resources or a path that system property
  de.sample.configfile points to."
  (:refer-clojure :exclude [load])
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [taoensso.timbre :as log]))


(defn- exists
  [f]
  (if (and f (.exists (io/file f)))
    f))


(def ^:private sources
  [{:strategy   :system-property
    :path       (System/getProperty "de.sample.configfile")
    :conversion exists}
   {:strategy   :default-config
    :path       "etc/config.edn"
    :conversion io/resource}])

(defn- find-source
  []
  (->> sources
       (map (fn [{:keys [strategy path conversion] :as source}]
              (assoc source :input (conversion path))))
       (drop-while (comp nil? :input))
       (first)))


;; Public API

(defn load
  []
  (let [{:keys [input path strategy]} (find-source)]
    (when (nil? input)
      (throw (ex-info (str "Cannot find any config file among sources" {:sources sources}))))
    (log/debug "Loading config file" strategy path)
    (-> input
        (io/input-stream)
        (slurp)
        (edn/read-string))))
