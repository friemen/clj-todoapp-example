(ns de.sample.todoapp.frontend.forms.todo.subscriptions
  (:require
   [re-frame.core :as rf]))


(rf/reg-sub
 :todo/items
 ;;:<- [:app/formstate]
 (fn [db _]
   (->> db :app/formstate :todos (vals)
        (remove :delete?)
        (sort-by :position))))
