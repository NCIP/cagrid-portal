
Portal ReadMe

===========================================================================
1. Pre-requistie software
===========================================================================
1. JDK 1.4.x + version (or caGrid recommended JDK version)
2. Install Globus and set $GLOBUS_LOCATION environment variable
3. Install Tomcat and set $CATALINA_LOCATION environment variable
4. mySQL database. Set the db connection details in the portal-build properties


===========================================================================
2. Configuration Information
===========================================================================

    ===========================================================================
    2.1 Database
    ===========================================================================

        Portal needs a Database configured in the following way

        - Configure the jdbc connection in portal-build.properties
            or in ${user.home}/.portal-build.properties/

        - Create DB with the portal/resources/Portal_Data_Model.SQL script
        - Run the
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
      2.3 Security Settings
      ===========================================================================
        Portal will automatically sync with the caGrid trust fabric to establish
        trust to other caGrid services/resources. For deployment, copy
        portal/resources/trust-ca-cert.1
        to ~/.globus/certificates directory in Unix
        OR
        C:\Documents and Settings\kherm\.globus or similar in Windows

      ===========================================================================
      2.4 Portal Localization and Internationalization
      ===========================================================================
        Portal is a I9 compliant application. All the text that is displayed in
        the portal is externalized into a message bundle. This is configured
         in the portal-faces-config.xml file.

         <message-bundle>
            Portal-Labels
        </message-bundle>

         By editing this message bundle file
        file (located portal/webcontent/resources/Portal-Labels_en.properties
        you can changet the text in the portal

        You can also localizae the portal by adding your own Portal-Labels message
        bundle. Look at the following section in faces-config.xml

          <locale-config>
        		<default-locale>en</default-locale>
        		<supported-locale>en</supported-locale>
          </locale-config>


        You can add other supported locale and provide an appropriate Portal-Labels

        For eg.

          <locale-config>
        		<default-locale>fr</default-locale>
        		<supported-locale>en</supported-locale>
        		<supported-locale>fr</supported-locale>
          </locale-config>

          This means Portal will look for a Portal_Labels_fr.propeerties
          file in the classpath to display its text.
          This way you can translate the portal into french or other languages

        For a brief tutorial on this please refer
        http://www.laliluna.de/javaserver-faces-message-resource-bundle-tutorial.html


===========================================================================
3. Build Instructions
===========================================================================
    -Use the portal-build.properties file to configure the build
    for your environment. This file will be used in absense of the
    ${user.home}/.portal-build.properties file.
    The ${user.home}/.portal-build.properties file is usually written by
    the caGrid Installer.

    - Use the build.xml file to build the project.
    (During development and source releases the Portal project is to be built
    from main (Master) build (../../caGrid)
        - Build with "ant all"
        - Optionally run "ant createDatabase" (see section 2.1 Database)

        - Run "ant deployTomcatExploded" OR "ant deployTomcatWar" to deploy
        into tomcat application server

           OR during DEVELOPMENT

        - Run "ant buildPortal"
        - Copy the webcontent/META-INF/context-dev.xml file to $CATALINA_HOME\conf\Catalina\localhost\portal.xml

            OR

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
