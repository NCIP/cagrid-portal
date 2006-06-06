package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.service.idp.AssertionCredentialsManager;
import gov.nih.nci.cagrid.dorian.service.idp.IdPConfiguration;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.test.Constants;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.InvalidCryptoException;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLStatement;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import junit.framework.TestCase;

import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestAssertionCredentialsManager extends TestCase {

	private Database db;

	private CertificateAuthority ca;

	private static String TEST_EMAIL = "test@test.com";
	private static String TEST_UID = "test";


	public void verifySAMLAssertion(SAMLAssertion saml, AssertionCredentialsManager cm) throws Exception {
		assertNotNull(saml);
		saml.verify(cm.getIdPCertificate());

		try {
			// Test against a bad certificate
			InputStream resource = TestCase.class.getResourceAsStream(Constants.BMI_CACERT);
			saml.verify(CertUtil.loadCertificate(resource));
			assertTrue(false);
		} catch (InvalidCryptoException ex) {

		}
		assertEquals(cm.getIdPCertificate().getSubjectDN().toString(), saml.getIssuer());
		Iterator itr = saml.getStatements();
		int count = 0;
		boolean emailFound = false;
		boolean authFound = false;
		while (itr.hasNext()) {
			count = count + 1;
			SAMLStatement stmt = (SAMLStatement) itr.next();
			if (stmt instanceof SAMLAuthenticationStatement) {
				if (authFound) {
					assertTrue(false);
				} else {
					authFound = true;
				}
				SAMLAuthenticationStatement auth = (SAMLAuthenticationStatement) stmt;
				assertEquals(TEST_UID, auth.getSubject().getNameIdentifier().getName());
				assertEquals("urn:oasis:names:tc:SAML:1.0:am:password", auth.getAuthMethod());
			}

			if (stmt instanceof SAMLAttributeStatement) {
				if (emailFound) {
					assertTrue(false);
				} else {
					emailFound = true;
				}
				SAMLAttributeStatement att = (SAMLAttributeStatement) stmt;
				assertEquals(TEST_UID, att.getSubject().getNameIdentifier().getName());
				Iterator i = att.getAttributes();
				assertTrue(i.hasNext());
				SAMLAttribute a = (SAMLAttribute) i.next();
				assertEquals(AssertionCredentialsManager.EMAIL_NAMESPACE, a.getNamespace());
				assertEquals(AssertionCredentialsManager.EMAIL_NAME, a.getName());
				Iterator vals = a.getValues();
				assertTrue(vals.hasNext());
				String val = (String) vals.next();
				assertEquals(TEST_EMAIL, val);
				assertTrue(!vals.hasNext());
				assertTrue(!i.hasNext());
			}

		}

		assertEquals(2, count);
		assertTrue(authFound);
		assertTrue(emailFound);
	}


	public void testAutoCredentialCreation() {
		try {

			IdPConfiguration conf = getIdpConfigurationAuto();
			assertEquals(true, conf.isAutoCreateAssertingCredentials());
			assertEquals(true, conf.isAutoRenewAssertingCredentials());
			assertEquals(null, conf.getAssertingCertificate());
			assertEquals(null, conf.getAssertingKey());
			AssertionCredentialsManager cm = new AssertionCredentialsManager(conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertNotNull(cm.getIdPKey());
			String expectedSub = Utils.CA_SUBJECT_PREFIX + ",CN=" + AssertionCredentialsManager.CA_SUBJECT;
			assertEquals(expectedSub, cert.getSubjectDN().toString());
			SAMLAssertion saml = cm.getAuthenticationAssertion(TEST_UID, TEST_EMAIL);
			verifySAMLAssertion(saml, cm);
			String xml = SAMLUtils.samlAssertionToString(saml);
			SAMLAssertion saml2 = SAMLUtils.stringToSAMLAssertion(xml);
			verifySAMLAssertion(saml2, cm);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAutoCredentialCreationRenew() {
		try {
			IdPConfiguration conf = getIdpConfigurationAuto();
			assertEquals(true, conf.isAutoCreateAssertingCredentials());
			assertEquals(true, conf.isAutoRenewAssertingCredentials());
			assertEquals(null, conf.getAssertingCertificate());
			assertEquals(null, conf.getAssertingKey());
			AssertionCredentialsManager cm = new AssertionCredentialsManager(conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertNotNull(cm.getIdPKey());
			String expectedSub = Utils.CA_SUBJECT_PREFIX + ",CN=" + AssertionCredentialsManager.CA_SUBJECT;
			assertEquals(expectedSub, cert.getSubjectDN().toString());

			String subject = cert.getSubjectDN().toString();
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.SECOND, 2);
			Date end = cal.getTime();

			X509Certificate shortCert = ca.requestCertificate(req, start, end);
			cm.storeCredentials(shortCert, pair.getPrivate(), conf.getKeyPassword());
			X509Certificate idpShortCert = cm.getIdPCertificate();
			assertEquals(shortCert, idpShortCert);
			if (cert.equals(idpShortCert)) {
				assertTrue(false);
			}

			Thread.sleep(2500);
			assertTrue(CertUtil.isExpired(idpShortCert));
			X509Certificate renewedCert = cm.getIdPCertificate();
			assertNotNull(renewedCert);

			PrivateKey renewedKey = cm.getIdPKey();
			assertNotNull(renewedKey);

			assertTrue(!CertUtil.isExpired(renewedCert));

			if (renewedCert.equals(idpShortCert)) {
				assertTrue(false);
			}

			if (renewedKey.equals(pair.getPrivate())) {
				assertTrue(false);
			}

			SAMLAssertion saml = cm.getAuthenticationAssertion(TEST_UID, TEST_EMAIL);
			verifySAMLAssertion(saml, cm);
			String xml = SAMLUtils.samlAssertionToString(saml);
			SAMLAssertion saml2 = SAMLUtils.stringToSAMLAssertion(xml);
			verifySAMLAssertion(saml2, cm);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAutoCredentialCreationNoRenewal() {
		try {
			IdPConfiguration conf = getIdpConfigurationAutoNoRenew();
			assertEquals(true, conf.isAutoCreateAssertingCredentials());
			assertEquals(false, conf.isAutoRenewAssertingCredentials());
			assertEquals(null, conf.getAssertingCertificate());
			assertEquals(null, conf.getAssertingKey());
			AssertionCredentialsManager cm = new AssertionCredentialsManager(conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertNotNull(cm.getIdPKey());
			String expectedSub = Utils.CA_SUBJECT_PREFIX + ",CN=" + AssertionCredentialsManager.CA_SUBJECT;
			assertEquals(expectedSub, cert.getSubjectDN().toString());

			String subject = cert.getSubjectDN().toString();
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.SECOND, 2);
			Date end = cal.getTime();

			X509Certificate shortCert = ca.requestCertificate(req, start, end);
			cm.storeCredentials(shortCert, pair.getPrivate(), conf.getKeyPassword());
			if (cert.equals(shortCert)) {
				assertTrue(false);
			}

			Thread.sleep(2500);
			assertTrue(CertUtil.isExpired(shortCert));

			try {
				cm.getIdPCertificate();
				assertTrue(false);
			} catch (DorianInternalFault fault) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testProvidedCredentials() {
		try {
			IdPConfiguration conf = getIdpConfiguration();
			assertEquals(null, conf.getKeyPassword());

			assertEquals(false, conf.isAutoCreateAssertingCredentials());
			assertEquals(false, conf.isAutoRenewAssertingCredentials());
			InputStream resource = TestCase.class.getResourceAsStream(Constants.DORIAN_CERT);
			X509Certificate providedCert = CertUtil.loadCertificate(resource);
			assertTrue(!CertUtil.isExpired(providedCert));
			assertEquals(providedCert, conf.getAssertingCertificate());
			resource = TestCase.class.getResourceAsStream(Constants.DORIAN_KEY);
			assertEquals(KeyUtil.loadPrivateKey(resource, conf.getKeyPassword()), conf.getAssertingKey());

			AssertionCredentialsManager cm = new AssertionCredentialsManager(conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertEquals(conf.getAssertingCertificate(), cert);
			PrivateKey key = cm.getIdPKey();
			assertNotNull(key);
			assertEquals(conf.getAssertingKey(), key);
			SAMLAssertion saml = cm.getAuthenticationAssertion(TEST_UID, TEST_EMAIL);
			verifySAMLAssertion(saml, cm);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	private IdPConfiguration getIdpConfigurationAuto() {
		IdPConfiguration config = new IdPConfiguration();
		config.setAutoCreateAssertingCredentials(true);
		config.setAutoRenewAssertingCredentials(true);
		config.setKeyPassword("password");
		return config;
	}


	private IdPConfiguration getIdpConfigurationAutoNoRenew() {
		IdPConfiguration config = new IdPConfiguration();
		config.setAutoCreateAssertingCredentials(true);
		config.setAutoRenewAssertingCredentials(false);
		config.setKeyPassword("password");
		return config;
	}


	private IdPConfiguration getIdpConfiguration() throws Exception {
		IdPConfiguration config = new IdPConfiguration();
		config.setAutoCreateAssertingCredentials(false);
		config.setAutoRenewAssertingCredentials(false);
		InputStream resource = TestCase.class.getResourceAsStream(Constants.DORIAN_CERT);
		config.setAssertingCertificate(CertUtil.loadCertificate(resource));
		resource = TestCase.class.getResourceAsStream(Constants.DORIAN_KEY);
		config.setAssertingKey(KeyUtil.loadPrivateKey(resource, config.getKeyPassword()));
		return config;
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			ca = Utils.getCA();

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
}
