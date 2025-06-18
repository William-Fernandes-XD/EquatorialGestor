FROM tomcat:9.0-jdk8

# Copia o WAR
COPY target/gestorcoi-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copia o context.xml específico da aplicação (JNDI)
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost
COPY lib/ROOT.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml

# Copia o driver JDBC PostgreSQL
COPY lib/postgresql-42.7.3.jar /usr/local/tomcat/lib/

# Copia o server.xml que você alterou a porta para 8081
COPY lib/server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8081

CMD ["catalina.sh", "run"]