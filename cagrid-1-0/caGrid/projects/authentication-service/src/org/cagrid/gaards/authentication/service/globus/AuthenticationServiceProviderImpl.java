package org.cagrid.gaards.authentication.service.globus;

import java.rmi.RemoteException;

import org.cagrid.gaards.authentication.service.AuthenticationServiceImpl;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the AuthenticationServiceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class AuthenticationServiceProviderImpl{
	
	AuthenticationServiceImpl impl;
	
	public AuthenticationServiceProviderImpl() throws RemoteException {
		impl = new AuthenticationServiceImpl();
	}
	

    public org.cagrid.gaards.authentication.stubs.AuthenticateUserResponse authenticateUser(org.cagrid.gaards.authentication.stubs.AuthenticateUserRequest params) throws RemoteException, org.cagrid.gaards.authentication.faults.AuthenticationProviderFault, org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault, org.cagrid.gaards.authentication.faults.InsufficientAttributeFault, org.cagrid.gaards.authentication.faults.InvalidCredentialFault {
    org.cagrid.gaards.authentication.stubs.AuthenticateUserResponse boxedResult = new org.cagrid.gaards.authentication.stubs.AuthenticateUserResponse();
    boxedResult.setAssertion(impl.authenticateUser(params.getCredential().getCredential()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.authentication.AuthenticateResponse authenticate(gov.nih.nci.cagrid.authentication.AuthenticateRequest params) throws RemoteException, gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault, gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault, gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault {
    gov.nih.nci.cagrid.authentication.AuthenticateResponse boxedResult = new gov.nih.nci.cagrid.authentication.AuthenticateResponse();
    boxedResult.setSAMLAssertion(impl.authenticate(params.getCredential().getCredential()));
    return boxedResult;
  }

}