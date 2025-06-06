FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/review-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]