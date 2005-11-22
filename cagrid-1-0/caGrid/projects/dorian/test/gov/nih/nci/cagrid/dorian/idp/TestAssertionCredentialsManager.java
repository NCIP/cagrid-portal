package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import junit.framework.TestCase;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.opensaml.InvalidCryptoException;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeDesignator;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLStatement;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestAssertionCredentialsManager extends TestCase {

	public static String CA_RESOURCES_DIR = "resources" + File.separator
			+ "ca-test";

	private Database db;

	private CertificateAuthority ca;

	private static String TEST_EMAIL = "test@test.com";
	private static String TEST_UID = "test";
	
	public void verifySAMLAssertion(SAMLAssertion saml, AssertionCredentialsManager cm)
	throws Exception{
		assertNotNull(saml);
		saml.verify(cm.getIdPCertificate(), false);
			
		try {
			// Test against a bad certificate
			saml.verify(CertUtil.loadCertificate(CA_RESOURCES_DIR
					+ "/bmi-cacert.pem"), false);
			assertTrue(false);
		} catch (InvalidCryptoException ex) {

		}
		assertEquals(cm.getIdPCertificate().getSubjectDN().toString(),saml.getIssuer());
		Iterator itr = saml.getStatements();
		int count = 0;
		boolean emailFound = false;
		boolean authFound = false;
		while(itr.hasNext()){
			count = count+1;
			SAMLStatement stmt = (SAMLStatement) itr.next();
			if(stmt instanceof SAMLAuthenticationStatement){
				if(authFound){
					assertTrue(false);
				}else{
					authFound = true;
				}
				SAMLAuthenticationStatement auth = (SAMLAuthenticationStatement)stmt;
				assertEquals(TEST_UID,auth.getSubject().getName());
				assertEquals("urn:oasis:names:tc:SAML:1.0:am:password",auth.getAuthMethod());
			}
			
			if(stmt instanceof SAMLAttributeStatement){
				if(emailFound){
					assertTrue(false);
				}else{
					emailFound = true;
				}
				SAMLAttributeStatement att = (SAMLAttributeStatement)stmt;
				assertEquals(TEST_UID,att.getSubject().getName());
				Iterator i = att.getAttributes();
				assertTrue(i.hasNext());
				SAMLAttribute a = (SAMLAttribute)i.next();
				assertEquals(AssertionCredentialsManager.EMAIL_NAMESPACE,a.getNamespace());
				assertEquals(AssertionCredentialsManager.EMAIL_NAME,a.getName());
				Iterator vals = a.getValues();
				assertTrue(vals.hasNext());
				String val = (String)vals.next();
				assertEquals(TEST_EMAIL,val);
				assertTrue(!vals.hasNext());
				assertTrue(!i.hasNext());
			}
			
		}
		
		assertEquals(2,count);
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
			AssertionCredentialsManager cm = new AssertionCredentialsManager(
					conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertNotNull(cm.getIdPKey());
			String expectedSub = TestUtils.CA_SUBJECT_PREFIX + ",CN="
					+ AssertionCredentialsManager.CA_SUBJECT;
			assertEquals(expectedSub, cert.getSubjectDN().toString());
			SAMLAssertion saml = cm.getAuthenticationAssertion(TEST_UID,TEST_EMAIL);
			verifySAMLAssertion(saml,cm);
			
			

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
			AssertionCredentialsManager cm = new AssertionCredentialsManager(
					conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertNotNull(cm.getIdPKey());
			String expectedSub = TestUtils.CA_SUBJECT_PREFIX + ",CN="
					+ AssertionCredentialsManager.CA_SUBJECT;
			assertEquals(expectedSub, cert.getSubjectDN().toString());

			String subject = cert.getSubjectDN().toString();
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PKCS10CertificationRequest req = CertUtil
					.generateCertficateRequest(subject, pair);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.SECOND, 2);
			Date end = cal.getTime();

			X509Certificate shortCert = ca.requestCertificate(req, start, end);
			cm.storeCredentials(shortCert, pair.getPrivate(), conf
					.getKeyPassword());
			if (cert.equals(shortCert)) {
				assertTrue(false);
			}

			Thread.sleep(2500);
			assertTrue(CertUtil.isExpired(shortCert));
			X509Certificate renewedCert = cm.getIdPCertificate();
			assertNotNull(renewedCert);

			PrivateKey renewedKey = cm.getIdPKey();
			assertNotNull(renewedKey);

			assertTrue(!CertUtil.isExpired(renewedCert));

			if (renewedCert.equals(shortCert)) {
				assertTrue(false);
			}

			if (renewedKey.equals(pair.getPrivate())) {
				assertTrue(false);
			}

			SAMLAssertion saml = cm.getAuthenticationAssertion(TEST_UID,TEST_EMAIL);
			verifySAMLAssertion(saml,cm);

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
			AssertionCredentialsManager cm = new AssertionCredentialsManager(
					conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertNotNull(cm.getIdPKey());
			String expectedSub = TestUtils.CA_SUBJECT_PREFIX + ",CN="
					+ AssertionCredentialsManager.CA_SUBJECT;
			assertEquals(expectedSub, cert.getSubjectDN().toString());

			String subject = cert.getSubjectDN().toString();
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PKCS10CertificationRequest req = CertUtil
					.generateCertficateRequest(subject, pair);
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.SECOND, 2);
			Date end = cal.getTime();

			X509Certificate shortCert = ca.requestCertificate(req, start, end);
			cm.storeCredentials(shortCert, pair.getPrivate(), conf
					.getKeyPassword());
			if (cert.equals(shortCert)) {
				assertTrue(false);
			}

			Thread.sleep(2500);
			assertTrue(CertUtil.isExpired(shortCert));

			try {
				cm.getIdPCertificate();
				assertTrue(false);
			} catch (GUMSInternalFault fault) {

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
			X509Certificate providedCert = CertUtil
					.loadCertificate(CA_RESOURCES_DIR + "/gums-cert.pem");
			assertTrue(!CertUtil.isExpired(providedCert));
			assertEquals(providedCert, conf.getAssertingCertificate());
			assertEquals(KeyUtil.loadPrivateKey(CA_RESOURCES_DIR
					+ "/gums-key.pem", conf.getKeyPassword()), conf
					.getAssertingKey());

			AssertionCredentialsManager cm = new AssertionCredentialsManager(
					conf, ca, db);
			X509Certificate cert = cm.getIdPCertificate();
			assertNotNull(cert);
			assertEquals(conf.getAssertingCertificate(), cert);
			PrivateKey key = cm.getIdPKey();
			assertNotNull(key);
			assertEquals(conf.getAssertingKey(), key);
			SAMLAssertion saml = cm.getAuthenticationAssertion(TEST_UID,TEST_EMAIL);
			verifySAMLAssertion(saml,cm);
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

		config.setAssertingCertificate(CertUtil
				.loadCertificate(CA_RESOURCES_DIR + "/gums-cert.pem"));
		config.setAssertingKey(KeyUtil.loadPrivateKey(CA_RESOURCES_DIR
				+ "/gums-key.pem", config.getKeyPassword()));
		return config;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = TestUtils.getDB();
			assertEquals(0,db.getUsedConnectionCount());
			ca = TestUtils.getCA();

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
