(ns de.sample.todoapp.frontend.app.subscriptions
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :app/formstate
 (fn [db _]
   (:app/formstate db)))


(rf/reg-sub
 :app/handler
 (fn [db _]
   (get-in db [:app/routing :route :handler])))
