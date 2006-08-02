


===========================================================================
Pre-requistie software
===========================================================================
1. JDK 1.4.x + version (or caGrid recommended JDK version)
2. Install Globus and set $GLOBUS_LOCATION set
3. Install Tomcat and set $CATALINA_LOCATION set


===========================================================================
Configuration Information
===========================================================================

Portal needs a Database configured in the following way

- Create DB with the portal/resources/Portal_Data_Model.SQL script
- Configure the jdbc connection in ${username}-portal-build.properties
   file.


When deploying Portal, the only information needed to connect the Portal
to caGrid are the URI's to the caGrid index services.

Edit the portal/src/properties/applicationContext-data-access.xml file
and look for the line
    '<bean id="dbInitBean"'

Add the index services that you want this portal to extract information
from.

<property name="indexList">
 <set>
  <value>http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService</value>


Replace the current entries in the indexList to your own



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


===========================================================================
Testing Instructions
===========================================================================

To execute the tests just run 'ant testLocal' after setting your
{username}-portal-build.properties file with correct settings for your local
environment.

All JUnit test cases are named *TestCase
All unit tests extend an abstract spring class and run in a mock environment
 created by spring and don't need an application/web server


You will need a database to run some of the tests (Nothing will be committed
to the DB). The tests are in the "test" directory. The test cases that need
a DB to execute are named *LocalTestCase
Other test cases can be executed without any backend
