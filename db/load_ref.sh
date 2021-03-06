#!/usr/bin/env bash

#Import hand crafted data

#set -o verbose

INSTANCE=$([ -z "$1" ] && echo "tuapp" || echo "tuapp_$1")

read -p "WARNING Recreating schema ref on database ${INSTANCE}, this cannot be undone..."

_SET_SEARCH_PATH="SET search_path = ref, public, pg_catalog;"
_DROP="${_PREFIX} drop schema if exists ref cascade;"

echo "${_DROP} ${_SET_SEARCH_PATH}" \
| cat -\
 common/schema/ref.sql \
| psql -U postgres -h 127.0.0.1 -d ${INSTANCE} \
&> load_ref.log

echo ${_SET_SEARCH_PATH} \
| cat -\
 ${INSTANCE}/stop-areas.sql \
 ${INSTANCE}/sched/*.sql \
 ${INSTANCE}/routes.sql \
 ${INSTANCE}/stops.sql \
 ${INSTANCE}/subroute/*.sql \
 ${INSTANCE}/vehicles.sql \
 common/index/*.sql \
| psql -U postgres -h 127.0.0.1 -d ${INSTANCE} \
>> load_ref.log 2>&1

echo "Success!"
