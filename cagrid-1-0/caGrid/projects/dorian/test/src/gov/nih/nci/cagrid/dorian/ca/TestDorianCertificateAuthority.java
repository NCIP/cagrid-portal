package gov.nih.nci.cagrid.dorian.ca;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.test.Constants;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.InputStream;
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
public class TestDorianCertificateAuthority extends TestCase {
	private static final String DB = "TEST_DORIAN";

	private static final String TABLE = "TEST_DORIAN_CA";

	private static final String SUBJECT_PREFIX = "O=Ohio State University,OU=BMI,OU=MSCL,CN=";

	private Database db;

	public void testNoCACredentials() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfNoAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
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
		}
	}

	public void testNoCACredentialsWithAutoRenew() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
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
		}
	}

	public void testSetCACredentials() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
			createAndStoreCA(ca);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testRequestCertificate() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testRequestCertificateBadDate() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
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
		}
	}
	
	public void testRequestCertificateBadSubject() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
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
		}
	}
	
	public void testExpiredCACredentialsWithRenewal() {
		try {
			DorianCertificateAuthorityConf conf = this
					.getDorianCAConfAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
			X509Certificate origRoot = createAndStoreCAShort(ca);
			Thread.sleep(2500);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, 5);
			Date end = cal.getTime();
			assertNotSame(origRoot,ca.getCACertificate());
			submitCertificateRequest(ca, SUBJECT_PREFIX,start, end);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}
	
	public void testExpiredCACredentialsNoRenewal() {
		try {
			DorianCertificateAuthorityConf conf = getDorianCAConfNoAutoRenewalLong();
			DorianCertificateAuthority ca = new DorianCertificateAuthority(db, conf);
			//ca.destroyTable();
			createAndStoreCAShort(ca);
			Thread.sleep(2500);
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

	private void createAndStoreCA(DorianCertificateAuthority ca) throws Exception {
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
	
	private X509Certificate createAndStoreCAShort(DorianCertificateAuthority ca) throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024();
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.SECOND, 2);
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

	private void submitCertificateRequest(DorianCertificateAuthority ca, String prefix,
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
			InputStream resource = TestCase.class.getResourceAsStream(Constants.DB_CONFIG);
			Document doc = XMLUtilities.streamToDocument(resource);
			ConnectionManager cm = new ConnectionManager(doc.getRootElement());
			db = new Database(cm, DB);
			db.destroyDatabase();
			db.createDatabaseIfNeeded();
			assertEquals(0,db.getUsedConnectionCount());
			DorianCertificateAuthority.CA_TABLE = TABLE;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	
	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0,db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
