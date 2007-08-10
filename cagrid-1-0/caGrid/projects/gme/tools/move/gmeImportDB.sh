#!/bin/sh

#
# This script is designed to work when there is no password on  the database.  If there is
#   a password on the database just add a "-p <password>" after the "-u root" line
#   of the mysqldump call.  The "root" username for the database can also be changed.  The filenames for the imports are assumed to be the <databasename>.sql.gz.
#

importFileName=$1

tar -xvf ${importFileName}

databases="GlobusGME_GME_REGISTRY GlobusGME_GME_SCHEMA_STORE GlobusGME_GME_SCHEMA_CACHE"

for database in $databases ; do

echo Importing database ${database}

gunzip ${database}.sql.gz

mysql -u root ${database} < ${database}.sql

rm -fr ${database}.sql.gz
rm -fr ${database}.sql

done

echo Finished Importing databases

exit 