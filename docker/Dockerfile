# BUILD STAGE
FROM maven:eclipse-temurin AS build 
WORKDIR /project
COPY backend/helloword-vscode/pom.xml /project/
COPY backend/helloword-vscode/src/ /project/src
RUN mvn -f /project/pom.xml clean package -DskipTests

# PACKAGE STAGE
FROM openjdk:23-slim
WORKDIR /project
COPY --from=build /project/target/*.jar /project/Application.jar
EXPOSE 8443
CMD ["java", "-jar", "/project/Application.jar"]