(ns de.sample.todoapp.backend.core
  (:require
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.components
    [app :as app-impl]
    [db :as db-impl]
    [jdbc-cp :as cp-impl]
    [server :as server-impl]]))


(defn new-system
  [config]
  (c/system-map
   :db-server
   (c/using
    (db-impl/new-db (:db config))
    [])

   :db
   (c/using
    (cp-impl/new-connection-pool (:db-connection config))
    [])

   :app
   (c/using
    (app-impl/new-app (:app config))
    [:db])

   :server
   (c/using
    (server-impl/new-server (:server config))
    [:app])))
