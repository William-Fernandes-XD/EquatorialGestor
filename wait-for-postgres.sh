#!/bin/sh

echo "Esperando o Postgres subir..."

until pg_isready -h dpg-d18rsmqli9vc73fu89gg-a.oregon-postgres.render.com -p 5432 -U gestorcoi_user; do
  echo "Ainda sem resposta do banco..."
  sleep 5
done

echo "Banco disponível! Subindo Tomcat."