(ns de.sample.todoapp.backend.tasks.reminder
  (:require
   [de.sample.todoapp.backend.protocols.itask :as task]))


(defrecord ReminderTask []
  task/ITask
  (execute! [task datetime]
    (println "Reminding someone about a todo")))
