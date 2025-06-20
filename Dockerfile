FROM tomcat:9.0-jdk8

# Instala netcat (necessário para o wait-for-database.sh funcionar)
RUN apt-get update && apt-get install -y netcat-openbsd

# Copia o WAR da sua aplicação
COPY target/gestorcoi-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copia o context.xml com o JNDI DataSource
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost
COPY lib/context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml

# Copia o driver JDBC PostgreSQL
COPY lib/postgresql-42.7.3.jar /usr/local/tomcat/lib/

# Copia o server.xml com a porta padrão (8080)
COPY lib/server.xml /usr/local/tomcat/conf/server.xml

# Copia o script de espera pelo banco de dados
COPY wait-for-database.sh /usr/local/tomcat/wait-for-database.sh
RUN chmod +x /usr/local/tomcat/wait-for-database.sh

# Exponha a porta padrão (8080) que o Render espera
EXPOSE 8080

# O container só sobe o Tomcat quando o banco estiver disponível
CMD ["/usr/local/tomcat/wait-for-database.sh"]