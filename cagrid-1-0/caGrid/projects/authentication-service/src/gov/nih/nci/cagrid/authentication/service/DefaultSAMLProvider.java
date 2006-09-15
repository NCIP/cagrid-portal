/**
 * $Id: DefaultSAMLProvider.java,v 1.1 2006-09-15 10:52:46 joshua Exp $
 *
 */
package gov.nih.nci.cagrid.authentication.service;

import gov.nih.nci.cagrid.authentication.common.InsufficientAttributeException;
import gov.nih.nci.cagrid.dorian.common.SAMLConstants;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLAttribute;
import gov.nih.nci.cagrid.opensaml.SAMLAttributeStatement;
import gov.nih.nci.cagrid.opensaml.SAMLAuthenticationStatement;
import gov.nih.nci.cagrid.opensaml.SAMLNameIdentifier;
import gov.nih.nci.cagrid.opensaml.SAMLSubject;
import gov.nih.nci.security.authentication.ext.pricipals.UserIdPrincipal;
import gov.nih.nci.security.authentication.ext.principals.EmailPrincipal;
import gov.nih.nci.security.authentication.ext.principals.FirstNamePrincipal;
import gov.nih.nci.security.authentication.ext.principals.LastNamePrincipal;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;

import org.apache.xml.security.signature.XMLSignature;

/**
 *
 * @version $Revision: 1.1 $
 * @author Joshua Phillips
 *
 */
public class DefaultSAMLProvider implements
	gov.nih.nci.cagrid.authentication.common.SAMLProvider {
    
    private String certificateFileName;
    private String privateKeyFileName;
    private X509Certificate certificate;
    private PrivateKey privateKey;
    private String password;
    
    public void loadCertificates(){
	try{
	    Reader certReader = new FileReader(getCertificateFileName());
	    setCertificate(CertUtil.loadCertificate(certReader));
	    File keyFile = new File(getPrivateKeyFileName());
	    setPrivateKey(KeyUtil.loadPrivateKey(keyFile, getPassword()));
	}catch(Exception ex){
	    throw new RuntimeException("Error loading certificates: " + ex.getMessage(), ex);
	}
    }
    
    public X509Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(X509Certificate certificate) {
        this.certificate = certificate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.authentication.common.SAMLProvider#getSAML(javax.security.auth.Subject)
     */
    public SAMLAssertion getSAML(Subject subject)
	    throws InsufficientAttributeException {

	//Get attributes
	String uid = null;
	String firstName = null;
	String lastName = null;
	String email = null;
	Set principals = subject.getPrincipals();

	Iterator it = principals.iterator();
	while (it.hasNext()) {
	    Principal p = (Principal) it.next();
	    if(p instanceof UserIdPrincipal){
		uid = p.getName();
	    }else
	    if (p instanceof FirstNamePrincipal) {
		firstName = p.getName();
	    }else
	    if (p instanceof LastNamePrincipal) {
		lastName = p.getName();
	    }else
	    if (p instanceof EmailPrincipal) {
		email = p.getName();
	    }
	}
	if (firstName == null || firstName.trim().length() < 1 || lastName == null || lastName.trim().length() < 1
		|| email == null || email.trim().length() < 1) {
	    throw new InsufficientAttributeException(
		    "Missing attributes for the user");
	}
	
	SAMLAssertion saml = null;

	try {
	    org.apache.xml.security.Init.init();
	    GregorianCalendar cal = new GregorianCalendar();
	    Date start = cal.getTime();
	    cal.add(Calendar.MINUTE, 2);
	    Date end = cal.getTime();
	    String issuer = this.certificate.getSubjectDN().toString();
	    String federation = this.certificate.getSubjectDN().toString();
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
	    List vals1 = new ArrayList();
	    vals1.add(uid);
	    SAMLAttribute uidAtt = new SAMLAttribute(quid.getLocalPart(), quid
		    .getNamespaceURI(), null, 0, vals1);

	    QName qfirst = new QName(
		    SAMLConstants.FIRST_NAME_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.FIRST_NAME_ATTRIBUTE);
	    List vals2 = new ArrayList();
	    vals2.add(firstName);
	    SAMLAttribute firstNameAtt = new SAMLAttribute(qfirst
		    .getLocalPart(), qfirst.getNamespaceURI(), null, 0, vals2);

	    QName qLast = new QName(
		    SAMLConstants.LAST_NAME_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.LAST_NAME_ATTRIBUTE);
	    List vals3 = new ArrayList();
	    vals3.add(lastName);
	    SAMLAttribute lastNameAtt = new SAMLAttribute(qLast.getLocalPart(),
		    qLast.getNamespaceURI(), null, 0, vals3);

	    QName qemail = new QName(SAMLConstants.EMAIL_ATTRIBUTE_NAMESPACE,
		    SAMLConstants.EMAIL_ATTRIBUTE);
	    List vals4 = new ArrayList();
	    vals4.add(email);
	    SAMLAttribute emailAtt = new SAMLAttribute(qemail.getLocalPart(),
		    qemail.getNamespaceURI(), null, 0, vals4);

	    List atts = new ArrayList();
	    atts.add(uidAtt);
	    atts.add(firstNameAtt);
	    atts.add(lastNameAtt);
	    atts.add(emailAtt);

	    SAMLAttributeStatement attState = new SAMLAttributeStatement(sub2,
		    atts);

	    List l = new ArrayList();
	    l.add(auth);
	    l.add(attState);

	    saml = new SAMLAssertion(issuer, start, end, null,
		    null, l);
	    List a = new ArrayList();
	    a.add(this.certificate);
	    saml.sign(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
		    this.privateKey, a);

	} catch (Exception e) {
	    throw new RuntimeException(e);

	}
	return saml;
    }

    public String getCertificateFileName() {
        return certificateFileName;
    }

    public void setCertificateFileName(String certificateFileName) {
        this.certificateFileName = certificateFileName;
    }

    public String getPrivateKeyFileName() {
        return privateKeyFileName;
    }

    public void setPrivateKeyFileName(String privateKeyFileName) {
        this.privateKeyFileName = privateKeyFileName;
    }


}
