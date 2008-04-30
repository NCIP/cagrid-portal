package edu.osu.idp.service;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.common.AuthenticationProvider;
import gov.nih.nci.cagrid.authentication.common.AuthenticationProviderException;
import gov.nih.nci.cagrid.authentication.common.InsufficientAttributeException;
import gov.nih.nci.cagrid.authentication.common.InvalidCredentialException;
import gov.nih.nci.cagrid.authentication.common.SAMLProvider;
import gov.nih.nci.cagrid.authentication.common.SubjectProvider;
import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLException;
import gov.nih.nci.cagrid.opensaml.SAMLResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.cagrid.gaards.pki.SecurityUtil;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class OSUAuthenticationProvider implements AuthenticationProvider {

	private final static String IDP_URL = "https://authdev.it.ohio-state.edu/shibboleth-grid/GRID?providerId=dorian";


	public OSUAuthenticationProvider() {
		super();
		SecurityUtil.init();
	}





	public SAMLAssertion authenticate(Credential credential) throws RemoteException, InvalidCredentialException,
		InsufficientAttributeException, AuthenticationProviderException {
		if (credential.getBasicAuthenticationCredential() == null) {
			InvalidCredentialFault fault = new InvalidCredentialFault();
			fault.setFaultString("The OSU IdP requires a username and password!!!");
			throw fault;
		} else {
			BasicAuthenticationCredential cred = credential.getBasicAuthenticationCredential();
			Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
			HttpClient client = new HttpClient();
			client.getState().setCredentials(new AuthScope(null, 443, null),
				new UsernamePasswordCredentials(cred.getUserId(), cred.getPassword()));
			GetMethod get = new GetMethod(IDP_URL);

			get.setDoAuthentication(true);

			try {
				// execute the GET
				int status = client.executeMethod(get);
				if (status == 401) {
					throw new InvalidCredentialException("Invalid Login Specified!!!");
				} else if (status > 200) {
					throw new AuthenticationProviderException("Error authenticating with server. (" + status + ")");
				}

				// print the status and response
				// System.out.println(status + "\n" +
				// get.getResponseBodyAsString());
				SAMLResponse res = new SAMLResponse(new ByteArrayInputStream(get.getResponseBodyAsString().getBytes()));
				SAMLAssertion saml = (SAMLAssertion) res.getAssertions().next();
				return saml;
			} catch (SAMLException se) {
				throw new AuthenticationProviderException(se.getMessage());
			} catch (IOException io) {
				throw new AuthenticationProviderException(io.getMessage());
			} finally {
				// release any connection resources used by the method
				get.releaseConnection();
			}
		}
	}


	public void setSAMLProvider(SAMLProvider samlProvider) {
		// TODO Auto-generated method stub

	}


	public void setSubjectProvider(SubjectProvider subjectProvider) {
		// TODO Auto-generated method stub

	}

}
