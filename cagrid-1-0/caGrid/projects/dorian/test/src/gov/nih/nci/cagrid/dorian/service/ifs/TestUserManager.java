package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAttributeDescriptor;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.service.PropertyManager;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.test.CA;
import gov.nih.nci.cagrid.dorian.test.Utils;

import java.io.StringReader;
import java.security.cert.X509Certificate;
import java.util.Map;

import junit.framework.TestCase;

import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.Credential;
import org.cagrid.tools.database.Database;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestUserManager extends TestCase implements Publisher {
	private static final int INIT_USER = 1;
	private static final String DEFAULT_IDP_NAME = "Dorian IdP";

	private Database db;

	private CertificateAuthority ca;
	private CA memoryCA;

	private PropertyManager props;

	private CertificateBlacklistManager blackList;

	public void testSingleUserIdPNameBasedIdentitfiers() {
		try {
			checkSingleUser(getUserManagerNameBasedIdentities());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testSingleUserIdPIdBasedIdentitfiers() {
		try {
			checkSingleUser(getUserManagerIdBasedIdentities());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testMultipleUsersIdPNameBasedIdentitfiers() {
		try {
			checkMultipleUsers(getUserManagerNameBasedIdentities());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testMultipleUsersIdPIdBasedIdentitfiers() {
		try {
			checkMultipleUsers(getUserManagerIdBasedIdentities());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void checkSingleUser(UserManager um) {
		try {
			// Test adding user
			IFSUser user = new IFSUser();
			user.setIdPId(INIT_USER + 1);
			user.setUID("user");
			user.setFirstName("John");
			user.setLastName("Doe");
			user.setEmail("user@user.com");
			user = um.addUser(getIdp(user), user);
			assertNotNull(user.getCertificate());
			assertNotNull(user.getGridId());
			assertNotNull(user.getUserStatus());
			assertEquals(IFSUserStatus.Pending, user.getUserStatus());
			StringReader ureader = new StringReader(user.getCertificate()
					.getCertificateAsString());
			X509Certificate cert = CertUtil.loadCertificate(ureader);
			assertEquals(user.getGridId(), UserManager.subjectToIdentity(cert
					.getSubjectDN().getName()));
			assertEquals(user, um.getUser(user.getIdPId(), user.getUID()));
			assertEquals(user, um.getUser(user.getGridId()));
			assertEquals(1, um.getDisabledUsers().size());
			assertTrue(isUserSerialIdInList(user, um.getDisabledUsers()));

			// Test Querying for users
			IFSUserFilter f1 = new IFSUserFilter();
			IFSUser[] l1 = um.getUsers(f1);
			assertEquals(1 + INIT_USER, l1.length);

			// Test querying by uid
			IFSUserFilter f2 = new IFSUserFilter();
			f2.setUID("nobody");
			IFSUser[] l2 = um.getUsers(f2);
			assertEquals(0, l2.length);
			f2.setUID("use");
			l2 = um.getUsers(f2);
			assertEquals(1, l2.length);
			assertEquals(user, l2[0]);

			// Test querying by IdP_Id
			IFSUserFilter f3 = new IFSUserFilter();
			f3.setIdPId(Long.MAX_VALUE);
			IFSUser[] l3 = um.getUsers(f3);
			assertEquals(0, l3.length);
			f3.setIdPId(user.getIdPId());
			l3 = um.getUsers(f3);
			assertEquals(1, l3.length);
			assertEquals(user, l3[0]);

			// Test querying by GID
			IFSUserFilter f4 = new IFSUserFilter();
			f4.setGridId("nobody");
			IFSUser[] l4 = um.getUsers(f4);
			assertEquals(0, l4.length);
			f4.setGridId(user.getGridId());
			l4 = um.getUsers(f4);
			assertEquals(1, l4.length);
			assertEquals(user, l4[0]);

			// Test querying by Email
			IFSUserFilter f5 = new IFSUserFilter();
			f5.setEmail("nobody");
			IFSUser[] l5 = um.getUsers(f5);
			assertEquals(0, l5.length);
			f5.setEmail(user.getEmail());
			l5 = um.getUsers(f5);
			assertEquals(1, l5.length);
			assertEquals(user, l5[0]);

			// Test querying by Status
			IFSUserFilter f7 = new IFSUserFilter();
			f7.setUserStatus(IFSUserStatus.Suspended);
			IFSUser[] l7 = um.getUsers(f7);
			assertEquals(0, l7.length);
			f7.setUserStatus(user.getUserStatus());
			l7 = um.getUsers(f7);
			assertEquals(1, l7.length);
			assertEquals(user, l7[0]);

			// Test querying by First Name
			IFSUserFilter f8 = new IFSUserFilter();
			f8.setFirstName("nobody");
			IFSUser[] l8 = um.getUsers(f8);
			assertEquals(0, l8.length);
			f8.setFirstName(user.getFirstName());
			l8 = um.getUsers(f8);
			assertEquals(1, l8.length);
			assertEquals(user, l8[0]);

			// Test querying by Last Name
			IFSUserFilter f9 = new IFSUserFilter();
			f9.setLastName("nobody");
			IFSUser[] l9 = um.getUsers(f9);
			assertEquals(0, l9.length);
			f9.setLastName(user.getLastName());
			l9 = um.getUsers(f9);
			assertEquals(1, l9.length);
			assertEquals(user, l9[0]);

			// Test All
			IFSUserFilter all = new IFSUserFilter();
			all.setIdPId(user.getIdPId());
			all.setUID(user.getUID());
			all.setGridId(user.getGridId());
			all.setFirstName(user.getFirstName());
			all.setLastName(user.getLastName());
			all.setEmail(user.getEmail());
			all.setUserStatus(user.getUserStatus());
			IFSUser[] allList = um.getUsers(all);
			assertEquals(1, allList.length);

			// Test Update
			IFSUser u1 = um.getUser(user.getGridId());
			u1.setFirstName("newfirst");
			u1.setLastName("newlast");
			u1.setEmail("newemail@example.com");
			um.updateUser(u1);
			assertEquals(u1, um.getUser(u1.getGridId()));

			IFSUser u3 = um.getUser(user.getGridId());
			u3.setUserStatus(IFSUserStatus.Active);
			um.updateUser(u3);
			assertEquals(u3, um.getUser(u3.getGridId()));
			assertEquals(0, um.getDisabledUsers().size());
			assertFalse(isUserSerialIdInList(user, um.getDisabledUsers()));

			IFSUser u4 = um.getUser(user.getGridId());
			u4.setUserStatus(IFSUserStatus.Suspended);
			u4.setEmail("newemail2@example.com");
			um.updateUser(u4);
			assertEquals(u4, um.getUser(u4.getGridId()));

			IFSUser u5 = um.getUser(user.getGridId());
			u5.setGridId("changed grid id");
			um.updateUser(u5);
			assertEquals(u5, um.getUser(u5.getGridId()));

			// Now we test updating credentials
			um.renewUserCredentials(getIdp(u5), u5);
			assertEquals(u5, um.getUser(u5.getGridId()));
			StringReader r = new StringReader(u5.getCertificate()
					.getCertificateAsString());
			X509Certificate newCert = CertUtil.loadCertificate(r);
			if (cert.equals(newCert)) {
				assertTrue(false);
			}

			um.removeUser(u5);
			assertEquals(INIT_USER, um.getUsers(new IFSUserFilter()).length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				um.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isUserSerialIdInList(IFSUser usr,
			Map<String, DisabledUser> list) throws Exception {
		if (list.containsKey(usr.getGridId())) {
			long sn = list.get(usr.getGridId()).getSerialNumber();
			X509Certificate cert = CertUtil.loadCertificate(usr
					.getCertificate().getCertificateAsString());
			long certsn = cert.getSerialNumber().longValue();
			if (sn == certsn) {
				return true;
			}
		}
		return false;
	}

	public void checkMultipleUsers(UserManager um) {
		try {

			String prefix = "user";
			String firstNamePrefix = "John";
			String lastNamePrefix = "Doe";

			int userCount = 9;

			for (int i = 0; i < userCount; i++) {
				// Test adding user
				long idpId = (i % 3) + 1 + INIT_USER;
				long idpCount = (i / 3) + 1;

				String uname = prefix + i;
				String firstName = firstNamePrefix + i;
				String lastName = lastNamePrefix + i;

				IFSUser user = new IFSUser();

				user.setIdPId(idpId);
				user.setUID(uname);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEmail(uname + "@user.com");
				user = um.addUser(getIdp(user), user);
				assertNotNull(user.getCertificate());
				assertNotNull(user.getGridId());
				assertNotNull(user.getUserStatus());
				assertEquals(IFSUserStatus.Pending, user.getUserStatus());
				StringReader ureader = new StringReader(user.getCertificate()
						.getCertificateAsString());
				X509Certificate cert = CertUtil.loadCertificate(ureader);
				assertEquals(user.getGridId(), UserManager
						.subjectToIdentity(cert.getSubjectDN().getName()));
				assertEquals(user, um.getUser(user.getIdPId(), user.getUID()));
				assertEquals(user, um.getUser(user.getGridId()));
				assertEquals((i + 1), um.getDisabledUsers().size());
				assertTrue(isUserSerialIdInList(user, um.getDisabledUsers()));

				// Test Querying for users
				IFSUserFilter f1 = new IFSUserFilter();
				IFSUser[] l1 = um.getUsers(f1);
				assertEquals((i + 1 + INIT_USER), l1.length);

				// Test querying by uid
				IFSUserFilter f2 = new IFSUserFilter();
				f2.setUID("nobody");
				IFSUser[] l2 = um.getUsers(f2);
				assertEquals(0, l2.length);
				f2.setUID("use");
				l2 = um.getUsers(f2);
				assertEquals((i + 1), l2.length);
				f2.setUID(uname);
				l2 = um.getUsers(f2);
				assertEquals(1, l2.length);
				assertEquals(user, l2[0]);

				// Test querying by IdP_Id
				IFSUserFilter f3 = new IFSUserFilter();
				f3.setIdPId(Long.MAX_VALUE);
				IFSUser[] l3 = um.getUsers(f3);
				assertEquals(0, l3.length);
				f3.setIdPId(user.getIdPId());
				l3 = um.getUsers(f3);
				assertEquals(idpCount, l3.length);

				// Test querying by GID
				IFSUserFilter f4 = new IFSUserFilter();
				f4.setGridId("nobody");
				IFSUser[] l4 = um.getUsers(f4);
				assertEquals(0, l4.length);

				String temp = user.getGridId();
				int index = temp.lastIndexOf("/");
				temp = temp.substring(0, index);
				f4.setGridId(temp);
				l4 = um.getUsers(f4);
				assertEquals(idpCount, l4.length);
				f4.setGridId(user.getGridId());
				l4 = um.getUsers(f4);
				assertEquals(1, l4.length);
				assertEquals(user, l4[0]);

				// Test querying by Email
				IFSUserFilter f5 = new IFSUserFilter();
				f5.setEmail("nobody");
				IFSUser[] l5 = um.getUsers(f5);
				assertEquals(0, l5.length);
				f5.setEmail(user.getEmail());
				l5 = um.getUsers(f5);
				assertEquals(1, l5.length);
				assertEquals(user, l5[0]);

				// Test querying by Status
				IFSUserFilter f7 = new IFSUserFilter();
				f7.setUserStatus(IFSUserStatus.Suspended);
				IFSUser[] l7 = um.getUsers(f7);
				assertEquals(i, l7.length);
				f7.setUserStatus(user.getUserStatus());
				l7 = um.getUsers(f7);
				assertEquals(1, l7.length);
				assertEquals(user, l7[0]);

				// Test querying by First Name
				IFSUserFilter f8 = new IFSUserFilter();
				f8.setFirstName("nobody");
				IFSUser[] l8 = um.getUsers(f8);
				assertEquals(0, l8.length);
				f8.setFirstName(firstNamePrefix);
				l8 = um.getUsers(f8);
				assertEquals((i + 1), l8.length);
				f8.setFirstName(user.getFirstName());
				l8 = um.getUsers(f8);
				assertEquals(1, l8.length);
				assertEquals(user, l8[0]);

				// Test querying by Last Name
				IFSUserFilter f9 = new IFSUserFilter();
				f9.setLastName("nobody");
				IFSUser[] l9 = um.getUsers(f9);
				assertEquals(0, l9.length);
				f9.setLastName(lastNamePrefix);
				l9 = um.getUsers(f9);
				assertEquals((i + 1), l9.length);
				f9.setLastName(user.getLastName());
				l9 = um.getUsers(f9);
				assertEquals(1, l9.length);
				assertEquals(user, l9[0]);

				// Test All
				IFSUserFilter all = new IFSUserFilter();
				all.setIdPId(user.getIdPId());
				all.setUID(user.getUID());
				all.setGridId(user.getGridId());
				all.setFirstName(user.getFirstName());
				all.setLastName(user.getLastName());
				all.setEmail(user.getEmail());
				all.setUserStatus(user.getUserStatus());
				IFSUser[] lall = um.getUsers(all);
				assertEquals(1, lall.length);

				// Test Update
				IFSUser u1 = um.getUser(user.getGridId());
				u1.setEmail("newemail@example.com");
				um.updateUser(u1);
				assertEquals(u1, um.getUser(u1.getGridId()));

				IFSUser u3 = um.getUser(user.getGridId());
				u3.setUserStatus(IFSUserStatus.Active);
				um.updateUser(u3);
				assertEquals(u3, um.getUser(u3.getGridId()));
				assertEquals(i, um.getDisabledUsers().size());
				assertFalse(isUserSerialIdInList(user, um.getDisabledUsers()));
				u3.setUserStatus(IFSUserStatus.Suspended);
				um.updateUser(u3);
				assertEquals(u3, um.getUser(u3.getGridId()));
				assertEquals((i + 1), um.getDisabledUsers().size());
				assertTrue(isUserSerialIdInList(user, um.getDisabledUsers()));

				IFSUser u4 = um.getUser(user.getGridId());
				u4.setUserStatus(IFSUserStatus.Suspended);
				u4.setEmail("newemail2@example.com");
				um.updateUser(u4);
				assertEquals(u4, um.getUser(u4.getGridId()));

				IFSUser u5 = um.getUser(user.getGridId());
				u5.setGridId("changed grid id");
				um.updateUser(u5);
				assertEquals(u5, um.getUser(u5.getGridId()));

				// Now we test updating credentials
				um.renewUserCredentials(getIdp(u5), u5);
				assertEquals(u5, um.getUser(u5.getGridId()));
				StringReader r = new StringReader(u5.getCertificate()
						.getCertificateAsString());
				X509Certificate newCert = CertUtil.loadCertificate(r);
				if (cert.equals(newCert)) {
					assertTrue(false);
				}

			}

			// um.removeUser(u5);
			IFSUser[] list = um.getUsers(new IFSUserFilter());
			assertEquals(userCount + INIT_USER, list.length);
			int count = userCount;
			for (int i = 0; i < list.length; i++) {
				count = count - 1;
				um.removeUser(list[i]);
				assertEquals(count + INIT_USER, um
						.getUsers(new IFSUserFilter()).length);
			}
			assertEquals(0, um.getUsers(new IFSUserFilter()).length);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				um.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private IdentityFederationProperties getConf(String policy)
			throws Exception {
		IdentityFederationProperties conf = Utils.getIdentityFederationProperties();
		conf.setIdentityAssignmentPolicy(policy);
		return conf;
	}

	private IFSDefaults getDefaults() throws Exception {
		TrustedIdP idp = new TrustedIdP();
		idp.setName("Initial IdP");

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
		idp.setUserPolicyClass(AutoApprovalAutoRenewalPolicy.class.getName());

		String subject = Utils.CA_SUBJECT_PREFIX + ",CN=" + idp.getName();
		Credential cred = memoryCA.createIdentityCertificate(idp.getName());
		X509Certificate cert = cred.getCertificate();
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
		idp.setIdPCertificate(CertUtil.writeCertificate(cert));
		idp.setStatus(TrustedIdPStatus.Active);
		IFSUser usr = new IFSUser();
		usr.setUID("inital_admin");
		usr.setFirstName("Mr");
		usr.setLastName("Admin");
		usr.setEmail("inital_admin@test.com");
		usr.setUserStatus(IFSUserStatus.Active);
		return new IFSDefaults(idp, usr);
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			blackList = new CertificateBlacklistManager(db);
			blackList.clearDatabase();
			assertEquals(0, db.getUsedConnectionCount());
			ca = Utils.getCA();
			memoryCA = new CA(Utils.getCASubject());
			props = new PropertyManager(db);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public UserManager getUserManagerNameBasedIdentities() throws Exception {
		IdentityFederationProperties conf = getConf(IdentityAssignmentPolicy.NAME);
		TrustedIdPManager tm = new TrustedIdPManager(conf, db);
		UserManager um = new UserManager(db, conf, props, ca, blackList, tm,
				this, getDefaults());
		um.clearDatabase();
		return um;
	}

	public UserManager getUserManagerIdBasedIdentities() throws Exception {
		IdentityFederationProperties conf = getConf(IdentityAssignmentPolicy.ID);
		TrustedIdPManager tm = new TrustedIdPManager(conf, db);
		UserManager um = new UserManager(db, conf, props, ca, blackList, tm,
				this, getDefaults());
		um.clearDatabase();
		return um;
	}

	protected void tearDown() throws Exception {
		super.setUp();
		try {
			blackList.clearDatabase();
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void publishCRL() {

	}

	private TrustedIdP getIdp(IFSUser usr) {
		TrustedIdP idp = new TrustedIdP();
		idp.setId(usr.getIdPId());
		idp.setName(DEFAULT_IDP_NAME + usr.getIdPId());
		return idp;
	}

}
