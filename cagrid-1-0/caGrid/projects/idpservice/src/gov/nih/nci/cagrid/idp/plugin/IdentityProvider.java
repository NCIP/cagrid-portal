package gov.nih.nci.cagrid.idp.plugin;

import gov.nih.nci.cagrid.idp.plugin.domain.Credential;
import gov.nih.nci.cagrid.idp.plugin.exception.*;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;


public interface IdentityProvider {
	
	public SAMLAssertion login(Credential Credentials) throws InValidCredentialException, InsufficientAttributesException, IdpInternalException,RemoteException;

}
