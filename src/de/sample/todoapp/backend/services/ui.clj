(ns de.sample.todoapp.backend.services.ui
  "UI devoted services multimethod."
  (:require
   [taoensso.timbre :as log]))


(defmulti service
  (fn [context request]
    (:service-id request)))


(defn invoke-services!
  [context request]
  (log/debug "Received service request" (:body request))
  (let [responses
        (->> request
             :body
             (mapv (partial service context)))]
    {:status 200
     :body responses}))
