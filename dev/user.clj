(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.jdbc :as jdbc]

            [com.stuartsierra.component :as c]
            [figwheel-sidecar.repl-api :as figwheel]
            [taoensso.timbre :as log]

            [de.sample.todoapp.backend.config :as config]
            [de.sample.todoapp.backend.core :as core]))

(defonce system
  nil)


(defn system-init!
  []
  (let [config (config/load)]
    (log/merge-config! (:logging config))
    (alter-var-root #'system
                    (constantly (core/new-system config)))))

(defn system-start!
  []
  (try
    (alter-var-root #'system c/start)
    (catch Exception ex
      (log/fatal ex)
      (when-let [system (-> ex ex-data :system)]
        (c/stop system))))
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


;; support for interaction with internal DB

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


;; re-export figwheel functions

(def start-figwheel! #'figwheel/start-figwheel!)

(def cljs-repl #'figwheel/cljs-repl)
