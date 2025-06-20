FROM tomcat:9.0-jdk8

# Expor a porta
EXPOSE 8081

# Criar página simples de teste
RUN echo "Hello from Render!" > /usr/local/tomcat/webapps/ROOT/index.html

# Corrigir server.xml pra porta 8081
COPY lib/server.xml /usr/local/tomcat/conf/server.xml

CMD ["catalina.sh", "run"]