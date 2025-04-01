FROM gradle:jdk21-corretto AS build

COPY ./src /tmp/credentialsgenerator/src/
COPY ./lombok.config /tmp/credentialsgenerator/
COPY ./build.gradle.kts /tmp/credentialsgenerator/
RUN gradle -p /tmp/credentialsgenerator/ clean assemble

# Второй этап
FROM eclipse-temurin:21-jre-noble

COPY --from=build "/tmp/credentialsgenerator/build/libs/*.jar" /opt/credentialsgenerator.jar

ENTRYPOINT ["java","-jar","/opt/credentialsgenerator.jar" ]