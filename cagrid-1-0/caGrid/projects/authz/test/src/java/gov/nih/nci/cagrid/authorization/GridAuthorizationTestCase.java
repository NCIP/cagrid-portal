package gov.nih.nci.cagrid.authorization;

import gov.nih.nci.cagrid.authorization.impl.CSMGridAuthorizationManager;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GridAuthorizationTestCase extends TestCase {
	
	private String localUsr = "reader";
	private String localPwd = "reader";
	private String gridIdentity = "/CN=test2/ST=MD/C=US/E=me@somewhere.com/O=semanticbits/OU=dev";
	private String serviceUrl = "http://someservice/url";
	private String app = "sdk";
	private String objectId = "gov.nih.nci.cabio.domain.Gene";
	private String privilege = "READ";
	
	public GridAuthorizationTestCase(String name){
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(new GridAuthorizationTestCase("testCSMAuthorization"));
		suite.addTest(new GridAuthorizationTestCase("testGridCSMAuthorization"));
		return suite;
	}
	
	public void setUp(){
		
	}
	
	public void testCSMAuthorization(){
		try{
			String implementation = "gov.nih.nci.cagrid.authorization.impl.CSMGridAuthorizationManager";
			
			AuthenticationManager mgr = SecurityServiceProvider.getAuthenticationManager(app);
			String classname = mgr.getClass().getName();
			assertEquals("wrong implementation of authentication manager '" + classname + "'", implementation, classname);
			assertTrue("didn't authenticate grid identity", mgr.login(gridIdentity, serviceUrl));
			assertTrue("didn't authenticate local identity", mgr.login(localUsr, localPwd));
			
			AuthorizationManager mgr2 = SecurityServiceProvider.getAuthorizationManager(app);
			classname = mgr2.getClass().getName();
			assertEquals("wrong implementation of authorization manager '" + classname + "'", implementation, classname);
			assertTrue("didn't authorize grid identity", mgr2.checkPermission(gridIdentity, objectId, privilege));
			assertTrue("didn't authorize local identity", mgr2.checkPermission(localUsr, objectId, privilege));
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error testing csm authorization: " + ex.getMessage());
		}
	}
	
	public void testGridCSMAuthorization(){
		try{
			
			GridAuthorizationManager mgr = new CSMGridAuthorizationManager(app);
			assertTrue("didn't authorize grid identity", mgr.isAuthorized(gridIdentity, objectId, privilege));
			assertTrue("didn't authorize local identity", mgr.isAuthorized(localUsr, objectId, privilege));
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error testing grid csm authorization: " + ex.getMessage());
		}
	}
}
