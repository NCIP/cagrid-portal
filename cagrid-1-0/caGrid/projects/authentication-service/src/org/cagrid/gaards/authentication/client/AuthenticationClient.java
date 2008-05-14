package org.cagrid.gaards.authentication.client;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.authentication.Credential;
import org.cagrid.gaards.authentication.faults.AuthenticationProviderFault;
import org.cagrid.gaards.authentication.faults.InsufficientAttributeFault;
import org.cagrid.gaards.authentication.faults.InvalidCredentialFault;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AuthenticationClient {

	private AuthenticationServiceClient client;

	public AuthenticationClient(String serviceURI)
			throws MalformedURIException, RemoteException {
		client = new AuthenticationServiceClient(serviceURI);
	}

	public SAMLAssertion authenticate(Credential cred) throws RemoteException,
			InvalidCredentialFault, InsufficientAttributeFault,
			AuthenticationProviderFault {
		try {
			return client.authenticateWithIdentityProvider(cred);
		} catch (InvalidCredentialFault gie) {
			throw gie;
		} catch (InsufficientAttributeFault ilf) {
			throw ilf;
		} catch (AuthenticationProviderFault ilf) {
			throw ilf;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public static SAMLAssertion authenticate(String serviceURI, Credential cred)
			throws RemoteException, InvalidCredentialFault,
			InsufficientAttributeFault, AuthenticationProviderFault {
		try {
			AuthenticationServiceClient client = new AuthenticationServiceClient(
					serviceURI);
			return client.authenticateWithIdentityProvider(cred);
		} catch (InvalidCredentialFault gie) {
			throw gie;
		} catch (InsufficientAttributeFault ilf) {
			throw ilf;
		} catch (AuthenticationProviderFault ilf) {
			throw ilf;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
}
