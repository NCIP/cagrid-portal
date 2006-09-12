package gov.nih.nci.cagrid.authentication.service;

import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.AuthenticationI;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import javax.security.auth.Subject;

public abstract class AbstractCredentialProvider implements AuthenticationI {
	
	public SAMLAssertion authenticate(Credential credential)
			throws RemoteException, InvalidCredentialFault,
			InsufficientAttributeFault, AuthenticationProviderFault {
		return null;
	}

	protected abstract Subject authenticateGetAttributes(Credential ced);
	
}
