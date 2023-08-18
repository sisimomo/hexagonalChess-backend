# Gradle Build
FROM arm64v8/gradle:8.2-jdk-focal AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

# Run
FROM openjdk:17-jdk-slim-buster
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]
