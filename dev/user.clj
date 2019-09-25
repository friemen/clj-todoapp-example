(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.jdbc :as jdbc]

            [com.stuartsierra.component :as c]
            [figwheel-sidecar.repl-api :refer :all]

            [de.sample.todoapp.backend.config :as config]
            [de.sample.todoapp.backend.core :as core]))

(defonce system
  nil)


(defn system-init!
  []
  (let [config (config/load)]
    (alter-var-root #'system
                    (constantly (core/new-system config)))))

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


(defn reset-db!
  []
  (jdbc/with-db-transaction [tx (:db system)]
    (doseq [stmt ["drop table todo if exists"
                  "create table todo (id int primary key auto_increment, position int, label varchar(250), done boolean)"]]
      (jdbc/execute! tx [stmt]))))


(comment

  ;; insert one todo into DB
  (jdbc/with-db-transaction [tx (:db system)]
    (jdbc/execute! tx ["insert into todo (position, label, done) values (0, 'Clean kitchen',false)"]))

  ,,,)
