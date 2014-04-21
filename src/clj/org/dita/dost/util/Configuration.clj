(ns org.dita.dost.util.Configuration
  (:import (java.util HashMap))
  (:gen-class ;; :name org.dita.dost.util.Configurationx
   :init ctor
   :state processingMode
   :state configuration
   :constructors [[][]]))

(def printTransType
  (do
    (println "Hello from Configuration.clj printTransType!!")
    (HashMap.)))

(def processingMode 'LAX)

(def configuration
  ;; public final static Map<String, String> configuration;
  (do
    (println "Hello from Configuration.clj configuration!!")
    (HashMap.)))

(defn -ctor []
  (do
    (println "CONFIGURATION!"
             [[] ;; superclass args
              ;; val
              ])))
