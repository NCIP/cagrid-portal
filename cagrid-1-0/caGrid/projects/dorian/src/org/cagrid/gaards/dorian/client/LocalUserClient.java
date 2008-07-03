package org.cagrid.gaards.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.exceptions.InvalidResourcePropertyException;
import gov.nih.nci.cagrid.metadata.exceptions.RemoteResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.gaards.authentication.faults.AuthenticationProviderFault;
import org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;
import org.cagrid.gaards.dorian.common.DorianFault;
import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.BasicAuthCredential;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class LocalUserClient {

	private DorianClient client;
	private String serviceURL;

	public LocalUserClient(String serviceURL) throws MalformedURIException,
			RemoteException {
		this.serviceURL = serviceURL;
		client = new DorianClient(serviceURL);
	}

	/**
	 * This method allows a client to determine whether or not a user id is
	 * already registered with the Dorian Identity Provider.
	 * 
	 * @param userId
	 *            The user id to determine whether or not is registered.
	 * @return True is returned a user with the user id is registered with the
	 *         Dorian Identity Provider, otherwise False is returned.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 */

	public boolean doesUserExist(String userId) throws DorianFault,
			DorianInternalFault {
		try {
			return client.doesLocalUserExist(userId);
		} catch (DorianInternalFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	/**
	 * This method allows a user to authenticate with Dorian's Identity
	 * Provider. If the authentication is is successful, a sign SAML Assertion
	 * will be issued asserting that they successfully authenticated. The signed
	 * SAML assertion can be used to request a PKI or Grid credential from
	 * Dorian that can be used to authenticate to the Grid.
	 * 
	 * @param cred
	 *            The user id and password of the user authenticating.
	 * @return A signed SAML Assertion, asserting that the user successfully
	 *         authenticated.
	 * @throws DorianFault
	 * @throws AuthenticationProviderFault
	 * @throws InvalidCredentialFault
	 * @throws CredentialNotSupportedFault
	 * @throws AuthenticationProviderFault
	 */

	public SAMLAssertion authenticate(Credential cred) throws DorianFault,
			AuthenticationProviderFault, InvalidCredentialFault,
			CredentialNotSupportedFault, AuthenticationProviderFault {

		try {
			AuthenticationClient auth = new AuthenticationClient(
					this.serviceURL);
			return auth.authenticate(cred);
		} catch (InvalidCredentialFault f) {
			throw f;
		} catch (CredentialNotSupportedFault f) {
			throw f;
		} catch (AuthenticationProviderFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	/**
	 * This method allows a user to change the password for their account with
	 * the Dorian Identity Provider.
	 * 
	 * @param cred
	 *            The user's existing user id and password.
	 * @param newPassword
	 *            The user's new password.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 * @throws PermissionDeniedFault
	 * @throws InvalidUserPropertyFault
	 */

	public void changePassword(BasicAuthentication cred, String newPassword)
			throws DorianFault, DorianInternalFault, PermissionDeniedFault,
			InvalidUserPropertyFault {
		try {
			AuthenticationClient auth = new AuthenticationClient(
					this.serviceURL);
			if (auth.getSupportedAuthenticationProfiles() == null) {
				BasicAuthCredential bac = new BasicAuthCredential();
				bac.setUserId(cred.getUserId());
				bac.setPassword(cred.getPassword());
				client.changeIdPUserPassword(bac, newPassword);
			} else {
				client.changeLocalUserPassword(cred, newPassword);
			}
		} catch (DorianInternalFault f) {
			throw f;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (InvalidUserPropertyFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	/**
	 * This method allow a client to apply for a user account with the Dorian
	 * Identity Provider.
	 * 
	 * @param a
	 *            The user application.
	 * @return A message regarding the status of the application.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 * @throws InvalidUserPropertyFault
	 */
	public String register(Application a) throws DorianFault,
			DorianInternalFault, InvalidUserPropertyFault {
		try {
			return client.registerWithIdP(a);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (InvalidUserPropertyFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	/**
	 * This method obtains the authentication profiles supported by the Dorian
	 * that the client is connecting to. The authentication profiles are
	 * represented by the resource property:
	 * (http://gaards.cagrid.org/authentication,AuthenticationProfiles).
	 * 
	 * @return If the resource property exists a set is returned containing the
	 *         QName(s) of the authentication profiles supported. If the
	 *         resource property does not exist null is returned.
	 * 
	 * @throws InvalidResourcePropertyException
	 * @throws RemoteResourcePropertyRetrievalException
	 * @throws ResourcePropertyRetrievalException
	 */
	public Set<QName> getSupportedAuthenticationProfiles()
			throws ResourcePropertyRetrievalException {
		AuthenticationClient auth = null;
		try {
			auth = new AuthenticationClient(this.serviceURL);
		} catch (Exception e) {
			throw new ResourcePropertyRetrievalException(
					"Unexpected error retrieving authentication profiles: "
							+ Utils.getExceptionMessage(e), e);
		}
		return auth.getSupportedAuthenticationProfiles();
	}

}
