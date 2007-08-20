package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.dorian.conf.IdentityProviderConfiguration;
import gov.nih.nci.cagrid.dorian.conf.PasswordSecurityPolicy;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.test.Constants;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLStatement;

import java.io.InputStream;
import java.io.InputStreamReader;
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

	private IdentityProviderConfiguration conf;

	private Database db;

	private CertificateAuthority ca;

	private int count = 0;


	public void testAutomaticRegistration() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
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
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAuthenticate() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			verifyAuthentication(idp, a);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testResetLockedPassword() {
		IdentityProvider idp = null;
		try {
			PasswordSecurityPolicy policy = conf.getPasswordSecurityPolicy();
			policy.setMaxConsecutiveInvalidLogins(3);
			policy.setMaxTotalInvalidLogins(4);
			policy.getLockoutTime().setHours(0);
			policy.getLockoutTime().setMinutes(0);
			policy.getLockoutTime().setSeconds(3);
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			Application a = createApplication();

			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());

			BasicAuthCredential bad = new BasicAuthCredential();
			bad.setUserId(a.getUserId());
			bad.setPassword("foobar");

			int localCount = 0;
			for (int i = 1; i <= (policy.getMaxTotalInvalidLogins() + 2); i++) {
				if (i > policy.getMaxTotalInvalidLogins()) {
					try {
						idp.authenticate(getCredential(a));
						fail("Should NOT be able to authenticate!!!");
					} catch (PermissionDeniedFault e) {
					}
				} else if (localCount != policy.getMaxConsecutiveInvalidLogins()) {
					try {
						idp.authenticate(bad);
						fail("Should NOT be able to authenticate!!!");
					} catch (PermissionDeniedFault e) {

					}
				} else {
					localCount = 0;
					try {
						idp.authenticate(getCredential(a));
						fail("Should NOT be able to authenticate!!!");
					} catch (PermissionDeniedFault e) {

					}
					Thread.sleep((policy.getLockoutTime().getSeconds() * 1000) + 100);
					verifyAuthentication(idp, a);
					try {
						idp.authenticate(bad);
						fail("Should NOT be able to authenticate!!!");
					} catch (PermissionDeniedFault e) {

					}
				}
				localCount = localCount + 1;
			}
			// Now we have an admin reset the password
			users[0].setPassword("$W0rdD0ct0R$2");
			a.setPassword(users[0].getPassword());
			idp.updateUser(cred.getUserId(), users[0]);
			verifyAuthentication(idp, a);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testResetPasswordSecurityOnSuccessfulLogin() {
		IdentityProvider idp = null;
		try {
			PasswordSecurityPolicy policy = conf.getPasswordSecurityPolicy();
			policy.setMaxConsecutiveInvalidLogins(3);
			policy.setMaxTotalInvalidLogins(10);
			policy.getLockoutTime().setHours(0);
			policy.getLockoutTime().setMinutes(0);
			policy.getLockoutTime().setSeconds(3);
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			Application a = createApplication();

			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());

			BasicAuthCredential bad = new BasicAuthCredential();
			bad.setUserId(a.getUserId());
			bad.setPassword("foobar");

			int localCount = 0;
			for (int i = 1; i <= (policy.getMaxTotalInvalidLogins()); i++) {
				if (localCount == (policy.getMaxConsecutiveInvalidLogins() - 1)) {
					verifyAuthentication(idp, a);
					localCount = 0;
				} else {
					try {
						idp.authenticate(bad);
						fail("Should NOT be able to authenticate!!!");
					} catch (PermissionDeniedFault e) {

					}
					localCount = localCount + 1;
				}

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testPasswordSecurity() {
		IdentityProvider idp = null;
		try {
			PasswordSecurityPolicy policy = conf.getPasswordSecurityPolicy();
			policy.setMaxConsecutiveInvalidLogins(3);
			policy.setMaxTotalInvalidLogins(7);
			policy.getLockoutTime().setHours(0);
			policy.getLockoutTime().setMinutes(0);
			policy.getLockoutTime().setSeconds(3);
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			for (int j = 0; j < 2; j++) {
				Application a = createApplication();

				idp.register(a);
				BasicAuthCredential cred = getAdminCreds();
				IdPUserFilter uf = new IdPUserFilter();
				uf.setUserId(a.getUserId());
				IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
				assertEquals(1, users.length);
				assertEquals(IdPUserStatus.Active, users[0].getStatus());
				assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());

				BasicAuthCredential bad = new BasicAuthCredential();
				bad.setUserId(a.getUserId());
				bad.setPassword("foobar");

				int localCount = 0;
				for (int i = 1; i <= (policy.getMaxTotalInvalidLogins() + 1); i++) {
					if (i > policy.getMaxTotalInvalidLogins()) {
						try {
							idp.authenticate(getCredential(a));
							fail("Should NOT be able to authenticate!!!");
						} catch (PermissionDeniedFault e) {

						}
					} else if (localCount != policy.getMaxConsecutiveInvalidLogins()) {
						try {
							idp.authenticate(bad);
							fail("Should NOT be able to authenticate!!!");
						} catch (PermissionDeniedFault e) {

						}
					} else {
						localCount = 0;
						try {
							idp.authenticate(getCredential(a));
							fail("Should NOT be able to authenticate!!!");
						} catch (PermissionDeniedFault e) {

						}
						Thread.sleep((policy.getLockoutTime().getSeconds() * 1000) + 100);
						verifyAuthentication(idp, a);
						try {
							idp.authenticate(bad);
							fail("Should NOT be able to authenticate!!!");
						} catch (PermissionDeniedFault e) {

						}
					}
					localCount = localCount + 1;

				}
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAuthenticateBadPassword() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			BasicAuthCredential c = getCredential(a);
			c.setPassword("bad password");
			try {
				idp.authenticate(c);
				fail("Should not be able to authenticate with a bad password!!!");
			} catch (PermissionDeniedFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testChangePassword() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			verifyAuthentication(idp, a);
			BasicAuthCredential c = getCredential(a);
			String newPassword = "$W0rdD0ct0R$2";
			idp.changePassword(getCredential(a), newPassword);

			try {
				idp.authenticate(c);
				fail("Should not be able to authenticate with the old password!!!");
			} catch (PermissionDeniedFault f) {

			}
			a.setPassword(newPassword);
			verifyAuthentication(idp, a);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testChangePasswordToBadPassword() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
			Application a = createApplication();
			idp.register(a);
			BasicAuthCredential cred = getAdminCreds();
			IdPUserFilter uf = new IdPUserFilter();
			uf.setUserId(a.getUserId());
			IdPUser[] users = idp.findUsers(cred.getUserId(), uf);
			assertEquals(1, users.length);
			assertEquals(IdPUserStatus.Active, users[0].getStatus());
			assertEquals(IdPUserRole.Non_Administrator, users[0].getRole());
			verifyAuthentication(idp, a);
			try {
				idp.changePassword(getCredential(a), "short");
				fail("Should not be able to change the password to something to short");
			} catch (InvalidUserPropertyFault f) {
			}

			try {
				idp.changePassword(getCredential(a), "$W0rdD0ct0R$$$$$$$$$$$$$$$$$$$$$$$W0rdD0ct0R$");
				fail("Should not be able to change the password to something to long");
			} catch (InvalidUserPropertyFault f) {
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testRegistrationNoAddress2() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
			Application a = createApplication();
			a.setAddress2(null);
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
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testManualRegistration() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(ManualRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(ManualRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
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
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testBadRegisterWithIdP() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
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
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testPasswordConstraints() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertTrue(isValidPassword(idp, UserManager.ADMIN_PASSWORD));
			assertFalse(isValidPassword(idp, "$$$$User44"));
			assertFalse(isValidPassword(idp, "12345Dorian6789"));
			assertFalse(isValidPassword(idp, "12345dorian6789$"));
			assertFalse(isValidPassword(idp, "12345DORIAN6789$"));
			assertFalse(isValidPassword(idp, "$$$$Dorian$$$$"));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testBadRemoveIdPUserNoSuchUser() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
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
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testBadRegisterWithIdPTwoIdenticalUsers() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(AutomaticRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(AutomaticRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
			Application a = createApplication();
			idp.register(a);
			Application b = a;
			idp.register(b);
			fail("Should not be able to register two identical users.");
		} catch (InvalidUserPropertyFault iupf) {
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testMultipleUsers() {
		IdentityProvider idp = null;
		try {
			conf.setRegistrationPolicy(ManualRegistrationPolicy.class.getName());
			idp = new IdentityProvider(conf, db, ca);
			assertEquals(ManualRegistrationPolicy.class.getName(), conf.getRegistrationPolicy());
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
		} finally {
			try {
				idp.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private boolean isValidPassword(IdentityProvider idp, String password) throws Exception {
		Application app = this.createApplication(password);
		try {
			idp.register(app);
		} catch (InvalidUserPropertyFault f) {
			if (f.getFaultString().equals(UserManager.INVALID_PASSWORD_MESSAGE)) {
				return false;
			}
		}

		return true;

	}


	private void verifyAuthentication(IdentityProvider idp, Application a) throws Exception {
		SAMLAssertion saml = idp.authenticate(getCredential(a));
		verifySAMLAssertion(saml, idp, a);

	}


	public void verifySAMLAssertion(SAMLAssertion saml, IdentityProvider idp, Application app) throws Exception {
		assertNotNull(saml);
		saml.verify(idp.getIdPCertificate());

		assertEquals(idp.getIdPCertificate().getSubjectDN().toString(), saml.getIssuer());
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

		assertEquals(2, statementCount);
		assertTrue(authFound);
	}


	private BasicAuthCredential getCredential(Application app) {
		BasicAuthCredential cred = new BasicAuthCredential();
		cred.setUserId(app.getUserId());
		cred.setPassword(app.getPassword());
		return cred;
	}


	private BasicAuthCredential getAdminCreds() {
		BasicAuthCredential cred = new BasicAuthCredential();
		cred.setUserId(UserManager.ADMIN_USER_ID);
		cred.setPassword(UserManager.ADMIN_PASSWORD);
		return cred;
	}


	private Application createApplication() {
		return createApplication(count + "$W0rdD0ct0R$");
	}


	private Application createApplication(String password) {
		Application u = new Application();
		u.setUserId(count + "user");
		u.setEmail(count + "user@mail.com");
		u.setPassword(password);
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
		u.setPassword(count + "$W0rdD0ct0R$$$$$$$$$$$$$$$$$W0rdD0ct0R$");
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
			this.conf = (IdentityProviderConfiguration) gov.nih.nci.cagrid.common.Utils.deserializeObject(
				new InputStreamReader(resource), IdentityProviderConfiguration.class);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
