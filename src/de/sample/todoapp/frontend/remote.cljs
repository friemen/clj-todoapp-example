(ns de.sample.todoapp.frontend.remote
  (:require
   [re-frame.core :as rf]

   [cljs-http.client :as http]
   [cljs-http.util :refer [transit-encode]]
   [cljs.core.async :as async :refer [<!]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]
   [cljs.core :refer [exists?]]))


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
      #_(js/console.log "Received response" (pr-str response))
      (js/console.log "Received response")
      (rf/dispatch [handler-id
                    (case status
                      200 (mapv #(let [{:keys [data error]} %]
                                   (if error
                                     (do (js/console.log "BACKEND RETURNED ERROR:" error)
                                         error)
                                     data))
                                body)
                      (js/Error. (str "Error, received status code: " status)))]))))
