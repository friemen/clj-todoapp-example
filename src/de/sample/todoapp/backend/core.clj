(ns de.sample.todoapp.backend.core
  (:require
   [com.stuartsierra.component :as c]
   [de.sample.todoapp.backend.components
    [app :as app-impl]
    [db :as db-impl]
    [jdbc-cp :as cp-impl]
    [server :as server-impl]
    [scheduler :as scheduler-impl]]
   [de.sample.todoapp.backend.tasks.reminder :as reminder]))


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
    [:db-server])

   :app
   (c/using
    (app-impl/new-app (:app config))
    [:db :scheduler])

   :scheduler
   (c/using
    (scheduler-impl/new-scheduler (:scheduler config))
    [:reminder-task])

   :server
   (c/using
    (server-impl/new-server (:server config))
    [:app])

   ;; tasks

   :reminder-task
   (c/using
    (reminder/map->ReminderTask {})
    [:db])

   ))
