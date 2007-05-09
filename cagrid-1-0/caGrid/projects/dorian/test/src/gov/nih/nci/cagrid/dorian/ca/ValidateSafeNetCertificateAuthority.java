package gov.nih.nci.cagrid.dorian.ca;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.conf.CredentialLifetime;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthorityFault;
import gov.nih.nci.cagrid.dorian.service.ca.NoCACredentialsFault;
import gov.nih.nci.cagrid.dorian.service.ca.SafeNetCertificateAuthority;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;

import au.com.eracom.crypto.provider.ERACOMProvider;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class ValidateSafeNetCertificateAuthority extends TestCase {
	private static final String SUBJECT_PREFIX = "O=Ohio State University,OU=BMI,OU=MSCL,CN=";
	private Provider provider;

	public void testNoCACredentials() {
		SafeNetCertificateAuthority ca = null;
		try {
			DorianCAConfiguration conf = this
					.getDorianCAConfNoAutoRenewalLong();
			ca = new SafeNetCertificateAuthority(conf);

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
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
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
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
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
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
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
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
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
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
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
		DorianCAConfiguration conf = this.getDorianCAConfAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
			ca.clearDatabase();
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
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testExpiredCACredentialsNoRenewal() {
		DorianCAConfiguration conf = getDorianCAConfNoAutoRenewalLong();
		SafeNetCertificateAuthority ca = null;
		try {
			ca = new SafeNetCertificateAuthority(conf);
			ca.clearDatabase();
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
				ca.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private DorianCAConfiguration getDorianCAConfAutoRenewalLong() {
		DorianCAConfiguration conf = new DorianCAConfiguration();
		conf.setCertificateAuthorityPassword("caGrid@bmi");
		CredentialLifetime lifetime = new CredentialLifetime();
		lifetime.setYears(5);
		lifetime.setMonths(0);
		lifetime.setDays(0);
		conf.setAutoRenewal(lifetime);
		return conf;
	}

	private DorianCAConfiguration getDorianCAConfNoAutoRenewalLong() {
		DorianCAConfiguration conf = new DorianCAConfiguration();
		conf.setCertificateAuthorityPassword("caGrid@bmi");
		return conf;
	}

	private void createAndStoreCA(SafeNetCertificateAuthority ca)
			throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024(provider.getName());
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(provider
				.getName(), rootSubject, start, end, rootPair,SafeNetCertificateAuthority.SIGNATURE_ALGORITHM);
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);		
		//assertEquals(r.getSubjectDN().getName(), root.getSubjectDN().getName());

	}

	private X509Certificate createAndStoreCAShort(SafeNetCertificateAuthority ca)
			throws Exception {
		KeyPair rootPair = KeyUtil.generateRSAKeyPair1024(provider.getName());
		assertNotNull(rootPair);
		String rootSub = SUBJECT_PREFIX + "Temp Certificate Authority";
		X509Name rootSubject = new X509Name(rootSub);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.SECOND, 5);
		Date end = cal.getTime();
		X509Certificate root = CertUtil.generateCACertificate(provider
				.getName(), rootSubject, start, end, rootPair,SafeNetCertificateAuthority.SIGNATURE_ALGORITHM);
		assertNotNull(root);
		ca.setCACredentials(root, rootPair.getPrivate());
		X509Certificate r = ca.getCACertificate();
		assertNotNull(r);
		//assertEquals(r.getSubjectDN().getName(), root.getSubjectDN().getName());
		return r;
	}

	private void submitCertificateRequest(CertificateAuthority ca,
			String prefix, Date start, Date end) throws Exception {
		String subject = prefix + "User";
		PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(provider.getName(),
				subject, KeyUtil.generateRSAKeyPair1024(provider.getName()),SafeNetCertificateAuthority.SIGNATURE_ALGORITHM);
		assertNotNull(req);
		X509Certificate cert = ca.requestCertificate(req, start, end);
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
	}

	protected void setUp() throws Exception {
		super.setUp();
		provider = new ERACOMProvider();
		Security.addProvider(provider);
	}

	protected void tearDown() throws Exception {
		super.setUp();
	}

}
