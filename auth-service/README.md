# Auth Service

Микросервис аутентификации и авторизации для маркетплейса "Бирге Базар".

- Spring Cloud Config Client (bootstrap.yml)
- Eureka Discovery Client
- Circuit Breaker (Resilience4j)
- Пример настроек берется из Config Server

    Команда для сборки  
  ``mvn package spring-boot:repackage -P client-build-and-copy``
  
    Команда для запуска в профиле dev   
  ``java -jar auth-service.jar --spring.profiles.active=dev``
  
    Команда для раската БД  
  ``mvn liquibase:update -Dliquibase.searchPath=./``