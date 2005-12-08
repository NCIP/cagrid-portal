package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.SimpleResourceManager;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.idp.AssertionCredentialsManager;
import gov.nih.nci.cagrid.gums.idp.IdPConfiguration;
import gov.nih.nci.cagrid.gums.idp.IdentityProvider;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.CountryCode;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.gums.idp.bean.StateCode;
import gov.nih.nci.cagrid.gums.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.gums.test.TestUtils;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.bouncycastle.asn1.x509.X509Name;
import org.opensaml.InvalidCryptoException;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLStatement;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGUMS extends TestCase{
	public static String RESOURCES_DIR = "resources" + File.separator
	+ "general-test";
	
	private int count = 0;
	
	private CertificateAuthority ca;
	
    public void testGUMSManager(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    	
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testGetResource(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		assertNotNull(jm.getResource(IdPConfiguration.RESOURCE));
    		assertNotNull(jm.getResource(IFSConfiguration.RESOURCE));
    		assertNotNull(jm.getResource(GUMSCertificateAuthorityConf.RESOURCE));
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
 
    public void testMultipleIdPUsers(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//add in code to test findIdPUsers
 
    		BasicAuthCredential cred = getAdminCreds();
    		for (int i = 0; i < 10; i++) {
				Application a = createApplication();
				jm.registerWithIdP(a);
				
				IdPUserFilter uf = new IdPUserFilter();
				uf.setUserId(a.getUserId());
				IdPUser[] users = jm.findIdPUsers(cred.getUserId(), uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Pending, users[0].getStatus());
				assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
				users[0].setStatus(IdPUserStatus.Active);
				jm.updateIdPUser(cred.getUserId(), users[0]);
				users = jm.findIdPUsers(cred.getUserId(), uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Active, users[0].getStatus());
				uf.setUserId("user");
				users = jm.findIdPUsers(cred.getUserId(), uf);
				assertEquals(i + 1, users.length);
				BasicAuthCredential auth = new BasicAuthCredential();
				auth.setUserId(a.getUserId());
				auth.setPassword(a.getPassword());
				org.opensaml.SAMLAssertion saml = jm.authenticate(auth);
				assertNotNull(saml);
			//	this.verifySAMLAssertion(saml,idp,a);
    		}
    		
    		IdPUserFilter uf = new IdPUserFilter();
			IdPUser[] users = jm.findIdPUsers(cred.getUserId(), uf);
			assertEquals(11, users.length);
			for (int i = 0; i < 10; i++) {
				IdPUserFilter f = new IdPUserFilter();
				f.setUserId(users[i].getUserId());
				IdPUser[] us = jm.findIdPUsers(cred.getUserId(), f);
				assertEquals(1, us.length);
				us[0].setFirstName("NEW NAME");
				jm.updateIdPUser(cred.getUserId(), us[0]);
				IdPUser[] us2 = jm.findIdPUsers(cred.getUserId(), f);
				assertEquals(1, us2.length);
				assertEquals(us[0], us2[0]);
				jm.removeIdPUser(cred.getUserId(), users[i].getUserId());
				us = jm.findIdPUsers(cred.getUserId(), f);
				assertEquals(0, us.length);
			}
			users = jm.findIdPUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
      
   
   protected void setUp() throws Exception {
		super.setUp();
		count = 0;
	}
    
    protected void tearDown() throws Exception {
		super.setUp();
	}

    
    private BasicAuthCredential getAdminCreds() {
		BasicAuthCredential cred = new BasicAuthCredential();
		cred.setUserId(IdentityProvider.ADMIN_USER_ID);
		cred.setPassword(IdentityProvider.ADMIN_PASSWORD);
		return cred;
	}
    
    private Application createApplication() {
		Application u = new Application();
		u.setUserId(count + "user");
		u.setEmail(count + "user@mail.com");
		u.setPassword(count + "password");
		u.setFirstName(count + "first");
		u.setLastName(count + "last");
		u.setAddress(count + "address");
		u.setAddress2(count + "address2");
		u.setCity("Columbus");
		u.setState(StateCode.OH);
		u.setCountry(CountryCode.US);
		u.setZipcode("43210");
		u.setPhoneNumber("614-555-5555");
		u.setOrganization(count + "organization");
		count = count + 1;
		return u;
	}
    
}
