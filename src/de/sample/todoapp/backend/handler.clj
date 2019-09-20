(ns de.sample.todoapp.backend.handler
  (:require
   [clojure.string :as str]
   [taoensso.timbre :as log]
   [compojure.core :as cp :refer [GET POST]]
   [compojure.route :as route]
   [hiccup.page :as hp]
   [ring.util.response :as response]
   [ring.middleware.transit :refer [wrap-transit-response wrap-transit-body]]
   ))


(defn- index
  []
  (hp/html5 [:head
             [:link {:href "css/stylesheet.css" :rel "stylesheet" :type "text/css"}]]
            [:body
             [:div#app
              "Loading..."]
             [:script {:type "text/javascript"
                       :src "js/main.js"}]]))

(defn- wrap-transit
  [handler]
  (-> handler
      (wrap-transit-response)
      (wrap-transit-body)))


(defn- invoke-services!
  [request]
  (log/debug (:body request))
  {:status 200
   :body ["ok"]})


(defn- ui-routes
  []
  (-> (cp/routes
       (GET "/" []
            (index))
       (POST "/" request
             (invoke-services! request))
       (route/not-found "Page not found"))
      (wrap-transit)))


(defn- exception->str
  ([ex]
   (exception->str nil ex))
  ([intro ex]
   (if ex
     (let [lines
           (->> (.getStackTrace ex)
                (map (fn [^StackTraceElement e]
                       (.toString e)))
                (cons (str intro (.getMessage ex)))
                (concat (exception->str "Caused by: " (.getCause ex))))]
       (str/join "\n" lines)))))


(defn- wrap-exception
  [handler]
  (fn [req]
    (try (handler req)
         (catch Exception ex
           (log/error ex)
           {:status 500
            :body (str "An error has occurred:\n" (exception->str ex))}))))



(defn new-handler
  []
  (-> (cp/routes
       (route/resources "")
       (cp/context "/ui" []
                   (ui-routes))
       (GET "/" []
            (response/redirect "/ui")))
      (wrap-exception)))
