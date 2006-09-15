/**
 * $Id: DefaultSubjectProvider.java,v 1.1 2006-09-15 10:52:46 joshua Exp $
 *
 */
package gov.nih.nci.cagrid.authentication.service;

import javax.security.auth.Subject;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.cagrid.authentication.common.SubjectProvider;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.exceptions.CSException;

/**
 * 
 * @version $Revision: 1.1 $
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
	    mgr.login(bac.getUserId(), bac.getPassword());
	    subject = mgr.getSubject();
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
