(ns cliente.http
  (:require [clj-http.client :as http]
            [cheshire.core :as json])
)

(def base-url
  (or (System/getenv "API_URL") "http://localhost:3000"))

(defn- json-headers []
  {"Content-Type" "application/json"
   "Accept" "application/json"}
  )

(defn- endpoint [caminho]
  (str base-url caminho)
)

(defn- tratar-resposta [resp]
  (let [status (:status resp)
        corpo  (:body resp)]
    (if (< status 400)
      {:ok? true
       :dados corpo}
      {:ok? false
       :erro (:erro corpo "Erro")}
    )
  )
)

(defn- post! [caminho corpo]
  (try
    (let [resp (http/post (endpoint caminho)
                          {:headers (json-headers)
                           :body (json/generate-string corpo)
                           :as :json
                           :content-type :json})]
      (tratar-resposta resp)
    )
    ;; descobri que tem try no clojure depois que testei e descobri que estava na porta errada ww - Luis
    (catch Exception e
      {:ok? false :erro (str "Falha na conexao: " (.getMessage e))}
    )
  )
)

(defn- get! [caminho params]
  (try
    (let [resp (http/get (endpoint caminho)
                         {:headers (json-headers)
                          :query-params params
                          :as :json})]
      (tratar-resposta resp)
    )
    (catch Exception e
      {:ok? false :erro (str "Falha na conexao: " (.getMessage e))}
    )
  )
)

(defn registrar-usuario! [dados]
  (post! "/usuario" dados)
)

(defn consultar-usuario! []
  (get! "/usuario" {})
)

(defn registrar-alimento! [dados]
  (post! "/transacoes/alimento" dados)
)

(defn registrar-exercicio! [dados]
  (post! "/transacoes/exercicio" dados)
)

(defn consultar-extrato! [data-inicio data-fim]
  (get! "/transacoes/extrato" {"data-inicio" data-inicio
                               "data-fim" data-fim})
)

(defn consultar-saldo! [data-inicio data-fim]
  (get! "/transacoes/saldo" {"data-inicio" data-inicio
                             "data-fim" data-fim})
)