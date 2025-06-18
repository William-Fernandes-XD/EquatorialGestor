FROM tomcat:9.0-jdk8

# Copia o WAR
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

# Copia o context.xml específico da aplicação (JNDI)
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost
COPY lib/Context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml

# Copia o driver JDBC
COPY lib/postgresql-42.7.3.jar /usr/local/tomcat/lib/

# Exponha a porta que o Tomcat realmente vai abrir
EXPOSE 8080

CMD ["catalina.sh", "run"]