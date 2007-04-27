package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.dorian.conf.CredentialLifetime;
import gov.nih.nci.cagrid.dorian.conf.CredentialPolicy;
import gov.nih.nci.cagrid.dorian.conf.IdentityAssignmentPolicy;
import gov.nih.nci.cagrid.dorian.conf.IdentityFederationConfiguration;
import gov.nih.nci.cagrid.dorian.conf.Length;
import gov.nih.nci.cagrid.dorian.conf.ProxyPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAttributeDescriptor;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.PropertyManager;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.CertificateExtensionsUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class TestIFS extends TestCase {

	private static final int MIN_NAME_LENGTH = 4;

	private static final int MAX_NAME_LENGTH = 50;

	private static final int SHORT_PROXY_VALID = 10;

	private static final int SHORT_CREDENTIALS_VALID = 35;

	private static final int DELEGATION_LENGTH = 5;

	public final static String INITIAL_ADMIN = "admin";

	private Database db;

	private CertificateAuthority ca;

	private PropertyManager props;


	public void testRenewUserCredentials() {
		IFS ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props,ca, defaults);
			String uid = "user";
			String adminSubject = UserManager.getUserSubject(conf.getIdentityAssignmentPolicy(), ca.getCACertificate()
				.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid, idp), publicKey, lifetime,
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			IFSUserFilter f1 = new IFSUserFilter();
			f1.setIdPId(idp.getIdp().getId());
			f1.setUID(uid);
			IFSUser[] users = ifs.findUsers(adminGridId, f1);
			assertEquals(1, users.length);
			IFSUser usr1 = users[0];
			String certStr = usr1.getCertificate().getCertificateAsString();
			X509Certificate cert1 = CertUtil.loadCertificate(certStr);
			Thread.sleep(1000);
			IFSUser usr2 = ifs.renewUserCredentials(adminGridId, usr1);
			assertEquals(usr1.getGridId(), usr2.getGridId());

			if (certStr.equals(usr2.getCertificate().getCertificateAsString())) {
				fail("Certificate was the same, but should have been renew.");
			}

			X509Certificate cert2 = CertUtil.loadCertificate(usr2.getCertificate().getCertificateAsString());

			assertTrue(cert2.getNotBefore().after(cert1.getNotBefore()));

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testFindRemoveUpdateUsers() {
		IFS ifs = null;
		try {
			int times = 3;
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			String uidPrefix = "user";
			String adminSubject = UserManager.getUserSubject(conf.getIdentityAssignmentPolicy(), ca.getCACertificate()
				.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			int ucount = 1;
			for (int i = 0; i < times; i++) {
				String uid = uidPrefix + i;
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ProxyLifetime lifetime = getProxyLifetime();
				X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid, idp), publicKey, lifetime, i);
				createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, i);
				ucount = ucount + 1;
				assertEquals(ucount, ifs.findUsers(adminGridId, new IFSUserFilter()).length);
				IFSUserFilter f1 = new IFSUserFilter();
				f1.setIdPId(idp.getIdp().getId());
				f1.setUID(uid);
				IFSUser[] usr = ifs.findUsers(adminGridId, f1);
				assertEquals(1, usr.length);

				try {
					ifs.findUsers(usr[0].getGridId(), new IFSUserFilter());
					fail("Should have thrown exception attempting to find users.");
				} catch (PermissionDeniedFault f) {

				}
				ifs.addAdmin(adminGridId, usr[0].getGridId());
				assertEquals(ucount, ifs.findUsers(usr[0].getGridId(), new IFSUserFilter()).length);
			}

			int rcount = ucount;

			for (int i = 0; i < times; i++) {
				String uid = uidPrefix + i;
				IFSUserFilter f1 = new IFSUserFilter();
				f1.setIdPId(idp.getIdp().getId());
				f1.setUID(uid);
				IFSUser[] usr = ifs.findUsers(adminGridId, f1);
				assertEquals(1, usr.length);
				ifs.removeUser(adminGridId, usr[0]);
				rcount = rcount - 1;
				assertEquals(rcount, ifs.findUsers(adminGridId, new IFSUserFilter()).length);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void testCreateProxy() {
		IFS ifs = null;
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props,ca, defaults);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion("user", idp), publicKey, lifetime,
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void testCreateProxySuspendedIdP() {
		IFS ifs = null;
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			idp.getIdp().setStatus(TrustedIdPStatus.Suspended);
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db,props, ca, defaults);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			try {
				ifs.createProxy(getSAMLAssertion("user", idp), publicKey, lifetime, DELEGATION_LENGTH);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void testCreateProxyAutoApproval() {
		IFS ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			String username = "user";
			IdPContainer idp = this.getTrustedIdpAutoApprove("My IdP");
			IdentityFederationConfiguration conf = getExpiringCredentialsConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db,props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(
				conf.getIdentityAssignmentPolicy(), ca.getCACertificate().getSubjectDN().getName(), idp.getIdp(),
				INITIAL_ADMIN));

			// give a chance for others to run right before we enter timing
			// sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime,
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);

			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			try {

				PublicKey publicKey2 = pair2.getPublic();
				ifs
					.createProxy(getSAMLAssertion(username, idp), publicKey2, getProxyLifetimeShort(),
						DELEGATION_LENGTH);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Expired);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyManualApproval() {
		IFS ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			String username = "user";
			IdPContainer idp = this.getTrustedIdpManualApprove("My IdP");
			IdentityFederationConfiguration conf = getExpiringCredentialsConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(
				conf.getIdentityAssignmentPolicy(), ca.getCACertificate().getSubjectDN().getName(), idp.getIdp(),
				defaults.getDefaultUser().getUID()));

			try {
				ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime, DELEGATION_LENGTH);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Pending);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyAutoApprovalAutoRenewal() {
		IFS ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			String username = "user";
			IdentityFederationConfiguration conf = getExpiringCredentialsConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(
				conf.getIdentityAssignmentPolicy(), ca.getCACertificate().getSubjectDN().getName(), idp.getIdp(),
				defaults.getDefaultUser().getUID()));

			PublicKey publicKey2 = pair2.getPublic();
			// give a chance for others to run right before we enter timing
			// sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime,
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser before = ifs.getUser(gridId, idp.getIdp().getId(), username);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);

			certs = ifs.createProxy(getSAMLAssertion(username, idp), publicKey2, getProxyLifetimeShort(),
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser after = ifs.getUser(gridId, idp.getIdp().getId(), username);
			if (before.getCertificate().equals(after.getCertificate())) {
				fail("Credentials were the same when should have been auto-renewed.");
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyManualApprovalAutoRenewal() {
		IFS ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			String username = "user";
			IdPContainer idp = this.getTrustedIdpManualApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getExpiringCredentialsConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(
				conf.getIdentityAssignmentPolicy(), ca.getCACertificate().getSubjectDN().getName(), idp.getIdp(),
				defaults.getDefaultUser().getUID()));

			try {
				ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), getProxyLifetimeShort(),
					DELEGATION_LENGTH);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault f) {

			}
			IFSUser usr = ifs.getUser(gridId, idp.getIdp().getId(), username);
			usr.setUserStatus(IFSUserStatus.Active);
			ifs.updateUser(gridId, usr);
			// give a chance for others to run right before we enter timing
			// sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime,
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser before = ifs.getUser(gridId, idp.getIdp().getId(), username);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			PublicKey publicKey2 = pair2.getPublic();
			certs = ifs.createProxy(getSAMLAssertion(username, idp), publicKey2, getProxyLifetimeShort(),
				DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser after = ifs.getUser(gridId, idp.getIdp().getId(), username);
			if (before.getCertificate().equals(after.getCertificate())) {
				fail("Certificate should have been renewed, but was not.");
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyInvalidProxyValid() {
		IFS ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			Thread.sleep(500);
			try {
				ProxyLifetime valid = new ProxyLifetime();
				valid.setHours(12);
				valid.setMinutes(0);
				valid.setSeconds(1);
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp), publicKey, valid, DELEGATION_LENGTH);
				fail("Should have thrown an exception creating an invalid proxy.");
			} catch (InvalidProxyFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyInvalidAuthenticationMethod() {
		IFS ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertionUnspecifiedMethod("user", idp), publicKey, getProxyLifetime(),
					DELEGATION_LENGTH);
				fail("Should have thrown an exception creating a proxy with an invalid SAML assertion.");
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyUntrustedIdP() {
		IFS ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdPContainer idp2 = this.getTrustedIdpAutoApproveAutoRenew("My IdP 2");

			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);

			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp2), publicKey, getProxyLifetime(), DELEGATION_LENGTH);
				fail("Should have thrown an exception creating a proxy with an invalid SAML assertion.");
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testCreateProxyExpiredAssertion() {
		IFS ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getExpiredSAMLAssertion("user", idp), publicKey, getProxyLifetime(), DELEGATION_LENGTH);
				fail("Should have thrown an exception creating a proxy with an invalid SAML assertion.");
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testGetTrustedIdPs() {
		IFS ifs = null;
		try {
			IdPContainer idp0 = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getExpiringCredentialsConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp0.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(
				conf.getIdentityAssignmentPolicy(), ca.getCACertificate().getSubjectDN().getName(), idp0.getIdp(),
				defaults.getDefaultUser().getUID()));
			int times = 3;
			String baseName = "Test IdP";
			String baseUpdateName = "Updated IdP";
			int count = 1;
			for (int i = 0; i < times; i++) {
				assertEquals(count, ifs.getTrustedIdPs(gridId).length);
				count = count + 1;
				String name = baseName + " " + count;
				IdPContainer cont = getTrustedIdpAutoApproveAutoRenew(name);
				TrustedIdP idp = cont.getIdp();
				idp = ifs.addTrustedIdP(gridId, idp);
				assertEquals(count, ifs.getTrustedIdPs(gridId).length);

				// Test Updates
				String updatedName = baseUpdateName + " " + i;
				IdPContainer updateCont = getTrustedIdpManualApproveAutoRenew(name);
				TrustedIdP updateIdp = updateCont.getIdp();
				updateIdp.setId(idp.getId());
				ifs.updateTrustedIdP(gridId, updateIdp);
				assertEquals(count, ifs.getTrustedIdPs(gridId).length);
			}

			TrustedIdP[] idps = ifs.getTrustedIdPs(gridId);
			assertEquals(times + 1, idps.length);
			count = times + 1;
			for (int i = 0; i < idps.length; i++) {
				if (idps[i].getId() != idp0.getIdp().getId()) {
					count = count - 1;
					ifs.removeTrustedIdP(gridId, idps[i].getId());
					assertEquals(count, ifs.getTrustedIdPs(gridId).length);
				}
			}

			assertEquals(count, ifs.getTrustedIdPs(gridId).length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAdministrators() {
		IFS ifs = null;
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationConfiguration conf = getConf();
			IFSDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IFS(conf, db, props, ca, defaults);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion("user", idp), publicKey, lifetime,
				DELEGATION_LENGTH);

			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs, DELEGATION_LENGTH);
			String userId = UserManager.subjectToIdentity(certs[1].getSubjectDN().toString());
			// Check that the user cannot call any admin methods
			validateAccessControl(ifs, userId);
			ifs.addAdmin(defaults.getDefaultUser().getGridId(), userId);
			assertEquals(2, ifs.findUsers(userId, null).length);
			ifs.removeAdmin(userId, userId);
			validateAccessControl(ifs, userId);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		} finally {
			try {
				ifs.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	private void validateAccessControl(IFS ifs, String userId) throws Exception {
		try {
			ifs.addAdmin(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.addTrustedIdP(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.findUsers(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.getAdmins(userId);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.getTrustedIdPs(userId);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.getUser(userId, 0, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.getUserPolicies(userId);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.removeAdmin(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.removeTrustedIdP(userId, 0);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.removeUser(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.renewUserCredentials(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.updateTrustedIdP(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}

		try {
			ifs.updateUser(userId, null);
			fail("Should not have permission to execute the operation.");
		} catch (PermissionDeniedFault f) {

		}
	}


	private IdentityFederationConfiguration getConf() throws Exception {
		IdentityFederationConfiguration conf = new IdentityFederationConfiguration();
		conf.setIdentityAssignmentPolicy(IdentityAssignmentPolicy.name);
		CredentialPolicy cp = new CredentialPolicy();
		CredentialLifetime l = new CredentialLifetime();
		l.setYears(1);
		l.setMonths(0);
		l.setDays(0);
		l.setHours(0);
		l.setMinutes(0);
		l.setSeconds(0);
		cp.setCredentialLifetime(l);
		conf.setCredentialPolicy(cp);
		Length len = new Length();
		len.setMin(MIN_NAME_LENGTH);
		len.setMax(MAX_NAME_LENGTH);
		conf.setIdentityProviderNameLength(len);

		ProxyPolicy policy = new ProxyPolicy();
		gov.nih.nci.cagrid.dorian.conf.ProxyLifetime pl = new gov.nih.nci.cagrid.dorian.conf.ProxyLifetime();
		pl.setHours(12);
		pl.setMinutes(0);
		pl.setSeconds(0);
		policy.setProxyLifetime(pl);
		conf.setProxyPolicy(policy);
		conf.setAccountPolicies(Utils.getAccountPolicies());
		return conf;
	}


	private IdentityFederationConfiguration getExpiringCredentialsConf() throws Exception {
		IdentityFederationConfiguration conf = new IdentityFederationConfiguration();
		conf.setIdentityAssignmentPolicy(IdentityAssignmentPolicy.name);
		CredentialPolicy cp = new CredentialPolicy();
		CredentialLifetime l = new CredentialLifetime();
		l.setYears(0);
		l.setMonths(0);
		l.setDays(0);
		l.setHours(0);
		l.setMinutes(0);
		l.setSeconds(SHORT_CREDENTIALS_VALID);
		cp.setCredentialLifetime(l);
		conf.setCredentialPolicy(cp);
		Length len = new Length();
		len.setMin(MIN_NAME_LENGTH);
		len.setMax(MAX_NAME_LENGTH);
		conf.setIdentityProviderNameLength(len);

		ProxyPolicy policy = new ProxyPolicy();
		gov.nih.nci.cagrid.dorian.conf.ProxyLifetime pl = new gov.nih.nci.cagrid.dorian.conf.ProxyLifetime();
		pl.setHours(12);
		pl.setMinutes(0);
		pl.setSeconds(0);
		policy.setProxyLifetime(pl);
		conf.setProxyPolicy(policy);
		conf.setAccountPolicies(Utils.getAccountPolicies());
		return conf;
	}


	private IFSDefaults getDefaults() throws Exception {
		TrustedIdP idp = this.getTrustedIdpAutoApproveAutoRenew("Initial IdP").getIdp();
		IFSUser usr = new IFSUser();
		usr.setUID(INITIAL_ADMIN);
		usr.setFirstName("Mr");
		usr.setLastName("Admin");
		usr.setEmail(INITIAL_ADMIN + "@test.com");
		usr.setUserStatus(IFSUserStatus.Active);
		return new IFSDefaults(idp, usr);
	}


	private SAMLAssertion getSAMLAssertion(String id, IdPContainer idp) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end = cal.getTime();
		return this.getSAMLAssertion(id, idp, start, end, "urn:oasis:names:tc:SAML:1.0:am:password");
	}


	private SAMLAssertion getSAMLAssertionUnspecifiedMethod(String id, IdPContainer idp) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end = cal.getTime();
		return this.getSAMLAssertion(id, idp, start, end, "urn:oasis:names:tc:SAML:1.0:am:unspecified");
	}


	private SAMLAssertion getExpiredSAMLAssertion(String id, IdPContainer idp) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		Date end = cal.getTime();
		return this.getSAMLAssertion(id, idp, start, end, "urn:oasis:names:tc:SAML:1.0:am:password");
	}


	private SAMLAssertion getSAMLAssertion(String id, IdPContainer idp, Date start, Date end, String method)
		throws Exception {
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
			SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(sub, method, new Date(), ipAddress,
				subjectDNS, null);

			QName quid = new QName(SAMLConstants.UID_ATTRIBUTE_NAMESPACE, SAMLConstants.UID_ATTRIBUTE);
			List vals1 = new ArrayList();
			vals1.add(id);
			SAMLAttribute uidAtt = new SAMLAttribute(quid.getLocalPart(), quid.getNamespaceURI(), quid, 0, vals1);

			QName qfirst = new QName(SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE, SAMLConstants.FIRST_NAME_ATTRIBUTE);
			List vals2 = new ArrayList();
			vals2.add(firstName);
			SAMLAttribute firstNameAtt = new SAMLAttribute(qfirst.getLocalPart(), qfirst.getNamespaceURI(), qfirst, 0,
				vals2);

			QName qLast = new QName(SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE, SAMLConstants.LAST_NAME_ATTRIBUTE);
			List vals3 = new ArrayList();
			vals3.add(lastName);
			SAMLAttribute lastNameAtt = new SAMLAttribute(qLast.getLocalPart(), qLast.getNamespaceURI(), qLast, 0,
				vals3);

			QName qemail = new QName(SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE, SAMLConstants.EMAIL_ATTRIBUTE);
			List vals4 = new ArrayList();
			vals4.add(email);
			SAMLAttribute emailAtt = new SAMLAttribute(qemail.getLocalPart(), qemail.getNamespaceURI(), qemail, 0,
				vals4);

			List atts = new ArrayList();
			atts.add(uidAtt);
			atts.add(firstNameAtt);
			atts.add(lastNameAtt);
			atts.add(emailAtt);
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


	private IdPContainer getTrustedIdpAutoApproveAutoRenew(String name) throws Exception {
		return this.getTrustedIdp(name, AutoApprovalAutoRenewalPolicy.class.getName());
	}


	private String identityToSubject(String identity) {
		String s = identity.substring(1);
		return s.replace('/', ',');
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
		idp.setIdPCertificate(CertUtil.writeCertificate(cert));
		return new IdPContainer(idp, cert, pair.getPrivate());
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			ca = Utils.getCA(db);
			props = new PropertyManager(db);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			assertEquals(0, db.getRootUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
		// return the thread to normal priority for those tests which raise the
		// thread priority
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}


	private ProxyLifetime getProxyLifetimeShort() {
		ProxyLifetime valid = new ProxyLifetime();
		valid.setHours(0);
		valid.setMinutes(0);
		valid.setSeconds(SHORT_PROXY_VALID);
		return valid;
	}


	private ProxyLifetime getProxyLifetime() {
		ProxyLifetime valid = new ProxyLifetime();
		valid.setHours(12);
		valid.setMinutes(0);
		valid.setSeconds(0);
		return valid;
	}


	private void createAndCheckProxyLifetime(ProxyLifetime lifetime, PrivateKey key, X509Certificate[] certs,
		int delegationLength) throws Exception {
		assertNotNull(certs);
		assertEquals(2, certs.length);
		GlobusCredential cred = new GlobusCredential(key, certs);
		assertNotNull(cred);
		long max = IFSUtils.getTimeInSeconds(lifetime);
		// what is this 3 for?
		long min = max - 3;
		long timeLeft = cred.getTimeLeft();
		assertTrue(min <= timeLeft);
		assertTrue(timeLeft <= max);
		assertEquals(certs[1].getSubjectDN().toString(), identityToSubject(cred.getIdentity()));
		assertEquals(cred.getIssuer(), identityToSubject(cred.getIdentity()));
		assertEquals(delegationLength, CertificateExtensionsUtil.getDelegationPathLength(certs[0]));
		cred.verify();
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

}
