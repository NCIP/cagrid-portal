############
# Contents #
############
1. Requirements
2. Installation
3. Deploying
4. SSL Configuration
5. Running
6. Configure the site structure

################
# Requirements #
################

1. Java 1.5
   - Make sure JAVA_HOME is set and Java SDK executable is on the PATH.
2. Ant 1.6.5
   - Make sure that ANT_HOME is set and Ant executable is on the PATH.
3. MySQL 5+


################
# Installation #
################

1. Get a Google Maps API key: http://www.google.com/apis/maps/signup.html
   - If your host's name is my.host.com, and the HTTP server is listening to port 8080
     then the URL you should use is: http://my.host.com:8080
   - Use this key to set the value of cagrid.portal.map.google.apiKey in build.properties.
   
2. Get an ApplicationID from Yahoo: https://developer.yahoo.com/wsregapp/index.php
   Use this ID to set the value of cagrid.portal.geocoder.yahoo.appId in build.properties.

3. Synchronize with NCICB production trust fabric
   - Run: ant config-trust

4. Configure DB profiles:
   - Set the following properties in build.properties
     - liferay.db.host
	 - liferay.db.port
	 - liferay.db.name
	 - liferay.db.username
	 - liferay.db.password

******* STILL WORKING *******	 

5. Create database:
 - For example, if the value of hibernate.connection.url is "jdbc:mysql://localhost:3306/portal2"
   then you should run something like this SQL:
   
   		create database portal2;
   		
6. Run: ant db:load-schema

7. Run: ant aggr:load-workspaces
   - NOTE: you'll see lots of warnings and error messages. Unless you see a stacktrace,
           it worked.
           

8. Configure the following properties in build-liferay.xml
 - liferay.db.username
 - liferay.db.password
 - liferay.db.url

 
9. Run: ant liferay:install-liferay

10. Create liferay database:

	create database lportal2 character set utf8;
	
#############
# Deploying #
#############

ant liferay:deploy-authn
ant liferay:deploy-layouts
ant liferay:deploy-theme
ant liferay:deploy-portlets

#####################
# SSL Configuration #
#####################

The portal must use HTTPS. The installation script will take care of configuring Tomcat (in JBoss)
appropriately. But, you must still provide/create the certificate and keystore. By default, 
the installation script will configure Tomcat to look for the keystore at HOME/portal-liferay/portal-keystore
And it will expect the password to be 'portal'.

For further information, look here:
 - http://tomcat.apache.org/tomcat-5.5-doc/ssl-howto.html
 - http://docs.jboss.org/jbossas/jboss4guide/r5/html/ch9.chapt.html#ch9.https.sect

To generate a keystore and certificate, execute the following commands:

$JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -keystore /path/to/my/keystore

Make sure to use the same password for keystore and key. When prompted for first and last
name, specify the host name.

###########
# Running #
###########

A JBoss instance will be installed at $HOME/portal-liferay/jboss-4.0.5.GA.
Set JBOSS_HOME to this directory.
In JBOSS_HOME/bin, type: chmod u+x *
Edit JBOSS_HOME/bin/run.sh, set the JAVA_OPTS variable as follows:

   JAVA_OPTS="-Xms128m -Xmx512m -XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=128m -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC"

Start JBoss by running: ./run.sh > portal.log &
Then: tail -500f portal.log

################################
# Configure the site structure #
################################

The basic site structure can be configured by using one of the Liferay administrative portlets
to import a Liferay archive (lar) files. I've created these lar files in the portals/liferay/lars/ directory.

Follow these directions to import the lar file:

1. Sign in:
   - username: portaladmin@cabig.nci.nih.gov
   - password: p0rtal@dmin
2. From upper right-hand corner, click drop-down list,
    then select MyPlaces > My Community. Then click on the icon to the right
    of the "Public Pages" text.
3. Click on the "Import/Export" tab.
4. Click on the "Import" sub tab.
5. Select the "Portlet Preferences" and "Portlet Data" checkboxes.
6. Press the "Browse..." button to navigate to and select "portaladmin-public-community.lar"
7. Press "Import"
8. Repeat steps 2 through 7 for the "Private Pages" and "portaladmin-private-community.lar".   
9. In upper right-hand corner, click drop-down list,
   then select My Places > My Community > Private Pages
   - This time, just click on the "Private Pages" text, NOT the icon to the right.
10. In Communities portlet, click "Communities I have joined" tab.
11. Click the "Configure Pages" icon for the "Guest" community. 
   - That's the 3rd icon from the left. It looks two pieces of paper.
12. Click on the "Import/Export" tab.
13. Click on the "Import" sub tab.
14. Select the "Portlet Preferences" and "Portlet Data" checkboxes.
15. Press the "Browse..." button to navigate to and select "guest-community.lar"
16. Press "Import"

Then you can navigate back to My Places > Guest > Public Pages. Or you could just sign out.

Note: If you see an error message on the Home page saying that "you don't have privileges to
view this portlet", right below the Google Map, just restart JBoss.

################
# Known Issues #
################

1. Custom caGrid favicon does not appear.
    - See: http://support.liferay.com/browse/LEP-3996;

