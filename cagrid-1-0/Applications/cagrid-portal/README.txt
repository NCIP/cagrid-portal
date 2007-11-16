############
# Contents #
############
1. Requirements
2. Installation
3. Running
4. Deploying Portlets
5. Configure the site structure

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
   - If your host's name is my.host.com, then the URL you should use is:
    	http://my.host.com:8080
   - Edit portlets/src/war/WEB-INF/context/common.xml
      - In the "mapBean" bean definition, set the "apiKey" property to the key that you obtained.

2. Synchronize with NCICB production trust fabric
   - Either the caGrid distribution to do this: http://www.cagrid.org/mwiki/index.php?title=GTS:1.1:Administrators_Guide:SyncGTS:Command_Line_Approach
   - Or, just copy aggr/certificates/nci_prod/* to ~/.globus/certificates/

3. Configure DB profiles:
 - DB profile configurations are under db/etc directory. Configure the "default" and "test"
   profiles.
   - In db/etc/default.hibernate.properties, edit:
     - hibernate.connection.url
     - hibernate.connection.username
     - hibernate.connection.password
   - Do the same for db/etc/test.hibernate.properties

4. Install mysql.

5. Create database:
 - For example, if the value of hibernate.connection.url is "jdbc:mysql://localhost:3306/portal2"
   then you should run something like this SQL:
   
   		create database portal2;
   		grant all privileges on portal2.* to 'portal2'@'%' identified by 'portal2';
   		flush privileges;
   		
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
	grant all privileges on lportal2.* to 'lportal2'@'%' identified by 'lportal2';
   	flush privileges;
         
###########
# Running #
###########

A JBoss instance will be installed at $HOME/portal-liferay/jboss-4.0.5.GA.
Set JBOSS_HOME to this directory.
In JBOSS_HOME/bin, type: chmod u+x *
Edit JBOSS_HOME/bin/run.sh, set the JAVA_OPTS variable as follows:

   JAVA_OPTS="-Xms128m -Xmx512m -XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=128m -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC"

Start JBoss by running: ./run.sh
You may see some error message about a ClassCastException related to Log4J. You can ignore this.

######################
# Deploying Portlets #
######################

By default, the liferay autodeploy directory is $HOME/liferay/deploy. If you place a war file that
contains a portlet application into that directory, it will be deployed.

You can deploy the cagridportlets.war file by typing:

	ant liferay:deploy-portlets
	

################################
# Configure the site structure #
################################

The basic site structure can be configured by using one of the Liferay administrative portlets
to import a Liferay archive (lar) files. I've created these lar files in the portals/liferay/lars/ directory.

Follow these directions to import the lar file:

1. Sign in:
   - username: portaladmin@cabig.nci.nih.gov
   - password: p0rtal@dmin
2. In upper right-hand corner, click drop-down list,
   then select My Places > My Community > Private Pages
3. In Communities portlet, click "Communities I have joined" tab.
4. Click the "Configure Pages" icon for the "Guest" community. 
   - That's the 3rd icon from the left. It looks two pieces of paper.
5. Click on the "Import/Export" tab.
6. Click on the "Import" sub tab.
7. Select NONE of the checkboxes.
9. Press the "Browse..." button to navigate to and select "guest-community.lar"
10. Press "Import"
11. From upper right-hand corner, click drop-down list,
    then select MyPlaces > My Community. Then click on the icon to the right
    of the "Public Pages" text.
12. Click on the "Import/Export" tab.
13. Click on the "Import" sub tab.
14. Select NONE of the checkboxes.
15. Press the "Browse..." button to navigate to and select "portaladmin-public-community.lar"
16. Press "Import"
17. Repeat steps 11 through 16 for the "Private Pages" and "portaladmin-private-community.lar".

Then you can navigate back to My Places > Guest > Public Pages. Or you could just sign out.
