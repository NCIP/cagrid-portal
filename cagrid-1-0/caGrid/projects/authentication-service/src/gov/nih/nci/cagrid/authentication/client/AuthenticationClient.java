package gov.nih.nci.cagrid.authentication.client;

import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.authentication.client.AuthenticationServiceClient;
import org.cagrid.gaards.saml.encoding.SAMLUtils;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AuthenticationClient {

	private Credential cred;

	private AuthenticationServiceClient client;

	public AuthenticationClient(String serviceURI, Credential cred)
			throws MalformedURIException, RemoteException {
		client = new AuthenticationServiceClient(serviceURI);
		this.cred = cred;
	}

	public SAMLAssertion authenticate() throws RemoteException,
			InvalidCredentialFault, InsufficientAttributeFault,
			AuthenticationProviderFault {
		try {
			String xml = client.authenticate(cred).getXml();
			// System.out.println(XMLUtilities.formatXML(xml));
			return SAMLUtils.stringToSAMLAssertion(xml);
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
