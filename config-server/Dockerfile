FROM ubuntu:latest
LABEL authors="Misha"

ENTRYPOINT ["top", "-b"]


FROM eclipse-temurin:21-jre-jammy


WORKDIR /app


COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]