# Use uma imagem oficial do Tomcat com JDK 11
FROM tomcat:9.0-jdk11-openjdk-slim

# Copie o WAR gerado para o diretório webapps do Tomcat como ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Exponha a porta padrão do Tomcat
EXPOSE 8080

# O Tomcat já tem o comando de start configurado, não precisa de ENTRYPOINT