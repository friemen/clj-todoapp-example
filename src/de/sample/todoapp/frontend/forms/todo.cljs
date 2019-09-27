(ns de.sample.todoapp.frontend.forms.todo
  (:require
   [re-frame.core :as rf]
   [de.sample.todoapp.frontend.widgets :as w]
   [de.sample.todoapp.frontend.forms.todo.subscriptions]
   [de.sample.todoapp.frontend.forms.todo.handler]))

(defn view
  []
  (rf/dispatch [:todo/remote-load-request])
  (fn []
    (let [!todos (rf/subscribe [:todo/items])]
      [:div.todos
       [:legend "My todo items"]
       [:ul (for [{:keys [id position label done]} @!todos]
              [:li.todo
               {:key id}
               [:span.position (inc position) "."]
               [w/checkbox {:path [:todos id :done]}
                done]
               [w/textfield {:path [:todos id :label]}
                label]
               [:span.delete
                {:on-click (fn [e]
                             (rf/dispatch [:todo/delete id]))}
                "x"]
               [:span.id "Id " id]])]
       [w/button {:id    :add
                  :label "+ Add item"
                  :event [:todo/new]}]
       [w/button {:id    :save
                  :label "Save"
                  :event [:todo/remote-save-request]}]])))
