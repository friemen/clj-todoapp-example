(ns de.sample.todoapp.frontend.views.todo.subscriptions
  (:require
   [re-frame.core :as rf]))


(rf/reg-sub
 :todo/items
 (fn [db _]
   (->> db :todos (vals)
        (remove :delete?)
        (sort-by :position))))
