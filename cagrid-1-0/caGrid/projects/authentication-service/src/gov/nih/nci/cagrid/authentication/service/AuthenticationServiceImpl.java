package gov.nih.nci.cagrid.authentication.service;

import gov.nih.nci.cagrid.authentication.common.AuthenticationProvider;
import gov.nih.nci.cagrid.authentication.common.InsufficientAttributeException;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class AuthenticationServiceImpl extends AuthenticationServiceImplBase {

    private AuthenticationProvider auth;
    
    public AuthenticationServiceImpl() throws RemoteException {
	super();
	try{
	    //Instatiate auth provider
	    ClassPathResource cpr = new ClassPathResource("authentication-config.xml");
	    XmlBeanFactory fact = new XmlBeanFactory(cpr);
	    this.auth = (AuthenticationProvider) fact.getBean("authenticationProvider");
	}catch(Exception ex){
	    throw new RemoteException("Error instantiating AuthenticationProvider: " + ex.getMessage(), ex);
	}
    }

    public gov.nih.nci.cagrid.authentication.bean.SAMLAssertion authenticate(
	    gov.nih.nci.cagrid.authentication.bean.Credential credential)
	    throws RemoteException,
	    gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault,
	    gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault,
	    gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault {

	gov.nih.nci.cagrid.authentication.bean.SAMLAssertion saml = null;
	try{
	    SAMLAssertion sa = this.auth.authenticate(credential);

	    saml = new gov.nih.nci.cagrid.authentication.bean.SAMLAssertion(sa.toString());
	}catch(InvalidCredentialException ex){
	    InvalidCredentialFault fault = new InvalidCredentialFault();
	    FaultHelper fh = new FaultHelper(fault);
	    fh.addFaultCause(ex);
	    throw fault;
	}catch(InsufficientAttributeException ex){
	    InsufficientAttributeFault fault = new InsufficientAttributeFault();
	    FaultHelper fh = new FaultHelper(fault);
	    fh.addFaultCause(ex);
	    throw fault;
	}catch(Exception ex){
	    AuthenticationProviderFault fault = new AuthenticationProviderFault();
	    FaultHelper fh = new FaultHelper(fault);
	    fh.addFaultCause(ex);
	    throw fault;
	}
	return saml;
    }

}
