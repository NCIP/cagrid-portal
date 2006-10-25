IMPORTANT:
Before deploying the GME the user is required to set the service.deployment.host variable
in the deploy.properties file.  This variable must be set to the name of the host as the
outside world would see it.  


GME installations should run a backup script to make sure the integrity of the database
can be restored apon any failures.  A general purpose script for this is provided in the
tools directory.  This script can be executed from a crontab and will maintain 5 rolling 
backup caches of the GME databases.

