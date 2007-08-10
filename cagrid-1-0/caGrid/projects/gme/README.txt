IMPORTANT:
Before deploying the GME the user is required to set the service.deployment.host variable
in the deploy.properties file.  This variable must be set to the name of the host as the
outside world would see it.  

The default GME configuration is set to connect to a MySQL database on the localhost with
no password and username root.  If you need to change this be sure to edit the gme config
file in the etc directory.  The GME also dynamically creates its databases and tables.
Make sure that the database priviliges are set correctly to enable this.

GME installations should run a backup script to make sure the integrity of the database
can be restored apon any failures.  A general purpose script for this is provided in the
tools/backup directory.  This script can be executed from a crontab and will maintain 5 rolling 
backup caches of the GME databases.

If it is desired to move a GME's database from one machine to another or from one service
URL to another.  Scripts for doing so can be found in the tools/move directory.  The gmeExportDB
script will save the databases data to a zip file.  The gmeImportDB script will import the data
into the new database from an export zip file.  The gmeChangeURL script will enable changing the service URL of the gme
so that local references stored in the GME registry database can be resolved.

