(ns de.sample.todoapp.backend.components.jdbc-cp
  "A JDBC connection pooling component based on C3P0."
  (:require
   [com.stuartsierra.component :as c])
  (:import
   com.mchange.v2.c3p0.ComboPooledDataSource))



(defrecord ConnectionPool [options
                           datasource]
  c/Lifecycle
  (start [component]
    (println ";; Connecting to DB with options" (pr-str options))
    (let [cpds (doto (ComboPooledDataSource.)
                 (.setDriverClass (:classname options))
                 (.setJdbcUrl (:url options))
                 (.setUser (:user options))
                 (.setPassword (:password options)))]
      (assoc component :datasource cpds)))
  (stop [component]
    (println ";; Closing DB connection pool")
    (when datasource
      (.close datasource))
    (assoc component :datasource nil)))


(defn new-connection-pool
  [options]
  (map->ConnectionPool {:options options}))
