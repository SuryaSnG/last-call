FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/user-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]