package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.IOUtils;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gums.wsrf.IFSCreateProxy;
import gov.nih.nci.cagrid.gums.wsrf.IFSCreateProxyResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSFindTrustedIdPs;
import gov.nih.nci.cagrid.gums.wsrf.IFSFindTrustedIdPsResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSFindUsersResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSGetUserPolicies;
import gov.nih.nci.cagrid.gums.wsrf.IFSGetUserPoliciesResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSRemoveTrustedIdPResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSRemoveUserResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSUpdateTrustedIdPResponse;
import gov.nih.nci.cagrid.gums.wsrf.IFSUpdateUserResponse;
import gov.nih.nci.cagrid.gums.wsrf.IdpFindUsersResponse;
import gov.nih.nci.cagrid.gums.wsrf.IdpRemoveUserResponse;
import gov.nih.nci.cagrid.gums.wsrf.IdpUpdateUserResponse;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;
import org.opensaml.SAMLAssertion;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMSService {

	public static String GUMS_CONFIGURATION_FILE = "etc/gums-conf.xml";

	private GUMS gums;


	public GUMSService() throws RemoteException {
		try {
			EndpointReferenceType type = AddressingUtils.createEndpointReference(null);
			this.gums = new GUMS(GUMS_CONFIGURATION_FILE, type.getAddress().toString());
		} catch (Exception e) {
			throw new RemoteException(IOUtils.getExceptionMessage(e));
		}

	}


	public gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP addTrustedIdP(
		gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.ifs.bean.InvalidTrustedIdPFault, gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		return gums.addTrustedIdP(getCallerIdentity(), parameters);
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSUpdateTrustedIdPResponse updateTrustedIdP(
		gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.ifs.bean.InvalidTrustedIdPFault, gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		gums.updatedTrustedIdP(getCallerIdentity(), parameters);
		return new IFSUpdateTrustedIdPResponse();
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSRemoveTrustedIdPResponse removeTrustedIdP(
		gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.ifs.bean.InvalidTrustedIdPFault, gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		gums.removeTrustedIdP(getCallerIdentity(), parameters);
		return new IFSRemoveTrustedIdPResponse();
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSGetUserPoliciesResponse getIFSUserPolicies(
		gov.nih.nci.cagrid.gums.wsrf.IFSGetUserPolicies parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		return new IFSGetUserPoliciesResponse(gums.getIFSUserPolicies(getCallerIdentity()));
	}


	public gov.nih.nci.cagrid.gums.ifs.bean.IFSUser renewIFSUserCredentials(
		gov.nih.nci.cagrid.gums.ifs.bean.IFSUser parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		return gums.renewIFSUserCredentials(getCallerIdentity(), parameters);
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSFindUsersResponse findIFSUsers(
		gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		return new IFSFindUsersResponse(gums.findIFSUsers(getCallerIdentity(), parameters));
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSUpdateUserResponse updateIFSUser(
		gov.nih.nci.cagrid.gums.ifs.bean.IFSUser parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		gums.updateIFSUser(getCallerIdentity(), parameters);
		return new IFSUpdateUserResponse();
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSRemoveUserResponse removeIFSUser(
		gov.nih.nci.cagrid.gums.ifs.bean.IFSUser parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		gums.removeIFSUser(getCallerIdentity(), parameters);
		return new IFSRemoveUserResponse();
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSFindTrustedIdPsResponse getTrustedIdPs(
		gov.nih.nci.cagrid.gums.wsrf.IFSFindTrustedIdPs parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {
		return new IFSFindTrustedIdPsResponse(gums.getTrustedIdPs(getCallerIdentity()));
	}


	public gov.nih.nci.cagrid.gums.wsrf.IFSCreateProxyResponse createProxy(IFSCreateProxy parameters)
		throws java.rmi.RemoteException, gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault, PermissionDeniedFault,
		gov.nih.nci.cagrid.gums.ifs.bean.UserPolicyFault, gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault,
		GUMSInternalFault {
		try {
			ProxyLifetime lifetime = parameters.getProxyLifetime();
			PublicKey key = KeyUtil.loadPublicKeyFromString(parameters.getPublicKey().getKeyAsString());
			SAMLAssertion saml = IOUtils.stringToSAMLAssertion(parameters.getSAMLAssertion().getXml());
			X509Certificate[] certs = gums.createProxy(saml, key, lifetime);

			gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate[] certList = new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i++) {
				certList[i] = new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate();
				certList[i].setCertificateAsString(CertUtil.writeCertificateToString(certs[i]));
			}

			return new IFSCreateProxyResponse(certList);
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (gov.nih.nci.cagrid.gums.ifs.bean.UserPolicyFault f) {
			throw f;
		} catch (gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault f) {
			throw f;
		} catch (gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault f) {
			throw f;
		} catch (GUMSInternalFault f) {
			throw f;
		} catch (Exception e) {

			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString(gov.nih.nci.cagrid.gums.common.IOUtils.getExceptionMessage(e));
			gov.nih.nci.cagrid.gums.common.FaultHelper helper = new gov.nih.nci.cagrid.gums.common.FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}


	public gov.nih.nci.cagrid.gums.bean.SAMLAssertion authenticateWithIdP(
		gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential auth) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {

		SAMLAssertion saml = gums.authenticate(auth);
		try {
			String xml = IOUtils.samlAssertionToString(saml);
			return new gov.nih.nci.cagrid.gums.bean.SAMLAssertion(xml);
		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.setDescription(gov.nih.nci.cagrid.gums.common.IOUtils.getExceptionMessage(e));
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}


	public String registerWithIdP(Application a) throws RemoteException, InvalidUserPropertyFault, GUMSInternalFault {
		return gums.registerWithIdP(a);
	}


	public gov.nih.nci.cagrid.gums.wsrf.IdpFindUsersResponse findIdPUsers(
		gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter filter) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault, gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {

		IdPUser[] users = gums.findIdPUsers(getCallerIdentity(), filter);
		return new IdpFindUsersResponse(users);
	}


	public gov.nih.nci.cagrid.gums.wsrf.IdpUpdateUserResponse updateIdPUser(
		gov.nih.nci.cagrid.gums.idp.bean.IdPUser user) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault, gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault,
		gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault, gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {

		gums.updateIdPUser(getCallerIdentity(), user);
		return new IdpUpdateUserResponse();
	}


	public gov.nih.nci.cagrid.gums.wsrf.IdpRemoveUserResponse removeIdPUser(java.lang.String parameters)
		throws java.rmi.RemoteException, gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault,
		gov.nih.nci.cagrid.gums.bean.GUMSInternalFault {

		gums.removeIdPUser(getCallerIdentity(), parameters);
		return new IdpRemoveUserResponse();
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


	private GUMSResource getResource() throws RemoteException {
		Object resource = null;
		try {
			resource = ResourceContext.getResourceContext().getResource();

		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		GUMSResource mathResource = (GUMSResource) resource;
		return mathResource;
	}

}