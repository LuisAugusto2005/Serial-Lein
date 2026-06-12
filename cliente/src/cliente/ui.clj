(ns cliente.ui)

;; Uma boa para deixar visualmente melhor de ler
(defn linha-separadora []
  (apply str (repeat 45 "-"))
)

(defn formatar-usuario [u]
  (str "\n" (linha-separadora) "\n"
       "  Usuario cadastrado\n"
       (linha-separadora) "\n"
       (format "  Nome   : %s\n" (:nome u))
       (format "  Altura : %s m\n" (:altura u))
       (format "  Peso   : %s kg\n" (:peso u))
       (format "  Idade  : %s anos\n" (:idade u))
       (format "  Sexo   : %s\n" (:sexo u))
       (linha-separadora)
  )
)

(defn formatar-transacao [t]
  (let [tipo (if (= (:tipo t) "alimento") "Alimento" "Exercicio")]
    (str (format "  [%s] %s\n" (:data t) tipo)
         (format "       %s - %.1f calorias\n" (:descricao t) (double (:calorias t)))
    )
  )
)

(defn formatar-extrato [extrato]
  (if (empty? (:transacoes extrato))
    "\n  Nenhuma transacao encontrada no periodo.\n"
    (str "\n" (linha-separadora) "\n"
         (format "  Extrato - %d transacoes\n" (:total extrato))
         (linha-separadora) "\n"
         (apply str (map formatar-transacao (:transacoes extrato)))
         (linha-separadora)
    )
  )
)

(defn formatar-saldo [saldo]
  (let [val (double (:saldo-calorias saldo))
        tag (cond
               (pos? val) "superavit"
               (neg? val) "deficit"
               :else "equilibrado")]
    (str "\n" (linha-separadora) "\n"
         "  Saldo de calorias\n"
         (linha-separadora) "\n"
         (format "  %.1f calorias  (%s)\n" val tag)
         (linha-separadora)
    )
  )
)

(defn formatar-erro [msg]
  (str "\n  ERRO: " msg "\n")
)

(defn formatar-ok [msg]
  (str "\n  OK: " msg "\n")
)