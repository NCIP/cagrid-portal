/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.security;

import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.EncryptionService;
import gov.nih.nci.cagrid.portal.authn.ProxyUtil;
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
     * @throws gov.nih.nci.cagrid.portal.authn.AuthnServiceException
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
