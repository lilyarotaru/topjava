Тестирование REST-API, используя curl:
1. GET запрос для получения списка всей еды юзера 

    GET request to get all meals for user

    curl http://localhost:8080/topJava/rest/meals


2. POST запрос для создания нового записи еды

    POST request to create new meal

    curl -X POST -H "Content-Type:application/json" --data "{\"dateTime\":\"2022-07-15T17:25:00\",\"description\":\"Lunch\",\"calories\":999 }" http://localhost:8080/topJava/rest/meals


3. GET запрос для получения записи о еде юзера по айди

    GET request to get meal by id

    curl http://localhost:8080/topJava/rest/meals/100003


4. DELETE запрос для удаления записи еды по айди

    DELETE request for remove meal by id

    curl -X DELETE http://localhost:8080/topJava/rest/meals/100005


5. PUT запрос для изменения записи о еде по айди

    PUT request to update meal by id

    curl -X PUT -H "Content-Type:application/json" --data "{\"id\":100004,\"dateTime\":\"2022-07-15T18:00:00\",\"description\":\"Updated Lunch\",\"calories\":888 }" http://localhost:8080/topJava/rest/meals/100004


6. GET запрос для фильтрации списка еды по дате/времени

    GET request to filter meals by date, then by time for every date

    curl “http://localhost:8080/topJava/rest/meals/filter?startDate=2020-01-31&endDate=2020-12-12&startTime=10:00&endTime=20:00”


7. GET запрос для фильтрации списка еды по дате/времени с нулевыми и пустыми параметрами

    GET request to filter meals by date, then by time for every date with empty and null parameters 

    curl “http://localhost:8080/topJava/rest/meals/filter?startDate=2020-01-31&endTime=”
