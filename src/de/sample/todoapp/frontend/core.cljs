(ns de.sample.todoapp.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]

   [de.sample.todoapp.frontend.remote :as remote]
   [de.sample.todoapp.frontend.routing :as routing]

   ;; views
   [de.sample.todoapp.frontend.views.todo :as todo]))


(rf/reg-event-db
 :app/init
 (fn [db event]
   {}))


(rf/reg-event-db
 :app/set
 (fn [db [_ path value]]
   (js/console.log "Setting" (pr-str path) "to" (pr-str value))
   (assoc-in db path value)))


(defn app
  []
  [:div
   [todo/view]])


(defn mount!
  []
  (routing/init!)
  (reagent/render [app]
                  (js/document.getElementById "app")))


(defn start!
  []
  (js/console.log "Starting frontend")
  (rf/dispatch-sync [:app/init])
  (mount!))

(start!)
