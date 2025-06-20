#!/bin/bash
echo "Aguardando banco de dados..."
until nc -z -v -w30 dpg-d18rsmqli9vc73fu89gg-a.oregon-postgres.render.com 5432
do
  echo "Aguardando 5 segundos pelo banco..."
  sleep 5
done
echo "Banco disponível! Iniciando Tomcat..."
catalina.sh run