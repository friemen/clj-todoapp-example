(ns de.sample.todoapp.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]

   [de.sample.todoapp.frontend.remote :as remote]
   [de.sample.todoapp.frontend.routing :as routing]

   ;; forms
   [de.sample.todoapp.frontend.forms.todo :as todo]))


(rf/reg-event-db
 :app/init
 (fn [db event]
   {:app/routing   {:handler :todo}
    :app/formstate {}}))


(rf/reg-event-db
 :app/set
 (fn [db [_ path value]]
   (let [path (into [:app/formstate] path)]
     (js/console.log "Setting" (pr-str path) "to" (pr-str value))
     (assoc-in db path value))))


(rf/reg-sub
 :app/formstate
 (fn [db _]
   (:app/formstate db)))


(rf/reg-sub
 :app/handler
 (fn [db _]
   (get-in db [:app/routing :route :handler])))


(defn app
  []
  (let [!handler (rf/subscribe [:app/handler])]
    [:div
     (case @!handler
       :todos
       [todo/view]
       (nil :default)
       [:div "Oops, unknown handler: " (pr-str @!handler)])]))


(defn mount!
  []
  (routing/init!)
  (reagent/render [app]
                  (js/document.getElementById "app")))


(defn reload
  []
  (aset js/window "debug_ui" 1)
  (mount!))


(defn start!
  []
  (js/console.log "Starting frontend")
  (rf/dispatch-sync [:app/init])
  (mount!)
  (routing/goto! {:handler :todos}))

(start!)
