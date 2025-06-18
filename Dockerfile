FROM tomcat:9.0-jdk8

USER root
RUN apt-get update && apt-get install -y netcat-openbsd

# Copia o WAR
COPY target/gestorcoi-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copia o context.xml com o JNDI
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost
COPY lib/context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml

# Copia o driver JDBC PostgreSQL
COPY lib/postgresql-42.7.3.jar /usr/local/tomcat/lib/

# Copia o server.xml com porta 8081
COPY lib/server.xml /usr/local/tomcat/conf/server.xml

# Copia o wait-for-database.sh
COPY wait-for-database.sh /usr/local/tomcat/wait-for-database.sh
RUN chmod +x /usr/local/tomcat/wait-for-database.sh


USER tomcat

EXPOSE 8081


CMD ["/usr/local/tomcat/wait-for-database.sh"]