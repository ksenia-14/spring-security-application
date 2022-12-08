#FROM openjdk:17-jdk-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:17-jdk-alpine
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM adoptopenjdk/openjdk17:jre-17.0.4-alpine
LABEL maintainer="Sunit Chatterjee (developerpod.com)"
RUN adduser --no-create-home --disabled-password springuser
USER springuser:springuser
COPY build/libs/spring-boot-app-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]