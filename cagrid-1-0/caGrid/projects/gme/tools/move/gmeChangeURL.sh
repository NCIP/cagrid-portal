#!/bin/sh

# This script will enable changing the GME service names in the
# registry if data in the GME has been moved to another service
# location via the gmeExport and gmeImport scripts or just a simple
# change in the GME service URL.
# The two parameters must be old hostname and new hostname
# feel free to change the database if it is different than below
# also, if the mysql root user has a password than add 
# -p <password> after the -u root on the below call to mysql


if [ $# -le 3 ]; then
         echo usage: gmeChangeURL.sh database_prefix old_service_url new_service_url [database_password]
         exit 1
fi

database_prefix=$1
oldservicename=$2
newservicename=$3


database="${databaseprefix}_GME_REGISTRY"


echo changing hostname to ${newservicename}
if [ $# -eq 3 ]; then
echo "use ${database}; update namespaces set service_ID='${newservicename}' where service_id='${oldservicename}';" | mysql -u root
fi

if [ $# -eq 4 ]; then
echo "use ${database}; update namespaces set service_ID='${newservicename}' where service_id='${oldservicename}';" | mysql -u root -p=$2
fi


echo Finished Modifying Databases

exit 