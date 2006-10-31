/**
 * $Id: DefaultSubjectProvider.java,v 1.3 2006-10-31 16:03:52 joshua Exp $
 *
 */
package gov.nih.nci.cagrid.authentication.service;

import java.security.Principal;

import javax.security.auth.Subject;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.cagrid.authentication.common.SubjectProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.authentication.principal.LoginIdPrincipal;
import gov.nih.nci.security.exceptions.CSException;

/**
 * 
 * @version $Revision: 1.3 $
 * @author Joshua Phillips
 * 
 */
public class DefaultSubjectProvider implements SubjectProvider {

    private AuthenticationManager authenticationManager;

   /*
    * (non-Javadoc)
    * 
    * @see gov.nih.nci.cagrid.authentication.common.SubjectProvider#getSubject(gov.nih.nci.cagrid.authentication.bean.Credential)
    */
    public Subject getSubject(Credential credential)
	    throws InvalidCredentialException {

	Subject subject = null;
	AuthenticationManager mgr = getAuthenticationManager();
	try {
	    BasicAuthenticationCredential bac = credential
		    .getBasicAuthenticationCredential();
	    System.out.println("Checking: userId=" + bac.getUserId() + ", password=" + bac.getPassword());
	    subject = mgr.authenticate(bac.getUserId(), bac.getPassword());

	} catch (CSException ex) {
	    throw new InvalidCredentialException("Invalid userid or password!",
		    ex);
	}
	return subject;
    }

    public AuthenticationManager getAuthenticationManager() {
	return authenticationManager;
    }

    public void setAuthenticationManager(
	    AuthenticationManager authenticationManager) {
	this.authenticationManager = authenticationManager;
    }

}
