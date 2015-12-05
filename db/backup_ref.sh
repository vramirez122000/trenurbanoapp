#!/usr/bin/env bash

set -o verbose

INSTANCE=$([ -z "$1" ] && echo "tuapp" || echo "tuapp_$1")
DBUSER=postgres

pg_dump -U ${DBUSER} -n ref -O -x ${INSTANCE} > backup/${INSTANCE}_ref.sql
