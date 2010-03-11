package gov.nih.nci.cagrid.portal.authn.service;

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
import gov.nih.nci.cagrid.portal.authn.AuthnTimeoutException;
import gov.nih.nci.cagrid.portal.authn.domain.IdPAuthnInfo;
import org.globus.gsi.GlobusCredential;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface AuthnService {
    IdPAuthnInfo authenticateToIdP(String username, String password,
            String idpUrl) throws AuthnServiceException,
            InvalidCredentialFault, AuthnTimeoutException;

    GlobusCredential authenticateToIFS(String ifsUrl, String saml)
                    throws AuthnServiceException, AuthnTimeoutException;

    
}
