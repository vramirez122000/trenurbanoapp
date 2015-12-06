#!/usr/bin/env bash

set -o verbose

INSTANCE=$([ -z "$1" ] && echo "tuapp" || echo "tuapp_$1")
DBUSER=postgres
DBHOST=127.0.0.1

echo "host: ${DBHOST}, database: ${INSTANCE}, user: ${DBUSER}"
read -p "Deleting schema ref, this cannot be undone..."
echo "drop schema ref cascade" | psql -U ${DBUSER} -d ${INSTANCE} -h ${DBHOST}
read -p "Press [Enter] key to start..."

cat backup/${INSTANCE}_ref.sql | psql -U ${DBUSER} -d ${INSTANCE} -h ${DBHOST}