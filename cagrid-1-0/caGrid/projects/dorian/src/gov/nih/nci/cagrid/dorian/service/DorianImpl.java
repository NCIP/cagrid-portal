package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.authentication.stubs.types.InvalidCredentialFault;
import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
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

  public java.lang.String registerWithIdP(gov.nih.nci.cagrid.dorian.idp.bean.Application application) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault {
		return dorian.registerWithIdP(application);
	}

  public gov.nih.nci.cagrid.dorian.idp.bean.IdPUser[] findIdPUsers(gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter filter) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.findIdPUsers(getCallerIdentity(), filter);
	}

  public void updateIdPUser(gov.nih.nci.cagrid.dorian.idp.bean.IdPUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.NoSuchUserFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateIdPUser(getCallerIdentity(), user);
	}

  public void removeIdPUser(java.lang.String userId) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeIdPUser(getCallerIdentity(), userId);
	}

  public gov.nih.nci.cagrid.dorian.bean.SAMLAssertion authenticateWithIdP(gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential cred) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		SAMLAssertion saml = dorian.authenticate(cred);
		try {
			String xml = SAMLUtils.samlAssertionToString(saml);
			return new gov.nih.nci.cagrid.dorian.bean.SAMLAssertion(xml);
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

  public gov.nih.nci.cagrid.dorian.bean.X509Certificate[] createProxy(gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml,gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey publicKey,gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime lifetime,gov.nih.nci.cagrid.dorian.ifs.bean.DelegationPathLength delegation) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault, gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		try {
			PublicKey key = KeyUtil.loadPublicKey(publicKey.getKeyAsString());
			SAMLAssertion s = SAMLUtils.stringToSAMLAssertion(saml.getXml());
			X509Certificate[] certs = dorian.createProxy(s, key, lifetime,
					delegation.getLength());

			gov.nih.nci.cagrid.dorian.bean.X509Certificate[] certList = new gov.nih.nci.cagrid.dorian.bean.X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i++) {
				certList[i] = new gov.nih.nci.cagrid.dorian.bean.X509Certificate();
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

  public gov.nih.nci.cagrid.dorian.bean.X509Certificate getCACertificate() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault {
		X509Certificate cert = dorian.getCACertificate();
		try {
			String certStr = CertUtil.writeCertificate(cert);
			return new gov.nih.nci.cagrid.dorian.bean.X509Certificate(certStr);
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An error while trying to write the CA certificate.");
			throw fault;
		}
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP[] getTrustedIdPs() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getTrustedIdPs(getCallerIdentity());
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP addTrustedIdP(gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP idp) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.addTrustedIdP(getCallerIdentity(), idp);
	}

  public void updateTrustedIdP(gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP trustedIdP) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateTrustedIdP(getCallerIdentity(), trustedIdP);
	}

  public void removeTrustedIdP(gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP trustedIdP) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeTrustedIdP(getCallerIdentity(), trustedIdP);
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser[] findIFSUsers(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter filter) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.findIFSUsers(getCallerIdentity(), filter);
	}

  public void updateIFSUser(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateIFSUser(getCallerIdentity(), user);
	}

  public void removeIFSUser(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeIFSUser(getCallerIdentity(), user);
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser renewIFSUserCredentials(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.renewIFSUserCredentials(getCallerIdentity(), user);
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy[] getIFSUserPolicies() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
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

  public void addAdmin(java.lang.String gridIdentity) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.addAdmin(getCallerIdentity(), gridIdentity);
	}

  public void removeAdmin(java.lang.String gridIdentity) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.removeAdmin(getCallerIdentity(), gridIdentity);
	}

  public java.lang.String[] getAdmins() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getAdmins(getCallerIdentity());
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord requestHostCertificate(gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRequest req) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateRequestFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.requestHostCertificate(getCallerIdentity(), req);
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord[] getOwnedHostCertificates() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.getOwnedHostCertificates(getCallerIdentity());
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord approveHostCertificate(java.math.BigInteger recordId) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.approveHostCertificate(getCallerIdentity(), recordId
				.longValue());
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord[] findHostCertificates(gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateFilter hostCertificateFilter) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.findHostCertificates(getCallerIdentity(),
				hostCertificateFilter);
	}

  public void updateHostCertificateRecord(gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateUpdate hostCertificateUpdate) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		dorian.updateHostCertificateRecord(getCallerIdentity(),
				hostCertificateUpdate);
	}

  public gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateRecord renewHostCertificate(java.math.BigInteger recordId) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidHostCertificateFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault {
		return dorian.renewHostCertificate(getCallerIdentity(), recordId
				.longValue());
	}

  public void changeIdPUserPassword(gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential credential,java.lang.String newPassword) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault {
		dorian.changeIdPUserPassword(credential, newPassword);
	}

  public boolean doesIdPUserExist(java.lang.String userId) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault {
		return dorian.doesIdPUserExist(userId);
	}

}
