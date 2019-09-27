(ns de.sample.todoapp.frontend.app.handler
  (:require
   [re-frame.core :as rf]

   [de.sample.todoapp.frontend.routing :as routing]))


(rf/reg-event-db
 :app/init
 (fn [db [_ seed]]
   seed))


(rf/reg-event-db
 :app/set
 (fn [db [_ path value]]
   (let [path (into [:app/formstate] path)]
     (js/console.log "Setting" (pr-str path) "to" (pr-str value))
     (assoc-in db path value))))


(rf/reg-event-db
 :app/route
 (fn [db [_ from-url to-url]]
   (let [routing {:url to-url
                  :route (routing/parse-from-url to-url)}]
     (js/console.log "Route set" (pr-str routing))
     (assoc db :app/routing routing))))
