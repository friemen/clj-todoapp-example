(ns de.sample.todoapp.backend.database.mutations
  (:require [clojure.java.jdbc :as jdbc]
            [taoensso.timbre :as log]))


(defn upsert!
  [tx table-key m]
  (let [id (:id m)]
    (if (and (not (nil? id)) (pos? id))
      ;; update
      (do (log/debug "Update DB with" (pr-str m))
          (jdbc/update! tx table-key (dissoc m :id) ["id=?" id])
          m)
      ;; insert
      (let [_      (log/debug "Insert into DB" (pr-str m))
            new-id (-> (jdbc/insert! tx table-key (dissoc m :id))
                       (first)
                       (second))]
        (assoc m :id new-id)))))
