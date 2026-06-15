(ns server.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [server.routes :as r]))

(defn- ler-body [req]
  (json/parse-string (slurp (:body req)) true))

(defn- json-resposta [resp]
  (-> resp
      (update :body json/generate-string)
      (assoc-in [:headers "Content-Type"] "application/json")))

;; Rotas - cada rota so pega o body/params e chama o handler correspondente em server.routes

(defroutes app-routes
  (GET "/" [] "Ligado!")

  (POST "/usuario" req
    (json-resposta (r/handle-registrar-usuario (ler-body req))))

  (GET "/usuario" _
    (json-resposta (r/handle-consultar-usuario)))

  (POST "/transacoes/alimento" req
    (json-resposta (r/handle-registrar-alimento (ler-body req))))

  (POST "/transacoes/exercicio" req
    (json-resposta (r/handle-registrar-exercicio (ler-body req))))

  (GET "/transacoes/extrato" req
    (json-resposta (r/handle-extrato (:params req))))

  (GET "/transacoes/saldo" req
    (json-resposta (r/handle-saldo (:params req))))

  (route/not-found {:erro "Rota nao encontrada"})

)

(def app
  (wrap-defaults app-routes api-defaults))