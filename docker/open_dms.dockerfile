FROM docker.io/openjdk:22

WORKDIR /app

ADD build/libs/server-0.0.1-SNAPSHOT.jar app.jar
ADD application.yml application.yml

RUN adduser -uid 1001 --system apprunner && addgroup -gid 1000 --system runnergroup && addgroup apprunner runnergroup
USER apprunner

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
