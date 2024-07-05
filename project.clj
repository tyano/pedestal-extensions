(defproject pedestal-extensions "1.0-SNAPSHOT"
  :description "Utility functions for Pedestal"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :profiles
  {:provided {:dependencies [[io.pedestal/pedestal.service "0.6.4"]]}}
  :repl-options {:init-ns pedestal-extensions.core})
