FROM docker.io/gradle:jdk22-alpine as build

COPY . .
RUN gradle clean build --no-daemon

FROM docker.io/eclipse-temurin:22-jre as runtime

RUN adduser -uid 1001 --system apprunner && addgroup -gid 1000 --system runnergroup && addgroup apprunner runnergroup
USER apprunner

EXPOSE 8080
WORKDIR /app

COPY --from=build /home/gradle/build/libs/server-*.jar server.jar

ENTRYPOINT ["java", "-jar", "server.jar"]
