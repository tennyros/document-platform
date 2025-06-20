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

WORKDIR /build/document-signing-service
COPY /document-signing-service/pom.xml /build/document-signing-service
COPY /document-signing-service/src /build/document-signing-service/src
RUN mvn -B -f pom.xml clean package -DskipTests -Dspring.profiles.active=dev

FROM eclipse-temurin:11-jre-alpine

COPY --from=build /build/document-signing-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
