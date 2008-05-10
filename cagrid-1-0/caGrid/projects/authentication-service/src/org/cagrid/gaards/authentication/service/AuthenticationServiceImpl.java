package org.cagrid.gaards.authentication.service;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.common.AuthenticationProvider;
import org.cagrid.gaards.authentication.common.InsufficientAttributeException;
import org.cagrid.gaards.authentication.common.InvalidCredentialException;
import org.cagrid.gaards.saml.encoding.SAMLUtils;
import org.springframework.core.io.FileSystemResource;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class AuthenticationServiceImpl extends AuthenticationServiceImplBase {

	private AuthenticationProvider auth;

	public AuthenticationServiceImpl() throws RemoteException {
		super();
		try {
			String configFile = AuthenticationServiceConfiguration
					.getConfiguration().getAuthenticationConfiguration();
			String propertiesFile = AuthenticationServiceConfiguration
					.getConfiguration().getAuthenticationProperties();
			BeanUtils utils = new BeanUtils(new FileSystemResource(configFile),
					new FileSystemResource(propertiesFile));
			this.auth = utils.getAuthenticationProvider();
		} catch (Exception ex) {
			throw new RemoteException(
					"Error instantiating AuthenticationProvider: "
							+ ex.getMessage(), ex);
		}
	}

	public gov.nih.nci.cagrid.opensaml.SAMLAssertion authenticateWithIdentityProvider(
			org.cagrid.gaards.authentication.Credential credential)
			throws RemoteException,
			org.cagrid.gaards.authentication.faults.AuthenticationProviderFault,
			org.cagrid.gaards.authentication.faults.CredentialNotSupportedFault,
			org.cagrid.gaards.authentication.faults.InsufficientAttributeFault,
			org.cagrid.gaards.authentication.faults.InvalidCredentialFault {
		try {
			return this.auth.authenticate(credential);
		} catch (InvalidCredentialException ex) {
			org.cagrid.gaards.authentication.faults.InvalidCredentialFault fault = new org.cagrid.gaards.authentication.faults.InvalidCredentialFault();
			fault.setFaultString(ex.getMessage());
			FaultHelper fh = new FaultHelper(fault);
			fh.addFaultCause(ex);
			fault = (org.cagrid.gaards.authentication.faults.InvalidCredentialFault) fh
					.getFault();
			throw fault;
		} catch (InsufficientAttributeException ex) {
			org.cagrid.gaards.authentication.faults.InsufficientAttributeFault fault = new org.cagrid.gaards.authentication.faults.InsufficientAttributeFault();
			fault.setFaultString(ex.getMessage());
			FaultHelper fh = new FaultHelper(fault);
			fh.addFaultCause(ex);
			fault = (org.cagrid.gaards.authentication.faults.InsufficientAttributeFault) fh
					.getFault();
			throw fault;
		} catch (Exception ex) {
			org.cagrid.gaards.authentication.faults.AuthenticationProviderFault fault = new org.cagrid.gaards.authentication.faults.AuthenticationProviderFault();
			fault.setFaultString(ex.getMessage());
			FaultHelper fh = new FaultHelper(fault);
			fh.addFaultCause(ex);
			fault = (org.cagrid.gaards.authentication.faults.AuthenticationProviderFault) fh
					.getFault();
			throw fault;
		}
	}

	public gov.nih.nci.cagrid.authentication.bean.SAMLAssertion authenticate(
			gov.nih.nci.cagrid.authentication.bean.Credential credential)
			throws RemoteException,
			gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault,
			gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault,
			gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault {
		if (credential.getBasicAuthenticationCredential() != null) {
			if (credential.getCredentialExtension() != null) {
				gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault fault = new gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault();
				fault
						.setFaultString("The credential extension cannot be used to authenticate with the deprecated authenticate method, only a basic authentication credential is supported.");
				throw fault;
			} else {
				BasicAuthenticationCredential cred = credential
						.getBasicAuthenticationCredential();
				BasicAuthentication auth = new BasicAuthentication();
				auth.setUserId(cred.getUserId());
				auth.setPassword(cred.getPassword());
				try {
					SAMLAssertion saml = this
							.authenticateWithIdentityProvider(auth);
					gov.nih.nci.cagrid.authentication.bean.SAMLAssertion assertion = new gov.nih.nci.cagrid.authentication.bean.SAMLAssertion();
					assertion.setXml(SAMLUtils.samlAssertionToString(saml));
					return assertion;
				} catch (org.cagrid.gaards.authentication.faults.InsufficientAttributeFault e) {
					gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault fault = new gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault();
					fault.setFaultString(e.getFaultString());
					FaultHelper fh = new FaultHelper(fault);
					fh.addFaultCause(e);
					fault = (gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault) fh
							.getFault();
					throw fault;
				} catch (org.cagrid.gaards.authentication.faults.InvalidCredentialFault e) {
					gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault fault = new gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault();
					fault.setFaultString(e.getFaultString());
					FaultHelper fh = new FaultHelper(fault);
					fh.addFaultCause(e);
					fault = (gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault) fh
							.getFault();
					throw fault;
				} catch (Exception e) {
					gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault fault = new gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault();
					fault.setFaultString(Utils.getExceptionMessage(e));
					FaultHelper fh = new FaultHelper(fault);
					fh.addFaultCause(e);
					fault = (gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault) fh
							.getFault();
					throw fault;
				}
			}

		} else {
			gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault fault = new gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault();
			fault
					.setFaultString("No basic authentication credential was provided, a basic authentication credential is required to authenticate to this service using the deprecated authenticate method.");
			throw fault;
		}
	}

}
