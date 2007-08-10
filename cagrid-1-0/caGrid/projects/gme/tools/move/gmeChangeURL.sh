#!/bin/sh

# This script will enable changing the GME service names in the
# registry if data in the GME has been moved to another service
# location via the gmeExport and gmeImport scripts or just a simple
# change in the GME service URL.
# The two parameters must be old hostname and new hostname
# feel free to change the database if it is different than below
# also, if the mysql root user has a password than add 
# -p <password> after the -u root on the below call to mysql


oldservicename=$1
newservicename=$2


database="GlobusGME_GME_REGISTRY"


echo changing hostname to ${newservicename}

echo "use ${database}; update namespaces set service_ID='${newservicename}' where service_id='${oldservicename}';" | mysql -u root

echo Finished Importing databases

exit 