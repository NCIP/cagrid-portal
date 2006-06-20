


===========================================================================
Pre-requistie software
===========================================================================
1. JDK 1.4.x + version (or caGrid recommended JDK version)
2. Install Globus and set $GLOBUS_LOCATION set
3. Install Tomcat and set $CATALINA_LOCATION set


===========================================================================
Build Instructions
===========================================================================
-Use the ${username}-portal-build.properties file to configure the build
for your environment.

- Use the build.xml file to build the project.
(During development and source releases the Portal project is to be built
from main (Master) build

- The project can be setup as a web project in an IDE, where webcontent
directory is the web resources root directory. This way you can use the IDE
to build the portal project as a web project