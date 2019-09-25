(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.jdbc :as jdbc]

            [com.stuartsierra.component :as c]
            [figwheel-sidecar.repl-api :refer :all]

            [de.sample.todoapp.backend.config :as config]
            [de.sample.todoapp.backend.core :as core]))

(defonce system
  nil)

(def config
  (config/load))


(defn system-init!
  []
  (alter-var-root #'system
                  (constantly (core/new-system config))))

(defn system-start!
  []
  (alter-var-root #'system c/start)
  :started)

(defn system-stop!
  []
  (alter-var-root #'system
                  (fn [s] (when s (c/stop s))))
  :stopped)

(defn system-go!
  []
  (system-init!)
  (system-start!))

(defn system-restart!
  []
  (system-stop!)
  (refresh :after 'user/system-go!))


(comment
  ;; Test DB access
  (require '[clojure.java.jdbc :as jdbc])

  (jdbc/with-db-transaction [tx (:db system)]
    (jdbc/execute! tx ["create table todos (label varchar(200), done boolean)"]))

  (jdbc/with-db-transaction [tx (:db system)]
    (jdbc/execute! tx ["insert into todos (label,done) values ('Putzen',false)"]))

  ,,,)
