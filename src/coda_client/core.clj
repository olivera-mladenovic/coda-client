(ns coda-client.core
  (:require [clojure.core :as clojure]
            [clj-http.client :as http]
            [cheshire.core :as json]))
            
(defn make-api-request [method endpoint api-key data params]
  (let [api-url (str "https://coda.io/apis/v1" endpoint)
        body-data (if (nil? data)
                    nil
                    (json/generate-string data)
                    )]
    (http/request
     {:method method :url api-url :body body-data :query-params params :throw-exceptions false :headers
      {:Authorization (str "Bearer " api-key)
       :content-type "application/json"}})
    ))

;; DOCS endpoints
(defn list-docs [api-key params]
  (let [endpoint "/docs"
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list docs."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn create-doc [api-key data]
  (let [endpoint "/docs"
        response (make-api-request :post endpoint api-key data nil)]
    (if (= (:status response) 201)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to create doc."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn delete-doc [api-key id]
  (let [endpoint (str "/docs/" id)
        response (make-api-request :delete endpoint api-key nil nil)]
    (if (= (:status response) 202)
      true
      (throw (ex-info (str "Failed to delete doc."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-doc [api-key id]
  (let [endpoint (str "/docs/" id)
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get doc."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;; PERMISSIONS endpoints
