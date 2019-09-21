(ns de.sample.todoapp.backend.handler
  (:require
   [clojure.string :as str]
   [taoensso.timbre :as log]
   [compojure.core :as cp :refer [GET POST]]
   [compojure.route :as route]
   [hiccup.page :as hp]
   [ring.util.response :as response]
   [ring.middleware.transit :refer [wrap-transit-response wrap-transit-body]]
   [de.sample.todoapp.backend.services.ui :as ui]
   [de.sample.todoapp.backend.services.ui.todos]
   [clojure.java.jdbc :as jdbc]))


(defn- wrap-transaction
  [handler db]
  (fn [request]
    (jdbc/with-db-transaction [tx db]
      (handler (assoc-in request [::ctx :tx] tx)))))


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


(defn- wrap-transit
  [handler]
  (-> handler
      (wrap-transit-response)
      (wrap-transit-body)))



;; ----------------------------------------------------------

(defn- index
  []
  (hp/html5 [:head
             [:link {:href "css/stylesheet.css" :rel "stylesheet" :type "text/css"}]]
            [:body
             [:div#app
              "Loading..."]
             [:script {:type "text/javascript"
                       :src "js/main.js"}]]))

(defn- ui-routes
  [db]
  (-> (cp/routes
       (GET "/" []
            (index))
       (POST "/" request
             (ui/invoke-services! (::ctx request) request))
       (route/not-found "Page not found"))
      (wrap-transaction db)
      (wrap-transit)))




(defn new-handler
  [db]
  (-> (cp/routes
       (route/resources "")
       (cp/context "/ui" []
                   (ui-routes db))
       (GET "/" []
            (response/redirect "/ui")))
      (wrap-exception)))
