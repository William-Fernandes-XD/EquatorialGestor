# Etapa de build com Maven e Java 8
FROM maven:3.8.1-openjdk-8-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY WebContent ./WebContent
RUN mvn clean package -DskipTests

# Etapa de runtime com Tomcat e Java 8
FROM tomcat:9.0-jdk8-openjdk-slim
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
# Tomcat inicia automaticamente, não precisa de ENTRYPOINT