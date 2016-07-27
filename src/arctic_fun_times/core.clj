(ns arctic-fun-times.core
    (:require 
      [arctic-fun-times.db :as db]
      [environ.core :refer [env]]
      [arctic-fun-times.handler :as handler]
      [org.httpkit.server :refer [run-server]]))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 8100))]
    (println "Starting web server on port " port ".\n")
    (run-server handler/run-locally {:port port})))

(defn -main [& [port]]
  (db/setup)
  (run-web-server port))
