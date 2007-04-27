package gov.nih.nci.cagrid.dorian.service.ca;


import gov.nih.nci.cagrid.gridca.common.CRLEntry;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.jce.PKCS10CertificationRequest;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface CertificateAuthority {	
	public X509Certificate requestCertificate(PKCS10CertificationRequest request, Date startDate, Date expirationDate) throws CertificateAuthorityFault,NoCACredentialsFault;
	public X509Certificate getCACertificate() throws CertificateAuthorityFault,NoCACredentialsFault;
	public X509Certificate renewCertifcateAuthorityCredentials(Date expirationDate) throws CertificateAuthorityFault,NoCACredentialsFault;
	public X509CRL getCRL(CRLEntry[] entries) throws CertificateAuthorityFault,NoCACredentialsFault;
}
