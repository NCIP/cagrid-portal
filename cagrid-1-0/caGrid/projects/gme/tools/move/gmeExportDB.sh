#!/bin/sh

# This script will export the data base to a file.
#   This script is designed to work when there is no password on database.  If there is
#   a password on the database just add a "-p <password>" after the "-u root" line
#   of the mysqldump call.  The "root" username of the database can also be changed.
#


echo Starting to backup databases

databases="GlobusGME_GME_REGISTRY GlobusGME_GME_SCHEMA_STORE GlobusGME_GME_SCHEMA_CACHE"

for database in $databases ; do

echo Backing up database ${database}

mysqldump -u root --add-drop-database --add-drop-table --add-locks ${database} | gzip > ${database}.sql.gz

done

tar cvf gmeDBExport.tar GlobusGME_GME_REGISTRY.sql.gz GlobusGME_GME_SCHEMA_STORE.sql.gz GlobusGME_GME_SCHEMA_CACHE.sql.gz

rm -fr GlobusGME_GME_REGISTRY.sql.gz
rm -fr GlobusGME_GME_SCHEMA_STORE.sql.gz
rm -fr GlobusGME_GME_SCHEMA_CACHE.sql.gz

echo Finished backing up databases into file gmeDBExport.tar

exit 