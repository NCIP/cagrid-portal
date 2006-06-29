GME installations should run a backup script to make sure the integrity of the database
can be restored apon any failures.  A general purpose script for this is provided in the
tools directory.  This script can be executed from a crontab and will maintain 5 rolling 
backup caches of the GME databases.