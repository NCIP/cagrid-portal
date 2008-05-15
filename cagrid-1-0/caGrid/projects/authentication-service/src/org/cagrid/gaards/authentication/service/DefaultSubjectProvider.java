/**
 * $Id: DefaultSubjectProvider.java,v 1.2 2008-05-15 19:54:01 langella Exp $
 *
 */
package org.cagrid.gaards.authentication.service;

import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.exceptions.CSException;

import javax.security.auth.Subject;

import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.common.InvalidCredentialException;

/**
 * 
 * @version $Revision: 1.2 $
 * @author Joshua Phillips
 * 
 */
public class DefaultSubjectProvider extends BasicAuthenticationSubjectProvider {

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
		if (credential instanceof BasicAuthentication) {
			try {
				BasicAuthentication bac = (BasicAuthentication) credential;
				// System.out.println("Checking: userId=" + bac.getUserId() + ",
				// password=" + bac.getPassword());
				subject = mgr.authenticate(bac.getUserId(), bac.getPassword());
			} catch (CSException ex) {
				throw new InvalidCredentialException(
						"Invalid userid or password!", ex);
			}
			return subject;
		} else {
			throw new InvalidCredentialException(
					"The credential type submitted is not supported by this service.");
		}
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

}
