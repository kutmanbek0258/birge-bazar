FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/admin-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]