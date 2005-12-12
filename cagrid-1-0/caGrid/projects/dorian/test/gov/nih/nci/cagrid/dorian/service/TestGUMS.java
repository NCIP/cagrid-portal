package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.idp.AssertionCredentialsManager;
import gov.nih.nci.cagrid.gums.idp.IdPConfiguration;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.CountryCode;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.StateCode;
import gov.nih.nci.cagrid.gums.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.gums.ifs.AutoApprovalPolicy;
import gov.nih.nci.cagrid.gums.ifs.IFS;
import gov.nih.nci.cagrid.gums.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.gums.ifs.IFSUtils;
import gov.nih.nci.cagrid.gums.ifs.ManualApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.gums.ifs.UserManager;
import gov.nih.nci.cagrid.gums.ifs.TestIFS.IdPContainer;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gums.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.xml.security.signature.XMLSignature;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.globus.gsi.GlobusCredential;
import org.opensaml.QName;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLStatement;
import org.opensaml.SAMLSubject;

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
	
	private static final int SHORT_PROXY_VALID = 2;
	
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
 
    /** *************** IdP TEST FUNCTIONS ********************** */
   
    public void testRegisterWithIdP(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//get the gridId
			String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,GUMS.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			//create an application and register
			Application a = createApplication();
			jm.registerWithIdP(a);	
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			//try to authenticate the IdPUser that has a pending status
			try{
				BasicAuthCredential auth = new BasicAuthCredential();
				auth.setUserId(a.getUserId());
				auth.setPassword(a.getPassword());
				org.opensaml.SAMLAssertion saml = jm.authenticate(auth);
				assertTrue(false);
			}catch (PermissionDeniedFault pdf){
			}
			
			users[0].setStatus(IdPUserStatus.Active);
			jm.updateIdPUser(gridId, users[0]);
			users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			uf.setUserId("user");
			users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(a.getUserId());
			auth.setPassword(a.getPassword());
			org.opensaml.SAMLAssertion saml = jm.authenticate(auth);
			assertNotNull(saml);
			this.verifySAMLAssertion(saml,jm.getIdPCertificate(),a);
    		
			IdPUserFilter uf2 = new IdPUserFilter();
			users = jm.findIdPUsers(gridId, uf2);
			assertEquals(2, users.length);
			
			IdPUserFilter f = new IdPUserFilter();
			f.setUserId(users[0].getUserId());
			IdPUser[] us = jm.findIdPUsers(gridId, f);
			assertEquals(1, us.length);
			us[0].setFirstName("NEW NAME");
			jm.updateIdPUser(gridId, us[0]);
			IdPUser[] us2 = jm.findIdPUsers(gridId, f);
			assertEquals(1, us2.length);
			assertEquals(us[0], us2[0]);
			jm.removeIdPUser(gridId, users[0].getUserId());
			us = jm.findIdPUsers(gridId, f);
			assertEquals(0, us.length);
			
			users = jm.findIdPUsers(gridId, uf2);
			assertEquals(1, users.length);
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidIdpUserPasswordTooLong(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
			
    		//test the password length too long
			Application a = createTooLongPasswordApplication();
			jm.registerWithIdP(a);	
			assertTrue(false);
    	}catch (InvalidUserPropertyFault iupf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidIdpUserPasswordTooShort(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
			
    		//test the password length too short
			Application a = createTooShortPasswordApplication();
			jm.registerWithIdP(a);	
			assertTrue(false);
    	}catch (InvalidUserPropertyFault iupf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    public void testInvalidIdpUserIdTooLong(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
			
    		//test the UserId too long
			Application a = createTooLongUserIdApplication();
			jm.registerWithIdP(a);	
			assertTrue(false);
    	}catch (InvalidUserPropertyFault iupf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidIdpUserIdTooShort(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
			
    		//test the UserId too short
			Application a = createTooShortUserIdApplication();
			jm.registerWithIdP(a);	
			assertTrue(false);
    	}catch (InvalidUserPropertyFault iupf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidIdpNoSuchUser(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
			
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,GUMS.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
    		//test for no such user
    		IdPUser u = new IdPUser();
			u.setUserId("No_SUCH_USER");
			jm.updateIdPUser(gridId, u);
			assertTrue(false);
    	}catch (NoSuchUserFault nsuf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidGridIdFindIdPUsers(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
			//create a valid IdP User
			Application a = createApplication();
			jm.registerWithIdP(a);
			IdPUserFilter f = new IdPUserFilter();
			f.setUserId(a.getUserId());
			
    		//create an invalid Grid Id and try to findIdPUsers()
    		String invalidGridId = "ThisIsInvalid";
    		IdPUser[] us = jm.findIdPUsers(invalidGridId, f);
			assertTrue(false);
    	}catch (PermissionDeniedFault pdf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testInvalidGridIdRemoveIdPUser(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
			//create a valid IdP User
			Application a = createApplication();
			jm.registerWithIdP(a);
			String userId = a.getUserId();
			
    		//create an invalid Grid Id and try to findIdPUsers()
    		String invalidGridId = "ThisIsInvalid";
    		jm.removeIdPUser(invalidGridId, userId);
			assertTrue(false);
    	}catch (PermissionDeniedFault pdf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    /** *************** IFS TEST FUNCTIONS ********************** */
    
    public void testCreateProxy() {
		try {
			GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(GUMS.IDP_ADMIN_USER_ID);
			auth.setPassword(GUMS.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    		
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}
 
    /** *************** HELPER FUNCTIONS ********************** */
    
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
    
    private String identityToSubject(String identity) {
		String s = identity.substring(1);
		return s.replace('/', ',');
	}

	private ProxyLifetime getProxyLifetime() {
		ProxyLifetime valid = new ProxyLifetime();
		valid.setHours(12);
		valid.setMinutes(0);
		valid.setSeconds(0);
		return valid;
	}

	private void createAndCheckProxyLifetime(ProxyLifetime lifetime,
			PrivateKey key, X509Certificate[] certs) throws Exception {
		assertNotNull(certs);
		assertEquals(2, certs.length);
		GlobusCredential cred = new GlobusCredential(key, certs);
		assertNotNull(cred);
		long max = IFSUtils.getTimeInSeconds(lifetime);
		long min = max - 3;
		long timeLeft = cred.getTimeLeft();
		if ((min > timeLeft) || (timeLeft > max)) {
			assertTrue(false);
		}
		assertEquals(certs[1].getSubjectDN().toString(), identityToSubject(cred
				.getIdentity()));
		assertEquals(cred.getIssuer(), identityToSubject(cred.getIdentity()));
		cred.verify();
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
