package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.IFS;
import gov.nih.nci.cagrid.dorian.service.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.dorian.service.ifs.IFSUtils;
import gov.nih.nci.cagrid.dorian.service.ifs.ManualApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.ManualApprovalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.UserManager;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
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

	private static final int SHORT_PROXY_VALID = 2;

	private static final int SHORT_CREDENTIALS_VALID = 7;

	public final static String EMAIL_NAMESPACE = "http://cagrid.nci.nih.gov/email";

	public final static String EMAIL_NAME = "email";

	public final static String INITIAL_ADMIN = "admin";

	private Database db;

	private CertificateAuthority ca;


	public void testRenewUserCredentials() {
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String uid = "user";
			String adminSubject = UserManager.getUserSubject(ca.getCACertificate().getSubjectDN().getName(), idp
				.getIdp().getId(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid, idp), publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
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
		}

	}


	public void testFindRemoveUpdateUsers() {
		try {
			int times = 3;
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String uidPrefix = "user";
			String adminSubject = UserManager.getUserSubject(ca.getCACertificate().getSubjectDN().getName(), idp
				.getIdp().getId(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			int ucount = 1;
			for (int i = 0; i < times; i++) {
				String uid = uidPrefix + i;
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ProxyLifetime lifetime = getProxyLifetime();
				X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid, idp), publicKey, lifetime);
				createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
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
				usr[0].setUserRole(IFSUserRole.Administrator);
				ifs.updateUser(adminGridId, usr[0]);
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
		}

	}


	public void testCreateProxy() {
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion("user", idp), publicKey, lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}

	}


	public void testCreateProxySuspendedIdP() {
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			idp.getIdp().setStatus(TrustedIdPStatus.Suspended);
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			try {
				ifs.createProxy(getSAMLAssertion("user", idp), publicKey, lifetime);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}

	}


	public void testCreateProxyAutoApproval() {
		try {
			String username = "user";
			IdPContainer idp = this.getTrustedIdpAutoApprove("My IdP");
			IFSConfiguration conf = getExpiringCredentialsConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(ca.getCACertificate()
				.getSubjectDN().getName(), idp.getIdp().getId(), conf.getInitialUser().getUID()));
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			//give a chance for others to run right before we enter timing sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);

			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			try {
				KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey2 = pair2.getPublic();
				ifs.createProxy(getSAMLAssertion(username, idp), publicKey2, getProxyLifetimeShort());
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Expired);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyManualApproval() {
		try {
			String username = "user";
			IdPContainer idp = this.getTrustedIdpManualApprove("My IdP");
			IFSConfiguration conf = getExpiringCredentialsConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(ca.getCACertificate()
				.getSubjectDN().getName(), idp.getIdp().getId(), conf.getInitialUser().getUID()));
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			try {
				ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Pending);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyAutoApprovalAutoRenewal() {
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			String username = "user";
			IFSConfiguration conf = getExpiringCredentialsConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(ca.getCACertificate()
				.getSubjectDN().getName(), idp.getIdp().getId(), conf.getInitialUser().getUID()));
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			//give a chance for others to run right before we enter timing sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser before = ifs.getUser(gridId, idp.getIdp().getId(), username);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			certs = ifs.createProxy(getSAMLAssertion(username, idp), publicKey2, getProxyLifetimeShort());
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser after = ifs.getUser(gridId, idp.getIdp().getId(), username);
			if (before.getCertificate().equals(after.getCertificate())) {
				fail("Credentials were the same when should have been auto-renewed.");
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyManualApprovalAutoRenewal() {
		try {
			String username = "user";
			IdPContainer idp = this.getTrustedIdpManualApproveAutoRenew("My IdP");
			IFSConfiguration conf = getExpiringCredentialsConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(ca.getCACertificate()
				.getSubjectDN().getName(), idp.getIdp().getId(), conf.getInitialUser().getUID()));
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();

			try {
				ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), getProxyLifetimeShort());
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault f) {

			}
			IFSUser usr = ifs.getUser(gridId, idp.getIdp().getId(), username);
			usr.setUserStatus(IFSUserStatus.Active);
			ifs.updateUser(gridId, usr);
			ProxyLifetime lifetime = getProxyLifetimeShort();
			//give a chance for others to run right before we enter timing sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(username, idp), pair.getPublic(), lifetime);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser before = ifs.getUser(gridId, idp.getIdp().getId(), username);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey2 = pair2.getPublic();
			certs = ifs.createProxy(getSAMLAssertion(username, idp), publicKey2, getProxyLifetimeShort());
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username).getUserStatus(), IFSUserStatus.Active);
			IFSUser after = ifs.getUser(gridId, idp.getIdp().getId(), username);
			if (before.getCertificate().equals(after.getCertificate())) {
				fail("Certificate should have been renewed, but was not.");
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyInvalidProxyValid() {
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			Thread.sleep(500);
			try {
				ProxyLifetime valid = new ProxyLifetime();
				valid.setHours(12);
				valid.setMinutes(0);
				valid.setSeconds(1);
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp), publicKey, valid);
				fail("Should have thrown an exception creating an invalid proxy.");
			} catch (InvalidProxyFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyInvalidAuthenticationMethod() {
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertionUnspecifiedMethod("user", idp), publicKey, getProxyLifetime());
				fail("Should have thrown an exception creating a proxy with an invalid SAML assertion.");
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyUntrustedIdP() {
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdPContainer idp2 = this.getTrustedIdpAutoApproveAutoRenew("My IdP 2");

			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp2), publicKey, getProxyLifetime());
				fail("Should have thrown an exception creating a proxy with an invalid SAML assertion.");
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testCreateProxyExpiredAssertion() {
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getConf();
			conf.setInitalTrustedIdP(idp.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getExpiredSAMLAssertion("user", idp), publicKey, getProxyLifetime());
				fail("Should have thrown an exception creating a proxy with an invalid SAML assertion.");
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}


	public void testGetTrustedIdPs() {
		try {
			IdPContainer idp0 = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IFSConfiguration conf = getExpiringCredentialsConf();
			conf.setInitalTrustedIdP(idp0.getIdp());
			IFS ifs = new IFS(conf, db, ca);
			String gridId = UserManager.subjectToIdentity(UserManager.getUserSubject(ca.getCACertificate()
				.getSubjectDN().getName(), idp0.getIdp().getId(), conf.getInitialUser().getUID()));
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
				IdPContainer updateCont = getTrustedIdpManualApproveAutoRenew(updatedName);
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
		}
	}


	private IFSConfiguration getConf() throws Exception {
		IFSConfiguration conf = new IFSConfiguration();
		conf.setCredentialsValidYears(1);
		conf.setCredentialsValidMonths(0);
		conf.setCredentialsValidDays(0);
		conf.setCredentialsValidHours(0);
		conf.setCredentialsValidMinutes(0);
		conf.setCredentialsValidSeconds(0);
		conf.setMinimumIdPNameLength(MIN_NAME_LENGTH);
		conf.setMaximumIdPNameLength(MAX_NAME_LENGTH);
		conf.setMaxProxyLifetimeHours(12);
		conf.setMaxProxyLifetimeMinutes(0);
		conf.setMaxProxyLifetimeSeconds(0);
		conf.setUserPolicies(Utils.getUserPolicies());
		TrustedIdP idp = this.getTrustedIdpAutoApproveAutoRenew("Initial IdP").getIdp();
		IFSUser usr = new IFSUser();
		usr.setUID(INITIAL_ADMIN);
		usr.setEmail(INITIAL_ADMIN + "@test.com");
		usr.setUserStatus(IFSUserStatus.Active);
		usr.setUserRole(IFSUserRole.Administrator);
		conf.setInitalTrustedIdP(idp);
		conf.setInitialUser(usr);
		return conf;
	}


	private IFSConfiguration getExpiringCredentialsConf() throws Exception {
		IFSConfiguration conf = new IFSConfiguration();
		conf.setCredentialsValidYears(0);
		conf.setCredentialsValidMonths(0);
		conf.setCredentialsValidDays(0);
		conf.setCredentialsValidHours(0);
		conf.setCredentialsValidMinutes(0);
		conf.setCredentialsValidSeconds(SHORT_CREDENTIALS_VALID);
		conf.setMinimumIdPNameLength(MIN_NAME_LENGTH);
		conf.setMaximumIdPNameLength(MAX_NAME_LENGTH);
		conf.setMaxProxyLifetimeHours(12);
		conf.setMaxProxyLifetimeMinutes(0);
		conf.setMaxProxyLifetimeSeconds(0);
		conf.setUserPolicies(Utils.getUserPolicies());
		TrustedIdP idp = this.getTrustedIdpAutoApproveAutoRenew("Initial IdP").getIdp();
		IFSUser usr = new IFSUser();
		usr.setUID("inital_admin");
		usr.setEmail("inital_admin@test.com");
		usr.setUserStatus(IFSUserStatus.Active);
		usr.setUserRole(IFSUserRole.Administrator);
		conf.setInitalTrustedIdP(idp);
		conf.setInitialUser(usr);
		return conf;
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
			QName name = new QName(EMAIL_NAMESPACE, EMAIL_NAME);
			List vals = new ArrayList();
			vals.add(email);
			SAMLAttribute att = new SAMLAttribute(name.getLocalPart(), name.getNamespaceURI(), name, 0, vals);

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
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
		//return the thread to normal priority for those tests which raise the thread priority
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


	private void createAndCheckProxyLifetime(ProxyLifetime lifetime, PrivateKey key, X509Certificate[] certs)
		throws Exception {
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
