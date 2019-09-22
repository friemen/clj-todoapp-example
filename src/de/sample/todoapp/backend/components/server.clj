(ns de.sample.todoapp.backend.components.server
  "An HTTP Server component based on http-kit."
  (:require
   [com.stuartsierra.component :as c]
   [org.httpkit.server :as httpkit-server]))


(defrecord Server [options     ;; options
                   app         ;; deps
                   stop-fn]    ;; managed state
  c/Lifecycle
  (start [component]
    (println ";; Starting http-kit on port" (:port options))
    (assoc component :stop-fn (httpkit-server/run-server (:handler app) options)))

  (stop [component]
    (println ";; Shutting http-kit down")
    (when stop-fn
      (stop-fn))
    (assoc component :stop-fn nil)))



(defn new-server
  [options]
  (map->Server {:options options}))
