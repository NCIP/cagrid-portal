package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyValid;
import gov.nih.nci.cagrid.gums.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import org.apache.xml.security.signature.XMLSignature;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.globus.gsi.GlobusCredential;
import org.globus.util.ConfigUtil;
import org.globus.wsrf.utils.FaultHelper;
import org.opensaml.QName;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLSubject;

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

	public final static String EMAIL_NAMESPACE = "http://cagrid.nci.nih.gov/email";

	public final static String EMAIL_NAME = "email";

	private Database db;

	private CertificateAuthority ca;

	public void testCreateProxy() {
		try {
			IFSManager.getInstance().configure(db, getConf(), ca);
			IFS ifs = new IFS();
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			ifs.addTrustedIdP(idp.getIdp());

			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			X509Certificate[] certs = ifs.createProxy(getSAMLAssertion("user", idp), publicKey,
					getProxyLifetime());
			
			GlobusCredential cred = new GlobusCredential(pair.getPrivate(),
					certs);
			FileOutputStream fos = new FileOutputStream(ConfigUtil
					.discoverProxyLocation());
			cred.save(fos);
			fos.close();

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testCreateProxyAutoApproval() {
		try {
			IFSManager.getInstance().configure(db,
					getExpiringCredentialsConf(), ca);
			IFS ifs = new IFS();
			String username = "user";
			IdPContainer idp = this.getTrustedIdpAutoApprove("My IdP");

			ifs.addTrustedIdP(idp.getIdp());
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			PublicKey publicKey = pair.getPublic();
			ifs.createProxy(getSAMLAssertion(username, idp), publicKey,
					getProxyValidShort());
			assertEquals(ifs.getUser(idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Active);
			Thread.sleep(3100);
			try {
				KeyPair pair2 = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey2 = pair2.getPublic();
				ifs.createProxy(getSAMLAssertion(username, idp), publicKey2,
						getProxyValidShort());
				assertTrue(false);
			} catch (PermissionDeniedFault fault) {

			}
			assertEquals(ifs.getUser(idp.getIdp().getId(), username)
					.getUserStatus(), IFSUserStatus.Expired);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testCreateProxyInvalidProxyValid() {
		try {
			IFSManager.getInstance().configure(db, getConf(), ca);
			IFS ifs = new IFS();
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			ifs.addTrustedIdP(idp.getIdp());
			Thread.sleep(500);
			try {
				ProxyValid valid = new ProxyValid();
				valid.setHours(12);
				valid.setMinutes(0);
				valid.setSeconds(1);
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs
						.createProxy(getSAMLAssertion("user", idp), publicKey,
								valid);
				assertTrue(false);
			} catch (InvalidProxyFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testCreateProxyInvalidAuthenticationMethod() {
		try {
			IFSManager.getInstance().configure(db, getConf(), ca);
			IFS ifs = new IFS();
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			ifs.addTrustedIdP(idp.getIdp());
			Thread.sleep(500);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertionUnspecifiedMethod("user", idp),
						publicKey, getProxyLifetime());
				assertTrue(false);
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testCreateProxyUntrustedIdP() {
		try {
			IFSManager.getInstance().configure(db, getConf(), ca);
			IFS ifs = new IFS();
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			IdPContainer idp2 = this
					.getTrustedIdpAutoApproveAutoRenew("My IdP 2");
			ifs.addTrustedIdP(idp.getIdp());
			Thread.sleep(500);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getSAMLAssertion("user", idp2), publicKey,
						getProxyLifetime());
				assertTrue(false);
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testCreateProxyExpiredAssertion() {
		try {
			IFSManager.getInstance().configure(db, getConf(), ca);
			IFS ifs = new IFS();
			IdPContainer idp = this.getTrustedIdpAutoApproveAutoRenew("My IdP");
			ifs.addTrustedIdP(idp.getIdp());
			Thread.sleep(500);
			try {
				KeyPair pair = KeyUtil.generateRSAKeyPair1024();
				PublicKey publicKey = pair.getPublic();
				ifs.createProxy(getExpiredSAMLAssertion("user", idp),
						publicKey, getProxyLifetime());
				assertTrue(false);
			} catch (InvalidAssertionFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	private IFSConfiguration getConf() {
		IFSConfiguration conf = new IFSConfiguration();
		conf.setCredentialsValidYears(1);
		conf.setCredentialsValidMonths(0);
		conf.setCredentialsValidDays(0);
		conf.setCredentialsValidHours(0);
		conf.setCredentialsValidMinutes(0);
		conf.setCredentialsValidSeconds(0);
		conf.setMinimumIdPNameLength(MIN_NAME_LENGTH);
		conf.setMaximumIdPNameLength(MAX_NAME_LENGTH);
		conf.setMaxProxyValidHours(12);
		conf.setMaxProxyValidMinutes(0);
		conf.setMaxProxyValidSeconds(0);
		return conf;
	}

	private IFSConfiguration getExpiringCredentialsConf() {
		IFSConfiguration conf = new IFSConfiguration();
		conf.setCredentialsValidYears(0);
		conf.setCredentialsValidMonths(0);
		conf.setCredentialsValidDays(0);
		conf.setCredentialsValidHours(0);
		conf.setCredentialsValidMinutes(0);
		conf.setCredentialsValidSeconds(3);
		conf.setMinimumIdPNameLength(MIN_NAME_LENGTH);
		conf.setMaximumIdPNameLength(MAX_NAME_LENGTH);
		conf.setMaxProxyValidHours(12);
		conf.setMaxProxyValidMinutes(0);
		conf.setMaxProxyValidSeconds(0);
		return conf;
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
			String email = id + "@test.com";

			String issuer = cert.getSubjectDN().toString();
			String federation = cert.getSubjectDN().toString();
			String ipAddress = null;
			String subjectDNS = null;

			SAMLSubject sub = new SAMLSubject(id, federation,
					"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified",
					null, null, null);
			SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(
					sub, method, new Date(), ipAddress, subjectDNS, null);
			QName name = new QName(EMAIL_NAMESPACE, EMAIL_NAME);
			List vals = new ArrayList();
			vals.add(email);
			SAMLAttribute att = new SAMLAttribute(name.getLocalName(), name
					.getNamespaceURI(), name, (long) 0, vals);

			List atts = new ArrayList();
			atts.add(att);
			SAMLAttributeStatement attState = new SAMLAttributeStatement(sub,
					atts);

			List l = new ArrayList();
			l.add(auth);
			l.add(attState);

			SAMLAssertion saml = new SAMLAssertion(issuer, start, end, null,
					null, l);
			List a = new ArrayList();
			a.add(cert);
			saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1, key, a, false);

			return saml;
		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error creating SAML Assertion.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;

		}
	}

	private IdPContainer getTrustedIdpAutoApproveAutoRenew(String name)
			throws Exception {
		return this.getTrustedIdp(name, AutoApprovalAutoRenewalPolicy.class
				.getName());
	}

	private IdPContainer getTrustedIdpAutoApprove(String name) throws Exception {
		return this.getTrustedIdp(name, AutoApprovalPolicy.class.getName());
	}

	private IdPContainer getTrustedIdp(String name, String policyClass)
			throws Exception {
		TrustedIdP idp = new TrustedIdP();
		idp.setName(name);
		idp.setPolicyClass(policyClass);

		SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
		methods[0] = SAMLAuthenticationMethod
				.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
		idp.setAuthenticationMethod(methods);

		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		String subject = TestUtils.CA_SUBJECT_PREFIX + ",CN=" + name;
		PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(
				subject, pair);
		assertNotNull(req);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MONTH, 10);
		Date end = cal.getTime();
		X509Certificate cert = ca.requestCertificate(req, start, end);
		assertNotNull(cert);
		assertEquals(cert.getSubjectDN().getName(), subject);
		idp.setIdPCertificate(CertUtil.writeCertificateToString(cert));
		return new IdPContainer(idp, cert, pair.getPrivate());
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = TestUtils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			ca = TestUtils.getCA(db);
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

	private ProxyValid getProxyValidShort() {
		ProxyValid valid = new ProxyValid();
		valid.setHours(0);
		valid.setMinutes(0);
		valid.setSeconds(1);
		return valid;
	}

	private ProxyValid getProxyLifetime() {
		ProxyValid valid = new ProxyValid();
		valid.setHours(12);
		valid.setMinutes(0);
		valid.setSeconds(0);
		return valid;
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
