(ns de.sample.todoapp.backend.components.app
  (:require
   [com.stuartsierra.component :as c]))


(defrecord App []
  c/Lifecycle
  (start [component]
    (println ";; Starting App")
    component)

  (stop [component]
    (println ";; Stopping App")
    component))


(defn new-app
  []
  (map->App {}))
