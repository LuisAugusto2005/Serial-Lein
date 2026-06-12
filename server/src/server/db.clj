(ns server.db)

;; Guarda os dados em memoria usando atomos

(def usuario (atom nil))

(def transacoes (atom '()))

;; Funcoes auxiliares
(defn- proximo-id [lista]
  (if (empty? lista)
    1
    (inc (apply max (map :id lista)))
  )
)

(defn- calcular-saldo [lista]
  (reduce (fn [acc t]
            (if (= (:tipo t) "exercicio")
              (- acc (:calorias t))
              (+ acc (:calorias t))
            )
          ) 0 lista)
)

(defn- filtrar-por-periodo [lista data-inicio data-fim]
  (filter (fn [t]
            (let [d (:data t)]
              (and (>= (compare d data-inicio) 0)
                   (<= (compare d data-fim) 0)
              )
            )
          )
          lista)
)

;; Funcoes que usam os atom (goddamn)

(defn salvar-usuario! [dados]
  (reset! usuario dados) @usuario
)

(defn obter-usuario []
  @usuario
)

(defn registrar-transacao! [transacao]
  (let [nova (swap! transacoes
                    (fn [lista]
                      (conj lista (merge transacao {:id (proximo-id lista)})))
             )]
    (first nova))
)

(defn obter-transacoes []
  (reverse @transacoes)
)

(defn obter-extrato [data-inicio data-fim]
  (filtrar-por-periodo (obter-transacoes) data-inicio data-fim)
)

(defn obter-saldo [data-inicio data-fim]
  (calcular-saldo (obter-extrato data-inicio data-fim))
)

(defn limpar! []
  (reset! usuario nil)
  (reset! transacoes '())
)