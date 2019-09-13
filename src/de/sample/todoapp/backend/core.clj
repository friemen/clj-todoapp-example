(ns de.sample.todoapp.backend.core
  (:require
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.components.app :as app-impl]))

(defn new-system
  [config]
  (c/system-map
   :app (app-impl/new-app)))
