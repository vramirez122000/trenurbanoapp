#!/usr/bin/env bash

#Import hand crafted data

INSTANCE=tuapp
DBNAME=tuapp

echo "SET search_path = ref, public, pg_catalog;" | cat - ${INSTANCE}/stop-areas.sql | psql -U postgres -d ${DBNAME}
echo "SET search_path = ref, public, pg_catalog;" | cat - ${INSTANCE}/sched/*.sql | psql -U postgres -d ${DBNAME}
echo "SET search_path = ref, public, pg_catalog;" | cat - ${INSTANCE}/routes.sql | psql -U postgres -d ${DBNAME}
echo "SET search_path = ref, public, pg_catalog;" | cat - ${INSTANCE}/subroute/*.sql | psql -U postgres -d ${DBNAME}