FROM maven:3.6.3-jdk-11-slim AS build

WORKDIR /build

WORKDIR /build/document-api-dto
COPY /document-api-dto/pom.xml /build/document-api-dto
COPY /document-api-dto/src /build/document-api-dto/src
RUN mvn -B clean install -DskipTests

WORKDIR /build/logging-starter
COPY /logging-starter/pom.xml /build/logging-starter
COPY /logging-starter/src /build/logging-starter/src
RUN mvn -B clean install -DskipTests

WORKDIR /build/document-management-service
COPY /document-management-service/pom.xml /build/document-management-service
COPY /document-management-service/src /build/document-management-service/src
COPY /.env /build/document-management-service/
RUN mvn -B -f pom.xml clean package -DskipTests -Dspring.profiles.active=dev

FROM eclipse-temurin:11-jre-alpine

COPY --from=build /build/document-management-service/target/*.jar app.jar
COPY .env ./

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
