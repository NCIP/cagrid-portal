package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.test.Constants;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLStatement;

import java.io.InputStream;
import java.util.Iterator;

import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdentityProvider extends TestCase {

	private IdPConfiguration conf;

	private Database db;

	private CertificateAuthority ca;

	private int count = 0;


	public void testAutomaticRegistration() {
		try {

			IdentityProvider idp = new IdentityProvider(conf, db, ca);
			conf.setRegistrationPolicy(new AutomaticRegistrationPolicy());
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass().getName());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testManualRegistration() {
		try {
			IdentityProvider idp = new IdentityProvider(conf, db, ca);
			conf.setRegistrationPolicy(new ManualRegistrationPolicy());
			assertEquals(ManualRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass().getName());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Pending, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			users[0].setStatus(IdPUserStatus.Active);
			idp.updateUser(cred.getUserId(), users[0]);
			users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testBadRegisterWithIdP() {
		try {
			IdentityProvider idp = new IdentityProvider(conf, db, ca);
			conf.setRegistrationPolicy(new AutomaticRegistrationPolicy());
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass().getName());
			// Application a = createApplication();
			// idp.register(a);
			// test the password length too long
			try {
				Application a = createTooLongPasswordApplication();
				idp.register(a);
				fail("Should not be able to register with a password of this length.");
			} catch (InvalidUserPropertyFault iupf) {
			}

			// test the password length is too short
			try {
				Application b = createTooShortPasswordApplication();
				idp.register(b);
				fail("Should not be able to register with a password of this length.");
			} catch (InvalidUserPropertyFault iupf) {
			}

			// test the UserId length is too long
			try {
				Application c = createTooLongUserIdApplication();
				idp.register(c);
				fail("Should not be able to register with a UserId of this length.");
			} catch (InvalidUserPropertyFault iupf) {
			}

			// test the UserId length is too short
			try {
				Application d = createTooShortUserIdApplication();
				idp.register(d);
				fail("Should not be able to register with a UserId of this length.");
			} catch (InvalidUserPropertyFault iupf) {
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testBadRemoveIdPUserNoSuchUser() {
		try {
			IdentityProvider idp = new IdentityProvider(conf, db, ca);
			conf.setRegistrationPolicy(new AutomaticRegistrationPolicy());
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass().getName());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			IdPUser[] us = idp.findUsers(cred.getUserId(), uf);
			assertEquals(2, us.length);

			// create a userId that does not exist
			String userId = "No_SUCH_USER";
			idp.removeUser(cred.getUserId(), userId);
			IdPUserFilter f = new IdPUserFilter();
			IdPUser[] users = idp.findUsers(cred.getUserId(), f);
			assertEquals(2, users.length);
		} catch (PermissionDeniedFault pdf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testBadRegisterWithIdPTwoIdenticalUsers() {
		try {
			IdentityProvider idp = new IdentityProvider(conf, db, ca);
			conf.setRegistrationPolicy(new AutomaticRegistrationPolicy());
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass().getName());
			Application a = createApplication();
			idp.register(a);
			Application b = a;
			idp.register(b);
			fail("Should not be able to register two identical users.");
		} catch (InvalidUserPropertyFault iupf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testMultipleUsers() {
		try {
			IdentityProvider idp = new IdentityProvider(conf, db, ca);
			conf.setRegistrationPolicy(new ManualRegistrationPolicy());
			assertEquals(ManualRegistrationPolicy.class.getName(), conf.getRegistrationPolicy().getClass().getName());
			BasicAuthCredential cred = getAdminCreds();
			int times = 3;
			for (int i = 0; i < times; i++) {
				Application a = createApplication();
				idp.register(a);

				IdPUserFilter uf = new IdPUserFilter();
				uf.setUserId(a.getUserId());
				IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Pending, users[0].getStatus());
				assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
				users[0].setStatus(IdPUserStatus.Active);
				idp.updateUser(cred.getUserId(), users[0]);
				users = idp.findUsers(cred.getUserId(), uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Active, users[0].getStatus());
				uf.setUserId("user");
				users = idp.findUsers(cred.getUserId(), uf);
				assertEquals(i + 1, users.length);
				BasicAuthCredential auth = new BasicAuthCredential();
				auth.setUserId(a.getUserId());
				auth.setPassword(a.getPassword());
				gov.nih.nci.cagrid.opensaml.SAMLAssertion saml = idp.authenticate(auth);
				assertNotNull(saml);
				this.verifySAMLAssertion(saml, idp, a);
			}

			IdPUserFilter uf = new IdPUserFilter();
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(times + 1, users.length);
			for (int i = 0; i < users.length; i++) {
				IdPUserFilter f = new IdPUserFilter();
				f.setUserId(users[i].getUserId());
				IdPUser[] us = idp.findUsers(cred.getUserId(), f);
				assertEquals(1, us.length);
				us[0].setFirstName("NEW NAME");
				idp.updateUser(cred.getUserId(), us[0]);
				IdPUser[] us2 = idp.findUsers(cred.getUserId(), f);
				assertEquals(1, us2.length);
				assertEquals(us[0], us2[0]);
				if (!users[i].getUserId().equals(cred.getUserId())) {
					idp.removeUser(cred.getUserId(), users[i].getUserId());
					us = idp.findUsers(cred.getUserId(), f);
					assertEquals(0, us.length);
				}
			}
			users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void verifySAMLAssertion(SAMLAssertion saml, IdentityProvider idp, Application app) throws Exception {
		assertNotNull(saml);
		saml.verify(idp.getIdPCertificate());

		assertEquals(idp.getIdPCertificate().getSubjectDN().toString(), saml.getIssuer());
		Iterator itr = saml.getStatements();
		int count = 0;
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
				assertEquals("urn:oasis:names:tc:SAML:1.0:am:password", auth.getAuthMethod());
			}

			if (stmt instanceof SAMLAttributeStatement) {
				String uid = Utils.getAttribute(saml, SAMLConstants.UID_ATTRIBUTE_NAMESPACE,
					SAMLConstants.UID_ATTRIBUTE);
				assertNotNull(uid);
				String email = Utils.getAttribute(saml, SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE,
					SAMLConstants.EMAIL_ATTRIBUTE);
				assertNotNull(email);
				String firstName = Utils.getAttribute(saml, SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE,
					SAMLConstants.FIRST_NAME_ATTRIBUTE);
				assertNotNull(firstName);
				String lastName = Utils.getAttribute(saml, SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE,
					SAMLConstants.LAST_NAME_ATTRIBUTE);
				assertNotNull(lastName);

				assertEquals(app.getUserId(), uid);
				assertEquals(app.getFirstName(), firstName);
				assertEquals(app.getLastName(), lastName);
				assertEquals(app.getEmail(), email);
			}

		}

		assertEquals(2, count);
		assertTrue(authFound);
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
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			ca = Utils.getCA();
			InputStream resource = TestCase.class.getResourceAsStream(Constants.IDP_CONFIG);
			SimpleResourceManager trm = new SimpleResourceManager(resource);
			this.conf = (IdPConfiguration) trm.getResource(IdPConfiguration.RESOURCE);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
