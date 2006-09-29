
Portal ReadMe

===========================================================================
1. Pre-requistie software
===========================================================================
1. JDK 1.4.x + version (or caGrid recommended JDK version)
2. Install Globus and set $GLOBUS_LOCATION set
3. Install Tomcat and set $CATALINA_LOCATION set
4. mySQL database. Set the db connection details in the build properties


===========================================================================
2. Configuration Information
===========================================================================

    ===========================================================================
    2.1 Database
    ===========================================================================

        Portal needs a Database configured in the following way

        - Configure the jdbc connection in portal-build.properties
            or in ${user.home}/.portal-build.properties/
           file.

        - Create DB with the portal/resources/Portal_Data_Model.SQL script
            OR
        - Create a database called portal in mysql
        - Configure connection details in the portal-build.properties file
        - run "ant createDatabase" This script will automatically create
          the tables etc. needed by portal and populate it with seed
          data.

    ===========================================================================
    2.2 Index Service Configuration
    ===========================================================================

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
3. Build Instructions
===========================================================================
    -Use the portal-build.properties file to configure the build
    for your environment.
    This file will be used in absense of the
    ${user.home}/.portal-build.properties file.
    The ${user.home}/.portal-build.properties file is usually written by
    the caGrid Installer.


    - Use the build.xml file to build the project.
    (During development and source releases the Portal project is to be built
    from main (Master) build
        - Build with "ant all"
        - Optionally run "ant createDatabase"

        - Run "ant deployTomcatExploded" OR "ant deployTomcatWar" to deploy
        into tomcat
           OR
        - Run "ant portalPortal"
        - Copy the webcontent/META-INF/context.xml file to $CATALINA_HOME\conf\Catalina\localhost\portal.xml
        (This *might* not work if you are running the app server from an IDE)

    - The project can be setup as a web project in an IDE, where webcontent
    directory is the web resources root directory. This way you can use the IDE
    to build the portal project as a web project



===========================================================================
4. Testing Instructions
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


===========================================================================
5. Logging Instructions
===========================================================================
    Logging is done through log4j

    Portal has built in logging (at info, debug and error) levels. This can be
    configured with the portal-build.properties file (described in Section 3)

    Other components like Spring, JSF can also be logged by setting the various
    log level settings in the build properties file

    The logging output can be configured in the resources/log4j.xml.template file.
    The default messages are logged to a
    $CATALINA_HOME/logs/portal.log that is rotated periodically.
    But this be can configured to log to the console (say for debugging)
