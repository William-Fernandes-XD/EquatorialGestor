# Etapa de build, nomeada como 'build'
FROM maven:3.8.1-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY WebContent ./WebContent
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM tomcat:9.0-jdk11-openjdk-slim
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
# Tomcat já inicia automaticamente, não precisa de ENTRYPOINT