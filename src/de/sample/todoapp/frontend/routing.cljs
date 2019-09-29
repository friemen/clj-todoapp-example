(ns de.sample.todoapp.frontend.routing
  (:require
   [re-frame.core :as rf]
   [bidi.bidi :as bidi]
   [clojure.string :as str]))

;; frontend route definitions in bidi format
(def routes
  ["/" {""     :default
        "todos" {"" :todos
                 ["/" :id] :todo}}])

;; private

(defn- key-value
  [s]
  (let [[k v] (str/split s #"=")]
    [(keyword (bidi/url-decode k))
     (bidi/url-decode v)]))


(defn- query-params
  [url]
  (let [[address query-param-str]
        (str/split url #"\?")]
    (if-not (str/blank? query-param-str)
      (->> (str/split query-param-str #"&")
           (map key-value)
           (into {})))))


;; Public API

(defn parse-from-url
  [url]
  (let [[_ anker] (str/split url "#")]
    (if-let [route (bidi/match-route routes (str "/" anker))]
      (let [query-params (query-params anker)]
        (-> route
            (cond-> query-params
              (assoc :query-params query-params)))))))


(defn format-to-url
  [{:keys [handler route-params query-params] :as route}]
  (let [[server-part anker]
        (str/split js/window.location.href "#")]
    (str server-part "#"
         (if-let [uri (bidi/path-for* routes handler route-params)]
           (-> uri
               (subs 1)
               (cond-> (seq query-params)
                 (str "?"
                      (->> query-params
                           (map (fn [[k v]]
                                  (str (bidi/url-encode (name k)) "=" (bidi/url-encode v))))
                           (str/join "&")))))))))


(defn init!
  "Registers event handler on js/Window"
  []
  (rf/dispatch-sync [:app/route nil js/window.location.href])
  (js/window.addEventListener
   "hashchange"
   (fn [e]
     (rf/dispatch [:app/route (.-oldURL e) (.-newURL e)]))))


(defn goto!
  "Routes to the given URL or route map."
  [url-or-route]
  (let [url (if (map? url-or-route)
              (format-to-url url-or-route)
              url-or-route)]
    (rf/dispatch-sync [:app/route js/window.location.href url])
    (set! js/window.location.href url)))
