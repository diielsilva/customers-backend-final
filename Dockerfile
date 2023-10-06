FROM openjdk:17-jdk-slim
LABEL authors="Diel"
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
