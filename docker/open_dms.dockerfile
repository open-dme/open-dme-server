FROM docker.io/eclipse-temurin:22-jre

WORKDIR /app

ADD build/libs/server-0.0.1-SNAPSHOT.jar app.jar

RUN adduser -uid 1001 --system apprunner && addgroup -gid 1000 --system runnergroup && addgroup apprunner runnergroup
USER apprunner

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
