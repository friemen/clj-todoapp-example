(ns de.sample.todoapp.backend.services.ui.todos
  (:require
   [de.sample.todoapp.backend.services.ui :refer [service]]
   [de.sample.todoapp.backend.database.queries :as queries]
   [de.sample.todoapp.backend.database.mutations :as mutations]
   [clojure.java.jdbc :as jdbc]))


(defmethod service :todos/load
  [{:keys [tx] :as ctx} _]
  {:data (queries/all-todos tx)})


(defmethod service :todos/save
  [{:keys [tx] :as ctx} {:keys [todos]}]
  (let [delete (->> todos (filter :delete?) (map :id))
        upsert (->> todos (remove :delete?))]
    (doseq [id delete]
      (jdbc/delete! tx :todo ["id=?" id]))
    (doseq [todo upsert]
      (mutations/upsert! tx :todo todo))
    {:data (queries/all-todos tx)}))
