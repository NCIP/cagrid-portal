package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.Metadata;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.MetadataManager;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;

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
public class AssertionCredentialsManager extends GUMSObject {

	private final static String IDP_PRIVATE_KEY = "IdP Private Key";

	private final static String IDP_CERTIFICATE = "IdP Certificate";

	private final static String IDP_PRIVATE_KEY_DESCRIPTION = "Private key used by IdP to sign authentication assersions.";

	private final static String IDP_CERTIFICATE_DESCRIPTION = "Certificate corresponding to the private key used by IdP to sign authentication assersions.";

	public final static String CA_SUBJECT = "IdP Authentication Asserter";

	public final static String EMAIL_NAMESPACE="http://cagrid.nci.nih.gov/email";
	
	public final static String EMAIL_NAME="email";
	
	private MetadataManager mm;

	private CertificateAuthority ca;

	private IdPConfiguration conf;

	public AssertionCredentialsManager(IdPConfiguration conf,
			CertificateAuthority ca, Database db) throws GUMSInternalFault {
		try {
			mm = new MetadataManager(db, "IDP_ASSERTER");
			this.ca = ca;
			this.conf = conf;

			if ((!mm.exists(IDP_PRIVATE_KEY)) || (!mm.exists(IDP_CERTIFICATE))) {
				if (conf.isAutoCreateAssertingCredentials()) {
					createNewCredentials();
				} else {
					storeCredentials(conf.getAssertingCertificate(), conf
							.getAssertingKey(), conf.getKeyPassword());
				}
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error initializing the IDP Asserting Manager.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public void storeCredentials(X509Certificate cert, PrivateKey pkey,
			String keyPassword) throws Exception {
		mm.remove(IDP_PRIVATE_KEY);
		mm.remove(IDP_CERTIFICATE);
		Metadata key = new Metadata();
		key.setName(IDP_PRIVATE_KEY);
		key.setValue(KeyUtil.writePrivateKeyToString(pkey, keyPassword));
		key.setDescription(IDP_PRIVATE_KEY_DESCRIPTION);
		mm.insert(key);

		Metadata certificate = new Metadata();
		certificate.setName(IDP_CERTIFICATE);
		certificate.setValue(CertUtil.writeCertificateToString(cert));
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
		PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(
				subject, pair);
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();

		X509Certificate cert = ca.requestCertificate(req, start, cacert
				.getNotAfter());
		storeCredentials(cert, pair.getPrivate(), conf.getKeyPassword());
	}
	
	

	public PrivateKey getIdPKey()
			throws GUMSInternalFault {
		try {
			//force updating expiring credentials
			getIdPCertificate();
			Metadata mkey = mm.get(IDP_PRIVATE_KEY);
			return KeyUtil.loadPrivateKey(new ByteArrayInputStream(mkey.getValue()
					.getBytes()), conf.getKeyPassword());
		
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error obtaining the IDP Asserting Key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}

	}
	
	public X509Certificate getIdPCertificate()
	throws GUMSInternalFault {
		return getIdPCertificate(true);
	}
	
	public SAMLAssertion getAuthenticationAssertion(String id, String email) throws GUMSInternalFault {
		try{
			org.apache.xml.security.Init.init();
		X509Certificate cert = getIdPCertificate();
		PrivateKey key = getIdPKey();
		GregorianCalendar cal = new GregorianCalendar();
		Date start = cal.getTime();
		cal.add(Calendar.MINUTE, 2);
		Date end = cal.getTime();
		String issuer = cert.getSubjectDN().toString();
		String federation = cert.getSubjectDN().toString();
		String ipAddress=null;
		String subjectDNS=null;
		
		SAMLSubject sub = new SAMLSubject(id,federation,"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified",null,null,null);	
		SAMLSubject sub2 = new SAMLSubject(id,federation,"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified",null,null,null);	
		SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(sub,"urn:oasis:names:tc:SAML:1.0:am:password",new Date(),ipAddress,subjectDNS,null);
	   
		QName name = new QName(EMAIL_NAMESPACE,EMAIL_NAME);
	    List vals = new ArrayList();
	    vals.add(email);
		SAMLAttribute att = new SAMLAttribute(name.getLocalName(),name.getNamespaceURI(),null,(long)0,vals);
		
		List atts = new ArrayList();
		atts.add(att);
		SAMLAttributeStatement attState = new SAMLAttributeStatement(sub2,atts);
		
	    List l = new ArrayList();
		l.add(auth);
		l.add(attState);
		
		SAMLAssertion saml = new SAMLAssertion(issuer, start, end, null,
				null, l);
		List a = new ArrayList();
		a.add(cert);
		saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
				key ,a,false);
		
		return saml;
		}catch(Exception e){
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error creating SAML Assertion.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
			
		}

	}

	private X509Certificate getIdPCertificate(boolean firstTime)
			throws GUMSInternalFault {
		try {
			Metadata mcert = mm.get(IDP_CERTIFICATE);
			StringReader reader = new StringReader(mcert.getValue());
			X509Certificate cert = CertUtil.loadCertificate(reader);
			Date now = new Date();
			if (now.before(cert.getNotBefore())
					|| (now.after(cert.getNotAfter()))) {
				if ((firstTime) && (conf.isAutoRenewAssertingCredentials())) {
					createNewCredentials();
					return getIdPCertificate(false);
				} else {
					GUMSInternalFault fault = new GUMSInternalFault();
					fault.setFaultString("IDP Asserting Certificate expired.");
					throw fault;
				}
			} else {
				return cert;
			}

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error obtaining the IDP Asserting Certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}


	}

}
