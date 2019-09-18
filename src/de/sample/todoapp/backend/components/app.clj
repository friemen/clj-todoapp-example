(ns de.sample.todoapp.backend.components.app
  (:require
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.handler :as handler]))


(defrecord App [handler]
  c/Lifecycle
  (start [component]
    (println ";; Starting App")
    (assoc component :handler (handler/new-handler)))

  (stop [component]
    (println ";; Stopping App")
    (assoc component :handler nil)))


(defn new-app
  []
  (map->App {}))
