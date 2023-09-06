(ns coda-client.core
  (:require [clojure.core :as clojure]
            [clj-http.client :as http]
            [cheshire.core :as json]))

(defn make-api-request [method endpoint api-key data params]
  (let [api-url (str "https://coda.io/apis/v1" endpoint)
        body-data (if (nil? data)
                    nil
                    (json/generate-string data))]
    (http/request
     {:method method :url api-url :body body-data :query-params params :throw-exceptions false :headers
      {:Authorization (str "Bearer " api-key)
       :content-type "application/json"}})))

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
      true
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

;; PUBLISHING endpoints
(defn get-doc-categories [api-key]
  (let [endpoint "/categories"
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get acl doc categories."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn publish-doc [api-key doc-id data]
  (let [endpoint (str "/docs/" doc-id "/publish")
        response (make-api-request :put endpoint api-key data nil)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to publish a doc."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn unpublish-doc [api-key doc-id]
  (let [endpoint (str "/docs/" doc-id "/publish")
        response (make-api-request :delete endpoint api-key nil nil)]
    (if (= (:status response) 200)
      true
      (throw (ex-info (str "Failed to unpublish doc."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;; PAGES endpoints
(defn list-pages [api-key doc-id params]
  (let [endpoint (str "/docs/" doc-id "/pages")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list pages."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn create-page [api-key doc-id data]
  (let [endpoint (str "/docs/" doc-id "/pages")
        response (make-api-request :post endpoint api-key data nil)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to create page."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-page [api-key doc-id page-id]
  (let [endpoint (str "/docs/" doc-id "/pages/" page-id)
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get page."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn update-page [api-key doc-id page-id data]
  (let [endpoint (str "/docs/" doc-id "/pages/" page-id)
        response (make-api-request :put endpoint api-key data nil)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to update a page."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;;AUTOMATION
(defn trigger-automation [api-key doc-id rule-id data]
  (let [endpoint (str "/docs/" doc-id "/hooks/automation/" rule-id)
        response (make-api-request :post endpoint api-key data nil)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to trigger automation."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;; TABLES
(defn list-tables [api-key doc-id params]
  (let [endpoint (str "/docs/" doc-id "/tables")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list tables."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-table [api-key doc-id table-id params]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id)
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get table."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;; COLUMNS
(defn list-columns [api-key doc-id table-id params]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/columns")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list columns"
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-column [api-key doc-id table-id column-id]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/columns/" column-id)
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get column."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;; ROWS
(defn list-rows [api-key doc-id table-id params]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list rows."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn insert-rows [api-key doc-id table-id data params]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows")
        response (make-api-request :post endpoint api-key data params)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to create rows."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn delete-rows [api-key doc-id table-id data]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows")
        response (make-api-request :delete endpoint api-key data nil)]
    (if (= (:status response) 202)
      true
      (throw (ex-info (str "Failed to delete rows."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-row [api-key doc-id table-id row-id params]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows/" row-id)
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get a row."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn update-row [api-key doc-id table-id row-id data params]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows/" row-id)
        response (make-api-request :put endpoint api-key data params)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to update a row."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn delete-row [api-key doc-id table-id row-id]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows/" row-id)
        response (make-api-request :delete endpoint api-key nil nil)]
    (if (= (:status response) 202)
      true
      (throw (ex-info (str "Failed to delete row."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn push-button [api-key doc-id table-id row-id column-id]
  (let [endpoint (str "/docs/" doc-id "/tables/" table-id "/rows/" row-id "/buttons/" column-id)
        response (make-api-request :post endpoint api-key nil nil)]
    (if (= (:status response) 202)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to push button."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;;FORMULAS
(defn list-formulas [api-key doc-id params]
  (let [endpoint (str "/docs/" doc-id "/formulas")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list formulas."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-formula [api-key doc-id formula-id params]
  (let [endpoint (str "/docs/" doc-id "/formulas/" formula-id)
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get formula."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;;CONTROLS
(defn list-controls [api-key doc-id params]
  (let [endpoint (str "/docs/" doc-id "/controls")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list controls."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-control [api-key doc-id control-id params]
  (let [endpoint (str "/docs/" doc-id "/controls/" control-id)
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get control."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;;ACCOUNT
(defn get-user-info [api-key]
  (let [endpoint "/whoami"
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get user info."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;;ANALYTICS
(defn list-doc-analytics [api-key params]
  (let [endpoint "/analytics/docs"
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list doc analytics."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn list-page-analytics [api-key doc-id params]
  (let [endpoint (str "/analytics/docs/" doc-id "/pages")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list page analytics."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-analytics-summary [api-key params]
  (let [endpoint "/analytics/docs/summary"
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get summarized analytics."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn list-pack-analytics [api-key params]
  (let [endpoint "/analytics/packs"
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to list analytics for packs."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-pack-analytics-summary [api-key params]
  (let [endpoint "/analytics/packs/summary"
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get summarized analytics for packs."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-pack-formulas-analytics [api-key pack-id params]
  (let [endpoint (str "/analytics/packs/" pack-id "/formulas")
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get analytics for pack formulas."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-analytics-last-updated [api-key]
  (let [endpoint "/analytics/updated"
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get days when analytics were last updated."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

;;MISCELLANEOUS
(defn resolve-browser-link [api-key params]
  (let [endpoint "/resolveBrowserLink"
        response (make-api-request :get endpoint api-key nil params)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to resolve browser link."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))

(defn get-mutation-status [api-key request-id]
  (let [endpoint (str "/mutationStatus/" request-id)
        response (make-api-request :get endpoint api-key nil nil)]
    (if (= (:status response) 200)
      (json/parse-string (:body response) true)
      (throw (ex-info (str "Failed to get mutation status."
                           " Response: " (:body response))
                      {:status (:status response)
                       :body (:body response)})))))
