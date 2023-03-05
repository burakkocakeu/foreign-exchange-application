FROM adoptopenjdk/openjdk17:jdk-17.0.2_8-alpine-slim AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -DskipTests

FROM adoptopenjdk/openjdk17:jdk-17.0.2_8-alpine-slim
WORKDIR /app
COPY --from=build /app/target/foreign-exchange-application-0.0.1-SNAPSHOT.jar /app/foreign-exchange-application.jar
ENTRYPOINT ["java","-jar","/app/foreign-exchange-application.jar"]
