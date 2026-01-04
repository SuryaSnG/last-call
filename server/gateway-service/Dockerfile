# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copy parent POM from build context root
COPY ../pom.xml ./pom.xml

# Copy gateway-service source
RUN ls -la
COPY . .

# Build only gateway-service + dependencies
RUN mvn clean package -DskipTests -pl gateway-service -am

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy only the JAR we need
COPY --from=build /build/gateway-service/target/gateway-service-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=$PORT"]
