(ns de.sample.todoapp.backend.handler
  (:require
   [clojure.string :as str]
   [taoensso.timbre :as log]
   [compojure.core :as cp :refer [GET POST]]
   [compojure.route :as route]
   [hiccup.page :as hp]))


(defn- index
  []
  (hp/html5 [:head]
            [:body
             [:div#app
              "Loading..."]
             [:script {:type "text/javascript"
                       :src "js/main.js"}]]))


(defn- ui-routes
  []
  (cp/routes
   (GET "/" []
        (index))
   (route/resources "")
   (route/not-found "Page not found")))


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
  (-> (ui-routes)
      (wrap-exception)))
