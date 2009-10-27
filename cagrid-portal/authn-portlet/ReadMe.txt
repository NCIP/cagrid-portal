

Build Instructions
---------------------
1.Checkout the cagrid-portal project from svn to ${SRC} directory
  https://ncisvn.nci.nih.gov/svn/cagrid/trunk/cagrid/Software/portal/cagrid-portal

2.Run the following ANT commands
  "ant -Dsingle.project.name=authn-portlet build-project"


3.Deploy the Portlet by running the following ANT command
  from the ${SRC}/authn-portlet directory

  "ant deploy-portlet"

4. Copy ${SRC}/authn-portlet/build/jars/caGrid-portal-authn-portlet-domain.jar
   to $CATALINA_HOME/common/lib/ext folder (if running Liferay in Tomcat)
   or in the Application servers ext folder

5. Copy ${SRC}/authn-portlet/ext/jars
   to $CATALINA_HOME/common/lib/ext folder (if running Liferay in Tomcat)
   or in the Application servers common/shared library folder

6. Copy authn-portlet/build/jars/caGrid-portal-authn-portlet-service.jar
   to the Liferay servers lib folder
   For eg. ${CATALINA_HOME}/webapps/ROOT/WEB-INF/lib

7. Edit/Create the portal-ext.properties file in the Liferay deployment
    For eg.  ${CATALINA_HOME}/webapps/ROOT/WEB-INF/classes folder
    (if running Liferay in Tomcat)

    and add the following text

    auto.login.hooks=gov.nih.nci.cagrid.portal.authn.service.SharedSessionAutoLoginLoader
    session.shared.attributes=org.apache.struts.action.LOCALE,COMPANY_,USER_,CAGRIDPORTAL_ATTS_


8. Start Liferay

9. Add the cagrid-authn Portlet to the Portal