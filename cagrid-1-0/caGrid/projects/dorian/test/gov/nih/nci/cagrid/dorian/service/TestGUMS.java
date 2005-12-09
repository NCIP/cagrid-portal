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
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.StateCode;
import gov.nih.nci.cagrid.gums.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.gums.ifs.UserManager;
import gov.nih.nci.cagrid.gums.ifs.IFS;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidPasswordFault;
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
    		
    		//get the gridId
			String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,GUMS.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			
    		for (int i = 0; i < 10; i++) {
				Application a = createApplication();
				jm.registerWithIdP(a);
				
				IdPUserFilter uf = new IdPUserFilter();
				uf.setUserId(a.getUserId());
		
				IdPUser[] users = jm.findIdPUsers(gridId, uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Pending, users[0].getStatus());
				assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
				users[0].setStatus(IdPUserStatus.Active);
				jm.updateIdPUser(gridId, users[0]);
				users = jm.findIdPUsers(gridId, uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Active, users[0].getStatus());
				uf.setUserId("user");
				users = jm.findIdPUsers(gridId, uf);
				assertEquals(i + 1, users.length);
				BasicAuthCredential auth = new BasicAuthCredential();
				auth.setUserId(a.getUserId());
				auth.setPassword(a.getPassword());
				org.opensaml.SAMLAssertion saml = jm.authenticate(auth);
				assertNotNull(saml);
				this.verifySAMLAssertion(saml,jm.getIdPCertificate(),a);
    		}
    		
    		IdPUserFilter uf = new IdPUserFilter();
			IdPUser[] users = jm.findIdPUsers(gridId, uf);
			assertEquals(11, users.length);
			for (int i = 0; i < 10; i++) {
				IdPUserFilter f = new IdPUserFilter();
				f.setUserId(users[i].getUserId());
				IdPUser[] us = jm.findIdPUsers(gridId, f);
				assertEquals(1, us.length);
				us[0].setFirstName("NEW NAME");
				jm.updateIdPUser(gridId, us[0]);
				IdPUser[] us2 = jm.findIdPUsers(gridId, f);
				assertEquals(1, us2.length);
				assertEquals(us[0], us2[0]);
				jm.removeIdPUser(gridId, users[i].getUserId());
				us = jm.findIdPUsers(gridId, f);
				assertEquals(0, us.length);
			}
			users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidIdpUser(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,GUMS.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			IdPUserFilter uf = new IdPUserFilter();
			IdPUser[] users;
			
    		//test the password length too long
    		try {
    			Application a = createTooLongPasswordApplication();
        		uf.setUserId(a.getUserId());
    			jm.registerWithIdP(a);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		users = jm.findIdPUsers(gridId, uf);
			assertEquals(0, users.length);
			
    		//test the password length too short
    		try {
    			Application a = createTooShortPasswordApplication();
    			uf.setUserId(a.getUserId());
    			jm.registerWithIdP(a);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		users = jm.findIdPUsers(gridId, uf);
			assertEquals(0, users.length);
    		
    		//test the userId length too long
    		try {
    			Application a = createTooLongUserIdApplication();
    			uf.setUserId(a.getUserId());
    			jm.registerWithIdP(a);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		users = jm.findIdPUsers(gridId, uf);
			assertEquals(0, users.length);
    		
    		//test the userId length too short
    		try {
    			Application a = createTooShortUserIdApplication();
    			uf.setUserId(a.getUserId());
    			jm.registerWithIdP(a);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		users = jm.findIdPUsers(gridId, uf);
			assertEquals(0, users.length);
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void verifySAMLAssertion(SAMLAssertion saml, X509Certificate idpCert,
			Application app) throws Exception {
		assertNotNull(saml);
		saml.verify(idpCert, false);

		assertEquals(idpCert.getSubjectDN().toString(), saml
				.getIssuer());
		Iterator itr = saml.getStatements();
		int count = 0;
		boolean emailFound = false;
		boolean authFound = false;
		while (itr.hasNext()) {
			count = count + 1;
			SAMLStatement stmt = (SAMLStatement) itr.next();
			if (stmt instanceof SAMLAuthenticationStatement) {
				if (authFound) {
					assertTrue(false);
				} else {
					authFound = true;
				}
				SAMLAuthenticationStatement auth = (SAMLAuthenticationStatement) stmt;
				assertEquals(app.getUserId(), auth.getSubject().getName());
				assertEquals("urn:oasis:names:tc:SAML:1.0:am:password", auth
						.getAuthMethod());
			}

			if (stmt instanceof SAMLAttributeStatement) {
				if (emailFound) {
					assertTrue(false);
				} else {
					emailFound = true;
				}
				SAMLAttributeStatement att = (SAMLAttributeStatement) stmt;
				assertEquals(app.getUserId(), att.getSubject().getName());
				Iterator i = att.getAttributes();
				assertTrue(i.hasNext());
				SAMLAttribute a = (SAMLAttribute) i.next();
				assertEquals(AssertionCredentialsManager.EMAIL_NAMESPACE, a
						.getNamespace());
				assertEquals(AssertionCredentialsManager.EMAIL_NAME, a
						.getName());
				Iterator vals = a.getValues();
				assertTrue(vals.hasNext());
				String val = (String) vals.next();
				assertEquals(app.getEmail(), val);
				assertTrue(!vals.hasNext());
				assertTrue(!i.hasNext());
			}

		}

		assertEquals(2, count);
		assertTrue(authFound);
		assertTrue(emailFound);
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
    
    private Application createTooLongPasswordApplication() {
		Application u = new Application();
		u.setUserId(count + "user");
		u.setEmail(count + "user@mail.com");
		u.setPassword(count + "thispasswordiswaytoolong");
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
    
    private Application createTooShortPasswordApplication() {
		Application u = new Application();
		u.setUserId(count + "user");
		u.setEmail(count + "user@mail.com");
		u.setPassword(count + "p");
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
    
    private Application createTooLongUserIdApplication() {
		Application u = new Application();
		u.setUserId(count + "thisuseridiswaytoolong");
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
    
    private Application createTooShortUserIdApplication() {
		Application u = new Application();
		u.setUserId(count + "u");
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
   protected void setUp() throws Exception {
		super.setUp();
		try {
			count = 0;
			ca = TestUtils.getCA();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    protected void tearDown() throws Exception {
		super.setUp();
	}
    
}
