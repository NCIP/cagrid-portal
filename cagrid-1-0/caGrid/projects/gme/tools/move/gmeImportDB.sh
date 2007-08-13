#!/bin/sh

#
# Import gme table data into a gme database on another server
#

if [ $# -le 1 ]; then
         echo usage: gmeImportDB.sh database_prefix import_file [database_password]
         exit 1
fi


databaseprefix=$1
importFileName=$2

tar -xvf ${importFileName}

databases="GME_REGISTRY GME_SCHEMA_STORE GME_SCHEMA_CACHE"

for database in $databases ; do

echo Importing gme database table data into ${database}

gunzip ${database}.sql.gz

if [ $# -eq 2 ]; then
        mysql -u root ${databaseprefix}_${database} < ${database}.sql
fi

if [ $# -eq 3 ]; then
        mysql -u root -p=$2 ${databaseprefix}_${database} < ${database}.sql
fi

rm -fr ${database}.sql.gz
rm -fr ${database}.sql

done

echo Finished Importing databases

exit 