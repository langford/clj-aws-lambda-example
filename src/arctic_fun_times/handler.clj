(ns arctic-fun-times.handler
    (:require 
      [ring.middleware.params :refer [wrap-params]]
      [ring.middleware.keyword-params :refer [wrap-keyword-params]]
      [ring.middleware.json :refer [wrap-json-params
                                    wrap-json-response]]
      [ring.util.response :as r]
      [ring.util.http-response :refer :all]
      [ring-aws-lambda-adapter.core :refer [defhandler]]

      [compojure.core :refer :all]
      [compojure.route :as route]
      [compojure.handler :refer [site]]

      [clojure.data.json  :as json]
      [arctic-fun-times.db :as db]))


(defn handle-get-main-list
  "This is a list of all the event attendees"
  []
  (r/response (db/main-list-fetch)))

(defn handle-add-to-list
  "This adds a new attendee to the list"
  [name event]
  (println "name " name "event " event)
  (db/add-to-list name event) 
  (handle-get-main-list))

(defroutes app-routes 
  (GET "/"
       []
       (handle-get-main-list)) 
 
 (POST "/"
      req 
      (println req)
      (let [name (get-in req [:json-params "name"])
            event (get-in req [:json-params "event"])]
      (handle-add-to-list name
                          event)))

 (route/not-found (Exception. "404: Page Not Found")))

(defhandler arctic-fun-times.handler.thelambdafn 
  (wrap-json-response
    (wrap-json-params
      (wrap-params
        (wrap-keyword-params
          app-routes)))) {})
          
(def app (wrap-json-response (wrap-json-params (wrap-params app-routes))))

(defn run-locally
  "this is for locally running the web server, but is entirely ignored by lambda"
  [req]
  (app req))

