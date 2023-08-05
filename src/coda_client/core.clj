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
(defn get-sharing-metadata [api-key doc-id]
  (let [endpoint (str "/docs/" doc-id "/acl/metadata")
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get sharing metadata."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn list-permissions [api-key doc-id params]
  (let [endpoint (str "/docs/" doc-id "/acl/permissions")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list permissions."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn add-permission [api-key doc-id data]
  (let [endpoint (str "/docs/" doc-id "/acl/permissions")
        response (make-api-request :post endpoint api-key data nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to add permission."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn delete-permission [api-key doc-id permission-id]
  (let [endpoint (str "/docs/" doc-id "/acl/permissions/" permission-id)
        response (make-api-request :delete endpoint api-key nil nil)]
    (if (= (:status response) 200)
      true
      (throw (ex-info (str "Failed to delete permission"
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn search-principals [api-key doc-id search-term]
  (let [endpoint (str "/docs/" doc-id "/acl/principals/search")
        response (make-api-request :get endpoint api-key nil {:query search-term})]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to search principals"
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-acl-settings [api-key doc-id]
  (let [endpoint (str "/docs/" doc-id "/acl/settings")
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get acl settings"
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))


(defn update-acl-settings [api-key doc-id settings]
  (let [endpoint (str "/docs/" doc-id "/acl/settings")
        response (make-api-request :patch endpoint api-key settings nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to update acl settings"
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))