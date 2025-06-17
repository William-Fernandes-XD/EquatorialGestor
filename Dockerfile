FROM tomcat:9.0-jdk8

# Copia seu WAR para a pasta do Tomcat
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Exponha a porta padrão
EXPOSE 8080

CMD ["catalina.sh", "run"]