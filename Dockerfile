
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY target/transaction-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
