package gov.nih.nci.cagrid.gums.ca;

import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGUMSCertificateAuthority extends TestCase {
	private static final String DB = "TEST_GUMS";

	private static final String TABLE = "TEST_GUMS_CA";

	private static final String SUBJECT_PREFIX = "O=Ohio State University,OU=BMI,OU=MSCL,CN=";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";

	private Database db;

	public void testNoCACredentials() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfNoAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
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
				submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testNoCACredentialsWithAutoRenew() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
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
				submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testSetCACredentials() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
			createAndStoreCA(ca);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testRequestCertificate() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testRequestCertificateBadDate() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.YEAR, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
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
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testRequestCertificateBadSubject() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, "O=OSU,OU=BMI,OU=MSCL,CN=foo",start, end);
			assertTrue(false);
		} catch (CertificateAuthorityFault f) {
			if (f
					.getFaultString()
					.indexOf(
							"Invalid certificate subject") == -1) {

				FaultUtil.printFault(f);
				assertTrue(false);
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testExpiredCACredentialsWithRenewal() {
		try {
			GUMSCertificateAuthorityConf conf = this
					.getGumsCAConfAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
			X509Certificate origRoot = createAndStoreCAShort(ca);
			Thread.sleep(2000);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			assertNotSame(origRoot,ca.getCACertificate());
			submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testExpiredCACredentialsNoRenewal() {
		try {
			GUMSCertificateAuthorityConf conf = getGumsCAConfNoAutoRenewalLong();
			GUMSCertificateAuthority ca = new GUMSCertificateAuthority(db, conf);
			ca.destroyTable();
			createAndStoreCAShort(ca);
			Thread.sleep(2000);
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
				submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private GUMSCertificateAuthorityConf getGumsCAConfAutoRenewalLong() {
		GUMSCertificateAuthorityConf conf = new GUMSCertificateAuthorityConf();
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
	


	private GUMSCertificateAuthorityConf getGumsCAConfNoAutoRenewalLong() {
		GUMSCertificateAuthorityConf conf = new GUMSCertificateAuthorityConf();
		conf.setCaPassword("password");
		conf.setAutoRenewal(false);
		return conf;
	}

	private void createAndStoreCA(GUMSCertificateAuthority ca) throws Exception {
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
	
	private X509Certificate createAndStoreCAShort(GUMSCertificateAuthority ca) throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.SECOND, 1);
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

	private void submitCertificateRequest(GUMSCertificateAuthority ca, String prefix,
			Date start, Date end) throws Exception {
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
			Document doc = XMLUtilities.fileNameToDocument(DB_CONFIG);
			ConnectionManager cm = new ConnectionManager(doc.getRootElement());
			db = new Database(cm, DB);
			db.destroyDatabase();
			db.createDatabaseIfNeeded();
			GUMSCertificateAuthority.CA_TABLE = TABLE;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
