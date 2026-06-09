(ns server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])

)

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/read" [] (str (read)))
  (GET "/ola/:nome" [nome] (str nome))
  (route/not-found "Not Found")
  
)

(def app
  (wrap-defaults app-routes site-defaults))



;; (ns teteeeee.handler
;;   (:require [compojure.core :refer :all]
;;             [compojure.route :as route]
;;             [cheshire.core :as json]
;;             [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; (def biblioteca 
;;   {:Fenomenologia_Do_Espirito {
;;     :name "Fenomenologia do Espirito"
;;     :autor "Georg Wilhelm Friedrich Hegel"
;;     :publicacao "1807"
;;     :paginas "552"}

;;     :Ciencia_Da_Logica {
;;     :name "A Ciencia da Logica"
;;     :autor "Georg Wilhelm Friedrich Hegel"
;;     :publicacao "1812"
;;     :paginas "500"}

;;     :Biblia {
;;     :name "Biblia"
;;     :autor "Varios sabios ai"
;;     :publicacao "0"
;;     :paginas "mais do q uma"}

;;     :BluePrince {
;;     :name "Blue Prince"
;;     :autor "Maria Jones"
;;     :publicacao "1980"
;;     :paginas "40"}

;;     :A_New_Clue {
;;     :name "A New Clue"
;;     :autor "Maria Jones"
;;     :publicacao "1980"
;;     :paginas "40"}}
;; )

;; (defroutes app-routes
;;   (GET "/" [] "Hello World")
;;   (GET "/read" [] (str (read)))
;;   (GET "/ola/:nome" [nome] (str (type (Integer/parseInt nome))))
;;   (GET "/biblioteca" [] (json/generate-string biblioteca) )
;;   (GET "/somar/:x/:y" [x y] (str "O resultado de sua soma vale: " (+ (Integer/parseInt x) (Integer/parseInt y) )))
;;   (route/not-found "Not Found"))

;; (def app
;;   (wrap-defaults app-routes site-defaults))
