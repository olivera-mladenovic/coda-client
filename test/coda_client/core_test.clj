(ns coda-client.core-test
  (:require [clojure.test :refer :all]
            [coda-client.core :refer :all]
            [environ.core :refer [env]]))

(def key (env :api-key))
(def wrong-key (env :wrong-api-key))
(def params {:limit 1})
(def doc-data {:title "Testing doc"})

;; TESTING LIST ENDPOINTS
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

