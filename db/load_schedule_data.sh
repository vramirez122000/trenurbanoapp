#!/usr/bin/env bash

#Import hand crafted data

DBNAME=tuapp

echo "SET search_path = ref, public, pg_catalog;" | cat - data.d/*.sql | psql -U postgres -d ${DBNAME}