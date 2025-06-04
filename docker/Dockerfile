FROM alpine:3.21 AS build
RUN apk add --no-cache --update \
    gradle

WORKDIR /usr/local/src/credentials-generator-service/
COPY ./src/ ./src/
COPY ./lombok.config ./build.gradle.kts ./
RUN gradle clean assemble

# Второй этап
FROM alpine:3.21
RUN apk add --no-cache --update \
    openjdk21

COPY --from=build "/usr/local/src/credentials-generator-service/build/libs/*.jar" /opt/credentials-generator-service.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/credentials-generator-service.jar" ]