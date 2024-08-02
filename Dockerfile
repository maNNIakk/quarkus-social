FROM openjdk:21
ARG IMAGE_NAME=quarkussocial

WORKDIR /app

COPY target/quarkus-app/lib/ /app/lib/
COPY target/quarkus-app/*.jar /app/
COPY target/quarkus-app/app/ /app/app/
COPY target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8080
USER 185
ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]