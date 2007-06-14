package gov.nih.nci.cagrid.dorian.service.ca;

import java.security.PrivateKey;


public interface WrappingCertificateAuthority {
	public WrappedKey wrap(PrivateKey key) throws CertificateAuthorityFault;
	public PrivateKey unwrap(WrappedKey key) throws CertificateAuthorityFault;
}
