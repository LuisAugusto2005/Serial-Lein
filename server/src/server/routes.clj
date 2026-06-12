(ns server.routes
  (:require [server.db :as db]
            [server.external :as ext]))

;; Funcoes para validar se os campos obrigatorios vieram no request

(defn- campos-usuario-validos? [dados]
  (every? #(contains? dados %) [:nome :altura :peso :idade :sexo])
)

(defn- campos-alimento-validos? [dados]
  (every? #(contains? dados %) [:alimento :quantidade-g :data])
)

(defn- campos-exercicio-validos? [dados]
  (every? #(contains? dados %) [:atividade :duracao-min :data])
)

(defn- campos-periodo-validos? [dados]
  (every? #(contains? dados %) [:data-inicio :data-fim])
)

;; Funcoes para montar a resposta HTTP

(defn- resposta-ok [corpo]
  {:status 200 :body corpo})

(defn- resposta-criado [corpo]
  {:status 201 :body corpo})

(defn- resposta-erro [status mensagem]
  {:status status :body {:erro mensagem}})

;; Handlers - chamados pelas rotas do handler.clj

(defn handle-registrar-usuario [dados]
  (if-not (campos-usuario-validos? dados)
    (resposta-erro 400 "Campos obrigatorios: nome, altura, peso, idade, sexo")
    (resposta-criado (db/salvar-usuario! dados))
  )
)

(defn handle-consultar-usuario []
  (if-let [u (db/obter-usuario)]
    (resposta-ok u)
    (resposta-erro 404 "Usuario nao cadastrado")
  )
)

(defn handle-registrar-alimento [dados]
  (if-not (campos-alimento-validos? dados)
    (resposta-erro 400 "Campos obrigatorios: alimento, quantidade-g, data")
    (let [calorias (ext/buscar-calorias-alimento (:alimento dados)
                                                  (:quantidade-g dados))]
      (if-not calorias
        (resposta-erro 422 (str "Alimento nao encontrado: " (:alimento dados)))
        (let [transacao { :tipo "alimento"
                          :descricao (:alimento dados)
                          :data (:data dados)
                          :calorias (double calorias)}]
          (resposta-criado (db/registrar-transacao! transacao))
        )
      )
    )
  )
)

(defn handle-registrar-exercicio [dados]
  (if-not (campos-exercicio-validos? dados)
    (resposta-erro 400 "Campos obrigatorios: atividade, duracao-min, data")
    (let [peso-kg (:peso (db/obter-usuario))
          calorias (ext/buscar-calorias-exercicio (:atividade dados)
                                                   (:duracao-min dados) peso-kg)]
      (if-not calorias
        (resposta-erro 422 (str "Atividade nao encontrada: " (:atividade dados)))
        (let [transacao { :tipo "exercicio"
                          :descricao (:atividade dados)
                          :data (:data dados)
                          :calorias (double calorias)}]
          (resposta-criado (db/registrar-transacao! transacao))
        )
      )
    )
  )
)

(defn handle-extrato [dados]
  (if-not (campos-periodo-validos? dados)
    (resposta-erro 400 "Campos obrigatorios: data-inicio, data-fim")
    (let [extrato (db/obter-extrato (:data-inicio dados) (:data-fim dados))]
      (resposta-ok {:transacoes extrato
                    :total (count extrato)})
    )
  )
)

(defn handle-saldo [dados]
  (if-not (campos-periodo-validos? dados)
    (resposta-erro 400 "Campos obrigatorios: data-inicio, data-fim")
    (resposta-ok {:saldo-calorias (double (db/obter-saldo (:data-inicio dados) (:data-fim dados)))})
  )
)