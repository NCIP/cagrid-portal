package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.bean.Metadata;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.common.MetadataManager;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.xml.security.signature.XMLSignature;
import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AssertionCredentialsManager extends LoggingObject {

	private final static String IDP_PRIVATE_KEY = "IdP Private Key";

	private final static String IDP_CERTIFICATE = "IdP Certificate";

	private final static String IDP_PRIVATE_KEY_DESCRIPTION = "Private key used by IdP to sign authentication assersions.";

	private final static String IDP_CERTIFICATE_DESCRIPTION = "Certificate corresponding to the private key used by IdP to sign authentication assersions.";

	public final static String CA_SUBJECT = "Dorian IdP Authentication Asserter";

	public final static String EMAIL_NAMESPACE = "http://cagrid.nci.nih.gov/email";

	public final static String EMAIL_NAME = "email";

	private MetadataManager mm;

	private CertificateAuthority ca;

	private IdPConfiguration conf;


	public AssertionCredentialsManager(IdPConfiguration conf, CertificateAuthority ca, Database db)
		throws DorianInternalFault {
		try {
			mm = new MetadataManager(db, "IDP_ASSERTER");
			this.ca = ca;
			this.conf = conf;

			if ((!mm.exists(IDP_PRIVATE_KEY)) || (!mm.exists(IDP_CERTIFICATE))) {
				if (conf.isAutoCreateAssertingCredentials()) {
					createNewCredentials();
				} else {
					storeCredentials(conf.getAssertingCertificate(), conf.getAssertingKey(), conf.getKeyPassword());
				}
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error initializing the IDP Asserting Manager.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public void storeCredentials(X509Certificate cert, PrivateKey pkey, String keyPassword) throws Exception {
		mm.remove(IDP_PRIVATE_KEY);
		mm.remove(IDP_CERTIFICATE);
		Metadata key = new Metadata();
		key.setName(IDP_PRIVATE_KEY);
		key.setValue(KeyUtil.writePrivateKey(pkey, keyPassword));
		key.setDescription(IDP_PRIVATE_KEY_DESCRIPTION);
		mm.insert(key);

		Metadata certificate = new Metadata();
		certificate.setName(IDP_CERTIFICATE);
		certificate.setValue(CertUtil.writeCertificate(cert));
		certificate.setDescription(IDP_CERTIFICATE_DESCRIPTION);
		mm.insert(certificate);
	}


	private void createNewCredentials() throws Exception {
		// VALIDATE DN
		X509Certificate cacert = ca.getCACertificate();
		String caSubject = cacert.getSubjectDN().getName();
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);

		String subject = caPreSub + ",CN=" + CA_SUBJECT;
		KeyPair pair = KeyUtil.generateRSAKeyPair1024();
		PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(subject, pair);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();

		X509Certificate cert = ca.requestCertificate(req, start, cacert.getNotAfter());
		storeCredentials(cert, pair.getPrivate(), conf.getKeyPassword());
	}


	public PrivateKey getIdPKey() throws DorianInternalFault {
		try {
			// force updating expiring credentials
			getIdPCertificate();
			Metadata mkey = mm.get(IDP_PRIVATE_KEY);
			return KeyUtil.loadPrivateKey(new ByteArrayInputStream(mkey.getValue().getBytes()), conf.getKeyPassword());

		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining the IDP Asserting Key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

	}


	public X509Certificate getIdPCertificate() throws DorianInternalFault {
		return getIdPCertificate(true);
	}


	public SAMLAssertion getAuthenticationAssertion(String id, String email) throws DorianInternalFault {
		try {
			org.apache.xml.security.Init.init();
			X509Certificate cert = getIdPCertificate();
			PrivateKey key = getIdPKey();
			GregorianCalendar cal = new GregorianCalendar();
			Date start = cal.getTime();
			cal.add(Calendar.MINUTE, 2);
			Date end = cal.getTime();
			String issuer = cert.getSubjectDN().toString();
			String federation = cert.getSubjectDN().toString();
			String ipAddress = null;
			String subjectDNS = null;

			SAMLNameIdentifier ni1 = new SAMLNameIdentifier(id, federation,
				"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
			SAMLSubject sub = new SAMLSubject(ni1, null, null, null);
			SAMLNameIdentifier ni2 = new SAMLNameIdentifier(id, federation,
				"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
			SAMLSubject sub2 = new SAMLSubject(ni2, null, null, null);
			SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(sub,
				"urn:oasis:names:tc:SAML:1.0:am:password", new Date(), ipAddress, subjectDNS, null);
			List vals = new ArrayList();
			vals.add(email);
			SAMLAttribute att = new SAMLAttribute(EMAIL_NAME, EMAIL_NAMESPACE, null, 0, vals);

			List atts = new ArrayList();
			atts.add(att);
			SAMLAttributeStatement attState = new SAMLAttributeStatement(sub2, atts);

			List l = new ArrayList();
			l.add(auth);
			l.add(attState);

			SAMLAssertion saml = new SAMLAssertion(issuer, start, end, null, null, l);
			List a = new ArrayList();
			a.add(cert);
			saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1, key, a);

			return saml;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error creating SAML Assertion.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;

		}

	}


	private X509Certificate getIdPCertificate(boolean firstTime) throws DorianInternalFault {
		try {
			Metadata mcert = mm.get(IDP_CERTIFICATE);
			StringReader reader = new StringReader(mcert.getValue());
			X509Certificate cert = CertUtil.loadCertificate(reader);
			Date now = new Date();
			if (now.before(cert.getNotBefore()) || (now.after(cert.getNotAfter()))) {
				if ((firstTime) && (conf.isAutoRenewAssertingCredentials())) {
					createNewCredentials();
					return getIdPCertificate(false);
				} else {
					DorianInternalFault fault = new DorianInternalFault();
					fault.setFaultString("IDP Asserting Certificate expired.");
					throw fault;
				}
			} else {
				return cert;
			}

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining the IDP Asserting Certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

	}

}
