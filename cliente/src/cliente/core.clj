(ns cliente.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))



;; (ns teteeeee2.core
;;   (:gen-class)

;; (:require 
;; [clj-http.client :as http-client]
;; [cheshire.core :as json])
;; )

;; (defn filtro [chave alvo json]
;;   (filter 
;;     (fn[[_ livro]]
;;       (= ((keyword chave) livro) alvo))
;;     json
;;   )
;; )

;; (defn -main
;;   "I don't do a whole lot ... yet."
;;   [& args]
;;     (let 
;;       [resposta_bruta 
;;       (:body (http-client/get "http://localhost:3000/biblioteca"))
;;       resposta (json/parse-string resposta_bruta true)
;;       ]

;;       ;; (println resposta_bruta)
;;       ;; (println resposta)
;;       (println (filtro (read-line) (read-line) resposta))
;;     )
;; )