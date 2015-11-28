#!/usr/bin/env bash

set -o verbose

DBNAME=tuapp
DBUSER=postgres

pg_dump -U ${DBUSER} -n ref ${DBNAME} > backups/ref_data.sql
