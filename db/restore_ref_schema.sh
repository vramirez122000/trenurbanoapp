#!/usr/bin/env bash

set -o verbose

DBNAME=tuapp
DBUSER=postgres
DBHOST=127.0.0.1

echo "host: ${DBHOST}, database: ${DBNAME}, user: ${DBUSER}"
read -p "Deleting schema ref, this cannot be undone..."
echo "drop schema ref cascade" | psql -U ${DBUSER} -d ${DBNAME} -h ${DBHOST}
read -p "Press [Enter] key to start..."

cat backups/ref_data.sql | psql -U ${DBUSER} -d ${DBNAME} -h ${DBHOST}