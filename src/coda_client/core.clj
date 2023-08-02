(ns coda-client.core
  (:require [clojure.core :as clojure]
            [clj-http.client :as http]
            [cheshire.core :as json]))
            
(defn make-api-request [method endpoint config data]
  (let [api-url (str (:base-url config) endpoint)
        body-data (if (nil? data)
                    nil
                    (json/generate-string data)
                    )]
    (http/request 
     {:method method :url api-url :body body-data :headers
      {:Authorization (str "Bearer " (:api-key config))
       :content-type "application/json"}})))


(defn list-docs [config]
  (let [endpoint "/docs"
        response (make-api-request :get endpoint config nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info "Failed to list docs"
                      {:status (:status response)
                       :body (:body response)})))))


(defn create-doc [config data]
  (let [endpoint "/docs"
        response (make-api-request :post endpoint config data)]
    (if (= (:status response) 201)
      (json/parse-string (:body response) true)
      (throw (ex-info "Failed to create doc"
                      {:status (:status response)
                       :body (:body response)})))))

