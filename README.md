
Инструкция по тестированию приложения в Postman
Подготовка
Установите и запустите Postman.
Запустите ваш сервер на http://localhost:8080.
Эндпоинты
1. Регистрация игрока
   Тип запроса: POST
   URL: http://localhost:8080/players/register
   Тело запроса:
   json
   Copy code
   {
   "username": "Player",
   "password": "353062"
   }
2. Вход для игрока
   Тип запроса: POST
   URL: http://localhost:8080/players/login
   Тело запроса:
   json
   Copy code
   {
   "username": "Player",
   "password": "353062"
   }
   После успешного выполнения этого запроса, вы получите токен. Сохраните его для следующих запросов.

3. Кредитная транзакция
   Тип запроса: POST
   URL: http://localhost:8080/transactions/credit
   Тело запроса:
   json
   Copy code
   {
   "amount": 100
   }
4. Дебетовая транзакция
   Тип запроса: POST
   URL: http://localhost:8080/transactions/debit
   Тело запроса:
   json
   Copy code
   {
   "amount": 90
   }
5. Просмотр баланса игрока
   Тип запроса: GET
   URL: http://localhost:8080/players/balance
6. Просмотр истории транзакций игрока
   Тип запроса: GET
   URL: http://localhost:8080/transactions/history
   Аутентификация
   Для запросов, требующих аутентификации, используйте полученный токен после входа:

В Postman, выберите раздел "Аутентификация".
Из выпадающего меню выберите "Bearer Token".
Вставьте ваш токен в поле "Token".
