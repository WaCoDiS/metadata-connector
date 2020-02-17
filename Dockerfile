# Build metadata-connector
FROM maven:3.5-jdk-8-alpine as build 
WORKDIR /app

COPY . /app/metadata-connector/

RUN mvn -f ./metadata-connector/pom.xml clean install

# Run metadata-connector
FROM adoptopenjdk/openjdk8:alpine
WORKDIR /app

COPY --from=build app/metadata-connector/metadata-connector-app/target/metadata-connector-app-0.0.1-SNAPSHOT.jar /app/metadata-connector-app.jar
CMD ["java", "-jar", "/app/metadata-connector-app.jar"]