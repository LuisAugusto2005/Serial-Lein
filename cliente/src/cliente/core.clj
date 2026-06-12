(ns cliente.core
  (:require [cliente.http :as api]
            [cliente.ui :as ui])
  (:gen-class)
)

;; le numero/texto sempre com read-line para nao misturar com read
(defn- ler-numero []
  (Double/parseDouble (clojure.string/trim (read-line))))

(defn- ler-inteiro []
  (Integer/parseInt (clojure.string/trim (read-line))))

(defn- ler-texto []
  (clojure.string/trim (read-line)))

(defn cadastrar-usuario []
  (println "Digite seu nome:")
  (let [nome (ler-texto)]
    (println "Digite sua altura (m, ex: 1.75):")
    (let [altura (ler-numero)]
      (println "Digite seu peso (kg, ex: 70.0):")
      (let [peso (ler-numero)]
        (println "Digite sua idade:")
        (let [idade (ler-inteiro)]
          (println "Digite seu sexo (M/F):")
          (let [sexo (ler-texto)
                resp (api/registrar-usuario!
                        {:nome nome :altura altura
                         :peso peso :idade idade :sexo sexo})]
            (if (:ok? resp)
              (println (ui/formatar-usuario (:dados resp)))
              (println (ui/formatar-erro (:erro resp))))
          )
        )
      )
    )
  )
)

(defn consultar-usuario []
  (let [resp (api/consultar-usuario!)]
    (if (:ok? resp)
      (println (ui/formatar-usuario (:dados resp)))
      (println (ui/formatar-erro (:erro resp)))
    )
  )
)

(defn dados-pessoais []
  (println "")
  (println "1. Cadastrar dados pessoais")
  (println "2. Consultar dados pessoais")
  (print "Escolha uma opcao: ")
  (flush)
  (let [opcao (ler-inteiro)]
    (cond
      (= opcao 1) (cadastrar-usuario)
      (= opcao 2) (consultar-usuario)
      :else (println (ui/formatar-erro "Opcao invalida"))
    )
  )
)

(defn registrar-alimento []
  (println "Digite o nome do alimento (ex: arroz, banana, frango):")
  (let [alimento (ler-texto)]
    (println "Digite a quantidade consumida (g):")
    (let [quantidade (ler-numero)]
      (println "Digite a data (AAAA-MM-DD):")
      (let [data (ler-texto)
            resp (api/registrar-alimento!
                  {:alimento     alimento
                   :quantidade-g quantidade
                   :data         data})]
        (if (:ok? resp)
          (println (ui/formatar-ok
                    (format "Registrado: %s - %.1f calorias"
                            (get-in resp [:dados :descricao])
                            (double (get-in resp [:dados :calorias] 0))))
          )
          (println (ui/formatar-erro (:erro resp)))
        )
      )
    )
  )
)

(defn registrar-exercicio []
  (println "Digite o nome da atividade (ex: Ski machine):")
  (let [atividade (ler-texto)]
    (println "Digite a duracao em minutos:")
    (let [duracao (ler-inteiro)]
      (println "Digite a data (AAAA-MM-DD):")
      (let [data (ler-texto)
            resp (api/registrar-exercicio!
                  {:atividade atividade
                   :duracao-min duracao
                   :data data})]
        (if (:ok? resp)
          (println (ui/formatar-ok
                    (format "Registrado: %s - %.1f calorias gastas"
                            (get-in resp [:dados :descricao])
                            (double (get-in resp [:dados :calorias] 0))))
          )
          (println (ui/formatar-erro (:erro resp)))
        )
      )
    )
  )
)

(defn consultar-extrato []
  (println "Digite a data de inicio (AAAA-MM-DD):")
  (let [inicio (ler-texto)]
    (println "Digite a data de fim (AAAA-MM-DD):")
    (let [fim  (ler-texto)
          resp (api/consultar-extrato! inicio fim)]
      (if (:ok? resp)
        (println (ui/formatar-extrato (:dados resp)))
        (println (ui/formatar-erro (:erro resp)))
      )
    )
  )
)

(defn consultar-saldo []
  (println "Digite a data de inicio (AAAA-MM-DD):")
  (let [inicio (ler-texto)]
    (println "Digite a data de fim (AAAA-MM-DD):")
    (let [fim  (ler-texto)
          resp (api/consultar-saldo! inicio fim)]
      (if (:ok? resp)
        (println (ui/formatar-saldo (:dados resp)))
        (println (ui/formatar-erro (:erro resp)))
      )
    )
  )
)

(defn -main
  [& args]
  (println "")
  (println "Bem-vindo a Calculadora de Calorias!")

  (println "")
  (println "1. Cadastrar/consultar dados pessoais")
  (println "2. Registrar consumo de alimento")
  (println "3. Registrar realizacao de atividade fisica")
  (println "4. Consultar extrato de transacoes")
  (println "5. Consultar saldo de calorias")
  (println "6. Sair")
  (print "Escolha uma opcao: ")
  (flush)

  (let [opcao (ler-inteiro)]
    (cond
      (= opcao 1) (do (dados-pessoais)      (-main))
      (= opcao 2) (do (registrar-alimento)  (-main))
      (= opcao 3) (do (registrar-exercicio) (-main))
      (= opcao 4) (do (consultar-extrato)   (-main))
      (= opcao 5) (do (consultar-saldo)     (-main))
      (= opcao 6) (println "Ate a proxima!")
      :else
        (do (println (ui/formatar-erro "Opcao invalida")) (-main))
    )
  )
)