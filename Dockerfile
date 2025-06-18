FROM tomcat:9.0-jdk8

COPY wait-for-postgres.sh /wait-for-postgres.sh
RUN chmod +x /wait-for-postgres.sh

CMD ["sh", "-c", "./wait-for-postgres.sh && catalina.sh run"]

# Copia o WAR para a raiz do Tomcat
COPY target/gestorcoi-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Garante que o diretório de contextos existe
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost

# Copia o context.xml (JNDI) como ROOT.xml no lugar certo
COPY lib/context.xml /usr/local/tomcat/conf/Catalina/localhost/ROOT.xml

# Copia o driver JDBC PostgreSQL
COPY lib/postgresql-42.7.3.jar /usr/local/tomcat/lib/

# Copia o server.xml com a porta alterada (8081)
COPY lib/server.xml /usr/local/tomcat/conf/server.xml

# Ativa o naming (necessário pra JNDI funcionar)
ENV CATALINA_OPTS="-Dcatalina.useNaming=true"

# Exponha a nova porta
EXPOSE 8081

# Inicia o Tomcat
CMD ["catalina.sh", "run"]