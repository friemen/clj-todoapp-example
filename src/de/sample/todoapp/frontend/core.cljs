(ns de.sample.todoapp.frontend.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as rf]

   [de.sample.todoapp.frontend.remote :as remote]
   [de.sample.todoapp.frontend.routing :as routing]

   ;; forms
   [de.sample.todoapp.frontend.app :as app]))


(defn mount!
  []
  (routing/init!)
  (reagent/render [app/view]
                  (js/document.getElementById "app")))


(defn reload
  []
  (aset js/window "debug_ui" 1)
  (mount!))


(def seed
  {:app/routing   {:handler :todo}
   :app/formstate {}})


(defn start!
  []
  (js/console.log "Starting frontend")
  (rf/dispatch-sync [:app/init seed])
  (mount!)
  (routing/goto! {:handler :todos}))

(start!)
