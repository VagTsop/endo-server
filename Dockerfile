# define base docker image
FROM maven:3.8.2-openjdk-8-slim
COPY . .
RUN mvn clean package  -DskipTests
ADD target/endo-server-0.0.1-SNAPSHOT.jar endo-server-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "endo-server-0.0.1-SNAPSHOT.jar"]