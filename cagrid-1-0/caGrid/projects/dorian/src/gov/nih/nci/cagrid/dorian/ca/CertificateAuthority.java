package gov.nih.nci.cagrid.gums.ca;

import java.util.Date;

import java.security.cert.X509Certificate;

import org.bouncycastle.jce.PKCS10CertificationRequest;

public interface CertificateAuthority {	
	public X509Certificate requestCertificate(PKCS10CertificationRequest request, Date startDate, Date expirationDate) throws CertificateAuthorityFault,NoCACredentialsFault;
	public X509Certificate getCACertificate() throws CertificateAuthorityFault,NoCACredentialsFault;
	public X509Certificate renewCertifcateAuthorityCredentials(Date expirationDate) throws CertificateAuthorityFault,NoCACredentialsFault;
}
