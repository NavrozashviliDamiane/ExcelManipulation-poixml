FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/exceltest-0.0.1-SNAPSHOT.jar /app/exceltest-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/exceltest-0.0.1-SNAPSHOT.jar"]