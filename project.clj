(defproject arctic-fun-times "0.1.0"
  :dependencies [
                 ;normal dependencies to make the code work anywhere
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-json "0.4.0"]
                 [metosin/ring-http-response "0.8.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.3"]
                 [ring "1.3.2"]
                 [http-kit "2.1.18"] 
                 [environ "1.0.3"]

                 ;code to talk to aws stuff, from anywhere
                 [com.amazonaws/aws-lambda-java-core "1.0.0"]
                 [com.taoensso/faraday "1.8.0"]  
               
                 ;code to make it possible to be in lambda 
                 [ring-aws-lambda-adapter "0.1.1"]

                 ;to render the view
                 [hiccup "1.0.5"]

                 ;quality of life  
                 [enlive "1.1.5"]
                 [io.aviso/pretty "0.1.26"]
                 [org.clojure/tools.trace "0.7.9"]
                 [venantius/ultra "0.4.1"]
                 [lein-kibit "0.1.2"]]
             

  :plugins [[lein-aws-api-gateway "1.10.68-1"] ; deployment of the aws gateway
            [lein-clj-lambda "0.4.1"] ;deployment of the code
            [lein-pprint "1.1.2"]
            [lein-cljfmt "0.5.3"]
            [lein-environ "1.0.3"]
            [io.aviso/pretty "0.1.26"] ]

  :lambda  { 
            "production" [{
                           ;this is what we're telling lambda to call
                           :handler "arctic-fun-times.handler.thelambdafn" 

                           ;You get slightly better warm up times with bigger instances, 
                           ;    keps you under 10 sec
                           :memory-size 1024 

                           ;10 sec is as high as they let you go for lambda functions 
                           ;    tied to the api gateway, otherwise max 5 minutes
                           ;    the value is in seconds...but you gotta make it higher
                           ;    to initially get the clojure function into the hot 
                           ;    cache
                           :timeout 300  

                           ;This is the name of the lambda function in the UI at AWS
                           :function-name "backendProduction"

                           ;Which datacenter you want to upload to. This is in N. Virgina
                           :region "us-east-1"

                           ;You don't *have* to upload to S3 first, but it is useful if you're
                           ;   wanting to keep an archive, or if you're wanting to use the same
                           ;   code in multiple regions/lambda functions
                           :s3 {:bucket "aft-api-clojure-build-bucket"
                                :object-key "production.jar"}}]

            "staging" [{:handler "arctic-fun-times.handler.thelambdafn"
                        :memory-size 1024
                        :timeout 300 
                        :function-name "backendStaging"
                        :region "us-east-1"
                        :s3 {:bucket "aft-api-clojure-build-bucket"
                             :object-key "staging.jar"}}]}

  :api-gateway {;This is the API ID you see on the AWS API gateway
                :api-id "booberry"

                ;This is a swagger file I got after using the UI on the AWS API Gateway, 
                ;    deploying, then exporting. It was not exportable until I deployed
                :swagger "resources/swagger.yaml"} 

  :main arctic-fun-times.core ;only used in the local execution situation

  :aot :all) ;; This project uses gen-class, hence the aot

