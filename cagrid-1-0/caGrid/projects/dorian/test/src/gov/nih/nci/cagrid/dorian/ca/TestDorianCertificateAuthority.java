package gov.nih.nci.cagrid.dorian.ca;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.conf.AutoCreate;
import gov.nih.nci.cagrid.dorian.conf.CredentialLifetime;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthorityFault;
import gov.nih.nci.cagrid.dorian.service.ca.CredentialsManager;
import gov.nih.nci.cagrid.dorian.service.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.NoCACredentialsFault;
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


	private CertificateAuthority getCertificateAuthority(DorianCAConfiguration conf) throws Exception{
		return new DorianCertificateAuthority(db, conf);
		//conf.setCertificateAuthorityPassword("password");
		//return new EracomCertificateAuthority(conf);
	}


	public void testNoCACredentials() {
		DorianCAConfiguration conf = this.getDorianCAConfNoAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
			try {
				ca.getCACertificate();
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
			assertEquals(0, db.getUsedConnectionCount());
			try {
				GregorianCalendar cal = new GregorianCalendar();
				Date start = cal.getTime();
				cal.add(Calendar.DAY_OF_MONTH, 5);
				Date end = cal.getTime();
				submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
				assertEquals(0, db.getUsedConnectionCount());
				assertTrue(false);
			} catch (NoCACredentialsFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testNoCACredentialsWithAutoRenew() {
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();

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
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testSetCACredentials() {
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
			// ca.destroyTable();
			createAndStoreCA(ca);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testAutoCreateCA() {
		DorianCAConfiguration conf = this.getDorianCAConfAutoCreateAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
			assertFalse(ca.hasCredentials(CertificateAuthority.CA_ALIAS));
			X509Certificate cert = ca.getCACertificate();
			assertTrue(ca.hasCredentials(CertificateAuthority.CA_ALIAS));
			assertEquals(conf.getAutoCreate().getCASubject(), cert.getSubjectDN().getName());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testRequestCertificate() {
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
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
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testRequestCertificateBadDate() {
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
			createAndStoreCA(ca);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.YEAR, 5);
			Date end = cal.getTime();
			submitCertificateRequest(ca, SUBJECT_PREFIX, start, end);
			assertTrue(false);
		} catch (CertificateAuthorityFault f) {
			if (f.getFaultString().indexOf("Certificate expiration date is after the CA certificates expiration date") == -1) {

				FaultUtil.printFault(f);
				assertTrue(false);
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}




	public void testExpiredCACredentialsWithRenewal() {
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
			// give a chance for others to run right before we enter timing
			// sensitive code
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
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testExpiredCACredentialsNoRenewal() {
		DorianCAConfiguration conf = getDorianCAConfNoAutoRenewalLong();
		CertificateAuthority ca = null;
		try {
			ca = getCertificateAuthority(conf);
			ca.clearCertificateAuthority();
			// ca.destroyTable();
			// give a chance for others to run right before we enter timing
			// sensitive code
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
				ca.clearCertificateAuthority();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private DorianCAConfiguration getDorianCAConfAutoRenewalLong() {
		DorianCAConfiguration conf = new DorianCAConfiguration();
		conf.setCertificateAuthorityPassword("password");
		CredentialLifetime lifetime = new CredentialLifetime();
		lifetime.setYears(5);
		lifetime.setMonths(0);
		lifetime.setDays(0);
		conf.setAutoRenewal(lifetime);
		return conf;
	}
	
	private DorianCAConfiguration getDorianCAConfAutoCreateAutoRenewalLong() {
		DorianCAConfiguration conf = new DorianCAConfiguration();
		conf.setCertificateAuthorityPassword("password");
		CredentialLifetime lifetime = new CredentialLifetime();
		lifetime.setYears(5);
		lifetime.setMonths(0);
		lifetime.setDays(0);
		conf.setAutoRenewal(lifetime);
		AutoCreate auto = new AutoCreate();
		auto.setLifetime(lifetime);
		auto.setCASubject(SUBJECT_PREFIX + "Temp Certificate Authority");
		conf.setAutoCreate(auto);
		return conf;
	}


	private DorianCAConfiguration getDorianCAConfNoAutoRenewalLong() {
		DorianCAConfiguration conf = new DorianCAConfiguration();
		conf.setCertificateAuthorityPassword("password");
		return conf;
	}


	private void createAndStoreCA(CertificateAuthority ca) throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024(ca.getProvider());
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(ca.getProvider(),rootSubject, start, end, rootPair,ca.getSignatureAlgorithm());
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);
		assertEquals(r, root);
	}


	private X509Certificate createAndStoreCAShort(CertificateAuthority ca) throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024(ca.getProvider());
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.SECOND, 5);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(ca.getProvider(),rootSubject, start, end, rootPair,ca.getSignatureAlgorithm());
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);
		assertEquals(r, root);
		return r;
	}


	private void submitCertificateRequest(CertificateAuthority ca, String prefix, Date start, Date end)
		throws Exception {
		String cn = "User";
		String subject = prefix + cn;
		
		//PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(ca.getProvider(),subject, KeyUtil.generateRSAKeyPair1024(ca.getProvider()),ca.getSignatureAlgorithm());
		//assertNotNull(req);
		//X509Certificate cert = ca.requestCertificate(req, start, end);
		//assertEquals(0, db.getUsedConnectionCount());
		ca.createCredentials(cn, cn, null, start, end);
		X509Certificate cert = ca.getCertificate(cn);
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			CredentialsManager.CREDENTIALS_TABLE = TABLE;
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
