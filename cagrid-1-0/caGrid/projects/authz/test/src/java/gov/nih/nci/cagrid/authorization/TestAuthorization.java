package gov.nih.nci.cagrid.authorization;

import java.util.Iterator;
import java.util.List;

import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.authorization.domainobjects.Group;

public class TestAuthorization {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String catalinaHome = "/Users/joshua/dev/ew_ext_1/authz/test/sdk-3.1-example/for_upt/jakarta-tomcat-5.0.30";
		
		System.setProperty("java.security.auth.login.config", catalinaHome + "/conf/jaas.config");
		System.setProperty("gov.nih.nci.security.configFile", catalinaHome + "/conf/ApplicationSecurityConfig.xml");
		
		String localUsr = "reader";
		String gridIdentity = "/CN=test2/ST=MD/C=US/E=me@somewhere.com/O=semanticbits/OU=dev";
		String serviceUrl = "http://someservice/url";
		String app = "sdk";
		String objectId = "gov.nih.nci.cabio.domain.Gene";
		String privilege = "READ";
		AuthenticationManager mgr = SecurityServiceProvider.getAuthenticationManager(app);
		System.out.println("mgr = " + mgr.getClass().getName());
		boolean authenticated = mgr.login(gridIdentity, serviceUrl);
		System.out.println("Authenticated: " + authenticated);
		
		AuthorizationManager mgr2 = SecurityServiceProvider.getAuthorizationManager(app);
		System.out.println("mgr2 = " + mgr2.getClass().getName());
		boolean authorized = mgr2.checkPermission(gridIdentity, objectId, privilege);
		System.out.println("Authorized: " + authorized);
		authorized = mgr2.checkPermission(localUsr, objectId, privilege);
		System.out.println("Authorized: " + authorized);
		
		List groups = mgr2.getAccessibleGroups(objectId, privilege);
		System.out.println("Got " + groups.size() + " groups.");
		for(Iterator i= groups.iterator(); i.hasNext();){
			Group group = (Group)i.next();
			System.out.println("Group: " + group.getGroupName());
		}
	}

}
