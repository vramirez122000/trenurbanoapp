#!/usr/bin/env bash

set -o verbose

INSTANCE=$([ -z "$1" ] && echo "tuapp" || echo "tuapp_$1")
DBUSER=postgres

pg_dump -U ${DBUSER} -h 127.0.0.1 -s -n ref -O -x ${INSTANCE} > common/schema/ref.sql
