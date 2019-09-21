(ns de.sample.todoapp.backend.services.ui.todos
  (:require
   [de.sample.todoapp.backend.services.ui :refer [service]]))


(defmethod service :todos/load
  [context request]
  {:data [{:id 1 :position 1 :label "Clean up living room" :done? false}
           {:id 2 :position 2 :label "Relax" :done? false}]})


(defmethod service :todos/save
  [context request]
  )
