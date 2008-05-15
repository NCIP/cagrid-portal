/**
 * $Id: DefaultAuthenticationProvider.java,v 1.1 2008-05-10 01:47:36 langella Exp $
 *
 */
package org.cagrid.gaards.authentication.service;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import javax.security.auth.Subject;

import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.common.AuthenticationProvider;
import org.cagrid.gaards.authentication.common.AuthenticationProviderException;
import org.cagrid.gaards.authentication.common.InsufficientAttributeException;
import org.cagrid.gaards.authentication.common.InvalidCredentialException;
import org.cagrid.gaards.authentication.common.SAMLProvider;
import org.cagrid.gaards.authentication.common.SubjectProvider;

/**
 * 
 * @version $Revision: 1.1 $
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.authentication.common.AuthenticationProvider#authenticate(gov.nih.nci.cagrid.authentication.bean.Credential)
	 */
	public SAMLAssertion authenticate(Credential credential)
			throws RemoteException, InvalidCredentialException,
			InsufficientAttributeException, AuthenticationProviderException {

		try {
			Subject subject = getSubjectProvider().getSubject(credential);
			return getSamlProvider().getSAML(subject);
		} catch (InvalidCredentialException ex) {
			throw ex;
		} catch (InsufficientAttributeException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AuthenticationProviderException("Error authenticating: "
					+ ex.getMessage(), ex);
		}

	}

	public void setSAMLProvider(SAMLProvider samlProvider) {
		this.samlProvider = samlProvider;
	}

	public void setSubjectProvider(SubjectProvider subjectProvider) {
		this.subjectProvider = subjectProvider;
	}

}