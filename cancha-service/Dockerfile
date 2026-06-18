FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/*.jar app.jar

RUN useradd -m springuser && mkdir -p /app/logs && chown -R springuser:springuser /app

USER springuser

EXPOSE 8083

ENTRYPOINT ["java","-jar","app.jar"]