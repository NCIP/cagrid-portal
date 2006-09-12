package gov.nih.nci.cagrid.idp.client;

import gov.nih.nci.cagrid.idp.beans.CredentialType;
import gov.nih.nci.cagrid.idp.stubs.InsufficientAttributeException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class IDPServiceClientTestCase extends TestCase {

    public IDPServiceClientTestCase() {

    }

    public IDPServiceClientTestCase(String test) {
	super(test);
    }
    
    public void testSimpleCredentials(){
	try{
	    String endpoint = System.getProperty("test.idp.endpoint", "http://localhost:8080/wsrf/services/cagrid/IDPService");
	    String user1 = System.getProperty("test.idp.user1", "user1");
	    String user2 = System.getProperty("test.idp.user2", "user2");
	    String pwd1 = System.getProperty("test.idp.pwd1", "password1");
	    String pwd2 = System.getProperty("test.idp.pwd1", "password2");
	    
	    IDPServiceClient client = new IDPServiceClient(endpoint);
	    CredentialType cred1 = new CredentialType();
	    cred1.setUserID(user1);
	    cred1.setPassword(pwd1);
	    client.login(cred1);
	    
	    try{
		CredentialType cred2 = new CredentialType();
		cred2.setUserID(user2);
		cred2.setPassword(pwd2);
		client.login(cred2);
		fail("Login for user2 should have failed with InsufficentAttributeException");
	    }catch(InsufficientAttributeException ex){

	    }

	}catch(Exception ex){
	    ex.printStackTrace();
	    fail("Error testing simple credentials: " + ex.getMessage());
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite(){
	TestSuite suite = new TestSuite();
	suite.addTest(new IDPServiceClientTestCase("testSimpleCredentials"));
	return suite;
    }

}
