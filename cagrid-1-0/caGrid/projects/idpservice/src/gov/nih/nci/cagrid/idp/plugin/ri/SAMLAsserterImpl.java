package gov.nih.nci.cagrid.idp.plugin.ri;

import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.idp.plugin.SAMLAsserter;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xml.security.signature.XMLSignature;

/**
 * This class is a simple implementation that should be replaced with something
 * more sophisticated.
 * 
 * @version $Revision: 1.1 $
 * @author Joshua Phillips
 * 
 */
public class SAMLAsserterImpl implements SAMLAsserter {

    private X509Certificate idpCertificate;

    private PrivateKey idpPrivateKey;

    public SAMLAsserterImpl() {
	try {
	    // Get password
	    String keyPwd = System.getProperty(
		    "gov.nih.nci.cagrid.idp.plugin.ri.idpPrivateKeyPwd", "idp");
	    
	    String homeDir = System.getProperty("user.home");

	    // Read cert
	    String idpCertFileName = System.getProperty(
		    "gov.nih.nci.cagrid.idp.plugin.ri.idpCertificate",
		    homeDir + "/idp_cert");
	    Reader certReader = new FileReader(idpCertFileName);
	    this.idpCertificate = CertUtil.loadCertificate(certReader);

	    String idpPrivKeyFileName = System.getProperty(
		    "gov.nih.nci.cagrid.idp.plugin.ri.idpPrivateKey",
		    homeDir + "/idp_key");
	    File keyFile = new File(idpPrivKeyFileName);
//	    InputStream keyStream = getClass().getResourceAsStream(
//		    idpPrivKeyResource);
	    this.idpPrivateKey = KeyUtil.loadPrivateKey(keyFile, keyPwd);

	} catch (Exception ex) {
	    ex.printStackTrace();
	    throw new RuntimeException(
		    "Error initializing: " + ex.getMessage(), ex);
	}
    }

    /**
         * The implementation of this method was ripped from
         * gov.nih.nci.cagrid.dorian.service.idp.AssertionCredentialsManager
         */
    public SAMLAssertion getAssertion(String uid, String firstName,
	    String lastName, String email) {
	if (this.idpCertificate == null) {
	    throw new IllegalStateException("certificate has not been set");
	}
	if (this.idpPrivateKey == null) {
	    throw new IllegalStateException("private key has not been set");
	}
	try {
	    org.apache.xml.security.Init.init();
	    GregorianCalendar cal = new GregorianCalendar();
	    Date start = cal.getTime();
	    cal.add(Calendar.MINUTE, 2);
	    Date end = cal.getTime();
	    String issuer = this.idpCertificate.getSubjectDN().toString();
	    String federation = this.idpCertificate.getSubjectDN().toString();
	    String ipAddress = null;
	    String subjectDNS = null;

	    SAMLNameIdentifier ni1 = new SAMLNameIdentifier(uid, federation,
		    "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
	    SAMLSubject sub = new SAMLSubject(ni1, null, null, null);
	    sub.addConfirmationMethod(SAMLSubject.CONF_BEARER);
	    SAMLNameIdentifier ni2 = new SAMLNameIdentifier(uid, federation,
		    "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
	    SAMLSubject sub2 = new SAMLSubject(ni2, null, null, null);
	    sub2.addConfirmationMethod(SAMLSubject.CONF_BEARER);
	    SAMLAuthenticationStatement auth = new SAMLAuthenticationStatement(
		    sub, "urn:oasis:names:tc:SAML:1.0:am:password", new Date(),
		    ipAddress, subjectDNS, null);

	    QName quid = new QName(SAMLConstants.UID_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.UID_ATTRIBUTE);
	    List<String> vals1 = new ArrayList<String>();
	    vals1.add(uid);
	    SAMLAttribute uidAtt = new SAMLAttribute(quid.getLocalPart(), quid
		    .getNamespaceURI(), null, 0, vals1);

	    QName qfirst = new QName(
		    SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.FIRST_NAME_ATTRIBUTE);
	    List<String> vals2 = new ArrayList<String>();
	    vals2.add(firstName);
	    SAMLAttribute firstNameAtt = new SAMLAttribute(qfirst
		    .getLocalPart(), qfirst.getNamespaceURI(), null, 0, vals2);

	    QName qLast = new QName(
		    SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.LAST_NAME_ATTRIBUTE);
	    List<String> vals3 = new ArrayList<String>();
	    vals3.add(lastName);
	    SAMLAttribute lastNameAtt = new SAMLAttribute(qLast.getLocalPart(),
		    qLast.getNamespaceURI(), null, 0, vals3);

	    QName qemail = new QName(SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.EMAIL_ATTRIBUTE);
	    List<String> vals4 = new ArrayList<String>();
	    vals4.add(email);
	    SAMLAttribute emailAtt = new SAMLAttribute(qemail.getLocalPart(),
		    qemail.getNamespaceURI(), null, 0, vals4);

	    List<SAMLAttribute> atts = new ArrayList<SAMLAttribute>();
	    atts.add(uidAtt);
	    atts.add(firstNameAtt);
	    atts.add(lastNameAtt);
	    atts.add(emailAtt);

	    SAMLAttributeStatement attState = new SAMLAttributeStatement(sub2,
		    atts);

	    List l = new ArrayList();
	    l.add(auth);
	    l.add(attState);

	    SAMLAssertion saml = new SAMLAssertion(issuer, start, end, null,
		    null, l);
	    List<X509Certificate> a = new ArrayList<X509Certificate>();
	    a.add(this.idpCertificate);
	    saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
		    this.idpPrivateKey, a);

	    return saml;
	} catch (Exception e) {
	    throw new RuntimeException(e);

	}
    }

    public void setIdpCertificate(X509Certificate certificate) {
	this.idpCertificate = certificate;
    }

    public void setIdpPrivateKey(PrivateKey privateKey) {
	this.idpPrivateKey = privateKey;
    }

    public static void main(String[] args) throws Exception {
	SAMLAsserterImpl asserter = new SAMLAsserterImpl();
	SAMLAssertion assertion = asserter.getAssertion("myUid", "myFirstName",
		"myLastName", "myEmail");
	System.out.println(assertion);
    }

}
