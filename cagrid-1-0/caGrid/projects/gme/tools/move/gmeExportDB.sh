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

databases="${databaseprefix}_GME_REGISTRY ${databaseprefix}_GME_SCHEMA_STORE ${databaseprefix}_GME_SCHEMA_CACHE"

for database in $databases ; do

echo Backing up database ${database}

if [ $# -eq 1 ]; then
	mysqldump -u root --add-locks -n -t ${database} | gzip > ${database}.sql.gz
fi

if [ $# -eq 2 ]; then
	mysqldump -u root -p=$2 --add-locks -n -t ${database} | gzip > ${database}.sql.gz
fi

done

tar cvf gmeDBExport.tar GlobusGME_GME_REGISTRY.sql.gz GlobusGME_GME_SCHEMA_STORE.sql.gz GlobusGME_GME_SCHEMA_CACHE.sql.gz

rm -fr GlobusGME_GME_REGISTRY.sql.gz
rm -fr GlobusGME_GME_SCHEMA_STORE.sql.gz
rm -fr GlobusGME_GME_SCHEMA_CACHE.sql.gz

echo Finished backing up databases into file gmeDBExport.tar

exit 