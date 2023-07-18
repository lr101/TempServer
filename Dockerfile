#Maven Build
FROM maven:3.8.3-openjdk-17-slim AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests -e DB_USER=postgres -e DB_PASSWORD=postgres -e DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/postgres

#Run
FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/SpringServer-1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
