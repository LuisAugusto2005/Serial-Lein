(ns server.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [server.routes :as r]))

;; Rotas - cada rota so pega o body/params e chama o handler correspondente em server.routes

(defroutes app-routes
  (GET "/" [] "Ligado!")

  (POST "/usuario" req
    (r/handle-registrar-usuario (:body req)))

  (GET "/usuario" _
    (r/handle-consultar-usuario))

  (POST "/transacoes/alimento" req
    (r/handle-registrar-alimento (:body req)))

  (POST "/transacoes/exercicio" req
    (r/handle-registrar-exercicio (:body req)))

  (GET "/transacoes/extrato" req
    (r/handle-extrato (:params req)))

  (GET "/transacoes/saldo" req
    (r/handle-saldo (:params req)))

  (route/not-found {:erro "Rota nao encontrada"})
)

;; Stack de middleware do Ring

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? false})
      wrap-json-response
      (wrap-defaults api-defaults)
  )
)