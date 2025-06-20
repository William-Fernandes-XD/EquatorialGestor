#!/bin/sh

echo "⏳ Esperando o Postgres ficar disponível em dpg-d18rsmqli9vc73fu89gg-a.oregon-postgres.render.com:5432..."

while ! nc -z dpg-d18rsmqli9vc73fu89gg-a.oregon-postgres.render.com 5432; do
  echo "Banco ainda não está pronto... aguardando 5s"
  sleep 5
done

sleep 90

echo "✅ Banco disponível! Iniciando o Tomcat..."
exec catalina.sh run