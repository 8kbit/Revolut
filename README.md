    В приложении использовал:
 - Gradle
 - Google Guice
 - MyBatis
 - Jersey
 - HSQLDB (In-Memory)

    Для запуска перейти в корневую папку проекта и выполнить "gradlew appRun".
    Пример использования API:

    Перевод денег.
        POST -> localhost:8082/services/v1/transfer/?from=0&to=1&amount=5&externalId=12312312312
    Запрос списка аккаунтов.
        GET -> localhost:8082/services/v1/account/?limit=1&offset=0
    Запрос списка транзакций, связанных с аккаунтом.
        GET -> localhost:8082/services/v1/transactionLog/?accountId=1&accountId=0&limit=100&offset=0

    Для целей тестирования при запуске в БД создаётся 3 аккаунта, описание которых можно получить через
  запрос списка аккаунтов.

    Тестами покрыты наиболее критичные участки (метод API по переводу денег и контроль одновременного изменения
  аккаунта (поле version) )

    Связка Gradle/Guice/MyBatis выбрана в качестве альтернативы стандартной Maven/Spring/Hibernate