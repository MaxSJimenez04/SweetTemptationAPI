FROM maven:3.9.6-eclipse-temurin-21 AS build

# --- Build ---
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

# --- RUNTIME ---
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]