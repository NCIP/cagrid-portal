package gov.nih.nci.cagrid.portal.security;

import org.cagrid.websso.common.WebSSOClientException;
import org.cagrid.websso.common.WebSSOClientHelper;
import org.globus.gsi.GlobusCredential;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CDSCredentialRetriever {

    private String certificateFilePath;
    private String keyFilePath;
    private EncryptionService encryptionService;


    /**
     * Will get the credentials give a delegated EPR
     *
     * @param delegatedEPR
     * @return Proxy serialized as a String
     * @throws AuthnServiceException
     */
    public String getCredential(String delegatedEPR) throws AuthnServiceException {
        String proxy = null;
        try {
            GlobusCredential cred = WebSSOClientHelper.getUserCredential(delegatedEPR,
                    certificateFilePath, keyFilePath);
            proxy = ProxyUtil.getProxyString(cred);
            assert (proxy != null);
        } catch (WebSSOClientException e) {
            throw new AuthnServiceException("Could not retreive user credentials from CDS service", e);
        }
        return getEncryptionService().encrypt(proxy);
    }

    public String getCertificateFilePath() {
        return certificateFilePath;
    }

    public void setCertificateFilePath(String certificateFilePath) {
        this.certificateFilePath = certificateFilePath;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public EncryptionService getEncryptionService() {
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }
}
