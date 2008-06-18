package org.cagrid.gaards.dorian.federation;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.xml.security.signature.XMLSignature;
import org.cagrid.gaards.dorian.ca.CertificateAuthority;
import org.cagrid.gaards.dorian.common.Lifetime;
import org.cagrid.gaards.dorian.service.PropertyManager;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidProxyFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.test.CA;
import org.cagrid.gaards.dorian.test.Utils;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.CertificateExtensionsUtil;
import org.cagrid.gaards.pki.Credential;
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.gaards.saml.encoding.SAMLConstants;
import org.cagrid.tools.database.Database;
import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdentityFederationManager extends TestCase {

	private static final int MIN_NAME_LENGTH = 4;

	private static final int MAX_NAME_LENGTH = 50;

	private static final int SHORT_PROXY_VALID = 10;

	private static final int SHORT_CREDENTIALS_VALID = 35;

	private static final int DELEGATION_LENGTH = 5;

	public final static String INITIAL_ADMIN = "admin";

	private Database db;

	private CertificateAuthority ca;

	private CA memoryCA;

	private PropertyManager props;

	public void testRequestHostCertificateManualApproval() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String host = "localhost";
			HostCertificateRequest req = getHostCertificateRequest(host);
			HostCertificateRecord record = ifs.requestHostCertificate(usr
					.getGridId(), req);
			assertEquals(HostCertificateStatus.Pending, record.getStatus());
			assertEquals(null, record.getCertificate());
			String subject = org.cagrid.gaards.dorian.common.Utils
					.getHostCertificateSubject(ca.getCACertificate(), host);
			record = ifs.approveHostCertificate(adminGridId, record.getId());
			assertEquals(HostCertificateStatus.Active, record.getStatus());
			;
			assertEquals(subject, record.getSubject());
			assertEquals(subject, CertUtil.loadCertificate(
					record.getCertificate().getCertificateAsString())
					.getSubjectDN().getName());
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

	public void testRequestHostCertificateAutoApproval() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(true);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String host = "localhost";
			HostCertificateRequest req = getHostCertificateRequest(host);
			HostCertificateRecord record = ifs.requestHostCertificate(usr
					.getGridId(), req);
			String subject = org.cagrid.gaards.dorian.common.Utils
					.getHostCertificateSubject(ca.getCACertificate(), host);
			assertEquals(HostCertificateStatus.Active, record.getStatus());
			;
			assertEquals(subject, record.getSubject());
			assertEquals(subject, CertUtil.loadCertificate(
					record.getCertificate().getCertificateAsString())
					.getSubjectDN().getName());
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

	public void testRequestHostCertificateInvalidUser() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			createUser(ifs, adminGridId, idp, "user");
			String host = "localhost";
			HostCertificateRequest req = getHostCertificateRequest(host);
			try {
				ifs.requestHostCertificate("bad user", req);
				fail("Should have failed.");
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

	public void testApproveHostCertificateInvalidUser() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String host = "localhost";
			HostCertificateRequest req = getHostCertificateRequest(host);
			HostCertificateRecord record = ifs.requestHostCertificate(usr
					.getGridId(), req);
			assertEquals(HostCertificateStatus.Pending, record.getStatus());
			assertEquals(null, record.getCertificate());

			try {
				ifs.approveHostCertificate("bad subject", record.getId());
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

	public void testFindHostCertificates() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(true);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String subjectPrefix = org.cagrid.gaards.dorian.common.Utils
					.getHostCertificateSubjectPrefix(ca.getCACertificate());
			String hostPrefix = "localhost";
			int total = 3;

			for (int i = 0; i < total; i++) {
				HostCertificateRequest req = getHostCertificateRequest(hostPrefix
						+ i);
				ifs.requestHostCertificate(usr.getGridId(), req);
			}

			// Find by Subject;
			HostCertificateFilter f1 = new HostCertificateFilter();
			f1.setSubject(subjectPrefix);
			assertEquals(total,
					ifs.findHostCertificates(adminGridId, f1).length);
			for (int i = 0; i < total; i++) {
				String subject = org.cagrid.gaards.dorian.common.Utils
						.getHostCertificateSubject(ca.getCACertificate(),
								hostPrefix + i);
				f1.setSubject(subject);
				HostCertificateRecord[] r = ifs.findHostCertificates(
						adminGridId, f1);
				assertEquals(1, r.length);
				assertEquals(subject, r[0].getSubject());
			}

			// Find by host;
			HostCertificateFilter f2 = new HostCertificateFilter();
			f2.setHost(hostPrefix);
			assertEquals(total,
					ifs.findHostCertificates(adminGridId, f2).length);
			for (int i = 0; i < total; i++) {
				String host = hostPrefix + i;
				f2.setHost(host);
				HostCertificateRecord[] r = ifs.findHostCertificates(
						adminGridId, f2);
				assertEquals(1, r.length);
				assertEquals(host, r[0].getHost());
			}

			// Find by Owner;
			HostCertificateFilter f3 = new HostCertificateFilter();
			f3.setOwner(usr.getGridId());
			assertEquals(total,
					ifs.findHostCertificates(adminGridId, f3).length);

			// Find by host;
			HostCertificateFilter f4 = new HostCertificateFilter();
			f4.setStatus(HostCertificateStatus.Active);
			assertEquals(total,
					ifs.findHostCertificates(adminGridId, f4).length);

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

	public void testFindHostCertificatesInvalidUser() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			createUser(ifs, adminGridId, idp, "user");
			try {
				ifs.findHostCertificates("bad user",
						new HostCertificateFilter());
				fail("Should have failed.");
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

	public void testUpdateHostCertificate() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(true);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String host = "localhost";
			HostCertificateRequest req = getHostCertificateRequest(host);
			HostCertificateRecord record = ifs.requestHostCertificate(usr
					.getGridId(), req);
			String subject = org.cagrid.gaards.dorian.common.Utils
					.getHostCertificateSubject(ca.getCACertificate(), host);
			assertEquals(HostCertificateStatus.Active, record.getStatus());
			;
			assertEquals(subject, record.getSubject());
			assertEquals(subject, CertUtil.loadCertificate(
					record.getCertificate().getCertificateAsString())
					.getSubjectDN().getName());
			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(record.getId());
			update.setStatus(HostCertificateStatus.Suspended);
			ifs.updateHostCertificateRecord(adminGridId, update);
			HostCertificateFilter f = new HostCertificateFilter();
			f.setId(BigInteger.valueOf(record.getId()));
			HostCertificateRecord[] r = ifs
					.findHostCertificates(adminGridId, f);
			assertEquals(1, r.length);
			assertEquals(HostCertificateStatus.Suspended, r[0].getStatus());
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

	public void testUpdateHostCertificatesInvalidUser() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			createUser(ifs, adminGridId, idp, "user");
			try {
				ifs.updateHostCertificateRecord("bad user",
						new HostCertificateUpdate());
				fail("Should have failed.");
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

	public void testUpdateInvalidOwner() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String host = "localhost";
			HostCertificateRequest req = getHostCertificateRequest(host);
			HostCertificateRecord record = ifs.requestHostCertificate(usr
					.getGridId(), req);
			try {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(record.getId());
				update.setOwner("invalid user");
				ifs.updateHostCertificateRecord(adminGridId, update);
				fail("Should have failed.");
			} catch (InvalidHostCertificateFault f) {

			}

			IFSUser usr2 = createUser(ifs, adminGridId, idp, "user2");
			usr2.setUserStatus(IFSUserStatus.Suspended);
			ifs.updateUser(adminGridId, usr2);

			try {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(record.getId());
				update.setOwner(usr2.getGridId());
				ifs.updateHostCertificateRecord(adminGridId, update);
				fail("Should have failed.");
			} catch (InvalidHostCertificateFault f) {

			}

			usr2.setUserStatus(IFSUserStatus.Active);
			ifs.updateUser(adminGridId, usr2);

			HostCertificateUpdate update = new HostCertificateUpdate();
			update.setId(record.getId());
			update.setOwner(usr2.getGridId());
			ifs.updateHostCertificateRecord(adminGridId, update);
			HostCertificateFilter f = new HostCertificateFilter();
			f.setId(BigInteger.valueOf(record.getId()));
			assertEquals(usr2.getGridId(), ifs.findHostCertificates(
					adminGridId, f)[0].getOwner());

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

	public void testGetHostCertificatesForCaller() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(true);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String hostPrefix = "localhost";
			int total = 3;

			for (int i = 0; i < total; i++) {
				HostCertificateRequest req = getHostCertificateRequest(hostPrefix
						+ i);
				ifs.requestHostCertificate(usr.getGridId(), req);
			}

			HostCertificateRecord[] r = ifs.getHostCertificatesForCaller(usr
					.getGridId());
			assertEquals(total, r.length);
			for (int i = 0; i < total; i++) {
				String host = hostPrefix + i;
				boolean found = false;
				for (int j = 0; j < r.length; j++) {
					if (host.equals(r[j].getHost())) {
						found = true;
						break;
					}
				}
				if (!found) {
					fail("A host certificate that was expected was not found.");
				}
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

	public void testGetHostCertificatesForCallerInvalidUser() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			createUser(ifs, adminGridId, idp, "user");
			try {
				ifs.getHostCertificatesForCaller("bad user");
				fail("Should have failed.");
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

	public void testHostCeritifcateStatusAfterUserRemoval() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf(false);
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			IFSUser usr = createUser(ifs, adminGridId, idp, "user");
			String host = "localhost1";
			HostCertificateRequest req = getHostCertificateRequest(host);
			HostCertificateRecord record = ifs.requestHostCertificate(usr
					.getGridId(), req);
			assertEquals(HostCertificateStatus.Pending, record.getStatus());
			assertEquals(null, record.getCertificate());
			String subject = org.cagrid.gaards.dorian.common.Utils
					.getHostCertificateSubject(ca.getCACertificate(), host);
			record = ifs.approveHostCertificate(adminGridId, record.getId());
			assertEquals(HostCertificateStatus.Active, record.getStatus());
			;
			assertEquals(subject, record.getSubject());
			assertEquals(subject, CertUtil.loadCertificate(
					record.getCertificate().getCertificateAsString())
					.getSubjectDN().getName());

			String host2 = "localhost2";
			HostCertificateRequest req2 = getHostCertificateRequest(host2);
			HostCertificateRecord record2 = ifs.requestHostCertificate(usr
					.getGridId(), req2);
			assertEquals(HostCertificateStatus.Pending, record2.getStatus());
			assertEquals(null, record2.getCertificate());

			ifs.removeUser(adminGridId, usr);

			HostCertificateFilter f = new HostCertificateFilter();
			f.setId(BigInteger.valueOf(record.getId()));

			HostCertificateRecord[] r = ifs
					.findHostCertificates(adminGridId, f);
			assertEquals(1, r.length);
			assertEquals(HostCertificateStatus.Compromised, r[0].getStatus());

			f.setId(BigInteger.valueOf(record2.getId()));
			HostCertificateRecord[] r2 = ifs.findHostCertificates(adminGridId,
					f);
			assertEquals(1, r2.length);
			assertEquals(HostCertificateStatus.Rejected, r2[0].getStatus());

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

	public void testRenewUserCredentials() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String uid = "user";
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(
					getSAMLAssertion(uid, idp), publicKey, lifetime,
					DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
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

			X509Certificate cert2 = CertUtil.loadCertificate(usr2
					.getCertificate().getCertificateAsString());

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

	public void testGetCRL() {
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);

			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);

			IdPContainer idp2 = this
					.getTrustedIdpAutoApproveAutoRenew("My IdP2");
			ifs.addTrustedIdP(adminGridId, idp2.getIdp());
			String hostPrefix = "myhost";
			int hostCount = 1;
			int totalUsers = 3;
			int userHostCerts = 2;
			int total = totalUsers + (totalUsers * userHostCerts);

			// Create users and host certificates
			List<UserContainer> list = new ArrayList();
			for (int i = 0; i < totalUsers; i++) {
				String uid = "user" + i;
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ProxyLifetime lifetime = getProxyLifetime();
				X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid,
						idp2), publicKey, lifetime, DELEGATION_LENGTH);
				createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
						DELEGATION_LENGTH);
				IFSUserFilter f1 = new IFSUserFilter();
				f1.setIdPId(idp2.getIdp().getId());
				f1.setUID(uid);
				IFSUser[] users = ifs.findUsers(adminGridId, f1);
				assertEquals(1, users.length);
				UserContainer usr = new UserContainer(users[0]);
				list.add(usr);
				for (int j = 0; j < userHostCerts; j++) {
					usr.addHostCertificate(createAndSubmitHostCert(ifs, conf,
							adminGridId, usr.getUser().getGridId(), hostPrefix
									+ hostCount));
					hostCount++;
				}
			}

			X509CRL crl = ifs.getCRL();
			assertEquals(null, crl.getRevokedCertificates());

			// Suspend IDP
			idp2.getIdp().setStatus(TrustedIdPStatus.Suspended);
			ifs.updateTrustedIdP(adminGridId, idp2.getIdp());
			crl = ifs.getCRL();
			assertEquals(total, crl.getRevokedCertificates().size());
			for (int i = 0; i < list.size(); i++) {
				X509Certificate cert = CertUtil.loadCertificate(list.get(i)
						.getUser().getCertificate().getCertificateAsString());
				assertNotNull(crl.getRevokedCertificate(cert));
				List<HostCertificateRecord> hosts = list.get(i)
						.getHostCertificates();
				for (int j = 0; j < hosts.size(); j++) {
					assertNotNull(crl.getRevokedCertificate(CertUtil
							.loadCertificate(hosts.get(j).getCertificate()
									.getCertificateAsString())));
				}
			}

			idp2.getIdp().setStatus(TrustedIdPStatus.Active);
			ifs.updateTrustedIdP(adminGridId, idp2.getIdp());

			crl = ifs.getCRL();
			assertEquals(null, crl.getRevokedCertificates());
			for (int i = 0; i < list.size(); i++) {
				X509Certificate cert = CertUtil.loadCertificate(list.get(i)
						.getUser().getCertificate().getCertificateAsString());
				assertNull(crl.getRevokedCertificate(cert));
				List<HostCertificateRecord> hosts = list.get(i)
						.getHostCertificates();
				for (int j = 0; j < hosts.size(); j++) {
					assertNull(crl.getRevokedCertificate(CertUtil
							.loadCertificate(hosts.get(j).getCertificate()
									.getCertificateAsString())));
				}
			}

			this.validateCRLOnDisabledUserStatus(ifs, list,
					IFSUserStatus.Suspended, adminGridId, userHostCerts);
			this.validateCRLOnDisabledUserStatus(ifs, list,
					IFSUserStatus.Rejected, adminGridId, userHostCerts);
			this.validateCRLOnDisabledUserStatus(ifs, list,
					IFSUserStatus.Expired, adminGridId, userHostCerts);

			assertTrue(userHostCerts >= 1);

			X509Certificate oldHostCert = CertUtil.loadCertificate(list.get(0)
					.getHostCertificates().get(0).getCertificate()
					.getCertificateAsString());
			HostCertificateRecord hcr = ifs.renewHostCertificate(adminGridId,
					list.get(0).getHostCertificates().get(0).getId());
			assertEquals(list.get(0).getHostCertificates().get(0).getId(), hcr
					.getId());
			X509Certificate newHostCert = CertUtil.loadCertificate(hcr
					.getCertificate().getCertificateAsString());
			crl = ifs.getCRL();

			assertNotNull(crl.getRevokedCertificate(oldHostCert));
			assertNull(crl.getRevokedCertificate(newHostCert));

			this.validateCRLOnDisabledHostStatus(ifs, list,
					HostCertificateStatus.Suspended, adminGridId,
					userHostCerts, 1);
			this.validateCRLOnDisabledHostStatus(ifs, list,
					HostCertificateStatus.Compromised, adminGridId,
					userHostCerts, 1);

			assertTrue(totalUsers >= 2);

			ifs.removeUser(adminGridId, list.get(0).getUser());
			crl = ifs.getCRL();
			assertNotNull(crl.getRevokedCertificate(CertUtil
					.loadCertificate(list.get(0).getUser().getCertificate()
							.getCertificateAsString())));

			X509Certificate oldCert = CertUtil.loadCertificate(list.get(1)
					.getUser().getCertificate().getCertificateAsString());
			ifs.renewUserCredentials(adminGridId, list.get(1).getUser());
			X509Certificate newCert = CertUtil.loadCertificate(list.get(1)
					.getUser().getCertificate().getCertificateAsString());
			crl = ifs.getCRL();
			assertNotNull(crl.getRevokedCertificate(oldCert));
			assertNull(crl.getRevokedCertificate(newCert));

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
		IdentityFederationManager ifs = null;
		try {
			int times = 3;
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String uidPrefix = "user";
			String adminSubject = UserManager.getUserSubject(conf
					.getIdentityAssignmentPolicy(), ca.getCACertificate()
					.getSubjectDN().getName(), idp.getIdp(), INITIAL_ADMIN);
			String adminGridId = UserManager.subjectToIdentity(adminSubject);
			int ucount = 1;
			for (int i = 0; i < times; i++) {
				String uid = uidPrefix + i;
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ProxyLifetime lifetime = getProxyLifetime();
				X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid,
						idp), publicKey, lifetime, i);
				createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
						i);
				ucount = ucount + 1;
				assertEquals(ucount, ifs.findUsers(adminGridId,
						new IFSUserFilter()).length);
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
				assertEquals(ucount, ifs.findUsers(usr[0].getGridId(),
						new IFSUserFilter()).length);
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
				assertEquals(rcount, ifs.findUsers(adminGridId,
						new IFSUserFilter()).length);
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
		IdentityFederationManager ifs = null;
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion("user",
					idp), publicKey, lifetime, DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
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
		IdentityFederationManager ifs = null;
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			idp.getIdp().setStatus(TrustedIdPStatus.Suspended);
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			try {
				ifs.createProxy(getSAMLAssertion("user", idp), publicKey,
						lifetime, DELEGATION_LENGTH);
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
		IdentityFederationManager ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			String username = "user";
			IdPContainer idp = this.getTrustedIdpAutoApprove("My IdP");
			IdentityFederationProperties conf = getExpiringCredentialsConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager
					.getUserSubject(conf.getIdentityAssignmentPolicy(), ca
							.getCACertificate().getSubjectDN().getName(), idp
							.getIdp(), INITIAL_ADMIN));

			// give a chance for others to run right before we enter timing
			// sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(
					username, idp), pair.getPublic(), lifetime,
					DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Active);

			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			try {

				PublicKey publicKey2 = pair2.getPublic();
				ifs.createProxy(getSAMLAssertion(username, idp), publicKey2,
						getProxyLifetimeShort(), DELEGATION_LENGTH);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Expired);
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
		IdentityFederationManager ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			String username = "user";
			IdPContainer idp = this.getTrustedIdpManualApprove("My IdP");
			IdentityFederationProperties conf = getExpiringCredentialsConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager
					.getUserSubject(conf.getIdentityAssignmentPolicy(), ca
							.getCACertificate().getSubjectDN().getName(), idp
							.getIdp(), defaults.getDefaultUser().getUID()));

			try {
				ifs.createProxy(getSAMLAssertion(username, idp), pair
						.getPublic(), lifetime, DELEGATION_LENGTH);
				fail("Should have thrown exception attempting to create proxy.");
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Pending);

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
		IdentityFederationManager ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			String username = "user";
			IdentityFederationProperties conf = getExpiringCredentialsConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager
					.getUserSubject(conf.getIdentityAssignmentPolicy(), ca
							.getCACertificate().getSubjectDN().getName(), idp
							.getIdp(), defaults.getDefaultUser().getUID()));

			PublicKey publicKey2 = pair2.getPublic();
			// give a chance for others to run right before we enter timing
			// sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(
					username, idp), pair.getPublic(), lifetime,
					DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Active);
			IFSUser before = ifs
					.getUser(gridId, idp.getIdp().getId(), username);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);

			certs = ifs.createProxy(getSAMLAssertion(username, idp),
					publicKey2, getProxyLifetimeShort(), DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Active);
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
		IdentityFederationManager ifs = null;
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			ProxyLifetime lifetime = getProxyLifetimeShort();
			KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
			String username = "user";
			IdPContainer idp = this
					.getTrustedIdpManualApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getExpiringCredentialsConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager
					.getUserSubject(conf.getIdentityAssignmentPolicy(), ca
							.getCACertificate().getSubjectDN().getName(), idp
							.getIdp(), defaults.getDefaultUser().getUID()));

			try {
				ifs.createProxy(getSAMLAssertion(username, idp), pair
						.getPublic(), getProxyLifetimeShort(),
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
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(
					username, idp), pair.getPublic(), lifetime,
					DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Active);
			IFSUser before = ifs
					.getUser(gridId, idp.getIdp().getId(), username);
			Thread.sleep((SHORT_CREDENTIALS_VALID * 1000) + 100);
			PublicKey publicKey2 = pair2.getPublic();
			certs = ifs.createProxy(getSAMLAssertion(username, idp),
					publicKey2, getProxyLifetimeShort(), DELEGATION_LENGTH);
			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			assertEquals(ifs.getUser(gridId, idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Active);
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
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			Thread.sleep(500);
			try {
				ProxyLifetime valid = new ProxyLifetime();
				valid.setHours(12);
				valid.setMinutes(0);
				valid.setSeconds(1);
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp), publicKey,
						valid, DELEGATION_LENGTH);
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
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertionUnspecifiedMethod("user", idp),
						publicKey, getProxyLifetime(), DELEGATION_LENGTH);
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
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdPContainer idp2 = this
					.getTrustedIdpAutoApproveAutoRenew("My IdP 2");

			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);

			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp2), publicKey,
						getProxyLifetime(), DELEGATION_LENGTH);
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
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getExpiredSAMLAssertion("user", idp),
						publicKey, getProxyLifetime(), DELEGATION_LENGTH);
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
		IdentityFederationManager ifs = null;
		try {
			IdPContainer idp0 = this
					.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getExpiringCredentialsConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp0.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			String gridId = UserManager.subjectToIdentity(UserManager
					.getUserSubject(conf.getIdentityAssignmentPolicy(), ca
							.getCACertificate().getSubjectDN().getName(), idp0
							.getIdp(), defaults.getDefaultUser().getUID()));
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
		IdentityFederationManager ifs = null;
		try {

			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdentityFederationProperties conf = getConf();
			FederationDefaults defaults = getDefaults();
			defaults.setDefaultIdP(idp.getIdp());
			ifs = new IdentityFederationManager(conf, db, props, ca, defaults);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ProxyLifetime lifetime = getProxyLifetime();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion("user",
					idp), publicKey, lifetime, DELEGATION_LENGTH);

			createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
					DELEGATION_LENGTH);
			String userId = UserManager.subjectToIdentity(certs[1]
					.getSubjectDN().toString());
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

	private void validateAccessControl(IdentityFederationManager ifs,
			String userId) throws Exception {
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

	private IdentityFederationProperties getConf() throws Exception {
		return getConf(true);
	}

	private IdentityFederationProperties getConf(
			boolean autoHostCertificateApproval) throws Exception {
		IdentityFederationProperties conf = new IdentityFederationProperties();
		conf
				.setIdentityAssignmentPolicy(org.cagrid.gaards.dorian.federation.IdentityAssignmentPolicy.NAME);
		Lifetime l = new Lifetime();
		l.setYears(1);
		l.setMonths(0);
		l.setDays(0);
		l.setHours(0);
		l.setMinutes(0);
		l.setSeconds(0);
		conf.setIssuedCertificateLifetime(l);
		conf.setAutoHostCertificateApproval(autoHostCertificateApproval);

		conf.setMinIdPNameLength(MIN_NAME_LENGTH);
		conf.setMaxIdPNameLength(MAX_NAME_LENGTH);

		Lifetime pl = new Lifetime();
		pl.setHours(12);
		pl.setMinutes(0);
		pl.setSeconds(0);
		conf.setMaxProxyLifetime(pl);
		List<AccountPolicy> policies = new ArrayList<AccountPolicy>();
		policies.add(new ManualApprovalPolicy());
		policies.add(new ManualApprovalAutoRenewalPolicy());
		policies.add(new AutoApprovalAutoRenewalPolicy());
		policies.add(new AutoApprovalPolicy());
		conf.setAccountPolicies(policies);
		return conf;
	}

	private IdentityFederationProperties getExpiringCredentialsConf()
			throws Exception {

		IdentityFederationProperties conf = new IdentityFederationProperties();
		conf
				.setIdentityAssignmentPolicy(org.cagrid.gaards.dorian.federation.IdentityAssignmentPolicy.NAME);
		Lifetime l = new Lifetime();
		l.setYears(0);
		l.setMonths(0);
		l.setDays(0);
		l.setHours(0);
		l.setMinutes(0);
		l.setSeconds(SHORT_CREDENTIALS_VALID);
		conf.setIssuedCertificateLifetime(l);
		conf.setAutoHostCertificateApproval(false);

		conf.setMinIdPNameLength(MIN_NAME_LENGTH);
		conf.setMaxIdPNameLength(MAX_NAME_LENGTH);

		Lifetime pl = new Lifetime();
		pl.setHours(12);
		pl.setMinutes(0);
		pl.setSeconds(0);
		conf.setMaxProxyLifetime(pl);
		List<AccountPolicy> policies = new ArrayList<AccountPolicy>();
		policies.add(new ManualApprovalPolicy());
		policies.add(new ManualApprovalAutoRenewalPolicy());
		policies.add(new AutoApprovalAutoRenewalPolicy());
		policies.add(new AutoApprovalPolicy());
		conf.setAccountPolicies(policies);
		return conf;
	}

	private FederationDefaults getDefaults() throws Exception {
		TrustedIdP idp = this.getTrustedIdpAutoApproveAutoRenew("Initial IdP")
				.getIdp();
		IFSUser usr = new IFSUser();
		usr.setUID(INITIAL_ADMIN);
		usr.setFirstName("Mr");
		usr.setLastName("Admin");
		usr.setEmail(INITIAL_ADMIN + "@test.com");
		usr.setUserStatus(IFSUserStatus.Active);
		return new FederationDefaults(idp, usr);
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

	private SAMLAssertion getSAMLAssertionUnspecifiedMethod(String id,
			IdPContainer idp) throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end = cal.getTime();
		return this.getSAMLAssertion(id, idp, start, end,
				"urn:oasis:names:tc:SAML:1.0:am:unspecified");
	}

	private SAMLAssertion getExpiredSAMLAssertion(String id, IdPContainer idp)
			throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.MINUTE, -1);
		Date start = cal.getTime();
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

	private IdPContainer getTrustedIdpAutoApproveAutoRenew(String name)
			throws Exception {
		return this.getTrustedIdp(name, AutoApprovalAutoRenewalPolicy.class
				.getName());
	}

	private String identityToSubject(String identity) {
		String s = identity.substring(1);
		return s.replace('/', ',');
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

	private void validateCRLOnDisabledUserStatus(IdentityFederationManager ifs,
			List<UserContainer> list, IFSUserStatus status, String adminGridId,
			int userHostCerts) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			IFSUser usr = list.get(i).getUser();
			usr.setUserStatus(status);
			ifs.updateUser(adminGridId, usr);
			X509CRL crl = ifs.getCRL();
			int sum = (i + 1) + ((i + 1) * userHostCerts);
			assertEquals(sum, crl.getRevokedCertificates().size());
			for (int j = 0; j < list.size(); j++) {
				UserContainer curr = list.get(j);
				if (j <= i) {
					assertNotNull(crl.getRevokedCertificate(CertUtil
							.loadCertificate(curr.getUser().getCertificate()
									.getCertificateAsString())));
					for (int x = 0; x < curr.getHostCertificates().size(); x++) {
						assertNotNull(crl.getRevokedCertificate(CertUtil
								.loadCertificate(curr.getHostCertificates()
										.get(x).getCertificate()
										.getCertificateAsString())));
					}
				} else {
					assertNull(crl.getRevokedCertificate(CertUtil
							.loadCertificate(curr.getUser().getCertificate()
									.getCertificateAsString())));
					for (int x = 0; x < curr.getHostCertificates().size(); x++) {
						assertNull(crl.getRevokedCertificate(CertUtil
								.loadCertificate(curr.getHostCertificates()
										.get(x).getCertificate()
										.getCertificateAsString())));
					}
				}

			}
		}

		for (int i = 0; i < list.size(); i++) {
			IFSUser usr = list.get(i).getUser();
			usr.setUserStatus(IFSUserStatus.Active);
			ifs.updateUser(adminGridId, usr);
		}
		assertEquals(null, ifs.getCRL().getRevokedCertificates());
	}

	private void validateCRLOnDisabledHostStatus(IdentityFederationManager ifs,
			List<UserContainer> list, HostCertificateStatus status,
			String adminGridId, int userHostCerts, int alreadyRevokedCerts)
			throws Exception {
		for (int i = 0; i < list.size(); i++) {
			UserContainer usr = list.get(i);
			for (int j = 0; j < usr.getHostCertificates().size(); j++) {
				HostCertificateUpdate update = new HostCertificateUpdate();
				update.setId(usr.getHostCertificates().get(j).getId());
				update.setStatus(status);
				ifs.updateHostCertificateRecord(adminGridId, update);
			}
			X509CRL crl = ifs.getCRL();
			int sum = ((i + 1) * userHostCerts) + alreadyRevokedCerts;
			assertEquals(sum, crl.getRevokedCertificates().size());
			for (int j = 0; j < list.size(); j++) {
				UserContainer curr = list.get(j);
				assertNull(crl.getRevokedCertificate(CertUtil
						.loadCertificate(curr.getUser().getCertificate()
								.getCertificateAsString())));
				for (int x = 0; x < curr.getHostCertificates().size(); x++) {
					if (j <= i) {
						assertNotNull(crl.getRevokedCertificate(CertUtil
								.loadCertificate(curr.getHostCertificates()
										.get(x).getCertificate()
										.getCertificateAsString())));
					} else {
						assertNull(crl.getRevokedCertificate(CertUtil
								.loadCertificate(curr.getHostCertificates()
										.get(x).getCertificate()
										.getCertificateAsString())));
					}
				}
			}
		}
		if (!status.equals(HostCertificateStatus.Compromised)) {
			for (int i = 0; i < list.size(); i++) {
				UserContainer usr = list.get(i);
				for (int j = 0; j < usr.getHostCertificates().size(); j++) {
					HostCertificateUpdate update = new HostCertificateUpdate();
					update.setId(usr.getHostCertificates().get(j).getId());
					update.setStatus(HostCertificateStatus.Active);
					ifs.updateHostCertificateRecord(adminGridId, update);
				}
			}
			if (alreadyRevokedCerts > 0) {
				assertEquals(alreadyRevokedCerts, ifs.getCRL()
						.getRevokedCertificates().size());
			} else {
				assertEquals(null, ifs.getCRL().getRevokedCertificates());
			}
		}
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

	private IFSUser createUser(IdentityFederationManager ifs,
			String adminGridId, IdPContainer idp, String uid) throws Exception {
		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		PublicKey publicKey = pair.getPublic();
		ProxyLifetime lifetime = getProxyLifetime();
		X509Certificate[] certs = ifs.createProxy(getSAMLAssertion(uid, idp),
				publicKey, lifetime, DELEGATION_LENGTH);
		createAndCheckProxyLifetime(lifetime, pair.getPrivate(), certs,
				DELEGATION_LENGTH);
		IFSUserFilter f1 = new IFSUserFilter();
		f1.setIdPId(idp.getIdp().getId());
		f1.setUID(uid);
		IFSUser[] users = ifs.findUsers(adminGridId, f1);
		assertEquals(1, users.length);
		return users[0];
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			ca = Utils.getCA();
			memoryCA = new CA(Utils.getCASubject());
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

	private HostCertificateRecord createAndSubmitHostCert(
			IdentityFederationManager ifs, IdentityFederationProperties conf,
			String admin, String owner, String host) throws Exception {
		HostCertificateRequest req = getHostCertificateRequest(host);
		HostCertificateRecord record = ifs.requestHostCertificate(owner, req);
		if (!conf.autoHostCertificateApproval()) {
			assertEquals(HostCertificateStatus.Pending, record.getStatus());
			record = ifs.approveHostCertificate(admin, record.getId());
		}
		assertEquals(HostCertificateStatus.Active, record.getStatus());
		assertEquals(host, record.getHost());
		return record;
	}

	private HostCertificateRequest getHostCertificateRequest(String host)
			throws Exception {
		KeyPair pair = KeyUtil.generateRSAKeyPair(ca.getProperties()
				.getIssuedCertificateKeySize());
		HostCertificateRequest req = new HostCertificateRequest();
		req.setHostname(host);
		org.cagrid.gaards.dorian.federation.PublicKey key = new org.cagrid.gaards.dorian.federation.PublicKey();
		key.setKeyAsString(KeyUtil.writePublicKey(pair.getPublic()));
		req.setPublicKey(key);
		return req;
	}

	private void createAndCheckProxyLifetime(ProxyLifetime lifetime,
			PrivateKey key, X509Certificate[] certs, int delegationLength)
			throws Exception {
		assertNotNull(certs);
		assertEquals(2, certs.length);
		GlobusCredential cred = new GlobusCredential(key, certs);
		assertNotNull(cred);
		long max = FederationUtils.getTimeInSeconds(lifetime);
		// what is this 3 for?
		long min = max - 3;
		long timeLeft = cred.getTimeLeft();
		assertTrue(min <= timeLeft);
		assertTrue(timeLeft <= max);
		assertEquals(certs[1].getSubjectDN().toString(), identityToSubject(cred
				.getIdentity()));
		assertEquals(cred.getIssuer(), identityToSubject(cred.getIdentity()));
		assertEquals(delegationLength, CertificateExtensionsUtil
				.getDelegationPathLength(certs[0]));
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

	public class UserContainer {
		private IFSUser usr;
		private List<HostCertificateRecord> hostCertificates;

		public UserContainer(IFSUser usr) {
			this.usr = usr;
			this.hostCertificates = new ArrayList<HostCertificateRecord>();
		}

		public IFSUser getUser() {
			return usr;
		}

		public List<HostCertificateRecord> getHostCertificates() {
			return hostCertificates;
		}

		public void addHostCertificate(HostCertificateRecord record) {
			hostCertificates.add(record);
		}

	}

}
