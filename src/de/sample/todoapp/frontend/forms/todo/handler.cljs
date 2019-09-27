(ns de.sample.todoapp.frontend.forms.todo.handler
  (:require
   [re-frame.core :as rf]
   [de.sample.todoapp.frontend.remote :as remote]))



(rf/reg-event-db
 :todo/remote-save-request
 (fn [db _]
   (remote/backend [{:service-id :todos/save
                     :todos      (-> db :app/formstate :todos (vals))}]
                   :todo/remote-load-response)
   db))


(rf/reg-event-db
 :todo/remote-load-request
 (fn [db _]
   (remote/backend [{:service-id :todos/load}]
                   :todo/remote-load-response)
   db))


(rf/reg-event-db
 :todo/remote-load-response
 (fn [db [_ [todos]]]
   (js/console.log "Received todos" (pr-str todos))
   (assoc-in db [:app/formstate :todos]
             (->> todos
                  (map (juxt :id identity))
                  (into {})))))


(rf/reg-event-db
 :todo/new
 (fn [db [_]]
   (let [todos    (->> db :app/formstate :todos (vals))
         temp-id  (->> todos (map :id) (remove pos?) (reduce min 0) (dec))
         position (count todos)]
     (assoc-in db [:app/formstate :todos temp-id]
               {:id       temp-id
                :position position
                :label    ""
                :done     false}))))


(rf/reg-event-db
 :todo/delete
 (fn [db [_ id]]
   (if (js/window.confirm (str "Delete item " id "?"))
     (let [todos
           (-> db :app/formstate :todos (assoc-in [id :delete?] true))

           position-id-pairs
           (->> todos
                (vals)
                (remove :delete?)
                (map :id)
                (map-indexed vector))

           todos
           (reduce (fn [todos [position id]]
                     (assoc-in todos [id :position] position))
                   todos
                   position-id-pairs)]
       (assoc-in db [:app/formstate :todos] todos))
     db)))
