#!/bin/bash

echo "Aguardando banco de dados estar disponível em host:porta..."

until nc -z -v -w30 dpg-d18rsmqli9vc73fu89gg-a.oregon-postgres.render.com 5432
do
  echo "Banco ainda não está pronto... aguardando 5s"
  sleep 5
done

echo "Banco pronto! Iniciando Tomcat..."

catalina.sh run