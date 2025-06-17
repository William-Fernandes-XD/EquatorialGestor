# Usa a imagem oficial do Tomcat 9
FROM tomcat:9.0

# Copia o WAR gerado para dentro do Tomcat no container
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Exponha a porta padrão do Tomcat
EXPOSE 8080

# Start do Tomcat
CMD ["catalina.sh", "run"]