(ns de.sample.todoapp.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]

   [cljs-http.client :as http]
   [cljs-http.util :refer [transit-encode]]
   [cljs.core.async :as async :refer [<!]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]
   [cljs.core :refer [exists?]]))



;; ---------------------------------------------

(def ^:private transit-mime-type
  "application/transit+json;charset=utf-8")

(defn backend
  [requests handler-id]
  (go
    (js/console.log "Remote request" (pr-str requests))
    (let [csrf-token (when (exists? js/__anti_forgery_token)
                       js/__anti_forgery_token)
          request
          {:content-type transit-mime-type
           :accept       transit-mime-type
           :headers      {"X-CSRF-Token" csrf-token}
           :body         (transit-encode requests :json {:encoding :json})}

          {:keys [status body] :as response}
          (<! (http/post "/ui" request))]
      ;; beware: logging responses is too expensive for production use
      (js/console.log "Received response" (pr-str response))
      (rf/dispatch [handler-id
                    (case status
                      200 (mapv #(let [{:keys [data error]} %]
                                   (if error
                                     (do (js/console.log "BACKEND RETURNED ERROR:" error)
                                         error)
                                     data))
                                body)
                      (js/Error. (str "Error, received status code: " status)))]))))

;; ---------------------------------------------

(rf/register-handler
 :todo/remote-save-request
 (fn [db _]
   (backend [{:service-id :todos/save
              :todos      (-> db :todos (vals))}]
            :todo/remote-save-response)
   db))


(rf/register-handler
 :todo/remote-save-response
 (fn [db [_ response]]
   db))


(rf/register-handler
 :todo/remote-load-request
 (fn [db _]
   (backend [{:service-id :todos/load}]
            :todo/remote-load-response)
   db))


(rf/register-handler
 :todo/remote-load-response
 (fn [db [_ [todos]]]
   (js/console.log "Received todos" (pr-str todos))
   (assoc db :todos (->> todos
                         (map (juxt :id identity))
                         (into {})))))



(rf/reg-event-db
 :app/init
 (fn [db event]
   {}))



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

(rf/reg-event-db
 :todo/delete
 (fn [db [_ id]]
   (if (js/window.confirm (str "Delete item " id "?"))
     (update db :todos dissoc id)
     db)))


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


(rf/reg-sub
 :app/todos
 (fn [db _]
   (->> db :todos (vals) (sort-by :position))))


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
              label]
             [:span.delete
              {:on-click (fn [e]
                           (rf/dispatch [:todo/delete id]))}
              "x"]])]
     [button {:id :add
              :label "+ Add item"
              :event [:todo/new]}]]))

(defn app
  []
  [:div
   [todos]])


(defn mount!
  []
  (js/console.log "Hello World")
  (rf/dispatch-sync [:app/init])
  (rf/dispatch [:todo/remote-load-request])
  (reagent/render [app]
                  (js/document.getElementById "app")))


(mount!)
