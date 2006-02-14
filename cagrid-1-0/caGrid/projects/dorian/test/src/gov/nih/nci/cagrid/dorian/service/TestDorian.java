package gov.nih.nci.cagrid.dorian.service;


import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
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
import gov.nih.nci.cagrid.dorian.ifs.AutoApprovalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.IFSUtils;
import gov.nih.nci.cagrid.dorian.ifs.ManualApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.ManualApprovalPolicy;
import gov.nih.nci.cagrid.dorian.ifs.UserManager;
import gov.nih.nci.cagrid.dorian.ifs.TestIFS.IdPContainer;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.dorian.test.Constants;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLStatement;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

import java.io.File;
import java.io.InputStream;
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

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.xml.security.signature.XMLSignature;
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
	
	private Dorian jm;
	
	private int count = 0;
	
	private CertificateAuthority ca;
	
	private static final int SHORT_PROXY_VALID = 2;
	
	private static final int SHORT_CREDENTIALS_VALID = 10;
	
	public final static String EMAIL_NAMESPACE = "http://cagrid.nci.nih.gov/email";

	public final static String EMAIL_NAME = "email";
	
	private InputStream resource = TestCase.class.getResourceAsStream(Constants.DORIAN_CONF);
	
	private InputStream expiringCredentialsResource = TestCase.class.getResourceAsStream(Constants.DORIAN_CONF_EXPIRING_CREDENTIALS);
	
 
 
    /** *************** IdP TEST FUNCTIONS ********************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */

    public void testAuthenticate(){
    	try{
    		//initialize a Dorian object
    		jm = new Dorian(resource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
			String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			//test authentication with an active user
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
			
			//test authentication with a status pending user
			Application b = createApplication();
			jm.registerWithIdP(b);	
			uf = new IdPUserFilter();
			uf.setUserId(b.getUserId());
			users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			auth = new BasicAuthCredential();
			auth.setUserId(b.getUserId());
			auth.setPassword(b.getPassword());
			try{
				saml = jm.authenticate(auth);
				assertTrue(false);
			}catch(PermissionDeniedFault pdf){	
			}
			
			//test authentication with a status rejected user
			Application c = createApplication();
			jm.registerWithIdP(c);	
			uf = new IdPUserFilter();
			uf.setUserId(c.getUserId());
			users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Rejected);
			jm.updateIdPUser(gridId, users[0]);
			auth = new BasicAuthCredential();
			auth.setUserId(c.getUserId());
			auth.setPassword(c.getPassword());
			try{
				saml = jm.authenticate(auth);
				assertTrue(false);
			}catch (PermissionDeniedFault pdf) {	
			}
    	
			//test authentication with a status suspended user
			Application d = createApplication();
			jm.registerWithIdP(d);	
			uf = new IdPUserFilter();
			uf.setUserId(d.getUserId());
			users = jm.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Suspended);
			jm.updateIdPUser(gridId, users[0]);
			auth = new BasicAuthCredential();
			auth.setUserId(d.getUserId());
			auth.setPassword(d.getPassword());
			try{
				saml = jm.authenticate(auth);
				assertTrue(false);
			}catch (PermissionDeniedFault pdf) {	
			}
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
		}				
   
    public void testFindUpdateRemoveIdPUser(){
    	try{
    		jm = new Dorian(resource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			Application a = createApplication();
			jm.registerWithIdP(a);
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			
			//test findIdPUsers with an invalid gridId
			try{
				String invalidGridId = "ThisIsInvalid";
	    		IdPUser[] users = jm.findIdPUsers(invalidGridId, uf);
				assertTrue(false);
			}catch (PermissionDeniedFault pdf) {	
			}
    		
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
    		
    		//try to update a user that does not exist
    		try{
    			IdPUser u = new IdPUser();
    			u.setUserId("No_SUCH_USER");
    			jm.updateIdPUser(gridId, u);
    			assertTrue(false);
    		}catch (NoSuchUserFault nsuf) {
    		}
    		
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
    		
    		//create an invalid Grid Id and try to removeIdPUser
    		try{
    			String invalidGridId = "ThisIsInvalid";
    			jm.removeIdPUser(invalidGridId, us[0].getUserId());
    			assertTrue(false);
    		}catch (PermissionDeniedFault pdf) {
    		}
    		
    		//remove the user
    		jm.removeIdPUser(gridId, us[0].getUserId());
    		us = jm.findIdPUsers(gridId, uf);
    		assertEquals(0, us.length);
    		
		}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    /** *************** IdP BAD TEST FUNCTIONS ****************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
    
    public void testBadRegisterWithIdPTwoIdenticalUsers(){
    	try{
    		jm = new Dorian(resource,"localhost");
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
		}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    public void testBadRegisterWithIdP(){
    	try{
    		jm = new Dorian(resource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		//test the password length too long
    		try{
    			Application a = createTooLongPasswordApplication();
    			jm.registerWithIdP(a);	
    			assertTrue(false);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		
    		//test the password length is too short
    		try{
    			Application b = createTooShortPasswordApplication();
    			jm.registerWithIdP(b);	
    			assertTrue(false);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		
    		//test the UserId length is too long
    		try{
    			Application c = createTooLongUserIdApplication();
    			jm.registerWithIdP(c);	
    			assertTrue(false);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    		
    		//test the UserId length is too short
    		try{
    			Application d = createTooShortUserIdApplication();
    			jm.registerWithIdP(d);	
    			assertTrue(false);
    		}catch (InvalidUserPropertyFault iupf) {
    		}
    	}
    	catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    public void testBadRemoveIdPUserNoSuchUser(){
    	try{
    		jm = new Dorian(resource,"localhost");
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
		}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    /** *************** IFS TEST FUNCTIONS ********************** */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
    
    public void testCreateProxy() {
		try {
			jm = new Dorian(resource,"localhost");
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
		}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}
    
    public void testAutoApprovalAutoRenewal() {	
    	try{
    		if(jm!=null){
    			jm.getDatabase().destroyDatabase();	
    		}
    		jm = new Dorian(expiringCredentialsResource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
    		IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
    		jm.addTrustedIdP(gridId, idp.getIdp());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
    		
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetimeShort();
			X509Certificate[] certs2 = jm.createProxy(saml, publicKey, lifetime2);
			createAndCheckProxyLifetime(lifetime2, pair2.getPrivate(), certs2);
					
			IFSUserFilter filter = new IFSUserFilter();
			IFSUser[] ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser before = ifsUser[1];
			assertEquals(before.getUserStatus(), IFSUserStatus.Active);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser after = ifsUser[1];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);
			if (before.getCertificate().equals(after.getCertificate())) {
				assertTrue(false);
			}
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    public void testManualApprovalAutoRenewal() {	
    	try{
    		if(jm!=null){
    			jm.getDatabase().destroyDatabase();	
    		}
    		jm = new Dorian(expiringCredentialsResource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetimeShort();
			X509Certificate[] certs2 = jm.createProxy(saml, publicKey, lifetime2);
			createAndCheckProxyLifetime(lifetime2, pair2.getPrivate(), certs2);
			
			IdPContainer idp = this.getTrustedIdpManualApproveAutoRenew("My IdP");
			jm.addTrustedIdP(gridId, idp.getIdp());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			try {
				jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), getProxyLifetimeShort());
				assertTrue(false);
			} catch (PermissionDeniedFault f) {

			}
			
			IFSUserFilter filter = new IFSUserFilter();
			IFSUser[] ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser before = ifsUser[1];
			assertEquals(before.getUserStatus(), IFSUserStatus.Pending);
			before.setUserStatus(IFSUserStatus.Active);
			jm.updateIFSUser(gridId, before);
			X509Certificate[] certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser after = ifsUser[1];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);
			if (before.getCertificate().equals(after.getCertificate())) {
				assertTrue(false);
			}
			
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    public void testAutoApprovalManualRenewal() {	
    	try{
    		if(jm!=null){
    			jm.getDatabase().destroyDatabase();	
    		}
    		jm = new Dorian(expiringCredentialsResource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
    		IdPContainer idp = this.getTrustedIdpAutoApprove("My IdP");
    		jm.addTrustedIdP(gridId, idp.getIdp());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
    		
			BasicAuthCredential auth = new BasicAuthCredential();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = jm.authenticate(auth);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair2.getPublic();
			ProxyLifetime lifetime2 = getProxyLifetimeShort();
			X509Certificate[] certs2 = jm.createProxy(saml, publicKey, lifetime2);
			createAndCheckProxyLifetime(lifetime2, pair2.getPrivate(), certs2);
					
			IFSUserFilter filter = new IFSUserFilter();
			IFSUser[] ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser before = ifsUser[1];
			assertEquals(before.getUserStatus(), IFSUserStatus.Active);
			X509Certificate certBefore = CertUtil.loadCertificateFromString(before.getCertificate().getCertificateAsString());

			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
	
			try {
				jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), getProxyLifetimeShort());
				assertTrue(false);
			} catch (PermissionDeniedFault f) {

			}
			jm.renewIFSUserCredentials(gridId, ifsUser[1]);
			
			certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser after = ifsUser[1];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);

			if (certBefore.equals(after.getCertificate())) {
				assertTrue(false);
			}
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    public void testManualApprovalManualRenewal() {	
    	try{
    		if(jm!=null){
    			jm.getDatabase().destroyDatabase();	
    		}
    		jm = new Dorian(expiringCredentialsResource,"localhost");
    		assertNotNull(jm.getConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		String gridSubject = UserManager.getUserSubject(jm.getCACertificate().getSubjectDN().getName(),1,Dorian.IDP_ADMIN_USER_ID);
    		String gridId = UserManager.subjectToIdentity(gridSubject);
    		
    		BasicAuthCredential auth = new BasicAuthCredential();
    		auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
    		auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
    		SAMLAssertion saml = jm.authenticate(auth);
    		KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
    		PublicKey publicKey = pair2.getPublic();
    		ProxyLifetime lifetime2 = getProxyLifetimeShort();
    		X509Certificate[] certs2 = jm.createProxy(saml, publicKey, lifetime2);
    		createAndCheckProxyLifetime(lifetime2, pair2.getPrivate(), certs2);
			
			IdPContainer idp = this.getTrustedIdpManualApprove("My IdP");
			jm.addTrustedIdP(gridId, idp.getIdp());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			try {
				jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), getProxyLifetimeShort());
				assertTrue(false);
			} catch (PermissionDeniedFault f) {

			}
			
			IFSUserFilter filter = new IFSUserFilter();
			IFSUser[] ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser before = ifsUser[1];
			assertEquals(before.getUserStatus(), IFSUserStatus.Pending);
			before.setUserStatus(IFSUserStatus.Active);
			jm.updateIFSUser(gridId, before);
			X509Certificate[] certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			X509Certificate certBefore = CertUtil.loadCertificateFromString(before.getCertificate().getCertificateAsString());
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
	
			try {
				jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), getProxyLifetimeShort());
				assertTrue(false);
			} catch (PermissionDeniedFault f) {

			}
			jm.renewIFSUserCredentials(gridId, ifsUser[1]);
			
			certs = jm.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			ifsUser = jm.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 2);
			IFSUser after = ifsUser[1];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);

			if (certBefore.equals(after.getCertificate())) {
				assertTrue(false);
			}
    	} catch (Exception e){
    		FaultUtil.printFault(e);
    		assertTrue(false);
    	}finally{
			try {
				assertEquals(0,jm.getDatabase().getUsedConnectionCount());
				jm.getDatabase().destroyDatabase();
				jm = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
    }
    
    /** *************** HELPER FUNCTIONS ************************ */
    /** ********************************************************* */
    /** ********************************************************* */
    /** ********************************************************* */
    
    public void verifySAMLAssertion(SAMLAssertion saml, X509Certificate idpCert,
			Application app) throws Exception {
		assertNotNull(saml);
		
		Calendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		if ((now.before(saml.getNotBefore())) || (now.after(saml.getNotOnOrAfter()))) {
		InvalidAssertionFault fault = new InvalidAssertionFault();
		fault.setFaultString("The Assertion is not valid at " + now + ", the assertion is valid from "
		+ saml.getNotBefore() + " to " + saml.getNotOnOrAfter());
		throw fault;
		}

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
    
    private SAMLAssertion getSAMLAssertion(String id, IdPContainer idp) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end = cal.getTime();
		return this.getSAMLAssertion(id, idp, start, end, "urn:oasis:names:tc:SAML:1.0:am:password");
	}
    
    private SAMLAssertion getSAMLAssertion(String id, IdPContainer idp, Date start, Date end, String method)
	throws Exception {
	try {
		org.apache.xml.security.Init.init();
		X509Certificate cert = idp.getCert();
		PrivateKey key = idp.getKey();
		String email = id + "@test.com";

		String issuer = cert.getSubjectDN().toString();
		String federation = cert.getSubjectDN().toString();
		String ipAddress = null;
		String subjectDNS = null;
		SAMLNameIdentifier ni = new SAMLNameIdentifier(id, federation, "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
		SAMLNameIdentifier ni2 = new SAMLNameIdentifier(id, federation, "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
		SAMLSubject sub = new SAMLSubject(ni,null, null, null);
		SAMLSubject sub2 = new SAMLSubject(ni2,null, null, null);
		SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(sub, method, new Date(), ipAddress,
			subjectDNS, null);
		QName name = new QName(EMAIL_NAMESPACE, EMAIL_NAME);
		List vals = new ArrayList();
		vals.add(email);
		SAMLAttribute att = new SAMLAttribute(name.getLocalPart(), name.getNamespaceURI(), name, (long) 0, vals);

		List atts = new ArrayList();
		atts.add(att);
		SAMLAttributeStatement attState = new SAMLAttributeStatement(sub2, atts);

		List l = new ArrayList();
		l.add(auth);
		l.add(attState);

		SAMLAssertion saml = new SAMLAssertion(issuer, start, end, null, null, l);
		List a = new ArrayList();
		a.add(cert);
		saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1, key, a);

		return saml;
	} catch (Exception e) {
		DorianInternalFault fault = new DorianInternalFault();
		fault.setFaultString("Error creating SAML Assertion.");
		FaultHelper helper = new FaultHelper(fault);
		helper.addFaultCause(e);
		fault = (DorianInternalFault) helper.getFault();
		throw fault;

	}
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
	
	private ProxyLifetime getProxyLifetimeShort() {
		ProxyLifetime valid = new ProxyLifetime();
		valid.setHours(0);
		valid.setMinutes(0);
		valid.setSeconds(SHORT_PROXY_VALID);
		return valid;
	}	
	
	private IdPContainer getTrustedIdpAutoApproveAutoRenew(String name) throws Exception {
		return this.getTrustedIdp(name, AutoApprovalAutoRenewalPolicy.class.getName());
	}
	
	private IdPContainer getTrustedIdpAutoApprove(String name) throws Exception {
		return this.getTrustedIdp(name, AutoApprovalPolicy.class.getName());
	}


	private IdPContainer getTrustedIdpManualApprove(String name) throws Exception {
		return this.getTrustedIdp(name, ManualApprovalPolicy.class.getName());
	}


	private IdPContainer getTrustedIdpManualApproveAutoRenew(String name) throws Exception {
		return this.getTrustedIdp(name, ManualApprovalAutoRenewalPolicy.class.getName());
	}
	
	private IdPContainer getTrustedIdp(String name, String policyClass) throws Exception {
		TrustedIdP idp = new TrustedIdP();
		idp.setName(name);
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
		
		return new IdPContainer(idp, cert, pair.getPrivate());
	}
	
	public class IdPContainer {

		TrustedIdP idp;

		X509Certificate cert;

		PrivateKey key;


		public IdPContainer(TrustedIdP idp, X509Certificate cert, PrivateKey key) {
			this.idp = idp;
			this.cert = cert;
			this.key = key;
		}


		public X509Certificate getCert() {
			return cert;
		}


		public TrustedIdP getIdp() {
			return idp;
		}


		public PrivateKey getKey() {
			return key;
		}
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
		super.tearDown();
	}
    
}