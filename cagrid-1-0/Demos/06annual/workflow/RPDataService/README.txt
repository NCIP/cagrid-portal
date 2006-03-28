Analytical Service Skeleton:
======================================
To use this skeleton you should have had it generated from the core
build file by running "ant createAnalyticalSkeleton" from the top level
of the caGrid core.  This should have created you this skeleton.  The instructions
below will help you in configuring, building, and deploying this analytical
service.

All that is needed for this service at this point is to populate the analytical
service provider class in the src/ directory.

Prerequisits:
=======================================
Globus 4.0 installed and GLOBUS_LOCATION env defined
Tomcat > 4.0 installed and "CATALINA_HOME" env defined

To Build:
=======================================
"ant all" will build 
"ant resync" will resyn the service skeleton with the methods described in the analyticalMethods.xml
"ant deploy" will deploy to "CATALINA_HOME"

To Deploy:
=======================================
Update mako configuration:

(1) Edit mako/conf/localhost-mako-config.xml
	Towards the bottom, change username and password to the admin login of your mysql.  
	Change the host and port if necessary

(2) Edit service-config.wsdd

	(a) Change dataDir to point to the full path of data in your RPDataService directory
	(b) Change binaryDataDir to a directory where you want binary portions of XML documents to be stored
		Note: this needs to be writable by the user that runs Tomcat
	(c) Change cqlMapFile to point to the full path of queries/scanFeatures_map.xml in your RPDataService directory

(3) Build

	ant all

(4) Deploy to Tomcat

	ant deployTomcat

(5) Start MySQL

	However you do that on your system - mysqld probably.

(6) Start Mako

	ant runMako

(7) Initialize Mako (while Mako is running)

	ant initMako

(8) Restart Tomcat

	However you do that on your system - stop.sh and start.sh probably

(9) Test the service

	ant testService

Running the service:
=======================================
Assuming you have deployed previously, you should do steps 5 and 6 in deploy and then start Tomcat