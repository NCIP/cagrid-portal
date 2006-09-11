package gov.nih.nci.cagrid.idp.plugin.ri;

import gov.nih.nci.cagrid.idp.plugin.IdentityProvider;
import gov.nih.nci.cagrid.idp.plugin.SAMLAsserter;
import gov.nih.nci.cagrid.idp.plugin.domain.Credential;
import gov.nih.nci.cagrid.idp.plugin.exception.IdpInternalException;
import gov.nih.nci.cagrid.idp.plugin.exception.InValidCredentialException;
import gov.nih.nci.cagrid.idp.plugin.exception.InsufficientAttributesException;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.authentication.ext.GridIDPAuthenticationManager;
import gov.nih.nci.security.authentication.ext.principals.EmailPrincipal;
import gov.nih.nci.security.authentication.ext.principals.FirstNamePrincipal;
import gov.nih.nci.security.authentication.ext.principals.LastNamePrincipal;
import gov.nih.nci.security.exceptions.CSException;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

public class IdentityProviderImpl implements IdentityProvider {
    
    public IdentityProviderImpl(){
	this.samlAsserter = new SAMLAsserterImpl();
    }

    private SAMLAsserter samlAsserter;

    public SAMLAsserter getSamlAsserter() {
	return samlAsserter;
    }

    public void setSamlAsserter(SAMLAsserter samlAsserter) {
	this.samlAsserter = samlAsserter;
    }

    public SAMLAssertion login(Credential credentials)
	    throws InValidCredentialException, InsufficientAttributesException,
	    IdpInternalException, RemoteException {

	String firstName = "";
	String lastName = "";
	String email = "";
	AuthenticationManager authenticationManager = new GridIDPAuthenticationManager(
		"grid");

	try {
	    boolean loggedIn = authenticationManager.login(credentials
		    .getUserId(), credentials.getPassword());
	    System.out.println("Logged In:" + loggedIn);
	} catch (CSException e) {
	    e.printStackTrace();
	    throw new InValidCredentialException("Invalid userid or password!");
	}

	Subject su = authenticationManager.getSubject();

	Set principals = su.getPrincipals();

	Iterator it = principals.iterator();
	while (it.hasNext()) {
	    Principal p = (Principal) it.next();

	    if (p instanceof FirstNamePrincipal) {
		firstName = p.getName();
	    }
	    if (p instanceof LastNamePrincipal) {
		lastName = p.getName();
	    }
	    if (p instanceof EmailPrincipal) {
		email = p.getName();
	    }
	}
	if (firstName == null || firstName.trim().length() < 1 || lastName == null || lastName.trim().length() < 1
		|| email == null || email.trim().length() < 1) {
	    throw new InsufficientAttributesException(
		    "Missing attributes for the user");
	}

	return getSamlAsserter().getAssertion(credentials.getUserId(),
		firstName, lastName, email);
    }

    public static void main(String[] args) throws Exception {
	Credential credentials = new Credential("user1", "password1");
	IdentityProvider idp = new IdentityProviderImpl();
	((IdentityProviderImpl)idp).setSamlAsserter(new SAMLAsserterImpl());
	System.out.println(idp.login(credentials));
    }
}
