;; Aqui é a parte de conexões com APIs externas, onde usamos:
;; Para comidas: Open Food Facts (eu achei o nome legal)
;; Para exercicios: API-Ninjas
(ns server.external
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

;; Chave da API Ninjas, pode usar a minha - Luis
(def api-ninjas-key
  (or (System/getenv "API_NINJAS_KEY") "yckoTzqL0NrcBRIUvGtQ9URIWMVuYUFUsMVRC3tZ"))

(def api-ninjas-headers
  {"X-Api-Key" api-ninjas-key})

(defn- kcal-por-100g
  ;; Extrai as kcal/100g de um produto do Open Food Facts (OFF)
  ;; Tenta energy-kcal_100g primeiro e converte kJ se necessario
  [nutriments]
  (or (get nutriments :energy-kcal_100g)
      (get nutriments (keyword "energy-kcal_100g"))
      (when-let [kj (or (get nutriments :energy_100g)
                        (get nutriments (keyword "energy_100g")))]
        (/ kj 4.184))
  )
)

(defn- extrair-calorias-alimento
  ;; com a lista de produtos do OFF, calcula as calorias para 'quantidade-g' gramas e retorna nil se não ter dados
  [produtos quantidade-g]
  (when-let [produto (first (filter #(get-in % [:nutriments]) produtos))]
    (when-let [cal100 (kcal-por-100g (:nutriments produto))]
      (when (pos? cal100)
        (* cal100 (/ quantidade-g 100.0)))
    )
  )
)

(defn- peso-kg->lb
  ;; Converte peso de kg para libras (API Ninjas usa libras)
  ;; Se não tiver nenhuma, o padrão da api-ninja é 160lb
  [peso-kg]
  (if peso-kg
    (-> peso-kg (* 2.20462) (max 50) (min 500)) 160
  )
)

(defn- extrair-total-calorias
  ;; Extrai o total_calories do primeiro resultado retornado pela API Ninjas e retorna nil se a lista vier vazia
  [resultados]
  (when (seq resultados)
    (:total_calories (first resultados))
  )
)

(defn buscar-calorias-alimento
  ;; Consulta o Open Food Facts e retorna as calorias para 'quantidade-g' gramas do 'alimento' que foi colocado
  ;; Retorna nil se o alimento nao foi encontrado
  [alimento quantidade-g]
  (try
    (let [resp (http/get "https://world.openfoodfacts.org/cgi/search.pl"
                             {:query-params {"search_terms" alimento
                                             "search_simple" "1"
                                             "action" "process"
                                             "json" "1"
                                             "page_size" "5"
                                             "fields" "product_name,nutriments"}
                              :headers      {"User-Agent" "CaloriasApp/1.0 (educational project)"}
                              :as :json})
          produtos  (get-in resp [:body :products])]
      (extrair-calorias-alimento produtos quantidade-g)
    )
    (catch Exception _ nil)
  )
)

(defn buscar-calorias-exercicio
  ;; Consulta a API Ninjas (/v1/caloriesburned) e retorna as calorias queimadas na atividade, ja ajustadas pelo peso
  ;; (kg, convertido para lb) e pela duracao em minutos
  ;; Retorna nil se a atividade nao for encontrada
  [atividade duracao-min peso-kg]
  (try
    (let [resp (http/get "https://api.api-ninjas.com/v1/caloriesburned"
                         {:headers api-ninjas-headers
                          :query-params {"activity" atividade
                                         "weight" (int (peso-kg->lb peso-kg))
                                         "duration" duracao-min}
                          :as :json})]
      (extrair-total-calorias (:body resp))
    )
    (catch Exception _ nil)
  )
)