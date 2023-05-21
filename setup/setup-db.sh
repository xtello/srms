#!/bin/sh

DB="srms"
USER="srms_usr"
PASSWD="ChangeMe"

echo "CREATE USER ${USER} WITH PASSWORD '${PASSWD}';" | sudo -u postgres psql -U postgres
echo "CREATE DATABASE ${DB} WITH OWNER ${USER};" | sudo -u postgres psql -U postgres
PGPASSWORD=${PASSWD} psql -d ${DB} -U ${USER} --host=localhost -f create-db-tables.sql
PGPASSWORD=${PASSWD} psql -d ${DB} -U ${USER} --host=localhost -f insert-mock-data.sql
