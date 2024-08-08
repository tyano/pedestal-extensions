(defproject org.clojars.t_yano/pedestal-extensions "1.0.1-SNAPSHOT"
  :description "Utility functions for Pedestal"
  :url "https://github.com/tyano/pedestal-extensions"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :profiles
  {:provided {:dependencies [[io.pedestal/pedestal.service "0.6.4"]]}}
  :repl-options {:init-ns org.clojars.t-yano.pedestal-extensions.handler})
