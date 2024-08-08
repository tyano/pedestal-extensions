(ns org.clojars.t-yano.pedestal-extensions.handler
  (:require [ring.util.response :refer [response status]]))

(defn exec-handler
  [context validator-fn exec-fn]
  (let [[errors params] (validator-fn context)
        result (if (seq errors)
                 (-> (response errors)
                     (status 400))

                 (exec-fn context params))]
    (assoc context :response result)))

(defmacro defhandler
  [intercepter-sym doc-string bindings & body]
  (let [metadata            (meta intercepter-sym)
        resolved-doc-string (if (string? doc-string) doc-string "")
        resolved-bindings   (if (string? doc-string) bindings doc-string)
        resolved-body       (if (string? doc-string) body (cons bindings body))

        [validation-key validation-body :as validation-pair] (take 2 resolved-body)
        exec-body       (drop 2 resolved-body)
        context-binding (first resolved-bindings)]

    (when-not (or (= validation-key :validate) (nil? validation-body))
      (throw (ex-info "The first 2 exprs in body must be `:validate validate-expression`" {:validation-pair validation-pair})))

    (let [current-ns (str *ns*)
          intercepter-sym intercepter-sym
          handler-sym (symbol (str (name intercepter-sym) "-handler"))
          intercepter-key (keyword current-ns (name intercepter-sym))]
      `(do
         (defn ~handler-sym
           ~resolved-doc-string
           [context#]
           (exec-handler context#
                         (fn [~context-binding] ~validation-body)
                         (fn ~resolved-bindings ~@exec-body)))
         (def ~(with-meta intercepter-sym metadata)
           (with-meta
             {:name ~intercepter-key
              :enter ~handler-sym}
             ~metadata))))))