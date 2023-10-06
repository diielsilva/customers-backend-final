FROM openjdk:17-jdk-slim
LABEL authors="Diel"
COPY COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
