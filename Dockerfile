# Usa Tomcat com Java 8
FROM tomcat:8-jdk8

# Remove apps padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia seu WAR para abrir em /
COPY target/com.gestorcoi.war /usr/local/tomcat/webapps/ROOT.war

# Copia seu context.xml
COPY Context.xml /usr/local/tomcat/conf/context.xml

#APENAS PARA O RENDER.COM
# Troca o <Connector port="8080"> para <Connector port="${PORT}">
ARG PORT=8080
ENV PORT=${PORT}
RUN sed -i "s/port=\"8080\"/port=\"$PORT\"/" /usr/local/tomcat/conf/server.xml

# Exponha a porta padrão (Render não usa EXPOSE pra mapear, mas é boa prática)
EXPOSE 8080

# Starta o Tomcat
CMD ["catalina.sh", "run"]