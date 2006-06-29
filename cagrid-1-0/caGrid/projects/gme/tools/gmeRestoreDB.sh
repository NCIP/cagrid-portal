#!/bin/sh

#
# This script will restore the databases from the last time it was synched.
# The argument to this script is the backup directory. 
# This script is designed to work when there is no password on database.  If there is
#   a password on the database just add a "-p <password>" after the "-u root" line
#   of the mysqldump call.  The "root" username for the database can also be changed.
#

backupdir=$0


databases="GlobusGME_GME_REGISTRY GlobusGME_GME_SCHEMA_STORE GlobusGME_GME_SCHEMA_CACHE"

for database in $databases ; do

echo Importing database ${database}

cp -fr ${backupdir}/${database}.sql.gz.1 ${backupdir}/${database}.sql.gz
gunzip ${backupdir}/${database}.sql.gz

mysql -uroot ${database} < ${backupdir}/${database}.sql

rm -fr ${backupdir}/${database}.sql.gz
rm -fr ${backupdir}/${database}.sql

done

echo Finished Importing databases

exit 