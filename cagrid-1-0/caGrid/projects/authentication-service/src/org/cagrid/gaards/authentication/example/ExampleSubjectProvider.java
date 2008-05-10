package org.cagrid.gaards.authentication.example;

import gov.nih.nci.security.authentication.principal.EmailIdPrincipal;
import gov.nih.nci.security.authentication.principal.FirstNamePrincipal;
import gov.nih.nci.security.authentication.principal.LastNamePrincipal;
import gov.nih.nci.security.authentication.principal.LoginIdPrincipal;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.BasicAuthenticationWithOneTimePassword;
import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.common.InvalidCredentialException;
import org.cagrid.gaards.authentication.common.SubjectProvider;

public class ExampleSubjectProvider implements SubjectProvider {

	public Subject getSubject(Credential credential)
			throws InvalidCredentialException {
		String userId = null;
		if (credential instanceof BasicAuthentication) {
			BasicAuthentication c = (BasicAuthentication) credential;
			if (c.getPassword().equals("password")) {
				userId = c.getUserId();
			} else {
				throw new InvalidCredentialException(
						"Invalid password specified!!!");
			}
		} else if (credential instanceof BasicAuthenticationWithOneTimePassword) {
			BasicAuthenticationWithOneTimePassword c = (BasicAuthenticationWithOneTimePassword) credential;
			if ((c.getPassword().equals("password"))
					&& (c.getOneTimePassword().equals("onetimepassword"))) {
				userId = c.getUserId();
			} else {
				throw new InvalidCredentialException(
						"Invalid password or one time password specified!!!");
			}
		} else {
			// TODO: GENERATE APPROPIATE FAULT
			throw new InvalidCredentialException(
					"Credential type not supported!!!");
		}
		Set<Principal> principals = new HashSet<Principal>();
		principals.add(new LoginIdPrincipal(userId));
		principals.add(new FirstNamePrincipal("John"));
		principals.add(new LastNamePrincipal("Doe"));
		principals.add(new EmailIdPrincipal("jdoe@jdoe.com"));
		Subject subject = new Subject(true, principals, new HashSet(), new HashSet());
		return subject;
	}
}
