#Maven Build
FROM maven:3.8.3-openjdk-17-slim AS builder
COPY pom.xml /tmp/pom.xml
WORKDIR /tmp
RUN mvn -B -f /tmp/pom.xml dependency:resolve
COPY src /tmp/src
RUN mvn clean install

#Run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /tmp/target/*jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
