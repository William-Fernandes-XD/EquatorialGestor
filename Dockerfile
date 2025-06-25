# Usa uma imagem Tomcat com Java 8
FROM tomcat:8-jdk8

# Remove as aplicações padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia seu WAR para dentro do container, com o nome ROOT.war (assim abre direto na /)
COPY target/com.gestorcoi.war /usr/local/tomcat/webapps/ROOT.war

# Copia o context.xml do projeto para o local correto
COPY /WebContent/META-INF/context.xml /usr/local/tomcat/conf/context.xml

# Exponha a porta padrão do Tomcat
EXPOSE 8080

# Comando para iniciar o Tomcat
CMD ["catalina.sh", "run"]