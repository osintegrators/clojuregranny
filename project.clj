(defproject clojuregranny "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [postgresql/postgresql "8.4-702.jdbc4"]
                 [ring/ring-json "0.1.2"]
                 [ring "1.1.8"]
                 [org.clojure/data.json "0.1.2"]
                 [ring-json-params "0.1.3"]
                 [clj-json "0.2.0"]                 
                 [cheshire "4.0.3"]]            
  :plugins [[lein-ring "0.8.2"]]
  :main clojuregranny.handler
  :ring {:handler clojuregranny.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
