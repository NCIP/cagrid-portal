package org.cagrid.gaards.authentication.client;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.io.File;
import java.rmi.RemoteException;
import java.security.cert.X509Certificate;

import org.apache.axis.client.Stub;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.authentication.BasicAuthenticationWithOneTimePassword;
import org.cagrid.gaards.authentication.common.AuthenticationServiceI;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.saml.encoding.SAMLUtils;
import org.globus.gsi.GlobusCredential;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS
 * METHODS.
 * 
 * This client is generated automatically by Introduce to provide a clean
 * unwrapped API to the service.
 * 
 * On construction the class instance will contact the remote service and
 * retrieve it's security metadata description which it will use to configure
 * the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class AuthenticationServiceClient extends
		AuthenticationServiceClientBase implements AuthenticationServiceI {

	public AuthenticationServiceClient(String url)
			throws MalformedURIException, RemoteException {
		this(url, null);
	}

	public AuthenticationServiceClient(String url, GlobusCredential proxy)
			throws MalformedURIException, RemoteException {
		super(url, proxy);
	}

	public AuthenticationServiceClient(EndpointReferenceType epr)
			throws MalformedURIException, RemoteException {
		this(epr, null);
	}

	public AuthenticationServiceClient(EndpointReferenceType epr,
			GlobusCredential proxy) throws MalformedURIException,
			RemoteException {
		super(epr, proxy);
	}

	public static void usage() {
		System.out.println(AuthenticationServiceClient.class.getName()
				+ " -url <service url>");
	}

	public static void main(String[] args) {
		System.out.println("Running the Grid Service Client");
		try {
			X509Certificate cert = CertUtil
					.loadCertificate(new File(
							"/Users/langella/certificates/wright.bmi.ohio-state.edu-cert.pem"));

			System.out.println(cert.getSubjectDN().toString());

			AuthenticationServiceClient client = new AuthenticationServiceClient(
					"https://localhost:8443/wsrf/services/cagrid/AuthenticationService");
			/*
			BasicAuthentication cred = new BasicAuthentication();
			cred.setUserId("jdoe");
			cred.setPassword("password");
			*/
			BasicAuthenticationWithOneTimePassword cred = new BasicAuthenticationWithOneTimePassword();
			cred.setUserId("jdoe2");
			cred.setPassword("password");
			cred.setOneTimePassword("onetimepassword");
			
			SAMLAssertion saml = client.authenticateWithIdentityProvider(cred);
			saml.verify(cert);
			System.out.println(SAMLUtils.samlAssertionToString(saml));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getMultipleResourceProperties");
    return portType.getMultipleResourceProperties(params);
    }
  }

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getResourceProperty");
    return portType.getResourceProperty(params);
    }
  }

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"queryResourceProperties");
    return portType.queryResourceProperties(params);
    }
  }

  public gov.nih.nci.cagrid.opensaml.SAMLAssertion authenticateWithIdentityProvider(org.cagrid.gaards.authentication.Credential credential) throws RemoteException, org.cagrid.gaards.authentication.faults.AuthenticationProviderFault, org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault, org.cagrid.gaards.authentication.faults.InsufficientAttributeFault, org.cagrid.gaards.authentication.faults.InvalidCredentialFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"authenticateWithIdentityProvider");
    org.cagrid.gaards.authentication.stubs.AuthenticateWithIdentityProviderRequest params = new org.cagrid.gaards.authentication.stubs.AuthenticateWithIdentityProviderRequest();
    org.cagrid.gaards.authentication.stubs.AuthenticateWithIdentityProviderRequestCredential credentialContainer = new org.cagrid.gaards.authentication.stubs.AuthenticateWithIdentityProviderRequestCredential();
    credentialContainer.setCredential(credential);
    params.setCredential(credentialContainer);
    org.cagrid.gaards.authentication.stubs.AuthenticateWithIdentityProviderResponse boxedResult = portType.authenticateWithIdentityProvider(params);
    return boxedResult.getAssertion();
    }
  }

  public gov.nih.nci.cagrid.authentication.bean.SAMLAssertion authenticate(gov.nih.nci.cagrid.authentication.bean.Credential credential) throws RemoteException, gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault, gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault, gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"authenticate");
    gov.nih.nci.cagrid.authentication.AuthenticateRequest params = new gov.nih.nci.cagrid.authentication.AuthenticateRequest();
    gov.nih.nci.cagrid.authentication.AuthenticateRequestCredential credentialContainer = new gov.nih.nci.cagrid.authentication.AuthenticateRequestCredential();
    credentialContainer.setCredential(credential);
    params.setCredential(credentialContainer);
    gov.nih.nci.cagrid.authentication.AuthenticateResponse boxedResult = portType.authenticate(params);
    return boxedResult.getSAMLAssertion();
    }
  }

}