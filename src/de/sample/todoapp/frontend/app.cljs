(ns de.sample.todoapp.frontend.app
  (:require
   [re-frame.core :as rf]

   [de.sample.todoapp.frontend.app.handler]
   [de.sample.todoapp.frontend.app.subscriptions]
   ;; forms
   [de.sample.todoapp.frontend.forms.todo :as todo]))


(defn view
  []
  (let [!handler (rf/subscribe [:app/handler])]
    [:div
     (case @!handler
       :todos
       [todo/view]
       (nil :default)
       [:div "Oops, unknown handler: " (pr-str @!handler)])]))
