# BUILD STAGE

# FRONTEND 

FROM  node:lts-alpine3.19 as builderFront
WORKDIR /frontend
COPY ../frontend/package*.json ../frontend/angular.json ../frontend/tsconfig*.json  /frontend/
RUN npm install --force
COPY ../frontend/src /frontend/src
RUN npm run build -- --configuration production --base-href="/new/"

# BACKEND

FROM maven:eclipse-temurin AS build 
WORKDIR /project
COPY backend/h2os/pom.xml /project/
COPY backend/h2os/src/ /project/src
COPY --from=builderFront /frontend/dist/frontend/browser/ ./src/main/resources/static/new
RUN mvn -f /project/pom.xml clean package -DskipTests

# PACKAGE STAGE
FROM openjdk:23-slim
WORKDIR /project
COPY --from=build /project/target/*.jar /project/Application.jar
EXPOSE 8443
CMD ["java", "-jar", "/project/Application.jar"]