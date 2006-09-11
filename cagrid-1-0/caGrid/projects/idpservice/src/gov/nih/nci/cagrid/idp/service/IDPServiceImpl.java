package gov.nih.nci.cagrid.idp.service;

import gov.nih.nci.cagrid.dorian.SAMLAssertion;
import gov.nih.nci.cagrid.idp.plugin.IdentityProvider;
import gov.nih.nci.cagrid.idp.plugin.ri.IdentityProviderImpl;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.exceptions.CSException;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;

/**
 * gov.nih.nci.cagrid.idpI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class IDPServiceImpl {
    private ServiceConfiguration configuration;

    public IDPServiceImpl() throws RemoteException {

    }

    public ServiceConfiguration getConfiguration() throws Exception {
	if (this.configuration != null) {
	    return this.configuration;
	}
	MessageContext ctx = MessageContext.getCurrentContext();

	String servicePath = ctx.getTargetService();

	String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath
		+ "/serviceconfiguration";
	try {
	    javax.naming.Context initialContext = new InitialContext();
	    this.configuration = (ServiceConfiguration) initialContext
		    .lookup(jndiName);
	} catch (Exception e) {
	    throw new Exception("Unable to instantiate service configuration.",
		    e);
	}

	return this.configuration;
    }

    public gov.nih.nci.cagrid.dorian.SAMLAssertion login(
	    gov.nih.nci.cagrid.idp.beans.CredentialType credential)
	    throws RemoteException,
	    gov.nih.nci.cagrid.idp.stubs.IdpInternalException,
	    gov.nih.nci.cagrid.idp.stubs.InValidCredentialException,
	    gov.nih.nci.cagrid.idp.stubs.InsufficientAttributeException {
	
	gov.nih.nci.cagrid.dorian.SAMLAssertion sa = null;
	IdentityProvider idp = new IdentityProviderImpl();
	try {
	    gov.nih.nci.cagrid.opensaml.SAMLAssertion openSa = idp
		    .login(new gov.nih.nci.cagrid.idp.plugin.domain.Credential(
			    credential.getUserID(), credential.getPassword()));
	    sa = new gov.nih.nci.cagrid.dorian.SAMLAssertion(openSa.toString());	    
	} catch (Exception ex) {
	    throw new RemoteException("Error getting assertion: "
		    + ex.getMessage(), ex);
	}

	
	return sa;
    }

}
