package org.cagrid.gaards.dorian.service;

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.cagrid.gaards.dorian.common.SAMLUtils;
import org.cagrid.gaards.dorian.idp.BasicAuthCredential;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidProxyFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.stubs.types.UserPolicyFault;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.globus.wsrf.Constants;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;
import org.springframework.core.io.FileSystemResource;

/**
 * gov.nih.nci.cagrid.dorianI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class DorianImpl {

	private DorianConfiguration configuration;

	private Dorian dorian;

	public DorianImpl() throws RemoteException {
		try {
			EndpointReferenceType type = AddressingUtils
					.createEndpointReference(null);
			String configFile = DorianConfiguration.getConfiguration()
					.getDorianConfiguration();
			String propertiesFile = DorianConfiguration.getConfiguration()
					.getDorianProperties();
			BeanUtils utils = new BeanUtils(new FileSystemResource(configFile),
					new FileSystemResource(propertiesFile));
			DorianProperties conf = utils.getDorianProperties();
			this.dorian = new Dorian(conf, type.getAddress().toString());
		} catch (Exception e) {
			FaultHelper.printStackTrace(e);
			throw new RemoteException(Utils.getExceptionMessage(e));
		}
	}

	public DorianConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath
				+ "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (DorianConfiguration) initialContext
					.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.",
					e);
		}

		return this.configuration;
	}

	private String getCallerIdentity() throws PermissionDeniedFault {
		String caller = SecurityManager.getManager().getCaller();
		// System.out.println("Caller: " + caller);
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("No Grid Credentials Provided.");
			throw fault;
		}
		return caller;
	}

  public java.lang.String registerWithIdP(org.cagrid.gaards.dorian.idp.Application application) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault {
		return dorian.registerWithIdP(application);
	}

  public org.cagrid.gaards.dorian.idp.IdPUser[] findIdPUsers(org.cagrid.gaards.dorian.idp.IdPUserFilter filter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.findIdPUsers(getCallerIdentity(), filter);
	}

  public void updateIdPUser(org.cagrid.gaards.dorian.idp.IdPUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.NoSuchUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateIdPUser(getCallerIdentity(), user);
	}

  public void removeIdPUser(java.lang.String userId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeIdPUser(getCallerIdentity(), userId);
	}

  public org.cagrid.gaards.dorian.SAMLAssertion authenticateWithIdP(org.cagrid.gaards.dorian.idp.BasicAuthCredential cred) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		SAMLAssertion saml = dorian.authenticate(cred);
		try {
			String xml = SAMLUtils.samlAssertionToString(saml);
			return new org.cagrid.gaards.dorian.SAMLAssertion(xml);
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.setDescription(Utils.getExceptionMessage(e));
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

  public org.cagrid.gaards.dorian.X509Certificate[] createProxy(org.cagrid.gaards.dorian.SAMLAssertion saml,org.cagrid.gaards.dorian.federation.PublicKey publicKey,org.cagrid.gaards.dorian.federation.ProxyLifetime lifetime,org.cagrid.gaards.dorian.federation.DelegationPathLength delegation) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault, org.cagrid.gaards.dorian.stubs.types.InvalidProxyFault, org.cagrid.gaards.dorian.stubs.types.UserPolicyFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		try {
			PublicKey key = KeyUtil.loadPublicKey(publicKey.getKeyAsString());
			SAMLAssertion s = SAMLUtils.stringToSAMLAssertion(saml.getXml());
			X509Certificate[] certs = dorian.createProxy(s, key, lifetime,
					delegation.getLength());

			org.cagrid.gaards.dorian.X509Certificate[] certList = new org.cagrid.gaards.dorian.X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i++) {
				certList[i] = new org.cagrid.gaards.dorian.X509Certificate();
				certList[i].setCertificateAsString(CertUtil
						.writeCertificate(certs[i]));
			}

			return certList;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (UserPolicyFault f) {
			throw f;
		} catch (InvalidProxyFault f) {
			throw f;
		} catch (InvalidAssertionFault f) {
			throw f;
		} catch (DorianInternalFault f) {
			throw f;
		} catch (Exception e) {

			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			gov.nih.nci.cagrid.common.FaultHelper helper = new gov.nih.nci.cagrid.common.FaultHelper(
					fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

  public org.cagrid.gaards.dorian.X509Certificate getCACertificate() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault {
		X509Certificate cert = dorian.getCACertificate();
		try {
			String certStr = CertUtil.writeCertificate(cert);
			return new org.cagrid.gaards.dorian.X509Certificate(certStr);
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An error while trying to write the CA certificate.");
			throw fault;
		}
	}

  public org.cagrid.gaards.dorian.federation.TrustedIdP[] getTrustedIdPs() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getTrustedIdPs(getCallerIdentity());
	}

  public org.cagrid.gaards.dorian.federation.TrustedIdP addTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP idp) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.addTrustedIdP(getCallerIdentity(), idp);
	}

  public void updateTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP trustedIdP) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateTrustedIdP(getCallerIdentity(), trustedIdP);
	}

  public void removeTrustedIdP(org.cagrid.gaards.dorian.federation.TrustedIdP trustedIdP) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeTrustedIdP(getCallerIdentity(), trustedIdP);
	}

  public org.cagrid.gaards.dorian.federation.IFSUser[] findIFSUsers(org.cagrid.gaards.dorian.federation.IFSUserFilter filter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.findIFSUsers(getCallerIdentity(), filter);
	}

  public void updateIFSUser(org.cagrid.gaards.dorian.federation.IFSUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateIFSUser(getCallerIdentity(), user);
	}

  public void removeIFSUser(org.cagrid.gaards.dorian.federation.IFSUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeIFSUser(getCallerIdentity(), user);
	}

  public org.cagrid.gaards.dorian.federation.IFSUser renewIFSUserCredentials(org.cagrid.gaards.dorian.federation.IFSUser user) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.renewIFSUserCredentials(getCallerIdentity(), user);
	}

  public org.cagrid.gaards.dorian.federation.IFSUserPolicy[] getIFSUserPolicies() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getIFSUserPolicies(getCallerIdentity());
	}

  public gov.nih.nci.cagrid.authentication.bean.SAMLAssertion authenticate(gov.nih.nci.cagrid.authentication.bean.Credential credential) throws RemoteException, gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault, gov.nih.nci.cagrid.authentication.stubs.types.InsufficientAttributeFault, gov.nih.nci.cagrid.authentication.stubs.types.AuthenticationProviderFault {

		if (credential.getBasicAuthenticationCredential() == null) {
			InvalidCredentialFault fault = new InvalidCredentialFault();
			fault
					.setFaultString("The Dorian IdP requires a username and password!!!");
			throw fault;
		} else {
			BasicAuthCredential cred = new BasicAuthCredential();
			cred.setUserId(credential.getBasicAuthenticationCredential()
					.getUserId());
			cred.setPassword(credential.getBasicAuthenticationCredential()
					.getPassword());

			try {
				SAMLAssertion saml = dorian.authenticate(cred);
				String xml = SAMLUtils.samlAssertionToString(saml);
				return new gov.nih.nci.cagrid.authentication.bean.SAMLAssertion(
						xml);
			} catch (PermissionDeniedFault f) {
				InvalidCredentialFault fault = new InvalidCredentialFault();
				fault.setFaultString(f.getFaultString());
				throw fault;
			} catch (Exception e) {
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString(e.getMessage());
				FaultHelper helper = new FaultHelper(fault);
				helper.setDescription(Utils.getExceptionMessage(e));
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
		}
	}

  public void addAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.addAdmin(getCallerIdentity(), gridIdentity);
	}

  public void removeAdmin(java.lang.String gridIdentity) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeAdmin(getCallerIdentity(), gridIdentity);
	}

  public java.lang.String[] getAdmins() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getAdmins(getCallerIdentity());
	}

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord requestHostCertificate(org.cagrid.gaards.dorian.federation.HostCertificateRequest req) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateRequestFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.requestHostCertificate(getCallerIdentity(), req);
	}

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord[] getOwnedHostCertificates() throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getOwnedHostCertificates(getCallerIdentity());
	}

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord approveHostCertificate(java.math.BigInteger recordId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.approveHostCertificate(getCallerIdentity(), recordId
				.longValue());
	}

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord[] findHostCertificates(org.cagrid.gaards.dorian.federation.HostCertificateFilter hostCertificateFilter) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.findHostCertificates(getCallerIdentity(),
				hostCertificateFilter);
	}

  public void updateHostCertificateRecord(org.cagrid.gaards.dorian.federation.HostCertificateUpdate hostCertificateUpdate) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateHostCertificateRecord(getCallerIdentity(),
				hostCertificateUpdate);
	}

  public org.cagrid.gaards.dorian.federation.HostCertificateRecord renewHostCertificate(java.math.BigInteger recordId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault {
		return dorian.renewHostCertificate(getCallerIdentity(), recordId
				.longValue());
	}

  public void changeIdPUserPassword(org.cagrid.gaards.dorian.idp.BasicAuthCredential credential,java.lang.String newPassword) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault, org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault, org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault {
		dorian.changeIdPUserPassword(credential, newPassword);
	}

  public boolean doesIdPUserExist(java.lang.String userId) throws RemoteException, org.cagrid.gaards.dorian.stubs.types.DorianInternalFault {
		return dorian.doesIdPUserExist(userId);
	}

}
