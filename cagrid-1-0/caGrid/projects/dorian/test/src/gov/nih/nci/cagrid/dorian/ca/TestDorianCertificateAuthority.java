package gov.nih.nci.cagrid.dorian.ca;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestDorianCertificateAuthority extends TestCase {
	private static final String TABLE = "test_dorian_ca";

	private static final String SUBJECT_PREFIX = "O=Ohio State University,OU=BMI,OU=MSCL,CN=";

	private Database db;

	public void testNoCACredentials() {

		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfNoAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
			try {
				ca.getCACertificate();
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
			try {
				GregorianCalendar cal = new GregorianCalendar();
				Date start = cal.getTime();
				cal.add(Calendar.DAY_OF_MONTH, 5);
				Date end = cal.getTime();
				submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testNoCACredentialsWithAutoRenew() {
		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();

			try {
				ca.getCACertificate();
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
			try {
				GregorianCalendar cal = new GregorianCalendar();
				Date start = cal.getTime();
				cal.add(Calendar.DAY_OF_MONTH, 5);
				Date end = cal.getTime();
				submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testSetCACredentials() {
		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
			// ca.destroyTable();
			createAndStoreCA(ca);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testRequestCertificate() {
		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
			// ca.destroyTable();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testRequestCertificateBadDate() {
		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.YEAR, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
			assertTrue(false);
		} catch (CertificateAuthorityFault f) {
			if (f
					.getFaultString()
					.indexOf(
							"Certificate expiration date is after the CA certificates expiration date") == -1) {

				FaultUtil.printFault(f);
				assertTrue(false);
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testRequestCertificateBadSubject() {
		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, "O=OSU,OU=BMI,OU=MSCL,CN=foo", start,
					end);
			assertTrue(false);
		} catch (CertificateAuthorityFault f) {
			if (f.getFaultString().indexOf("Invalid certificate subject") == -1) {

				FaultUtil.printFault(f);
				assertTrue(false);
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testExpiredCACredentialsWithRenewal() {
		DorianCertificateAuthorityConf conf = this
				.getDorianCAConfAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
//			give a chance for others to run right before we enter timing sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			X509Certificate origRoot = createAndStoreCAShort(ca);
			Thread.sleep(5500);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			assertNotSame(origRoot, ca.getCACertificate());
			submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testExpiredCACredentialsNoRenewal() {
		DorianCertificateAuthorityConf conf = getDorianCAConfNoAutoRenewalLong();
		DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
		try {
			ca.clearDatabase();
			// ca.destroyTable();
//			give a chance for others to run right before we enter timing sensitive code
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			Thread.currentThread().yield();
			createAndStoreCAShort(ca);
			Thread.sleep(5500);
			try {
				ca.getCACertificate();
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}

			try {
				GregorianCalendar cal = new GregorianCalendar();
				Date start = cal.getTime();
				cal.add(Calendar.DAY_OF_MONTH, 5);
				Date end = cal.getTime();
				submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private DorianCertificateAuthorityConf getDorianCAConfAutoRenewalLong() {
		DorianCertificateAuthorityConf conf = new DorianCertificateAuthorityConf();
		conf.setCaPassword("password");
		conf.setAutoRenewal(true);
		conf.setAutoRenewalYears(5);
		conf.setAutoRenewalMonths(0);
		conf.setAutoRenewalDays(0);
		conf.setAutoRenewalHours(0);
		conf.setAutoRenewalMinutes(0);
		conf.setAutoRenewalSeconds(0);
		return conf;
	}

	private DorianCertificateAuthorityConf getDorianCAConfNoAutoRenewalLong() {
		DorianCertificateAuthorityConf conf = new DorianCertificateAuthorityConf();
		conf.setCaPassword("password");
		conf.setAutoRenewal(false);
		return conf;
	}

	private void createAndStoreCA(DorianCertificateAuthority ca)
			throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(rootSubject,
				start, end, rootPair);
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);
		assertEquals(r, root);
	}

	private X509Certificate createAndStoreCAShort(DorianCertificateAuthority ca)
			throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.SECOND, 5);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(rootSubject,
				start, end, rootPair);
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);
		assertEquals(r, root);
		return r;
	}

	private void submitCertificateRequest(DorianCertificateAuthority ca,
			String prefix, Date start, Date end) throws Exception {
		String subject = prefix + "User";
		PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(
				subject, KeyUtil.generateRSAKeyPair1024());
		assertNotNull(req);
		X509Certificate cert = ca.requestCertificate(req, start, end);
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			DorianCertificateAuthority.CA_TABLE = TABLE;
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
