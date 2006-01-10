package gov.nih.nci.cagrid.dorian.service;


import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.ca.CertUtil;
import gov.nih.nci.cagrid.dorian.common.ca.KeyUtil;
import gov.nih.nci.cagrid.dorian.idp.AssertionCredentialsManager;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.IFSUtils;
import gov.nih.nci.cagrid.dorian.ifs.ManualApprovalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.UserManager;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLStatement;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import junit.framework.TestCase;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class TestDorian extends TestCase{
	public static String RESOURCES_DIR = "resources" + File.separator
	+ "general-test";
	
	private Dorian jm;
	
	private int count = 0;
	
	private CertificateAuthority ca;
	
 
 
    /** *************** IdP TEST FUNCTIONS ********************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */

    public void testAuthenticate(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
			String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
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
			assertEquals(1, users.length);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(a.getUserId());
			auth.setPassword(a.getPassword());
			SAMLAssertion saml = jm.authenticate(auth);
			assertNotNull(saml);
			this.verifySAMLAssertion(saml,jm.getIdPCertificate(),a);
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
   
    
    public void testUpdateIdPUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application a = createApplication();
			jm.registerWithIdP(a);
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
    		IdPUser[] us = jm.findIdPUsers(gridId, uf);
    		assertEquals(1, us.length);
    		us[0].setAddress("New_Address");
    		us[0].setAddress2("New_Address2");
    		us[0].setCity("New_City");
    		us[0].setCountry(CountryCode.AD);
    		us[0].setEmail("NewUser@mail.com");
    		us[0].setFirstName("New_First_Name");
    		us[0].setLastName("New_Last_Name");
    		us[0].setOrganization("New_Organization");
    		us[0].setPassword("PASSWORD");
    		us[0].setPhoneNumber("012-345-6789");
    		us[0].setRole(IdPUserRole.Non_Administrator);
    		us[0].setState(StateCode.AK);
    		us[0].setStatus(IdPUserStatus.Active);
    		us[0].setZipcode("11111");
    		jm.updateIdPUser(gridId, us[0]);
    		us = jm.findIdPUsers(gridId, uf);
    		assertEquals(1, us.length);
    		assertEquals("New_Address", us[0].getAddress());
    		assertEquals("New_Address2", us[0].getAddress2());
    		assertEquals("New_City", us[0].getCity());
    		assertEquals(CountryCode.AD, us[0].getCountry());
    		assertEquals("NewUser@mail.com", us[0].getEmail());
    		assertEquals("New_First_Name", us[0].getFirstName());
    		assertEquals("New_Last_Name", us[0].getLastName());
    		assertEquals("New_Organization", us[0].getOrganization());
    		assertEquals("012-345-6789", us[0].getPhoneNumber());
    		assertEquals(IdPUserRole.Non_Administrator, us[0].getRole());
    		assertEquals(StateCode.AK, us[0].getState());
    		assertEquals(IdPUserStatus.Active, us[0].getStatus());
    		assertEquals("11111", us[0].getZipcode());
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testRemoveIdPUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application a = createApplication();
			jm.registerWithIdP(a);
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
    		IdPUser[] us = jm.findIdPUsers(gridId, uf);
    		assertEquals(1, us.length);
			
    		//remove the user
    		jm.removeIdPUser(gridId, us[0].getUserId());
    		us = jm.findIdPUsers(gridId, uf);
    		assertEquals(0, us.length);
    	}catch (PermissionDeniedFault pdf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    /** *************** IdP BAD TEST FUNCTIONS ****************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
    
    public void testBadAuthenticateStatusPendingUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application a = createApplication();
			jm.registerWithIdP(a);	
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(a.getUserId());
			auth.setPassword(a.getPassword());
			SAMLAssertion saml = jm.authenticate(auth);
			assertTrue(false);
    	}catch (PermissionDeniedFault pdf) {	
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testBadAuthenticateStatusRejectedUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application a = createApplication();
			jm.registerWithIdP(a);	
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Rejected);
			jm.updateIdPUser(gridId, users[0]);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(a.getUserId());
			auth.setPassword(a.getPassword());
			SAMLAssertion saml = jm.authenticate(auth);
			assertTrue(false);
    	}catch (PermissionDeniedFault pdf) {	
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testBadAuthenticateStatusSuspendedUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application a = createApplication();
			jm.registerWithIdP(a);	
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Suspended);
			jm.updateIdPUser(gridId, users[0]);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(a.getUserId());
			auth.setPassword(a.getPassword());
			SAMLAssertion saml = jm.authenticate(auth);
			assertTrue(false);
    	}catch (PermissionDeniedFault pdf) {	
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testBadFindIdPUsersInvalidGridId(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadFindIdPUsersUserNotAdmin(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadRegisterWithIdPTwoIdenticalUsers(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
			Application a = createApplication();
			Application b = a;
			jm.registerWithIdP(a);
			
			jm.registerWithIdP(b);
			assertTrue(false);
    	}catch (InvalidUserPropertyFault iupf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testBadRegisterWithIdPPasswordTooLong(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadRegisterWithIdPPasswordTooShort(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadRegisterWithIdPUserIdTooLong(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadRegisterWithIdpUserIdTooShort(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadUpdateIdPUserNoSuchUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
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
    
    public void testBadRemoveIdPUserInvalidGridId(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
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
    
    public void testBadRemoveIdPUserNoSuchUser(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application a = createApplication();
			jm.registerWithIdP(a);
			IdPUserFilter uf = new IdPUserFilter();
    		IdPUser[] us = jm.findIdPUsers(gridId, uf);
    		assertEquals(2, us.length);
			
    		//create a userId that does not exist
    		String userId = "No_SUCH_USER";
    		jm.removeIdPUser(gridId, userId);
    		IdPUserFilter f = new IdPUserFilter();
    		IdPUser[] users = jm.findIdPUsers(gridId, f);
    		assertEquals(2, users.length);
    	}catch (PermissionDeniedFault pdf) {
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    /** *************** IFS TEST FUNCTIONS ********************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
    
    public void testCreateProxy() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testRemoveIFSUser() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			
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
			BasicAuthCredential auth2 = new BasicAuthCredential();
			auth2.setUserId(a.getUserId());
			auth2.setPassword(a.getPassword());
			SAMLAssertion saml2 = jm.authenticate(auth2);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetime();
			X509Certificate[] certs2 = jm.createProxy(saml2, publicKey2, lifetime2);
			users = jm.findIdPUsers(gridId, uf);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setUID(users[0].getUserId());
			IFSUser[] ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(1, ifsu.length);
			jm.removeIFSUser(gridId, ifsu[0]);
			ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(0, ifsu.length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testUpdateIFSUser() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			
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
			BasicAuthCredential auth2 = new BasicAuthCredential();
			auth2.setUserId(a.getUserId());
			auth2.setPassword(a.getPassword());
			SAMLAssertion saml2 = jm.authenticate(auth2);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetime();
			X509Certificate[] certs2 = jm.createProxy(saml2, publicKey2, lifetime2);
			users = jm.findIdPUsers(gridId, uf);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setUID(users[0].getUserId());
			IFSUser[] ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(1, ifsu.length);
			ifsu[0].setUserRole(IFSUserRole.Administrator);
			ifsu[0].setUserStatus(IFSUserStatus.Suspended);
			jm.updateIFSUser(gridId, ifsu[0]);
			ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(1, ifsu.length);
			assertEquals(IFSUserRole.Administrator, ifsu[0].getUserRole());
			assertEquals(IFSUserStatus.Suspended, ifsu[0].getUserStatus());                         
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testFindIFSUsers() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			
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
			BasicAuthCredential auth2 = new BasicAuthCredential();
			auth2.setUserId(a.getUserId());
			auth2.setPassword(a.getPassword());
			SAMLAssertion saml2 = jm.authenticate(auth2);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetime();
			X509Certificate[] certs2 = jm.createProxy(saml2, publicKey2, lifetime2);
			users = jm.findIdPUsers(gridId, uf);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setUID(users[0].getUserId());
			IFSUser[] ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(1, ifsu.length);                       
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testGetTrustedIdPs(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			int times = 5;
			String baseName = "Test IdP";
			int count = 1;
			for (int i = 0; i < times; i++) {
				assertEquals(count, jm.getTrustedIdPs(gridId).length);
				count = count + 1;
				String name = baseName + " " + count;
				TrustedIdP idp = new TrustedIdP();
				idp.setName(name);
				String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
				idp.setUserPolicyClass(policyClass);
				idp.setStatus(TrustedIdPStatus.Active);

				SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
				methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
				idp.setAuthenticationMethod(methods);

				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
				PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
				assertNotNull(req);
				GregorianCalendar cal = new GregorianCalendar();
				Date start = cal.getTime();
				cal.add(Calendar.MONTH, 10);
				Date end = cal.getTime();
				X509Certificate cert = ca.requestCertificate(req, start, end);
				assertNotNull(cert);
				assertEquals(cert.getSubjectDN().getName(), subject);
				idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
				idp = jm.addTrustedIdP(gridId, idp);
				assertEquals(count, jm.getTrustedIdPs(gridId).length);
			}
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    public void testAddRemoveTrustedIdP(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			assertEquals(1, jm.getTrustedIdPs(gridId).length);
			String name = "Test IdP";
			TrustedIdP idp = new TrustedIdP();
			idp.setName(name);
			String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Active);
			
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			assertNotNull(req);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MONTH, 10);
			Date end = cal.getTime();
			X509Certificate cert = ca.requestCertificate(req, start, end);
			assertNotNull(cert);
			assertEquals(cert.getSubjectDN().getName(), subject);
			idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
			idp = jm.addTrustedIdP(gridId, idp);
			assertEquals(2, jm.getTrustedIdPs(gridId).length);
			
			jm.removeTrustedIdP(gridId, idp);
			assertEquals(1, jm.getTrustedIdPs(gridId).length);
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    public void testUpdateTrustedIdP(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			//add a trusted IdP
			assertEquals(1, jm.getTrustedIdPs(gridId).length);
			String name = "Test IdP";
			TrustedIdP idp = new TrustedIdP();
			idp.setName(name);
			String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Active);
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			assertNotNull(req);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MONTH, 10);
			Date end = cal.getTime();
			X509Certificate cert = ca.requestCertificate(req, start, end);
			assertNotNull(cert);
			assertEquals(cert.getSubjectDN().getName(), subject);
			idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
			idp = jm.addTrustedIdP(gridId, idp);
			assertEquals(2, jm.getTrustedIdPs(gridId).length);
			//update the trusted IdP
			idp.setName("Updated");
			policyClass = ManualApprovalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Suspended);
			jm.updatedTrustedIdP(gridId, idp);
			TrustedIdP[] idps = jm.getTrustedIdPs(gridId);
			assertEquals(idp.getName(), idps[1].getName());
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    public void testRenewIFSUserCredentials() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String adminGridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String adminGridId = UserManager.subjectToIdentity(adminGridSubject);
			
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			IFSUserFilter f1 = new IFSUserFilter();
			TrustedIdP[] idps = jm.getTrustedIdPs(adminGridId);
			f1.setIdPId(idps[0].getId());
			f1.setUID(Dorian.IDP_ADMIN_USER_ID);
			IFSUser[] users = jm.findIFSUsers(adminGridId, f1);
			assertEquals(1, users.length);
			IFSUser usr1 = users[0];
			String certStr = usr1.getCertificate().getCertificateAsString();
			X509Certificate cert1 = CertUtil.loadCertificateFromString(certStr);
			Thread.sleep(2000);
			IFSUser usr2 = jm.renewIFSUserCredentials(adminGridId, usr1);
			assertEquals(usr1.getGridId(), usr2.getGridId());

			if (certStr.equals(usr2.getCertificate().getCertificateAsString())) {
				assertTrue(false);
			}

			X509Certificate cert2 = CertUtil.loadCertificateFromString(usr2.getCertificate().getCertificateAsString());

			assertTrue(cert2.getNotBefore().after(cert1.getNotBefore()));

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    /** *************** IFS BAD TEST FUNCTIONS ****************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
 
    public void testBadRemoveIFSUserInvalidGridId() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			
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
			BasicAuthCredential auth2 = new BasicAuthCredential();
			auth2.setUserId(a.getUserId());
			auth2.setPassword(a.getPassword());
			SAMLAssertion saml2 = jm.authenticate(auth2);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetime();
			X509Certificate[] certs2 = jm.createProxy(saml2, publicKey2, lifetime2);
			users = jm.findIdPUsers(gridId, uf);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setUID(users[0].getUserId());
			IFSUser[] ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(1, ifsu.length);
			gridId = "Invalid";
			jm.removeIFSUser(gridId, ifsu[0]);
			assertTrue(false);
		}catch (InvalidUserFault iuf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testBadUpdateIFSUserInvalidGridId() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			
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
			BasicAuthCredential auth2 = new BasicAuthCredential();
			auth2.setUserId(a.getUserId());
			auth2.setPassword(a.getPassword());
			SAMLAssertion saml2 = jm.authenticate(auth2);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetime();
			X509Certificate[] certs2 = jm.createProxy(saml2, publicKey2, lifetime2);
			users = jm.findIdPUsers(gridId, uf);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setUID(users[0].getUserId());
			IFSUser[] ifsu = jm.findIFSUsers(gridId, f1);
			assertEquals(1, ifsu.length);
			ifsu[0].setUserRole(IFSUserRole.Administrator);
			ifsu[0].setUserStatus(IFSUserStatus.Suspended);
			gridId = "Invalid";
			jm.updateIFSUser(gridId, ifsu[0]); 
			assertTrue(false);
		}catch (InvalidUserFault iuf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testBadFindIFSUsersInvalidGridId() {
		try {
			jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = jm.createProxy(saml, publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			
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
			BasicAuthCredential auth2 = new BasicAuthCredential();
			auth2.setUserId(a.getUserId());
			auth2.setPassword(a.getPassword());
			SAMLAssertion saml2 = jm.authenticate(auth2);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetime();
			X509Certificate[] certs2 = jm.createProxy(saml2, publicKey2, lifetime2);
			users = jm.findIdPUsers(gridId, uf);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setUID(users[0].getUserId());
			gridId = "Invalid";
			IFSUser[] ifsu = jm.findIFSUsers(gridId, f1);
			assertTrue(false);
		}catch (InvalidUserFault iuf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    public void testBadGetTrustedIdPs(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			String baseName = "Test IdP";
			int count = 1;
			assertEquals(count, jm.getTrustedIdPs(gridId).length);
			count = count + 1;
			String name = baseName + " " + count;
			TrustedIdP idp = new TrustedIdP();
			idp.setName(name);
			String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Active);
			
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
		
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			assertNotNull(req);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MONTH, 10);
			Date end = cal.getTime();
			X509Certificate cert = ca.requestCertificate(req, start, end);
			assertNotNull(cert);
			assertEquals(cert.getSubjectDN().getName(), subject);
			idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
			idp = jm.addTrustedIdP(gridId, idp);
			gridId = "Invalid";
			TrustedIdP[] tidps = jm.getTrustedIdPs(gridId);
			assertTrue(false);
    	}catch (InvalidUserFault iuf) {
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    public void testBadAddTrustedIdPInvalidGridId(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			assertEquals(1, jm.getTrustedIdPs(gridId).length);
			String name = "Test IdP";
			TrustedIdP idp = new TrustedIdP();
			idp.setName(name);
			String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Active);
			
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			assertNotNull(req);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MONTH, 10);
			Date end = cal.getTime();
			X509Certificate cert = ca.requestCertificate(req, start, end);
			assertNotNull(cert);
			assertEquals(cert.getSubjectDN().getName(), subject);
			idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
			gridId = "Invalid";
			idp = jm.addTrustedIdP(gridId, idp);
			assertTrue(false);
			
    	}catch (InvalidUserFault iuf) {
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    public void testBadRemoveTrustedIdP(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			assertEquals(1, jm.getTrustedIdPs(gridId).length);
			String name = "Test IdP";
			TrustedIdP idp = new TrustedIdP();
			idp.setName(name);
			String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Active);
			
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			assertNotNull(req);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MONTH, 10);
			Date end = cal.getTime();
			X509Certificate cert = ca.requestCertificate(req, start, end);
			assertNotNull(cert);
			assertEquals(cert.getSubjectDN().getName(), subject);
			idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
			idp = jm.addTrustedIdP(gridId, idp);
			assertEquals(2, jm.getTrustedIdPs(gridId).length);
			gridId = "Invalid";
			jm.removeTrustedIdP(gridId, idp);
			assertTrue(false);
    	}catch (InvalidUserFault iuf) {
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    public void testBadUpdateTrustedIdPInvalidGridId(){
    	try{
    		jm = new Dorian(RESOURCES_DIR+File.separator+"dorian-conf.xml","localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			//add a trusted IdP
			assertEquals(1, jm.getTrustedIdPs(gridId).length);
			String name = "Test IdP";
			TrustedIdP idp = new TrustedIdP();
			idp.setName(name);
			String policyClass = AutoApprovalAutoRenewalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Active);
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			assertNotNull(req);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MONTH, 10);
			Date end = cal.getTime();
			X509Certificate cert = ca.requestCertificate(req, start, end);
			assertNotNull(cert);
			assertEquals(cert.getSubjectDN().getName(), subject);
			idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
			idp = jm.addTrustedIdP(gridId, idp);
			assertEquals(2, jm.getTrustedIdPs(gridId).length);
			//update the trusted IdP
			idp.setName("Updated");
			policyClass = ManualApprovalPolicy.class.getName();
			idp.setUserPolicyClass(policyClass);
			idp.setStatus(TrustedIdPStatus.Suspended);
			gridId = "Invalid";
			jm.updatedTrustedIdP(gridId, idp);
			assertTrue(false);
    	}catch (InvalidUserFault iuf) {
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}
    }
    
    /** *************** HELPER FUNCTIONS ************************ */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
    
    public void verifySAMLAssertion(SAMLAssertion saml, X509Certificate idpCert,
			Application app) throws Exception {
		assertNotNull(saml);
		saml.verify(idpCert);

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
				assertEquals(app.getUserId(), auth.getSubject().getNameIdentifier().getName());
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
				assertEquals(app.getUserId(), att.getSubject().getNameIdentifier().getName());
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
			ca = Utils.getCA();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
    protected void tearDown() throws Exception {
		super.setUp();
		try {
			if(jm != null)
			{
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
    
}