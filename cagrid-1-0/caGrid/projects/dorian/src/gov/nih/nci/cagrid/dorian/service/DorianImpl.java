package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.io.File;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Constants;
import org.globus.wsrf.config.ContainerConfig;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;

/**
 * gov.nih.nci.cagrid.dorianI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class DorianImpl {
	private static final String DORIAN_CONFIG = "dorianConfig";
	private ServiceConfiguration configuration;

	private Dorian dorian;

	public DorianImpl() throws RemoteException {
		try {
			EndpointReferenceType type = AddressingUtils.createEndpointReference(null);
			String configFileEnd = (String)MessageContext.getCurrentContext().getProperty(DORIAN_CONFIG);
			String configFile = ContainerConfig.getBaseDirectory() + File.separator+configFileEnd;
			this.dorian = new Dorian(configFile, type.getAddress().toString());
		} catch (Exception e) {
			FaultHelper.printStackTrace(e);
			throw new RemoteException(Utils.getExceptionMessage(e));
		}
	}

	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return this.configuration;
	}

	private String getCallerIdentity() throws PermissionDeniedFault {
		String caller = SecurityManager.getManager().getCaller();
		System.out.println("Caller: " + caller);
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("No Grid Credentials Provided.");
			throw fault;
		}
		return caller;
	}

	public java.lang.String registerWithIdP(gov.nih.nci.cagrid.dorian.idp.bean.Application application) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidUserPropertyFault {
		return dorian.registerWithIdP(application);
	}

	public gov.nih.nci.cagrid.dorian.idp.bean.IdPUser[] findIdPUsers(gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter filter) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		return dorian.findIdPUsers(getCallerIdentity(), filter);
	}

	public void updateIdPUser(gov.nih.nci.cagrid.dorian.idp.bean.IdPUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.NoSuchUserFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		dorian.updateIdPUser(getCallerIdentity(), user);
	}

	public void removeIdPUser(java.lang.String userId) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		dorian.removeIdPUser(getCallerIdentity(), userId);
	}

	public gov.nih.nci.cagrid.dorian.bean.SAMLAssertion authenticateWithIdP(gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential cred) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
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

	public gov.nih.nci.cagrid.dorian.bean.X509Certificate[] createProxy(gov.nih.nci.cagrid.dorian.bean.SAMLAssertion saml,gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey publicKey,gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime lifetime) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidAssertionFault, gov.nih.nci.cagrid.dorian.stubs.InvalidProxyFault, gov.nih.nci.cagrid.dorian.stubs.UserPolicyFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		try {
			PublicKey key = KeyUtil.loadPublicKey(publicKey.getKeyAsString());
			SAMLAssertion s = SAMLUtils.stringToSAMLAssertion(saml.getXml());
			X509Certificate[] certs = dorian.createProxy(s, key, lifetime);

			gov.nih.nci.cagrid.dorian.bean.X509Certificate[] certList = new gov.nih.nci.cagrid.dorian.bean.X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i++) {
				certList[i] = new gov.nih.nci.cagrid.dorian.bean.X509Certificate();
				certList[i].setCertificateAsString(CertUtil.writeCertificate(certs[i]));
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
			gov.nih.nci.cagrid.common.FaultHelper helper = new gov.nih.nci.cagrid.common.FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

	public gov.nih.nci.cagrid.dorian.bean.X509Certificate getCACertificate() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault {
		X509Certificate cert = dorian.getCACertificate();
		try {
			String certStr = CertUtil.writeCertificate(cert);
			return new gov.nih.nci.cagrid.dorian.bean.X509Certificate(certStr);
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An error while trying to write the CA certificate.");
			throw fault;
		}
	}

	public gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP[] getTrustedIdPs() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		return dorian.getTrustedIdPs(getCallerIdentity());
	}

	public gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP addTrustedIdP(gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP idp) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		return dorian.addTrustedIdP(getCallerIdentity(), idp);
	}

	public void updateTrustedIdP(gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP trustedIdP) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		dorian.updateTrustedIdP(getCallerIdentity(), trustedIdP);
	}

	public void removeTrustedIdP(gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP trustedIdP) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		dorian.removeTrustedIdP(getCallerIdentity(), trustedIdP);
	}

	public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser[] findIFSUsers(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter filter) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		return dorian.findIFSUsers(getCallerIdentity(), filter);
	}

	public void updateIFSUser(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidUserFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		dorian.updateIFSUser(getCallerIdentity(), user);
	}

	public void removeIFSUser(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidUserFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		dorian.removeIFSUser(getCallerIdentity(), user);
	}

	public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser renewIFSUserCredentials(gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser user) throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.InvalidUserFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		return dorian.renewIFSUserCredentials(getCallerIdentity(), user);
	}

	public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy[] getIFSUserPolicies() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault, gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault {
		return dorian.getIFSUserPolicies(getCallerIdentity());
	}

	public void temp() throws RemoteException, gov.nih.nci.cagrid.dorian.stubs.IllegalUserAttributeFault {
		//TODO: Implement this autogenerated method
		throw new RemoteException("Not yet implemented");
	}

}
