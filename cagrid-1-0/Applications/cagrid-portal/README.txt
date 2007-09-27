################
# Installation #
################

1. Get a Google Maps API key: http://www.google.com/apis/maps/signup.html
   - If your host's name is my.host.com, then the URL you should use is:
    	http://my.host.com:8080/cagridportal
   - Edit portlets/src/war/WEB-INF/context/gmap-portlet.xml
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
   		
8. If you don't have Maven1 installed, run: ant js2:install-maven

9. Run: ant js2:deploy-portlets
 - NOTE: If you have just installed Maven, this will take while because Maven needs to download
         all the dependencies. But future runs of this target will be fast.
         
###########
# Running #
###########

By default, the a Tomcat instance will be installed and configured at HOME/portal-tomcat/apache-tomcat-5.5.23.
Just start up tomcat and let it work for a few minutes. Then browse to:

  http://localhost:8080/cagridportal
  
 