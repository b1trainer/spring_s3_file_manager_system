FROM gradle:8.4-jdk21 AS builder

WORKDIR /app
COPY . .

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]