(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [com.stuartsierra.component :as c]
            [de.sample.todoapp.backend.core :as core]))

(def system
  nil)

;; TODO load config from file
(def config
  {:server {:port 1337}})

(defn system-init! []
  (alter-var-root #'system
                  (constantly (core/new-system config))))

(defn system-start! []
  (alter-var-root #'system c/start))

(defn system-stop! []
  (alter-var-root #'system
    (fn [s] (when s (c/stop s)))))

(defn system-go! []
  (system-init!)
  (system-start!))

(defn system-restart! []
  (system-stop!)
  (refresh :after 'user/system-go!))
