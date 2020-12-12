
# Тестовое задание              
С использованием фреймворка Spring необходимо разработать сервис для конвертации валют и сбора статистики операций.
API: /exchange, /stats

/exchange

Запрос: id пользователя, сумма в исходной валюте, исходная валюта, целевая валюта.

Ответ: id запроса, сумма в целевой валюте.

Можно использовать внешние api для конвертации или для получения курса конвертации.

/stats

Предоставление доступа к выборочной информации по запросам.

Примеры запросов:

Пользователи, запросившие конвертацию больше 10 000 $ за один запрос.

Пользователи, суммарный запрошенный объём которых больше 100 000 $.

Рейтинг направлений конвертации валют по популярности.

Будет плюсом:

Удобное логгирование

Docker

tar.gz архив со сделанным заданием содержит:

1. Исходный код.

2. Инструкцию по деплою.

3. Описание API.

4. Скрипт с тестовыми запросами на /exchange.
----------------------------------------------------------------------------------------------------------------
## Инструкцию по деплою
перейдите в корневую папку exchanger и запустите docker docker-compose up


## Описание API
Документация введется при помощи Swagger
Запустите приложение и перейдите http://localhost:8080/swagger-ui.html


## Скрипт с тестовыми запросами на /exchange.
curl -X GET "http://localhost:8080/exchange?amountSource=2.2&currencySource=USD&currencyTarget=RUB&userId=123e4567-e89b-12d3-a456-000000000111" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=30&currencySource=USD&currencyTarget=RUB&userId=123e4567-e89b-12d3-a456-000000000111" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=1000&currencySource=USD&currencyTarget=RUB&userId=123e4567-e89b-12d3-a456-000000000111" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=1562&currencySource=USD&currencyTarget=RUB&userId=123e4567-e89b-12d3-a456-000000000111" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=34000&currencySource=RUB&currencyTarget=EUR&userId=123e4567-e89b-12d3-a456-000000000111" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=34000&currencySource=USD&currencyTarget=GBP&userId=123e4567-e89b-12d3-a456-000000000111" -H "accept: */*"

curl -X GET "http://localhost:8080/exchange?amountSource=147&currencySource=USD&currencyTarget=EUR&userId=123e4567-e89b-12d3-a456-000000000222" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=1687&currencySource=USD&currencyTarget=EUR&userId=123e4567-e89b-12d3-a456-000000000222" -H "accept: */*"
curl -X GET "http://localhost:8080/exchange?amountSource=35000&currencySource=RUB&currencyTarget=USD&userId=123e4567-e89b-12d3-a456-000000000222" -H "accept: */*"

## Скрипт с тестовыми запросами на /stats
allExchange null
curl -X GET "http://localhost:8080/stats?currencyAmount=500&currencyCode=USD" -H "accept: */*"

allExchange true
curl -X GET "http://localhost:8080/stats?allExchange=true&currencyAmount=10000&currencyCode=USD" -H "accept: */*"

popularDestination true
curl -X POST "http://localhost:8080/stats?popularDestination=true" -H "accept: */*"


