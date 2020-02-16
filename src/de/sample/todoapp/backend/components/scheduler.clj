(ns de.sample.todoapp.backend.components.scheduler
  (:require
   [clojure.core.async :as async :refer [<! go go-loop]]
   [taoensso.timbre :as log]
   [com.stuartsierra.component :as c]
   [chime :as chime]
   [cronstar.core :as cron]
   [de.sample.todoapp.backend.protocols.itask :as task]))



(defn- <start-process!
  [task-key task times]
  (let [ch (chime/chime-ch times)]
    (log/info "Starting lightweight process for task" task-key)
    (go-loop []
      (if-let [dt (<! ch)]
        (do
          (future (try
                    (log/info "Executing task" task-key)
                    (task/execute! task dt)
                    (catch Exception ex
                      (log/error ex "Error executing task" task-key))))
          (recur))))
    (log/info "First planned execution for task" task-key (first times))
    ch))


(defn- start-task!
  [schedules [task-key task]]
  (let [expr
        (get schedules task-key)

        _
        (log/debug "Cron expression for task" task-key expr)

        times
        (try (cron/times expr)
             (catch Exception ex
               (log/warn "Deactived task caused by failure to parse cron expression" task-key)
               nil))]
    (if times
      [task-key (assoc task
                       :ch (<start-process! task-key task times))]
      [task-key task])))


(defn- stop-task!
  [[task-key {:keys [ch] :as task}]]
  (when ch (async/close! ch))
  [task-key (assoc task :ch nil)])



(defn- tasks
  [component]
  (filter #(->> % (second) (satisfies? task/ITask)) component))


(defrecord Scheduler [schedules]
  c/Lifecycle

  (start [component]
    (println ";; Starting Scheduler")
    (->> (tasks component)
         (map (partial start-task! schedules))
         (into component)))

  (stop [component]
    (println ";; Stopping Scheduler")
    (->> (tasks component)
         (map stop-task!)
         (into component))))


;; Public API

(defn new-scheduler
  [config]
  (map->Scheduler config))
