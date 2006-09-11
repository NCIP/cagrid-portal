package gov.nih.nci.cagrid.idp.plugin;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface SAMLAsserter
{
    /**
     * Must be set before calling {@link}getAssertion(String,String,String,String).
     * 
     * @param certificate representing the identity of the identity provider
     */
    void setIdpCertificate(X509Certificate certificate);
    
    /**
     * Must be set before calling {@link}getAssertion(String,String,String,String).
     * 
     * @param privateKey matching the certificate
     */
    void setIdpPrivateKey(PrivateKey privateKey);
    
    /**
     * Returns a SAMLAssertion given user pricipals.
     * 
     * @param uid
     * @param firstName
     * @param lastName
     * @param email
     * @return SAMLAssertion
     * @throws IllegalStateException if certificate or privateKey have not been set.
     */
    SAMLAssertion getAssertion(String uid, String firstName, String lastName, String email);

}
