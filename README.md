# coda-client

A Clojure library designed to programmatically interact with Coda docs:

## Instalation
Instalation info found on [clojars](https://clojars.org/net.clojars.zenerzeppelin/coda-client/versions/0.1.0-SNAPSHOT):

The latest version:
[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.zenerzeppelin/coda-client.svg)](https://clojars.org/net.clojars.zenerzeppelin/coda-client)
## Usage

This library uses the same method names as found in [Coda docs](https://coda.io/developers/apis/v1). It has all features supported by Coda's REST API:
* **DOCS** endpoints (listing docs, creating doc, get info about specific doc, delete doc)
* **PERMISSIONS** endpoints (listing, deleting and adding persmission, get sharing metadata, search principals, get or update ACL settings)
* **PUBLISHING** endpoints (get doc categories, publish or unpublish doc)
* **PAGES** endpoints (list pages, create, update or get a page)
* **AUTOMATIONS** (trigger automation)
* **TABLES** (list or get a table)
* **COLUMNS** (list or get a column)
* **ROWS** (list table rows, insert or upsert roes, delete multiple rows, get, update or delete a row, push a button on a row in table)
* **FORMULAS** (list or get a formula)
* **CONTROLS** (List or get a control)
* **ACCOUNT** (get user info)
* **ANALYTICS** (list doc or page analytics, get doc analytics summary, get unalytics last updated day...)
* **MISCELLANEOUS** (resolve browser link or get mutation status)

## Token

You need to generate token in your Coda profile settings. Do not share token with anyone and do not use it on client side!

## Consistency

Changes made in Coda are real-time, but it can take a few seconds for them to become available via the API. **SO IF YOU EXPERIENCE HAVING NOT UP-TO-DATE DATA, IT IS NOT A BUG WITHIN LIBRARY.** Similarly, changes made via API are not immediate. There endpoints return HTTP 202 status code, indicating that the edit has been accepted and queued for processing.

## Usage

Library uses kebab-case naming convention.
~~~
    (ns example.core
        (:require [coda-client.core :as coda]))
        
    (def api-key "your-secret-key") ;;prefer reading it from env to keep it secret
    
    (coda/get-user-info api-key)

    ;;expect your response to look like this

    {:name "Your name",
    :loginId "your id or email",
    :type "user",
    :href "link to your profile",
    :tokenName "name used when generating token",
    :scoped boolean
    :pictureLink "link to your avatar",
    :workspace {
        :id "id of workspace",
        :type "workspace",
        :browserLink "link to your browser"
        :name "workspace name"
      }
    }
~~~
All the functions are called with arguments in this order:
1. api-key
2. body-data
3. parameters

For functions that support parameters, you must send `nil` if you want to skip them and get all the data.
Example:
~~~
;;list all docs
(coda/list-docs api-key nil)

;;limit to only one doc
(coda/list-docs api-key {:limit 1})
~~~
 
 ## Error Handling
 
 Every function handles error on its own. Which means that every function will throw [ex-info](https://clojuredocs.org/clojure.core/ex-info) with a detailed explanation from the response (the status code and body from the response)
