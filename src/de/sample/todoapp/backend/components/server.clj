(ns de.sample.todoapp.backend.components.server
  (:require
   [com.stuartsierra.component :as c]
   [org.httpkit.server :as httpkit-server]))


(defrecord Server [options
                   app
                   stop-fn]
  c/Lifecycle
  (start [component]
    (println ";; Starting HttpKit on port" (:port options))
    (assoc component :stop-fn (httpkit-server/run-server (:handler app) options)))

  (stop [component]
    (println ";; Shutting HttpKit down")
    (when stop-fn
      (stop-fn))
    (assoc component :stop-fn nil)))



(defn new-server
  [options]
  (map->Server {:options options}))
