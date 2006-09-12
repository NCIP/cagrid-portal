package gov.nih.nci.cagrid.authentication.common;

import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;



public interface AuthenticationI {
	public SAMLAssertion authenticate(Credential credential)
			throws RemoteException, InvalidCredentialFault,
			InsufficientAttributeFault, AuthenticationProviderFault;

}
