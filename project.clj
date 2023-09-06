(defproject coda-client "0.1.0-SNAPSHOT"
  :description "Library for interacting with Coda docs"
  :url "https://github.com/olivera-mladenovic/coda-client"
  :license {:name "MIT License"
            :url "https://github.com/olivera-mladenovic/coda-client/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clj-http "3.12.0"]
                 [cheshire "5.11.0"]
                 [environ "0.5.0"]
                 ]
  :repositories {"clojars" "https://clojars.org/repo"}
  :group "net.clojars.zenerzeppelin group"
  :artifact "coda-library"
  :repl-options {:init-ns coda-client.core})

