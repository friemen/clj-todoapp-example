(ns de.sample.todoapp.backend.protocols.itask)


(defprotocol ITask
  (execute! [this datetime]))
