(defproject todoapp "0.1.0-SNAPSHOT"
  :description
  "A sample todo manager as demonstration of a Clojure
  single page webapp."

  :url
  "https://github.com/friemen/todoapp"

  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [org.clojure/core.async "0.4.500"]

   ;; Frontend
   [org.clojure/clojurescript "1.10.520"]
   [re-frame "0.10.9"]
   [cljs-http "0.1.46"]
   [bidi "2.1.6"]

   ;; Backend general
   [com.stuartsierra/component "0.4.0"]
   [com.taoensso/timbre "4.10.0"]

   ;; Backend http related
   [http-kit "2.3.0"]
   [hiccup "1.0.5"]
   [compojure "1.6.1"]
   [ring/ring-core "1.7.1"]
   [ring/ring-defaults "0.3.2"]
   [ring/ring-anti-forgery "1.3.0"]
   [ring-transit "0.1.6"]

   ;; Scheduler related
   [jarohen/chime "0.2.2"]
   [cronstar "1.0.0"]

   ;; DB related
   [com.h2database/h2 "1.4.199"]
   [org.clojure/java.jdbc "0.7.10"]
   [honeysql "0.9.8"]
   [com.mchange/c3p0 "0.9.5.4"]]

  :plugins
  [[lein-cljsbuild "1.1.7"]
   [lein-figwheel "0.5.19"]]

  :profiles
  {:dev     {:source-paths
             ["dev"]
             :dependencies
             [[org.clojure/tools.namespace "0.3.1"]
              [cider/piggieback "0.4.1"]
              [figwheel-sidecar "0.5.19"
               :exclusions [org.clojure/tools.nrepl]]
              [re-frisk "0.5.3"]]
             :repl-options
             {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
             :cljsbuild
             {:builds {:dev
                       {:source-paths ["src"]
                        :figwheel     {:on-jsload "de.sample.todoapp.frontend.core/reload"}
                        :compiler     {:preloads   [re-frisk.preload]
                                       :main       "de.sample.todoapp.frontend.core"
                                       :asset-path "js/out"
                                       :output-to  "resources/public/js/main.js"
                                       :output-dir "resources/public/js/out"
                                       :source-map true}}}}}
   :uberjar {:aot  :all
             :main de.sample.todoapp.backend.main
             :prep-tasks
             ["compile" ["cljsbuild" "once"]]
             :cljsbuild
             {:builds {:main
                       {:source-paths ["src"]
                        :jar          true
                        :compiler     {:output-to     "resources/public/js/main.js"
                                       :optimizations :advanced
                                       :pretty-print  false}}}}}}

  :figwheel
  {:css-dirs
   ["resources/public/css"]}

  :uberjar-name
  "todoapp.jar"

  :repl-options
  {:init-ns user})
