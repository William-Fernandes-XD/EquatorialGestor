FROM tomcat:9.0-jdk8

# Copia seu WAR para a pasta do Tomcat
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Copia o context.xml com o JNDI configurado
COPY lib/context.xml

# Copia o driver JDBC (exemplo: mysql-connector)
COPY lib/postgresql-42.7.3.jar /usr/local/tomcat/lib/

# Exponha a porta padrão
EXPOSE 8081

CMD ["catalina.sh", "run"]