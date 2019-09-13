(ns de.sample.todoapp.backend.core
  (:require
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.components.app :as app-impl]
   [de.sample.todoapp.backend.components.server :as server-impl]))

(defn new-system
  [config]
  (c/system-map
   :app
   (c/using
    (app-impl/new-app)
    [])

   :server
   (c/using
    (server-impl/new-server (:server config))
    [:app])))
