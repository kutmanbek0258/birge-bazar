FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/auth-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]