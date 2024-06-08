FROM eclipse-temurin:22-jammy AS builder
LABEL authors="jj2806"

WORKDIR /app

COPY ./.mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY ./src src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:22-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]