For the latest version of this information, see here:
   http://www.cagrid.org/mwiki/index.php?title=CaGrid:How-To:Install_caGrid_Portal_2.0

############
# Contents #
############
1. Pre-Installation
2. Installation
3. Post-Installation
4. Re-Installation
5. Firewall/Connectivity Considerations

####################
# Pre-Installation #
####################

# Check-out caGrid Portal from CVS #

You can use the following settings to check out the source code:
 - username: anonymous
 - password: anonymous
 - protocol: ssh
 - host: cbiocvs2.nci.nih.gov
 - repository: /share/content/gforge/cagrid-1-0
 - module: cagrid-1-0/Applications/cagrid-portal
 
If you are using a commandline CVS client, you could use this command:

   export CVS_RSH=ssh
   cvs -d :ext:anonymous@cbiocvs2.nci.nih.gov:/share/content/gforge/cagrid-1-0 co \
      cagrid-1-0/Applications/cagrid-portal

After checkout, the caGrid Portal source code directory (referred to henceforth as $SRC) 
will be located under ./cagrid-1-0/Applications/cagrid-portal.

# Obtain Required Software #

The following software must be installed:
 - Java 1.5
   - Make sure JAVA_HOME is set and Java SDK executable is on the PATH.
 - Ant 1.6.5
   - Make sure that ANT_HOME is set and Ant executable is on the PATH.
 - MySQL 5+
   - You will need privileges to create and delete databases.

# Create Databases #

Two databases are needed for the caGrid Portal application. One for Liferay data, 
and one for caGrid Portal data. By convention, the names of these databases are
'lportal' and 'portal2'. If you are using these database names, then you need to
execute the following SQL in your MySQL database:

   create database lportal character set utf8;
   create database portal2;

You will also need to provision an account that has access to these databases. If
the same account will have full access to both database, you can uses the following
SQL:

   grant all privileges on lportal.* to 'portalacct'@'%' identified by 'mypwd';
   grant all privileges on portal2.* to 'portalacct'@'%' identified by 'mypwd';
   flush privileges;
   
This gives the user 'portalacct' all privileges on these databases and this user can
access the databases from any host. See the MySQL admin documentation for details:
http://dev.mysql.com/doc/refman/5.0/en/index.html

# Create SSL Certificate and Keystore #

Some pages in the portal need to be protected with HTTPS. You will need to create
an SSL certificate that the embedded Tomcat instance (running in JBoss) will use.
The installation script will take care of configuring the Tomcat HTTPS Connector,
but you still must either create a certificate and PKCS12 keystore, or specify the
path to an existing keystore and provide the keystore password.

The basic process of creating a keystore using Java's keytool is as follows:

$JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -keystore /path/to/my/keystore

Make sure to use the same password for keystore and key. When prompted for first and last
name, specify the host name.

Complete instructions on using keytool can be found here: 
 - http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html

Instructions on configure JBoss and Tomcat to use SSL are here:
 - http://tomcat.apache.org/tomcat-5.5-doc/ssl-howto.html
 - http://docs.jboss.org/jbossas/jboss4guide/r5/html/ch9.chapt.html#ch9.https.sect

By default, the installation script will assume that the keystore is located at
$HOME/portal-liferay/portal-keystore and that the keystore password is 'portal'. This 
location and password can be configured in the properties file that the installation
script uses (described later).

# Obtain Google Maps API Key #

You can get a Google Maps API key here: http://www.google.com/apis/maps/signup.html
If your host's name is my.host.com, and the HTTP server is listening to port 8080
then the URL you should use is: http://my.host.com:8080
Save this key for future use.

# Obtain Yahoo ApplicationID #

Get an ApplicationID from Yahoo: https://developer.yahoo.com/wsregapp/index.php
Save this for future use.

# Configure caGrid Trust Fabric #

The caGrid Portal uses GAARDS to authenticate users, so the caGrid trust fabric must be
configured on the machine that will host the portal. The portal itself will use the 
GTS client to maintain the trust fabric, but the trust fabric must be bootstrapped.

By default, the portal will use the nci_prod as the target grid. If you are using this
grid, you don't need to do anything. 

If you are using one of the following grids:
 - nci_dev, nci_stage, osu_dev, training
Then the trust synchronization configuration has already been provided. You will just
need to create a corresponding build.properties file. Look at build-nci_qa.properties
as an example. When you run the installation script, you'll have to specify the name
of your target environment. More details about this are provided in the Configure
caGrid Portal Installation section below.

If you are using another target grid, the you need 
to do three things:
 1. Create a sync-description.xml file to configure the GTS client that the portal uses.
 2. Bootstrap the trust fabric by placing root certificates under the 
    $HOME/.globus/certificates directory
 3. Configure caGrid Portal to use your sync-description.xml configuration.
 
Do configure the portal to uses your sync-description.xml and certificates, you need to edit
the 'aggr.trust.syncgts.file' and 'aggr.trust.certs.dir' properties to the path to your
sync-description.xm file and the directory in which the root certificates are found, respectively.
 
Directions for configuring a trust fabric using caGrid tools are here:
 - http://www.cagrid.org/mwiki/index.php?title=GTS:1.1:Administrators_Guide:Syncing_With_the_Trust_Fabric

# Configure caGrid Portal Installation #

The caGrid Portal installation script is at $SRC/build.xml. This is an Ant build file. It is configured
by the properties that are defined in the build.properties file in the same directory. 
To customize the installation, you can directly edit build.properties or you can override those 
properties by adding them to the build-local.properties file. 

Furthermore, if you want to maintain installation configurations for multiple deployment tiers, 
you can create multiple properties files whose names have the form: build-<tier>.properties, where 
'<tier>' is replaced with the name of the tier. For example, if I have created a configuration
for the 'testing' tier, then I would create a file named build-testing.properties, and then run
Ant from the $SRC directory like this:

   ant -target.env=testing install
   
See the $SRC/build.properties file itself for descriptions of all the properties. If you
are using the nci_prod target grid and default installation location, then you will usually only need to 
edit the following properties:
 - liferay.admin.password
 - liferay.db.host
 - liferay.db.port
 - liferay.db.name
 - liferay.db.username
 - liferay.db.password
 - cagrid.portal.admin.email
 - cagrid.portal.db.url
 - cagrid.portal.db.username
 - cagrid.portal.db.password
 - cagrid.portal.geocoder.yahoo.appId
 - cagrid.portal.map.google.apiKey
 - cagrid.portal.security.encryption.key

################
# Installation #
################

From the $SRC directory, run the following command:

   ant -Dtarget.env=<envname> install
   
If you have just updated build.properties or build-local.properties directly, then the command would be:

   ant install

#####################
# Post-Installation #
#####################

# Setup Environment #

On MS Windows, use the System application in the Control Panel to set the JBOSS_HOME environment
variable to point to the directory in which JBoss was installed. By default, this will be:

   %HOMEDRIVE%%HOMEPATH%\portal-liferay\jboss-4.0.5.GA
   
On Unix/Linux/Mac, the default location will be $HOME/portal-liferay/jboss-4.0.5.GA. You can set
the environment variable in the bash shell as follows:

   export JBOSS_HOME=$HOME/portal/liferay/jboss-4.0.5.GA

# Start the Application Server #

On MS Windows, go to %JBOSS_HOME%\bin and double-click run.bat

On Unix/Linux/Mac, you should do something like this:

   cd $JBOSS_HOME/bin
   chmod u+x *
   ./run.sh > portal.log &

# Import the Site Structure #

The basic site structure can be configured by using the Liferay administrative portlets
to import Liferay Archive (lar) files. I've created these lar files in the $SRC/portals/liferay/lars/ 
directory.

Follow these directions to import the lar file:

1. In your browser, go to: https://<hostname>:8443/
2. Sign in:
   - username: portaladmin@cabig.nci.nih.gov
   - password: p0rtal@dmin
3. From upper right-hand corner, click "Welcome" drop-down list,
    then select MyPlaces > My Community. Then click on the icon to the right
    of the "Private Pages" text.
4. Click on the "Import/Export" tab.
5. Click on the "Import" sub tab.
6. Select the "Portlet Preferences" and "Portlet Data" checkboxes.
7. Press the "Browse..." button to navigate to and select "portaladmin-private-community.lar"
8. Click "OK"
9. Click "Import" button.
10. Click back arror icon in upper, right-hand corner.n
11. In upper right-hand corner, click "Welcome" drop-down list,
   then select My Places > My Community > Private Pages
   - This time, just click on the "Private Pages" text, NOT the icon to the right.
12. In Communities portlet, click "Communities I have joined" tab.
13. Click the "Configure Pages" icon for the "Guest" community. 
   - That's the 3rd icon from the left. It looks two pieces of paper.
14. Click on the "Import/Export" tab.
15. Click on the "Import" sub tab.
16. Select the "Portlet Preferences" and "Portlet Data" checkboxes.
17. Press the "Browse..." button to navigate to and select "guest-community.lar"
18. Click "OK"
19. Click "Import" button.
20. Click back arrow icon in upper, right-hand corner of page.
21. In upper, right-hand cornder, click "Welcome" drop-down list,
    then selected My Places > Guest Community > Public Pages.

You should see the caGrid Portal Home page. Sign out by selecting the "Sign Out" option
from the "Welcome" drop-down list.

Note: If you see an error message on the Home page saying that "you don't have privileges to
view this portlet", right below the Google Map, just restart JBoss.

# Secure Encryption Key #

A file named 'cagridportal.properties' will be generated and placed in two locations on 
the filesystem:
 - $JBOSS_HOME/server/default/deploy/liferay-portal.war/WEB-INF/classes/cagridportal.properties
 - $JBOSS_HOME/server/default/deploy/cagridportlets/WEB-INF/classes/cagridportal.properties

The value of the 'cagrid.portal.security.encryption.key' property in this file will be used to
encrypt authentication tickets as well as portal users' temporary grid credentials (in the 
database). It is important that these files are protected so that users' grid credentials
cannot be decrypted by a malicious user who has access to both the hosting system and database. 
Set file permissions appropriately for your system.

###################
# Re-Installation #
###################

This section describes the steps needed to wipe out an existing installation and re-install.
All existing data will be destroyed. See the Administrator's Guide for directions on backing
up data and doing batch imports of data.

1. Delete the JBoss installation directory
2. Drop the portal2 and lportal databases.
3. Re-create databases.
4. From $SRC, run: ant -Dtarget.env=<env> clean install

########################################
# Firewall/Connectivity Considerations #
########################################

By default, the portal installation script will download JBoss and several Liferay artifacts. If you
are behind a firewall, you will need to provide the proxy configuration for your JVM. Essentially,
you just need to set the ANT_OPTS environment variable to include the standard Java proxy
settings, like so:

  export ANT_OPTS="-Dhttp.proxyHost=proxy -Dhttp.proxyPort=8080"

Further instructions are here: http://ant.apache.org/manual/proxy.html

IF YOU HAVE NO INTERNET ACCESS, you can still use the installation script. You will just need
to download the dependencies manually and then edit/provide the following properties in build.properties
so that they point to local directories/files:

 - liferay.jboss.home
 - liferay.jboss.zip
 - liferay.downloads.dir
 - liferay.dependencies.zip
 - liferay.war

 See build-liferay.xml for details.
 