(ns de.sample.todoapp.backend.components.app
  "The application component."
  (:require
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.handler :as handler]))


(defrecord App [options       ;; options
                db            ;; deps
                handler]      ;; managed state
  c/Lifecycle
  (start [component]
    (println ";; Starting App")
    (assoc component :handler (handler/new-handler db)))

  (stop [component]
    (println ";; Stopping App")
    (assoc component :handler nil)))


(defn new-app
  [options]
  (map->App {:options options}))
