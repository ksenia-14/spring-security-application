#FROM openjdk:17-jdk-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


#FROM openjdk:10-jre-slim
#COPY ./target/hola-docker-1.0.0-SNAPSHOT.jar /usr/src/hola/
#WORKDIR /usr/src/hola
#EXPOSE 8080
#CMD ["java", "-jar", "hola-docker-1.0.0-SNAPSHOT.jar"]
