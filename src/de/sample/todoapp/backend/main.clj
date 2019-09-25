(ns de.sample.todoapp.backend.main
  (:require
   [taoensso.timbre :as log]
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.config :as config]
   [de.sample.todoapp.backend.core :as core])
  (:gen-class))



;; load config

;; propagate logging config to logger

;; register shutdown hook

(defn -main
  [& args]
  (let [config (config/load)
        _      (log/merge-config! (:logging config))
        system (core/new-system config)
        system (c/start system)]
    (doto (Runtime/getRuntime)
      (.addShutdownHook
       (Thread.
        (fn []
          (c/stop system)))))
    0))
