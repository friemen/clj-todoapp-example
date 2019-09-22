(ns de.sample.todoapp.backend.components.db
  "An in-process H2 database component."
  (:require
   [com.stuartsierra.component :as c])
  (:import
   [org.h2.tools Server]))


(defrecord Database [options
                     server]
  c/Lifecycle
  (start [component]
    (println ";; Starting H2 DB, web console is available on localhost:8082")
    (let [tcp (Server/createTcpServer (into-array String ["-ifNotExists"]))
          web (Server/createWebServer (into-array String []))
          server {:tcp tcp :web web}]
      (.start tcp)
      (.start web)
      (assoc component :server server)))

  (stop [component]
    (println ";; Stopping H2 DB")
    (when-let [web (:web server)]
      (.stop web))
    (when-let [tcp (:tcp server)]
      (.stop tcp))
    (assoc component :server nil)))


(defn new-db
  [options]
  (map->Database {:options options}))
