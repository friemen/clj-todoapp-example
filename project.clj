(defproject todoapp "0.1.0-SNAPSHOT"
  :description
  "A sample todo manager as demonstration of a Clojure
  single page webapp."

  :url
  "https://github.com/friemen/todoapp"

  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [com.stuartsierra/component "0.4.0"]

   ;; web related
   [http-kit "2.3.0"]

   ;; DB related
   [com.h2database/h2 "1.4.199"]
   [org.clojure/java.jdbc "0.7.10"]
   [com.mchange/c3p0 "0.9.5.4"]]

  :profiles
  {:dev {:source-paths
         ["dev"]
         :dependencies
         [[org.clojure/tools.namespace "0.3.1"]]}}

  :repl-options
  {:init-ns user})
