#!/bin/sh

#
#	This script is designed to be used from a crontab.  The script will maintain 5
#   rolling backups of the GME databases.  The gap in these backups will be depended 
#   on the crontab.  The argument to this script is the backup directory (IT MUST EXIST).  This 
#   script is designed to work when there is no password on database.  If there is
#   a password on the database just add a "-p <password>" after the "-u root" line
#   of the mysqldump call.  The "root" username of the database can also be changed.
#

#make sure directory exists first
backupdir=$1

echo Starting to backup databases

databases="GlobusGME_GME_REGISTRY GlobusGME_GME_SCHEMA_STORE GlobusGME_GME_SCHEMA_CACHE"

for database in $databases ; do

echo Backing up database ${database}

mysqldump -u root --add-drop-database --add-drop-table --add-locks ${database} | gzip > ${backupdir}/${database}.sql.gz

mv ${backupdir}/${database}.sql.gz.4 ${backupdir}/${database}.sql.gz.5
mv ${backupdir}/${database}.sql.gz.3 ${backupdir}/${database}.sql.gz.4
mv ${backupdir}/${database}.sql.gz.2 ${backupdir}/${database}.sql.gz.3
mv ${backupdir}/${database}.sql.gz.1 ${backupdir}/${database}.sql.gz.2
mv ${backupdir}/${database}.sql.gz ${backupdir}/${database}.sql.gz.1

done

echo Finished backing up databases

exit 