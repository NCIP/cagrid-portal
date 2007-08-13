#!/bin/sh

# This script will export the gme databases data to a tar file.
# this file will contain the database data only and not the database
# or table creation commands

if [ $# -le 0 ]; then
         echo usage: gmeExportDB.sh database_prefix [database_password]
         exit 1
fi


databaseprefix=$1


echo Starting to backup databases


if [ $# -eq 1 ]; then
	mysqldump -u root --add-locks -n -t ${databaseprefix}_GME_REGISTRY | gzip > GME_REGISTRY.sql.gz

	mysqldump -u root --add-locks -n -t ${databaseprefix}_GME_SCHEMA_STORE | gzip > GME_SCHEMA_STORE.sql.gz

	mysqldump -u root --add-locks -n -t ${databaseprefix}_GME_SCHEMA_CACHE | gzip > GME_SCHEMA_CACHE.sql.gz
fi

if [ $# -eq 2 ]; then
	mysqldump -u root -p=$2 --add-locks -n -t ${databaseprefix}_GME_REGISTRY | gzip > GME_REGISTRY.sql.gz

	mysqldump -u root -p=$2 --add-locks -n -t ${databaseprefix}_GME_SCHEMA_STORE | gzip > GME_SCHEMA_STORE.sql.gz

	mysqldump -u root -p=$2 --add-locks -n -t ${databaseprefix}_GME_SCHEMA_CACHE | gzip > GME_SCHEMA_CACHE.sql.gz
fi


tar cvf gmeDBExport.tar GME_REGISTRY.sql.gz GME_SCHEMA_STORE.sql.gz GME_SCHEMA_CACHE.sql.gz

rm -fr GME_REGISTRY.sql.gz
rm -fr GME_SCHEMA_STORE.sql.gz
rm -fr GME_SCHEMA_CACHE.sql.gz

echo Finished backing up databases into file gmeDBExport.tar

exit 