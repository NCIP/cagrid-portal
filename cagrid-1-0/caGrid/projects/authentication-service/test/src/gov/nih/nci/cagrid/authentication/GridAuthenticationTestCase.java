package gov.nih.nci.cagrid.authentication;

import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.SecurityServiceProvider;

import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GridAuthenticationTestCase extends TestCase {
	
	private String localUsr = "reader1";

	private String localPwd = "reader1";

	private String app = "SDK";
	
	public GridAuthenticationTestCase(String name) {
		super(name);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new GridAuthenticationTestCase("testCSMAuthentication"));
		return suite;
	}

	public void setUp() {

	}
	public void testCSMAuthentication() {
		try {
			AuthenticationManager mgr = SecurityServiceProvider
			.getAuthenticationManager(app);
			
			Subject subject = mgr.authenticate(localUsr, localPwd);
			assertNotNull("didn't authenticate local identity", subject);

			Set principals = subject.getPrincipals();
			assertNotNull("principals is null", principals);
			assertTrue("should've gotten 4, got " + principals.size(), principals.size() == 4);
			for(Iterator i = principals.iterator(); i.hasNext();){
				Principal p = (Principal)i.next();
				System.out.println("principal: " + p.getName());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error testing csm authentication: " + ex.getMessage());
		}
	}

}
