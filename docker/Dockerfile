FROM gradle:jdk21-corretto AS build

WORKDIR /usr/local/src/credentials-generator-service/
COPY ./src/ ./src/
COPY ./lombok.config ./build.gradle.kts ./
RUN gradle clean assemble

# Второй этап
FROM eclipse-temurin:21-jre-noble

COPY --from=build "/usr/local/src/credentials-generator-service/build/libs/*.jar" /opt/credentials-generator-service.jar

ENV CREDENTIALS_GENERATOR_PORT=$CREDENTIALS_GENERATOR_PORT
EXPOSE $CREDENTIALS_GENERATOR_PORT

ENTRYPOINT ["java","-jar","/opt/credentials-generator-service.jar" ]