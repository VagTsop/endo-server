# define base docker image
FROM maven:3.8.2-openjdk-8-slim as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn clean package  -DskipTests
FROM openjdk:8u171-jre-alpine
COPY src/endofusion.jpg  usr/local/endofusion.jpg
COPY --from=maven target/endo-server-0.0.1-SNAPSHOT.jar endo-server-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "endo-server-0.0.1-SNAPSHOT.jar"]