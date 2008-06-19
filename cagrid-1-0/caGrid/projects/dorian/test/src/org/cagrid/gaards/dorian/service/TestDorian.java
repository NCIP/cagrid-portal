package org.cagrid.gaards.dorian.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLStatement;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

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
import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;
import org.cagrid.gaards.dorian.federation.AutoApprovalAutoRenewalPolicy;
import org.cagrid.gaards.dorian.federation.AutoApprovalPolicy;
import org.cagrid.gaards.dorian.federation.FederationUtils;
import org.cagrid.gaards.dorian.federation.IFSUser;
import org.cagrid.gaards.dorian.federation.IFSUserFilter;
import org.cagrid.gaards.dorian.federation.IFSUserStatus;
import org.cagrid.gaards.dorian.federation.ManualApprovalAutoRenewalPolicy;
import org.cagrid.gaards.dorian.federation.ManualApprovalPolicy;
import org.cagrid.gaards.dorian.federation.ProxyLifetime;
import org.cagrid.gaards.dorian.federation.SAMLAttributeDescriptor;
import org.cagrid.gaards.dorian.federation.SAMLAuthenticationMethod;
import org.cagrid.gaards.dorian.federation.TrustedIdP;
import org.cagrid.gaards.dorian.federation.TrustedIdPStatus;
import org.cagrid.gaards.dorian.federation.UserManager;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.CountryCode;
import org.cagrid.gaards.dorian.idp.IdPUser;
import org.cagrid.gaards.dorian.idp.IdPUserFilter;
import org.cagrid.gaards.dorian.idp.IdPUserRole;
import org.cagrid.gaards.dorian.idp.IdPUserStatus;
import org.cagrid.gaards.dorian.idp.StateCode;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.NoSuchUserFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.test.CA;
import org.cagrid.gaards.dorian.test.Utils;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.CertificateExtensionsUtil;
import org.cagrid.gaards.pki.Credential;
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.gaards.saml.encoding.SAMLConstants;
import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class TestDorian extends TestCase {

	private Dorian dorian;

	private int count = 0;

	private static final int DELEGATION_LENGTH = 5;

	private static final int SHORT_PROXY_VALID = 2;

	private static final int SHORT_CREDENTIALS_VALID = 10;

	private CA memoryCA;

	/** *************** IdP TEST FUNCTIONS ********************** */
	/** ********************************************************* */
	/** ********************************************************* */
	/** ********************************************************* */

	public void testAuthenticate() {
		try {
			// initialize a Dorian object
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());

			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);

			// test authentication with an active user
			Application a = createApplication();
			assertFalse(dorian.doesLocalUserExist(a.getUserId()));
			dorian.registerWithIdP(a);
			assertTrue(dorian.doesLocalUserExist(a.getUserId()));
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = dorian.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Active);
			dorian.updateIdPUser(gridId, users[0]);
			users = dorian.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			BasicAuthentication auth = new BasicAuthentication();
			auth.setUserId(a.getUserId());
			auth.setPassword(a.getPassword());
			SAMLAssertion saml = dorian.authenticate(auth);
			assertNotNull(saml);
			this.verifySAMLAssertion(saml, dorian.getIdPCertificate(), a);

			// test authentication with a status pending user
			Application b = createApplication();
			dorian.registerWithIdP(b);
			uf = new IdPUserFilter();
			uf.setUserId(b.getUserId());
			users = dorian.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			auth = new BasicAuthentication();
			auth.setUserId(b.getUserId());
			auth.setPassword(b.getPassword());
			try {
				saml = dorian.authenticate(auth);
				fail("User is pending and should not be able to authenticate.");
			} catch (InvalidCredentialFault pdf) {
			}

			// test authentication with a status rejected user
			Application c = createApplication();
			dorian.registerWithIdP(c);
			uf = new IdPUserFilter();
			uf.setUserId(c.getUserId());
			users = dorian.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Rejected);
			dorian.updateIdPUser(gridId, users[0]);
			auth = new BasicAuthentication();
			auth.setUserId(c.getUserId());
			auth.setPassword(c.getPassword());
			try {
				saml = dorian.authenticate(auth);
				fail("User is rejected and should not be able to authenticate.");
			} catch (InvalidCredentialFault pdf) {
			}

			// test authentication with a status suspended user
			Application d = createApplication();
			dorian.registerWithIdP(d);
			uf = new IdPUserFilter();
			uf.setUserId(d.getUserId());
			users = dorian.findIdPUsers(gridId, uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Suspended);
			dorian.updateIdPUser(gridId, users[0]);
			auth = new BasicAuthentication();
			auth.setUserId(d.getUserId());
			auth.setPassword(d.getPassword());
			try {
				saml = dorian.authenticate(auth);
				fail("User is suspended and should not be able to authenticate.");
			} catch (InvalidCredentialFault pdf) {
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testFindUpdateRemoveIdPUser() {
		try {
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());

			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);

			Application a = createApplication();
			dorian.registerWithIdP(a);
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());

			// test findIdPUsers with an invalid gridId
			try {
				String invalidGridId = "ThisIsInvalid";
				dorian.findIdPUsers(invalidGridId, uf);
				fail("Invoker should not be able to invoke.");
			} catch (PermissionDeniedFault pdf) {
			}

			// try to update a user that does not exist
			try {
				IdPUser u = new IdPUser();
				u.setUserId("No_SUCH_USER");
				dorian.updateIdPUser(gridId, u);
				fail("Should not be able to update no such user.");
			} catch (NoSuchUserFault nsuf) {
			}

			IdPUser[] us = dorian.findIdPUsers(gridId, uf);
			assertEquals(1, us.length);
			String address = us[0].getAddress();
			us[0].setAddress("New_Address");
			us[0].setAddress2("New_Address2");
			us[0].setCity("New_City");
			us[0].setCountry(CountryCode.AD);
			us[0].setEmail("NewUser@mail.com");
			us[0].setFirstName("New_First_Name");
			us[0].setLastName("New_Last_Name");
			us[0].setOrganization("New_Organization");
			us[0].setPassword("$W0rdD0ct0R$");
			us[0].setPhoneNumber("012-345-6789");
			us[0].setRole(IdPUserRole.Non_Administrator);
			us[0].setState(StateCode.AK);
			us[0].setStatus(IdPUserStatus.Active);
			us[0].setZipcode("11111");

			dorian.updateIdPUser(gridId, us[0]);
			uf.setAddress(address);
			us = dorian.findIdPUsers(gridId, uf);
			assertEquals(0, us.length);
			uf.setAddress("New_Address");
			us = dorian.findIdPUsers(gridId, uf);
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

			// create an invalid Grid Id and try to removeIdPUser
			try {
				String invalidGridId = "ThisIsInvalid";
				dorian.removeIdPUser(invalidGridId, us[0].getUserId());
				fail("Should not be able to Remove User with invalid Grid Id");
			} catch (PermissionDeniedFault pdf) {
			}

			// remove the user
			dorian.removeIdPUser(gridId, us[0].getUserId());
			us = dorian.findIdPUsers(gridId, uf);
			assertEquals(0, us.length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
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
			
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());
			BasicAuthentication auth = new BasicAuthentication();
			auth.setUserId(Dorian.IDP_ADMIN_USER_ID);
			auth.setPassword(Dorian.IDP_ADMIN_PASSWORD);
			SAMLAssertion saml = dorian.authenticate(auth);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = dorian.createProxy(saml, publicKey,
					lifetime, DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testRemoveDorianIdPDefaultAdmin() {
		try {
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());
			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);

			IFSUserFilter uf = new IFSUserFilter();
			uf.setGridId(gridId);
			assertEquals(1, dorian.findIFSUsers(gridId, uf).length);

			Application app = createApplication();
			dorian.registerWithIdP(app);

			IdPUserFilter f = new IdPUserFilter();
			f.setUserId(app.getUserId());
			IdPUser[] list = dorian.findIdPUsers(gridId, f);
			assertEquals(1, list.length);
			assertEquals(app.getUserId(), list[0].getUserId());
			list[0].setStatus(IdPUserStatus.Active);
			list[0].setRole(IdPUserRole.Administrator);
			dorian.updateIdPUser(gridId, list[0]);
			String username = list[0].getUserId();
			BasicAuthentication cred = new BasicAuthentication();
			cred.setUserId(username);
			cred.setPassword(app.getPassword());
			SAMLAssertion saml = dorian.authenticate(cred);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = dorian.createProxy(saml,
					pair.getPublic(), lifetime, DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(1);
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(1, ifsUser.length);
			String userGridId = ifsUser[0].getGridId();
			dorian.addAdmin(gridId, userGridId);

			assertEquals(2, dorian.findIFSUsers(gridId, null).length);
			String[] users = dorian.getAdmins(gridId);
			boolean found1 = false;
			boolean found2 = false;
			for (int i = 0; i < users.length; i++) {
				if (users[i].equals(userGridId)) {
					found1 = true;
				} else if (users[i].equals(gridId)) {
					found2 = true;
				}
			}
			assertTrue(found1);
			assertTrue(found2);
			dorian.removeIdPUser(userGridId, Dorian.IDP_ADMIN_USER_ID);

			Dorian dorian2 = new Dorian(conf, "localhost");

			assertEquals(1, dorian2.findIFSUsers(userGridId, null).length);
			assertEquals(0, dorian2.findIFSUsers(userGridId, uf).length);
			users = dorian2.getAdmins(userGridId);
			found1 = false;
			found2 = false;
			for (int i = 0; i < users.length; i++) {
				if (users[i].equals(userGridId)) {
					found1 = true;
				} else if (users[i].equals(gridId)) {
					found2 = true;
				}
			}
			assertTrue(found1);
			assertFalse(found2);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testSuspendDorianIdPDefaultAdmin() {
		try {
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());
			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);

			IFSUserFilter uf = new IFSUserFilter();
			uf.setGridId(gridId);
			assertEquals(1, dorian.findIFSUsers(gridId, uf).length);
			Application app = createApplication();
			dorian.registerWithIdP(app);
			IdPUserFilter f = new IdPUserFilter();
			f.setUserId(app.getUserId());
			IdPUser[] list = dorian.findIdPUsers(gridId, f);
			assertEquals(1, list.length);
			assertEquals(app.getUserId(), list[0].getUserId());
			list[0].setStatus(IdPUserStatus.Active);
			list[0].setRole(IdPUserRole.Administrator);
			dorian.updateIdPUser(gridId, list[0]);
			String username = list[0].getUserId();
			BasicAuthentication cred = new BasicAuthentication();
			cred.setUserId(username);
			cred.setPassword(app.getPassword());
			SAMLAssertion saml = dorian.authenticate(cred);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = dorian.createProxy(saml,
					pair.getPublic(), lifetime, DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(1);
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(1, ifsUser.length);
			String userGridId = ifsUser[0].getGridId();
			dorian.addAdmin(gridId, userGridId);
			assertEquals(2, dorian.findIFSUsers(gridId, null).length);

			IdPUserFilter f2 = new IdPUserFilter();
			f2.setUserId(Dorian.IDP_ADMIN_USER_ID);
			IdPUser[] list2 = dorian.findIdPUsers(gridId, f2);
			assertEquals(1, list2.length);
			assertEquals(Dorian.IDP_ADMIN_USER_ID, list2[0].getUserId());
			list2[0].setStatus(IdPUserStatus.Suspended);
			dorian.updateIdPUser(userGridId, list2[0]);

			Dorian dorian2 = new Dorian(conf, "localhost");

			try {
				dorian2.findIdPUsers(gridId, null);
				fail("Should not have permission to execute.");
			} catch (PermissionDeniedFault pdf) {

			}
			IdPUserFilter idpf = new IdPUserFilter();
			idpf.setUserId(Dorian.IDP_ADMIN_USER_ID);
			idpf.setStatus(IdPUserStatus.Suspended);
			assertEquals(1, dorian2.findIdPUsers(userGridId, idpf).length);
			idpf.setStatus(IdPUserStatus.Active);
			assertEquals(0, dorian2.findIdPUsers(userGridId, idpf).length);

			assertEquals(2, dorian2.findIFSUsers(userGridId, null).length);
			assertEquals(1, dorian2.findIFSUsers(userGridId, uf).length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testMembershipRemovalOnIdPUserRemoval() {
		try {
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());
			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			Application app = createApplication();
			dorian.registerWithIdP(app);

			IdPUserFilter f = new IdPUserFilter();
			f.setUserId(app.getUserId());
			IdPUser[] list = dorian.findIdPUsers(gridId, f);
			assertEquals(1, list.length);
			assertEquals(app.getUserId(), list[0].getUserId());
			list[0].setStatus(IdPUserStatus.Active);
			dorian.updateIdPUser(gridId, list[0]);
			String username = list[0].getUserId();
			BasicAuthentication cred = new BasicAuthentication();
			cred.setUserId(username);
			cred.setPassword(app.getPassword());
			SAMLAssertion saml = dorian.authenticate(cred);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = dorian.createProxy(saml,
					pair.getPublic(), lifetime, DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(1);
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(1, ifsUser.length);
			String userGridId = ifsUser[0].getGridId();
			dorian.addAdmin(gridId, userGridId);

			String[] users = dorian.getAdmins(gridId);
			boolean found = false;
			for (int i = 0; i < users.length; i++) {
				if (users[i].equals(userGridId)) {
					found = true;
				}
			}
			assertTrue(found);
			dorian.removeIdPUser(gridId, username);
			assertEquals(0, dorian.findIdPUsers(gridId, f).length);
			assertEquals(0, dorian.findIFSUsers(gridId, filter).length);

			users = null;
			users = dorian.getAdmins(gridId);
			found = false;
			for (int i = 0; i < users.length; i++) {
				if (users[i].equals(userGridId)) {
					found = true;
				}
			}
			assertFalse(found);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testMembershipRemovalOnUserRemoval() {
		try {
			DorianProperties conf = Utils.getDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());
			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			idp.getIdp().setId(
					dorian.addTrustedIdP(gridId, idp.getIdp()).getId());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = dorian.createProxy(getSAMLAssertion(
					username, idp), pair.getPublic(), lifetime,
					DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(idp.getIdp().getId());
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(1, ifsUser.length);
			String userGridId = ifsUser[0].getGridId();
			dorian.addAdmin(gridId, userGridId);

			String[] users = dorian.getAdmins(gridId);
			boolean found = false;
			for (int i = 0; i < users.length; i++) {
				if (users[i].equals(userGridId)) {
					found = true;
				}
			}
			assertTrue(found);
			dorian.removeIFSUser(gridId, ifsUser[0]);
			assertEquals(0, dorian.findIFSUsers(gridId, filter).length);

			users = null;
			users = dorian.getAdmins(gridId);
			found = false;
			for (int i = 0; i < users.length; i++) {
				if (users[i].equals(userGridId)) {
					found = true;
				}
			}
			assertFalse(found);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testAutoApprovalAutoRenewal() {
		try {
			DorianProperties conf = Utils.getExpiringDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());

			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			idp.getIdp().setId(
					dorian.addTrustedIdP(gridId, idp.getIdp()).getId());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			X509Certificate[] certs = dorian.createProxy(getSAMLAssertion(
					username, idp), pair.getPublic(), lifetime,
					DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);

			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(idp.getIdp().getId());
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser before = ifsUser[0];
			assertEquals(before.getUserStatus(), IFSUserStatus.Active);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			certs = dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);
			ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser after = ifsUser[0];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);
			if (before.getCertificate().equals(after.getCertificate())) {
				fail("A problem occured with the renewal of credentials");
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testManualApprovalAutoRenewal() {
		try {
			DorianProperties conf = Utils.getExpiringDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());

			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);

			IdPContainer idp = this
					.getTrustedIdpManualApproveAutoRenew("My IdP");
			idp.getIdp().setId(
					dorian.addTrustedIdP(gridId, idp.getIdp()).getId());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			try {
				dorian.createProxy(getSAMLAssertion(username, idp), pair
						.getPublic(), getProxyLifetimeShort(),
						DELEGATION_LENGTH);
				fail("Should not be able to create a proxy with an IdP that is not approved");
			} catch (PermissionDeniedFault f) {

			}

			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(idp.getIdp().getId());
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser before = ifsUser[0];
			assertEquals(before.getUserStatus(), IFSUserStatus.Pending);
			before.setUserStatus(IFSUserStatus.Active);
			dorian.updateIFSUser(gridId, before);
			dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);
			ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser after = ifsUser[0];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);
			if (before.getCertificate().equals(after.getCertificate())) {
				fail("A problem occured with the renewal of credentials");
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testAutoApprovalManualRenewal() {
		try {
			DorianProperties conf = Utils.getExpiringDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());

			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);
			IdPContainer idp = this.getTrustedIdpAutoApprove("My IdP");
			idp.getIdp().setId(
					dorian.addTrustedIdP(gridId, idp.getIdp()).getId());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);

			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(idp.getIdp().getId());
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser before = ifsUser[0];
			assertEquals(before.getUserStatus(), IFSUserStatus.Active);
			X509Certificate certBefore = CertUtil.loadCertificate(before
					.getCertificate().getCertificateAsString());

			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);

			try {
				dorian.createProxy(getSAMLAssertion(username, idp), pair
						.getPublic(), getProxyLifetimeShort(),
						DELEGATION_LENGTH);
				fail("Should not be able to create a proxy with an IdP that has not been renewed");
			} catch (PermissionDeniedFault f) {

			}
			dorian.renewIFSUserCredentials(gridId, ifsUser[0]);

			dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);
			ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser after = ifsUser[0];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);

			if (certBefore.equals(after.getCertificate())) {
				fail("A problem occured with the renewal of credentials");
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
			} catch (Exception e) {
				FaultUtil.printFault(e);
				assertTrue(false);
			}
		}
	}

	public void testManualApprovalManualRenewal() {
		try {
			DorianProperties conf = Utils.getExpiringDorianProperties();
			dorian = new Dorian(conf, "localhost");
			assertNotNull(dorian.getConfiguration());
			assertNotNull(dorian.getDatabase());

			String gridSubject = getDorianIdPUserId(conf, dorian,
					Dorian.IDP_ADMIN_USER_ID);
			String gridId = UserManager.subjectToIdentity(gridSubject);

			IdPContainer idp = this.getTrustedIdpManualApprove("My IdP");
			idp.getIdp().setId(
					dorian.addTrustedIdP(gridId, idp.getIdp()).getId());
			String username = "user";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			try {
				dorian.createProxy(getSAMLAssertion(username, idp), pair
						.getPublic(), getProxyLifetimeShort(),
						DELEGATION_LENGTH);
				fail("Should not be able to create a proxy with an IdP that has not been approved");
			} catch (PermissionDeniedFault f) {

			}

			IFSUserFilter filter = new IFSUserFilter();
			filter.setUID(username);
			filter.setIdPId(idp.getIdp().getId());
			IFSUser[] ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser before = ifsUser[0];
			assertEquals(before.getUserStatus(), IFSUserStatus.Pending);
			before.setUserStatus(IFSUserStatus.Active);
			dorian.updateIFSUser(gridId, before);
			dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);
			X509Certificate certBefore = CertUtil.loadCertificate(before
					.getCertificate().getCertificateAsString());
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);

			try {
				dorian.createProxy(getSAMLAssertion(username, idp), pair
						.getPublic(), getProxyLifetimeShort(),
						DELEGATION_LENGTH);
				fail("Should not be able to create a proxy with an IdP that has not been renewed");
			} catch (PermissionDeniedFault f) {

			}
			dorian.renewIFSUserCredentials(gridId, ifsUser[0]);

			dorian.createProxy(getSAMLAssertion(username, idp), pair
					.getPublic(), lifetime, DELEGATION_LENGTH);
			ifsUser = dorian.findIFSUsers(gridId, filter);
			assertEquals(ifsUser.length, 1);
			IFSUser after = ifsUser[0];
			assertEquals(after.getUserStatus(), IFSUserStatus.Active);

			if (certBefore.equals(after.getCertificate())) {
				fail("A problem occured with the renewal of credentials");
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				assertEquals(0, dorian.getDatabase().getUsedConnectionCount());
				dorian.clearDatabase();
				dorian = null;
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

	public void verifySAMLAssertion(SAMLAssertion saml,
			X509Certificate idpCert, Application app) throws Exception {
		assertNotNull(saml);

		Calendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		if ((now.before(saml.getNotBefore()))
				|| (now.after(saml.getNotOnOrAfter()))) {
			InvalidAssertionFault fault = new InvalidAssertionFault();
			fault.setFaultString("The Assertion is not valid at " + now
					+ ", the assertion is valid from " + saml.getNotBefore()
					+ " to " + saml.getNotOnOrAfter());
			throw fault;
		}

		saml.verify(idpCert);

		assertEquals(idpCert.getSubjectDN().toString(), saml.getIssuer());
		Iterator itr = saml.getStatements();
		int statementCount = 0;
		boolean authFound = false;
		while (itr.hasNext()) {
			statementCount = statementCount + 1;
			SAMLStatement stmt = (SAMLStatement) itr.next();
			if (stmt instanceof SAMLAuthenticationStatement) {
				if (authFound) {
					assertTrue(false);
				} else {
					authFound = true;
				}
				SAMLAuthenticationStatement auth = (SAMLAuthenticationStatement) stmt;
				assertEquals(app.getUserId(), auth.getSubject()
						.getNameIdentifier().getName());
				assertEquals("urn:oasis:names:tc:SAML:1.0:am:password", auth
						.getAuthMethod());
			}

			if (stmt instanceof SAMLAttributeStatement) {
				String uid = Utils.getAttribute(saml,
						SAMLConstants.UID_ATTRIBUTE_NAMESPACE,
						SAMLConstants.UID_ATTRIBUTE);
				assertNotNull(uid);
				String email = Utils.getAttribute(saml,
						SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE,
						SAMLConstants.EMAIL_ATTRIBUTE);
				assertNotNull(email);
				String firstName = Utils.getAttribute(saml,
						SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE,
						SAMLConstants.FIRST_NAME_ATTRIBUTE);
				assertNotNull(firstName);
				String lastName = Utils.getAttribute(saml,
						SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE,
						SAMLConstants.LAST_NAME_ATTRIBUTE);
				assertNotNull(lastName);

				assertEquals(app.getUserId(), uid);
				assertEquals(app.getFirstName(), firstName);
				assertEquals(app.getLastName(), lastName);
				assertEquals(app.getEmail(), email);
			}

		}

		assertEquals(2, statementCount);
		assertTrue(authFound);
	}

	private SAMLAssertion getSAMLAssertion(String id, IdPContainer idp)
			throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end = cal.getTime();
		return this.getSAMLAssertion(id, idp, start, end,
				"urn:oasis:names:tc:SAML:1.0:am:password");
	}

	private SAMLAssertion getSAMLAssertion(String id, IdPContainer idp,
			Date start, Date end, String method) throws Exception {
		try {
			org.apache.xml.security.Init.init();
			X509Certificate cert = idp.getCert();
			PrivateKey key = idp.getKey();
			String firstName = "first" + id;
			String lastName = "first" + id;
			String email = id + "@test.com";

			String issuer = cert.getSubjectDN().toString();
			String federation = cert.getSubjectDN().toString();
			String ipAddress = null;
			String subjectDNS = null;
			SAMLNameIdentifier ni = new SAMLNameIdentifier(id, federation,
					"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
			SAMLNameIdentifier ni2 = new SAMLNameIdentifier(id, federation,
					"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
			SAMLSubject sub = new SAMLSubject(ni, null, null, null);
			SAMLSubject sub2 = new SAMLSubject(ni2, null, null, null);
			SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(
					sub, method, new Date(), ipAddress, subjectDNS, null);

			QName quid = new QName(SAMLConstants.UID_ATTRIBUTE_NAMESPACE,
					SAMLConstants.UID_ATTRIBUTE);
			List vals1 = new ArrayList();
			vals1.add(id);
			SAMLAttribute uidAtt = new SAMLAttribute(quid.getLocalPart(), quid
					.getNamespaceURI(), quid, 0, vals1);

			QName qfirst = new QName(
					SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE,
					SAMLConstants.FIRST_NAME_ATTRIBUTE);
			List vals2 = new ArrayList();
			vals2.add(firstName);
			SAMLAttribute firstNameAtt = new SAMLAttribute(qfirst
					.getLocalPart(), qfirst.getNamespaceURI(), qfirst, 0, vals2);

			QName qLast = new QName(
					SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE,
					SAMLConstants.LAST_NAME_ATTRIBUTE);
			List vals3 = new ArrayList();
			vals3.add(lastName);
			SAMLAttribute lastNameAtt = new SAMLAttribute(qLast.getLocalPart(),
					qLast.getNamespaceURI(), qLast, 0, vals3);

			QName qemail = new QName(SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE,
					SAMLConstants.EMAIL_ATTRIBUTE);
			List vals4 = new ArrayList();
			vals4.add(email);
			SAMLAttribute emailAtt = new SAMLAttribute(qemail.getLocalPart(),
					qemail.getNamespaceURI(), qemail, 0, vals4);

			List atts = new ArrayList();
			atts.add(uidAtt);
			atts.add(firstNameAtt);
			atts.add(lastNameAtt);
			atts.add(emailAtt);
			SAMLAttributeStatement attState = new SAMLAttributeStatement(sub2,
					atts);

			List l = new ArrayList();
			l.add(auth);
			l.add(attState);

			SAMLAssertion saml = new SAMLAssertion(issuer, start, end, null,
					null, l);
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
		u.setPassword(count + "$D0ct0rC0de$");
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
			PrivateKey key, X509Certificate[] certs, int delegationLength)
			throws Exception {
		assertNotNull(certs);
		assertEquals(2, certs.length);
		GlobusCredential cred = new GlobusCredential(key, certs);
		assertNotNull(cred);
		long max = FederationUtils.getTimeInSeconds(lifetime);
		long min = max - 3;
		long timeLeft = cred.getTimeLeft();
		if ((min > timeLeft) || (timeLeft > max)) {
			assertTrue(false);
		}
		assertEquals(certs[1].getSubjectDN().toString(), identityToSubject(cred
				.getIdentity()));
		assertEquals(cred.getIssuer(), identityToSubject(cred.getIdentity()));
		assertEquals(delegationLength, CertificateExtensionsUtil
				.getDelegationPathLength(certs[0]));
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

	private IdPContainer getTrustedIdpAutoApproveAutoRenew(String name)
			throws Exception {
		return this.getTrustedIdp(name, AutoApprovalAutoRenewalPolicy.class
				.getName());
	}

	private IdPContainer getTrustedIdpAutoApprove(String name) throws Exception {
		return this.getTrustedIdp(name, AutoApprovalPolicy.class.getName());
	}

	private IdPContainer getTrustedIdpManualApprove(String name)
			throws Exception {
		return this.getTrustedIdp(name, ManualApprovalPolicy.class.getName());
	}

	private IdPContainer getTrustedIdpManualApproveAutoRenew(String name)
			throws Exception {
		return this.getTrustedIdp(name, ManualApprovalAutoRenewalPolicy.class
				.getName());
	}

	private IdPContainer getTrustedIdp(String name, String policyClass)
			throws Exception {
		TrustedIdP idp = new TrustedIdP();
		idp.setName(name);
		idp.setUserPolicyClass(policyClass);
		idp.setStatus(TrustedIdPStatus.Active);
		SAMLAttributeDescriptor uid = new SAMLAttributeDescriptor();
		uid.setNamespaceURI(SAMLConstants.UID_ATTRIBUTE_NAMESPACE);
		uid.setName(SAMLConstants.UID_ATTRIBUTE);
		idp.setUserIdAttributeDescriptor(uid);

		SAMLAttributeDescriptor firstName = new SAMLAttributeDescriptor();
		firstName.setNamespaceURI(SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE);
		firstName.setName(SAMLConstants.FIRST_NAME_ATTRIBUTE);
		idp.setFirstNameAttributeDescriptor(firstName);

		SAMLAttributeDescriptor lastName = new SAMLAttributeDescriptor();
		lastName.setNamespaceURI(SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE);
		lastName.setName(SAMLConstants.LAST_NAME_ATTRIBUTE);
		idp.setLastNameAttributeDescriptor(lastName);

		SAMLAttributeDescriptor email = new SAMLAttributeDescriptor();
		email.setNamespaceURI(SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE);
		email.setName(SAMLConstants.EMAIL_ATTRIBUTE);
		idp.setEmailAttributeDescriptor(email);

		SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
		methods[0] = SAMLAuthenticationMethod
				.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
		idp.setAuthenticationMethod(methods);

		String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + name;
		Credential cred = memoryCA.createIdentityCertificate(name);
		X509Certificate cert = cred.getCertificate();
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
		idp.setIdPCertificate(CertUtil.writeCertificate(cert));
		return new IdPContainer(idp, cert, cred.getPrivateKey());
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
			memoryCA = new CA(Utils.getCASubject());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private String getDorianIdPUserId(DorianProperties conf, Dorian d,
			String uid) throws Exception {
		String caSubject = d.getCACertificate().getSubjectDN().getName();
		String policy = conf.getIdentityFederationProperties()
				.getIdentityAssignmentPolicy();
		TrustedIdP idp = new TrustedIdP();
		idp.setId(1);
		idp.setName(conf.getIdentityProviderProperties().getName());
		return Utils.getDorianIdPUserId(policy, conf
				.getIdentityProviderProperties().getName(), caSubject, uid);
	}

}