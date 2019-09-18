(ns de.sample.todoapp.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]))


(rf/reg-event-db
  :app/init
  (fn [db event]
    {:message "Hello re-frame world."}))


(rf/reg-sub
 :app/message
 (fn [db query]
   (:message db)))


(defn app
  []
  (let [!message (rf/subscribe [:app/message])]
    (fn []
      [:div @!message])))


(defn mount!
  []
  (js/console.log "Hello World")
  (rf/dispatch-sync [:app/init])
  (reagent/render [app]
                  (js/document.getElementById "app")))


(mount!)
