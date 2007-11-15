/**
 * $Id: DefaultAuthenticationProvider.java,v 1.2 2007-11-15 00:54:58 dervin Exp $
 *
 */
package gov.nih.nci.cagrid.authentication.service;

import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.AuthenticationProvider;
import gov.nih.nci.cagrid.authentication.common.AuthenticationProviderException;
import gov.nih.nci.cagrid.authentication.common.InsufficientAttributeException;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.cagrid.authentication.common.SAMLProvider;
import gov.nih.nci.cagrid.authentication.common.SubjectProvider;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import javax.security.auth.Subject;

/**
 *
 * @version $Revision: 1.2 $
 * @author Joshua Phillips
 *
 */
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    
    private SAMLProvider samlProvider;
    private SubjectProvider subjectProvider;

    public SAMLProvider getSamlProvider() {
        return samlProvider;
    }

    public void setSamlProvider(SAMLProvider samlProvider) {
        this.samlProvider = samlProvider;
    }

    public SubjectProvider getSubjectProvider() {
        return subjectProvider;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cagrid.authentication.common.AuthenticationProvider#authenticate(gov.nih.nci.cagrid.authentication.bean.Credential)
     */
    public SAMLAssertion authenticate(Credential credential)
	    throws RemoteException, InvalidCredentialException,
	    InsufficientAttributeException, AuthenticationProviderException {

	try{
	    Subject subject = getSubjectProvider().getSubject(credential);
	    return getSamlProvider().getSAML(subject);
	}catch(InvalidCredentialException ex){
	    throw ex;	    
	}catch(InsufficientAttributeException ex){
	    throw ex;
	}catch(Exception ex){
	    ex.printStackTrace();
	    throw new AuthenticationProviderException("Error authenticating: " + ex.getMessage(), ex);
	}

    }

    public void setSAMLProvider(SAMLProvider samlProvider) {
	this.samlProvider = samlProvider;
    }

    public void setSubjectProvider(SubjectProvider subjectProvider) {
	this.subjectProvider = subjectProvider;
    }

}
