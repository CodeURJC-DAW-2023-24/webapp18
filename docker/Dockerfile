# BUILD STAGE

# FRONTEND 

FROM  node:lts-alpine3.19 as buildFront
WORKDIR /frontend
COPY frontend/h2os/package*.json /frontend/h2os/angular.json /frontend/h2os/tsconfig*.json  /frontend/
RUN npm install --force
COPY /frontend/h2os/src /frontend/src
RUN npm run build -- --configuration production --base-href="/new/"

# BACKEND

FROM maven:eclipse-temurin AS build 
WORKDIR /project
COPY backend/h2os/pom.xml /project/
COPY backend/h2os/src/ /project/src
COPY --from=buildFront /frontend/dist/h2os/browser/ ./src/main/resources/static/new
RUN mvn -f /project/pom.xml clean package -DskipTests

# PACKAGE STAGE
FROM openjdk:23-slim
WORKDIR /project
COPY --from=build /project/target/*.jar /project/Application.jar
EXPOSE 8443
CMD ["java", "-jar", "/project/Application.jar"]