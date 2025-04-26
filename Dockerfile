FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
ENV JAVA_HOME=/opt/java/openjdk

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .

COPY src ./src

RUN ./gradlew build -x test --stacktrace

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]