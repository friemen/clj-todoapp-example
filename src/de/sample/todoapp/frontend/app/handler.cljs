(ns de.sample.todoapp.frontend.app.handler
  (:require
   [re-frame.core :as rf]))


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
