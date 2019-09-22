(ns de.sample.todoapp.backend.database.queries
  "Collection of SQL queries."
  (:require
   [clojure.java.jdbc :as jdbc]
   [honeysql.helpers :as h]
   [honeysql.format :as sql]))


(def all-todos-query
  (-> (h/select :todo.*)
      (h/from :todo)))


(defn all-todos
  [tx]
  (->> (sql/format all-todos-query)
       (jdbc/query tx)))
