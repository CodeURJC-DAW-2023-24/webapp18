# BUILD STAGE
FROM maven:eclipse-temurin AS build 
WORKDIR /project
COPY backend/h2os/pom.xml /project/
COPY backend/h2os/src/ /project/src
RUN mvn -f /project/pom.xml clean package -DskipTests

# PACKAGE STAGE
FROM openjdk:23-slim
WORKDIR /project
COPY --from=build /project/target/*.jar /project/Application.jar
EXPOSE 8443
CMD ["java", "-jar", "/project/Application.jar"]