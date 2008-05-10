package org.cagrid.gaards.authentication.common;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.cagrid.gaards.authentication.Credential;


public interface AuthenticationProvider {
    
    void setSAMLProvider(SAMLProvider samlProvider);
    
    void setSubjectProvider(SubjectProvider subjectProvider);

    SAMLAssertion authenticate(Credential credential)
	throws RemoteException, InvalidCredentialException,
	InsufficientAttributeException, AuthenticationProviderException;
    
}
