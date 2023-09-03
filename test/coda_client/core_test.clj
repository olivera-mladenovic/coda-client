(ns coda-client.core-test
  (:require [clojure.test :refer :all]
            [coda-client.core :refer :all]
            [environ.core :refer [env]]))

(def key (env :api-key))
(def wrong-key (env :wrong-api-key))
(def params {:limit 1})
(def doc-data {:title "Testing doc"})

;; TESTING DOCS ENDPOINTS
(deftest test-list-docs-exception
  (testing "Listing docs with invalid api key"
    (is (thrown? clojure.lang.ExceptionInfo
                 (list-docs wrong-key nil)))))

(deftest test-list-docs-params
  (testing "Listing docs with params"
    (is (= 1 (count (:items (list-docs key params)))))))

(deftest test-create-fetch-delete-doc
  (testing "Creating, fetching and deleting doc"
    (let [created-doc-id (:id (create-doc key doc-data))]
      (println (get-doc key created-doc-id))
      (is true (delete-doc key created-doc-id)))))

;; TESTING PERMISSIONS ENDPOINTS
(def test-doc-id "PO3l9QQ8C7")
(def permission {:access "write" :principal {:type "email" :email "testing@email.com"}})
(def invalid-permission-id "111")
(def acl-settings {:allowEditorsToChangePermissions true :allowCopying true :allowViewersToRequestEditing true})

(deftest test-get-sharing-metadata
  (testing "Getting sharing metadata for doc"
    (is (not (= nil (get-sharing-metadata key test-doc-id))))))

(deftest test-add-permission
  (testing "Adding new permission for doc"
    (is (= true (add-permission key test-doc-id permission)))))

(deftest test-delete-permission
  (testing "Deleting permission with invalid permission id")
  (is (thrown? clojure.lang.ExceptionInfo
               (delete-permission key test-doc-id invalid-permission-id))))

(deftest test-update-acl-settings
  (testing "Updating acl settings for doc"
    (is (= acl-settings (update-acl-settings key test-doc-id acl-settings)))))

