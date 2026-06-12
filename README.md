# Serial-Lein
Clojure Project - Calculadora de Calorias

---

## Estrutura

- `calorias-cli/` -> Front-end (template app, roda no terminal)
- `calorias-api/` -> Back-end  (template compojure, sobe um servidor)

---

## API:

- Chave da api-ninjas (a minha, sem problema usar):
- Para exercicios: https://api-ninjas.com/api/caloriesburned
- Para comidas:    https://world.openfoodfacts.org/cgi/search.pl?search_terms=

API-NINJAS precisa de chave, Sem ela o registro de exercicio sempre retorna erro 422
(atividade nao encontrada), porque a API-NINJAS responde 401.

---

## Comandos importantes:

- `lein new app cliente`        -> cria um template no estilo app (cliente)
- `lein new compojure server`   -> cria um template no estilo servidor (compojure)
- `lein run`                    -> roda o Cliente (APP)
- `lein ring server`            -> roda o Server (Compojure), com browser
- `lein ring server-headless`   -> roda o Server sem abrir o browser

---

## Ordem para usar

1. Abrir um terminal e subir o server:
   ```
   cd calorias-api
   lein ring server
   ```
   (está indo para: http://localhost:3000)

2. Abrir outro terminal e rodar o cliente:
   ```
   cd calorias-cli
   lein run
   ```

3. No menu do cliente, testar nessa ordem:
   - Opcao 1 -> cadastrar dados pessoais (nome, altura, peso, idade, sexo)
   - Opcao 2 -> registrar alimento (ex: arroz, 200, 2026-06-10)
   - Opcao 3 -> registrar exercicio (ex: running, 30, 2026-06-10)
   - Opcao 4 -> ver extrato (ex: 2026-06-01 a 2026-06-30)
   - Opcao 5 -> ver saldo (mesmas datas)

---

## Lembretes / coisas pra nao esquecer

- Server precisa estar de ligado ANTES de rodar o cliente
- Nomes de exercicio funcionam em ingles (running, swimming, cycling, push up, squat...)
- Nomes de comida em portugues normalmente funcionam bem no Open Food Facts (arroz, feijao, frango, banana...)
- Datas sempre no formato AAAA-MM-DD.
- Se reiniciar o server, perde tudo (dados ficam so em memoria/atom)