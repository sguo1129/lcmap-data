(ns lcmap.data.dev
  (:require [lcmap.data.config :as config]
            [lcmap.data.system :as system]
            [lcmap.data.ingest :as ingest]
            [lcmap.data.tile :as tile]
            [lcmap.data.tile-spec :as tile-spec]
            [lcmap.data.scene :as scene]
            [lcmap.data.util :as util]
            [lcmap.data.espa  :as espa]
            [gdal.core :as gc]
            [gdal.dataset :as gd]
            [gdal.band :as gb]
            [clojurewerkz.cassaforte.client :as cc]
            [clojurewerkz.cassaforte.query :as cq]
            [clojurewerkz.cassaforte.utils :as cu]
            [clojurewerkz.cassaforte.cql :as cql]
            [clojure.tools.logging :as log]
            [twig.core :as logger]
            [clojure.tools.namespace.repl :as repl]
            [com.stuartsierra.component :as component]
            [leiningen.core.project :as lein-prj]))

(def sys nil)

(def cfg-opts config/defaults)

(defn init
  "Prepare the system without starting it"
  []
  (alter-var-root #'sys #(when-not % (system/build cfg-opts))))

(defn start
  "Start the system (if it exists)"
  []
  (alter-var-root #'sys #(when % (component/start %))))

(defn stop
  "Stop the system (if it exists)"
  []
  (alter-var-root #'sys #(when % (component/stop %))))

(defn deinit
  "Forget the system map"
  []
  (alter-var-root #'sys (fn [_] nil)))

(defn run
  "init -> start"
  []
  (init)
  (start))

(defn reset
  "stop -> init -> start -> refresh repl"
  []
  (stop)
  (deinit)
  (repl/refresh-all :after 'lcmap.data.dev/run))

(def reload #'reset)
