(defproject todoapp "0.1.0-SNAPSHOT"
  :description
  "A sample todo manager as demonstration of a Clojure
  single page webapp."

  :url
  "https://github.com/friemen/todoapp"

  :dependencies
  [[org.clojure/clojure "1.10.1"]

   [com.stuartsierra/component "0.4.0"]
   [http-kit "2.3.0"]
   ]

  :profiles
  {:dev {:source-paths
         ["dev"]
         :dependencies
         [[org.clojure/tools.namespace "0.3.1"]]}}

  :repl-options
  {:init-ns user})
