FROM openjdk:17-jdk-slim as builder

LABEL maintainer="martincastro.10.5@gmail.com"

EXPOSE 8080

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} app.jar

RUN mkdir -p /tmp/app/files

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
