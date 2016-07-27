(ns arctic-fun-times.db
    (:require
      [taoensso.faraday :as far]
      [environ.core :refer [env]]))


;You generally want to never put keys in source files. Use profile.clj or 
; a manager like awspm. If you use a profile.clj, do your team a favor
; and include a profile.clj.template file in source control
(def client-opts 
  {:access-key (:ddb-access env) 
   :secret-key (:ddb-secret env)
   :endpoint   (:ddb-endpoint env)}) ; endpoint is datacenter/region specific

;This isn't used, but is an example
(def normal-table 
  {:pk [:a-primary-key :s]
   :opts {:block? true
          :throughput {:read 5 
                       :write 5}}})

(def event-table 
  {:pk [:event :s] 
   :opts { ;range-keydef is used to make a secondary part of the query key
          ;  you can only query on the primary key or range-keydef keys
          ;  consider using a relational database if you need more complex
          ;  queries 
          :range-keydef [:name :s] 
          :block? true
          :throughput {:read 5 
                       :write 5}}})

(def table-defintions 
  {:Events event-table})

(defn setup
  "this makes sure all the tables are there we use elsewhere"
  []
  (println "For DB in location " (:endpoint client-opts))
  (doseq [[table-name t] table-defintions]
         (far/ensure-table client-opts 
                           table-name
                           (:pk t)
                           (:opts t))))


(defn main-list-fetch
  "gives back a list of people attending"
  [& rs]
  (let [result (far/scan client-opts :Events)]
    (println result)
    result))

(defn add-to-list
  "adds a person to the list of attendees"
  [name event]
  (far/put-item client-opts ;how to connect to the database
                :Events     ;the table
                {:name name
                 :event event}))





