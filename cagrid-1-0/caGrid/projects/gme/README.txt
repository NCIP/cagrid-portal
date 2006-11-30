IMPORTANT:
Before deploying the GME the user is required to set the service.deployment.host variable
in the deploy.properties file.  This variable must be set to the name of the host as the
outside world would see it.  

The default GME configuration is set to connect to a MySQL database on hte localhost with
no password and username root.  If you need to change this be sure to edit the gme config
file in the etc directory.

GME installations should run a backup script to make sure the integrity of the database
can be restored apon any failures.  A general purpose script for this is provided in the
tools directory.  This script can be executed from a crontab and will maintain 5 rolling 
backup caches of the GME databases.

