(ns de.sample.todoapp.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]))


(rf/reg-event-db
  :app/init
  (fn [db event]
    {:message "Hello World"
     :todos {1  {:id 1 :position 1 :label "Clean up living room" :done? false}
             2  {:id 2 :position 2 :label "Relax" :done? false}}}))


(rf/reg-sub
 :app/message
 (fn [db query]
   (:message db)))


(rf/reg-sub
 :app/todos
 (fn [db _]
   (->> db :todos (vals) (sort-by :position))))

(rf/reg-event-db
 :app/set
 (fn [db [_ path value]]
   (js/console.log "Setting" (pr-str path) "to" (pr-str value))
   (assoc-in db path value)))


(rf/reg-event-db
 :todo/new
 (fn [db [_]]
   (let [todos    (->> db :todos (vals))
         temp-id  (->> todos (map :id) (remove pos?) (reduce min 0) (dec))
         position (count todos)]
     (assoc-in db [:todos temp-id]
               {:id       temp-id
                :position position
                :label    ""
                :done?    false}))))

(defn textfield
  [{:keys [id path label]} value]
  (fn [_ value]
    [:div.formfield.textfield
     (if label
       [:label {:for (str id "-textfield")} label])
     [:input {:id (str id "-textfield")
              :value value
              :on-change (fn [e]
                           (rf/dispatch-sync [:app/set path (-> e .-target .-value)]))}]]))

(defn checkbox
  [{:keys [id path label]} value]
  (fn [_ value]
    [:div.formfield.checkbox
     [:input {:id        (str id "-checkbox")
              :type      "checkbox"
              :checked   value
              :on-change (fn [e]
                           (rf/dispatch-sync [:app/set path (-> e .-target .-checked)]))}]]))

(defn button
  [{:keys [id label event]}]
  [:div.formfield.button
   [:button {:id id
             :on-click (fn [e]
                         (rf/dispatch event))}
    label]])


(defn todos
  []
  (let [!todos (rf/subscribe [:app/todos])]
    [:div.todos
     [:legend "My todo items"]
     [:ul (for [{:keys [id label done?]} @!todos]
            [:li.todo
             {:key id}
             [:span.id id]
             [checkbox {:path [:todos id :done?]}
              done?]
             [textfield {:path [:todos id :label]}
              label]])]
     [button {:id :add
              :label "+ Add item"
              :event [:todo/new]}]]))

(defn app
  []
  (let [!message (rf/subscribe [:app/message])]
    (fn []
      [:div
       [todos]])))


(defn mount!
  []
  (js/console.log "Hello World")
  (rf/dispatch-sync [:app/init])
  (reagent/render [app]
                  (js/document.getElementById "app")))


(mount!)
