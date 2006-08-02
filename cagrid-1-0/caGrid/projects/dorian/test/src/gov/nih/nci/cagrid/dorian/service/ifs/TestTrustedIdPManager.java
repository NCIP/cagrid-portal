package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAttributeDescriptor;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.stubs.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.test.Utils;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import org.apache.xml.security.signature.XMLSignature;
import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestTrustedIdPManager extends TestCase {

	private static final int MIN_NAME_LENGTH = 4;

	private static final int MAX_NAME_LENGTH = 50;

	private Database db;

	private TrustedIdPManager tm;

	private List list;

	private CertificateAuthority ca;


	public void testUniqueCertificates() {
		try {
			TrustedIdP idp1 = getTrustedIdp("IdP 1").getIdp();
			idp1 = tm.addTrustedIdP(idp1);
			assertEquals(1, tm.getTrustedIdPs().length);
			TrustedIdP idp2 = getTrustedIdp("IdP 2").getIdp();
			idp2.setIdPCertificate(idp1.getIdPCertificate());
			try {
				idp2 = tm.addTrustedIdP(idp2);
				assertTrue(false);
			} catch (InvalidTrustedIdPFault f) {

			}
			assertEquals(1, tm.getTrustedIdPs().length);
			TrustedIdP idp3 = getTrustedIdp("IdP 3").getIdp();
			idp3 = tm.addTrustedIdP(idp3);
			assertEquals(2, tm.getTrustedIdPs().length);
			idp3.setIdPCertificate(idp1.getIdPCertificate());
			try {
				tm.updateIdP(idp3);
				assertTrue(false);
			} catch (InvalidTrustedIdPFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}


	public void testSingleIdP() {
		try {
			// We want to run this multiple times
			int times = getAuthenticationMethods().size();
			for (int i = 0; i < times; i++) {
				assertNotNull(tm);
				assertEquals(0, tm.getTrustedIdPs().length);
				String name = "Test IdP";
				IdPContainer cont = getTrustedIdp(name);
				TrustedIdP idp = cont.getIdp();
				idp = tm.addTrustedIdP(idp);
				assertEquals(1, tm.getTrustedIdPs().length);
				assertEquals(idp.getAuthenticationMethod().length, tm.getAuthenticationMethods(idp.getId()).length);
				TrustedIdP[] list = tm.getTrustedIdPs();
				assertEquals(idp, list[0]);
				assertTrue(tm.determineTrustedIdPExistsByName(name));
				TrustedIdP temp = tm.getTrustedIdPByName(idp.getName());
				assertEquals(idp, temp);
				TrustedIdP temp2 = tm.getTrustedIdPById(idp.getId());
				assertEquals(idp, temp2);
				TrustedIdP temp3 = tm.getTrustedIdP(cont.getSAMLAssertion());
				assertEquals(idp, temp3);

				// Test for bad assertion
				IdPContainer bad = getTrustedIdp("BAD ASSERTION");
				try {
					tm.getTrustedIdP(bad.getSAMLAssertion());
					assertTrue(false);
				} catch (InvalidAssertionFault f) {

				}

				StringReader reader = new StringReader(idp.getIdPCertificate());
				X509Certificate cert = CertUtil.loadCertificate(reader);
				assertTrue(tm.determineTrustedIdPExistsByDN(cert.getSubjectDN().toString()));
				assertEquals(idp, tm.getTrustedIdPByDN(cert.getSubjectDN().toString()));

				// Test Updates
				String updatedName = "Update IdP";
				IdPContainer updatedCont = getTrustedIdp(updatedName);
				TrustedIdP updateIdp = updatedCont.getIdp();
				updateIdp.setId(idp.getId());
				updateIdp.setStatus(TrustedIdPStatus.Suspended);
				tm.updateIdP(updateIdp);

				TrustedIdP[] ulist = tm.getTrustedIdPs();
				assertEquals(1, ulist.length);
				assertEquals(updateIdp, ulist[0]);
				assertEquals(TrustedIdPStatus.Suspended, ulist[0].getStatus());
				assertTrue(!tm.determineTrustedIdPExistsByName(name));
				assertTrue(tm.determineTrustedIdPExistsByName(updatedName));
				TrustedIdP utemp = tm.getTrustedIdPByName(updateIdp.getName());
				assertEquals(updateIdp, utemp);
				TrustedIdP utemp2 = tm.getTrustedIdPById(updateIdp.getId());
				assertEquals(updateIdp, utemp2);
				TrustedIdP utemp3 = tm.getTrustedIdP(updatedCont.getSAMLAssertion());
				assertEquals(updateIdp, utemp3);

				StringReader ureader = new StringReader(updateIdp.getIdPCertificate());
				X509Certificate ucert = CertUtil.loadCertificate(ureader);
				assertTrue(!tm.determineTrustedIdPExistsByDN(cert.getSubjectDN().toString()));
				assertTrue(tm.determineTrustedIdPExistsByDN(ucert.getSubjectDN().toString()));
				assertEquals(updateIdp, tm.getTrustedIdPByDN(ucert.getSubjectDN().toString()));

				tm.removeTrustedIdP(idp.getId());
				assertEquals(0, tm.getTrustedIdPs().length);
				assertEquals(0, tm.getAuthenticationMethods(idp.getId()).length);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testMultipleIdPs() {
		try {
			// We want to run this multiple times
			int times = getAuthenticationMethods().size();
			String baseName = "Test IdP";
			String baseUpdateName = "Updated IdP";
			for (int i = 0; i < times; i++) {
				assertNotNull(tm);
				assertEquals(i, tm.getTrustedIdPs().length);
				String name = baseName + " " + i;
				IdPContainer cont = getTrustedIdp(name);
				TrustedIdP idp = cont.getIdp();
				idp = tm.addTrustedIdP(idp);
				assertEquals((i + 1), tm.getTrustedIdPs().length);
				assertEquals(idp.getAuthenticationMethod().length, tm.getAuthenticationMethods(idp.getId()).length);

				assertTrue(tm.determineTrustedIdPExistsByName(name));
				TrustedIdP temp = tm.getTrustedIdPByName(idp.getName());
				assertEquals(idp, temp);
				TrustedIdP temp2 = tm.getTrustedIdPById(idp.getId());
				assertEquals(idp, temp2);
				TrustedIdP temp3 = tm.getTrustedIdP(cont.getSAMLAssertion());
				assertEquals(idp, temp3);

				// Test for bad assertion
				IdPContainer bad = getTrustedIdp("BAD ASSERTION");
				try {
					tm.getTrustedIdP(bad.getSAMLAssertion());
					assertTrue(false);
				} catch (InvalidAssertionFault f) {

				}

				StringReader reader = new StringReader(idp.getIdPCertificate());
				X509Certificate cert = CertUtil.loadCertificate(reader);
				assertTrue(tm.determineTrustedIdPExistsByDN(cert.getSubjectDN().toString()));
				assertEquals(idp, tm.getTrustedIdPByDN(cert.getSubjectDN().toString()));

				// Test Updates
				String updatedName = baseUpdateName + " " + i;
				IdPContainer updateCont = getTrustedIdp(updatedName);
				TrustedIdP updateIdp = updateCont.getIdp();
				updateIdp.setId(idp.getId());
				updateIdp.setStatus(TrustedIdPStatus.Suspended);
				tm.updateIdP(updateIdp);

				assertEquals((i + 1), tm.getTrustedIdPs().length);
				assertTrue(!tm.determineTrustedIdPExistsByName(name));
				assertTrue(tm.determineTrustedIdPExistsByName(updatedName));
				TrustedIdP utemp = tm.getTrustedIdPByName(updateIdp.getName());
				assertEquals(updateIdp, utemp);
				assertEquals(TrustedIdPStatus.Suspended, utemp.getStatus());
				TrustedIdP utemp2 = tm.getTrustedIdPById(updateIdp.getId());
				assertEquals(updateIdp, utemp2);
				TrustedIdP utemp3 = tm.getTrustedIdP(updateCont.getSAMLAssertion());
				assertEquals(updateIdp, utemp3);

				StringReader ureader = new StringReader(updateIdp.getIdPCertificate());
				X509Certificate ucert = CertUtil.loadCertificate(ureader);
				assertTrue(!tm.determineTrustedIdPExistsByDN(cert.getSubjectDN().toString()));
				assertTrue(tm.determineTrustedIdPExistsByDN(ucert.getSubjectDN().toString()));
				assertEquals(updateIdp, tm.getTrustedIdPByDN(ucert.getSubjectDN().toString()));

			}

			TrustedIdP[] idps = tm.getTrustedIdPs();
			assertEquals(times, idps.length);
			int count = times;
			for (int i = 0; i < idps.length; i++) {
				count = count - 1;
				tm.removeTrustedIdP(idps[i].getId());
				assertEquals(count, tm.getTrustedIdPs().length);
			}

			assertEquals(0, tm.getTrustedIdPs().length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testUpdateAuthMethodsOnly() {
		try {
			TrustedIdP idp = getTrustedIdp("Test IdP").getIdp();
			int count = getAuthenticationMethods().size() / 2;
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[count];
			for (int i = 0; i < count; i++) {
				methods[i] = (SAMLAuthenticationMethod) getAuthenticationMethods().get(i);
			}
			idp.setAuthenticationMethod(methods);
			idp = tm.addTrustedIdP(idp);
			assertEquals(1, tm.getTrustedIdPs().length);

			methods = new SAMLAuthenticationMethod[count - 1];
			for (int i = 0; i < (count - 1); i++) {
				methods[i] = (SAMLAuthenticationMethod) getAuthenticationMethods().get(i);
			}
			idp.setAuthenticationMethod(methods);
			tm.updateIdP(idp);
			assertEquals(idp, tm.getTrustedIdPById(idp.getId()));

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}


	private IdPContainer getTrustedIdp(String name) throws Exception {
		TrustedIdP idp = new TrustedIdP();
		idp.setName(name);
		idp.setStatus(TrustedIdPStatus.Active);
		idp.setUserPolicyClass(AutoApprovalPolicy.class.getName());
		idp.setAuthenticationMethod(getRandomMethodList());
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

		GregorianCalendar cal2 = new GregorianCalendar();
		Date start2 = cal2.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end2 = cal2.getTime();
		String issuer = cert.getSubjectDN().toString();
		String federation = cert.getSubjectDN().toString();
		String ipAddress = null;
		String subjectDNS = null;
		SAMLNameIdentifier ni = new SAMLNameIdentifier(name, federation, "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
		SAMLSubject sub = new SAMLSubject(ni,null, null, null);
		SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(sub,
			"urn:oasis:names:tc:SAML:1.0:am:password", new Date(), ipAddress, subjectDNS, null);

		List l = new ArrayList();
		l.add(auth);
		SAMLAssertion saml = new SAMLAssertion(issuer, start2, end2, null, null, l);
		List a = new ArrayList();
		a.add(cert);
		saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1, pair.getPrivate(), a);

		return new IdPContainer(idp, cert, saml);
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			org.apache.xml.security.Init.init();
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			IFSConfiguration conf = new IFSConfiguration();
			conf.setMinimumIdPNameLength(MIN_NAME_LENGTH);
			conf.setMaximumIdPNameLength(MAX_NAME_LENGTH);
			conf.setUserPolicies(Utils.getUserPolicies());
			ca = Utils.getCA(db);
			tm = new TrustedIdPManager(conf, db);
			tm.clearDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			tm.clearDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	private SAMLAuthenticationMethod[] getRandomMethodList() {
		double val = Math.random();
		int size = getAuthenticationMethods().size() + 1;
		double num = 1 / ((double) size);

		int count = (int) (val / num);

		SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[count];

		for (int i = 0; i < count; i++) {
			methods[i] = (SAMLAuthenticationMethod) getAuthenticationMethods().get(i);
		}

		return methods;
	}


	public List getAuthenticationMethods() {
		if (list == null) {
			list = new ArrayList();

			Field[] fields = SAMLAuthenticationMethod.class.getFields();

			for (int i = 0; i < fields.length; i++) {
				if (SAMLAuthenticationMethod.class.isAssignableFrom(fields[i].getType())) {
					try {
						Object o = fields[i].get(null);
						list.add(o);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return list;
	}


	public class IdPContainer {

		TrustedIdP idp;

		X509Certificate cert;

		SAMLAssertion saml;


		public IdPContainer(TrustedIdP idp, X509Certificate cert, SAMLAssertion saml) {
			this.idp = idp;
			this.cert = cert;
			this.saml = saml;
		}


		public X509Certificate getCert() {
			return cert;
		}


		public TrustedIdP getIdp() {
			return idp;
		}


		public SAMLAssertion getSAMLAssertion() {
			return saml;
		}

	}

}
