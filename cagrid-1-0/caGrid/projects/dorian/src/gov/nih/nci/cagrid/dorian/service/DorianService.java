package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.IOUtils;
import gov.nih.nci.cagrid.dorian.common.ca.CertUtil;
import gov.nih.nci.cagrid.dorian.common.ca.KeyUtil;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.wsrf.IFSCreateProxy;
import gov.nih.nci.cagrid.dorian.wsrf.IFSCreateProxyResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSFindTrustedIdPsResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSFindUsersResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSGetUserPoliciesResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSRemoveTrustedIdPResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSRemoveUserResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSUpdateTrustedIdPResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IFSUpdateUserResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IdpFindUsersResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IdpRemoveUserResponse;
import gov.nih.nci.cagrid.dorian.wsrf.IdpUpdateUserResponse;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DorianService {

	public static String CONFIGURATION_FILE = "etc/dorian/dorian-conf.xml";

	private Dorian dorian;


	public DorianService() throws RemoteException {
		try {
			EndpointReferenceType type = AddressingUtils.createEndpointReference(null);
			this.dorian = new Dorian(CONFIGURATION_FILE, type.getAddress().toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(Utils.getExceptionMessage(e));
		}

	}


	public gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP addTrustedIdP(
		gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.ifs.bean.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		return dorian.addTrustedIdP(getCallerIdentity(), parameters);
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSUpdateTrustedIdPResponse updateTrustedIdP(
		gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.ifs.bean.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		dorian.updatedTrustedIdP(getCallerIdentity(), parameters);
		return new IFSUpdateTrustedIdPResponse();
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSRemoveTrustedIdPResponse removeTrustedIdP(
		gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.ifs.bean.InvalidTrustedIdPFault, gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		dorian.removeTrustedIdP(getCallerIdentity(), parameters);
		return new IFSRemoveTrustedIdPResponse();
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSGetUserPoliciesResponse getIFSUserPolicies(
		gov.nih.nci.cagrid.dorian.wsrf.IFSGetUserPolicies parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		return new IFSGetUserPoliciesResponse(dorian.getIFSUserPolicies(getCallerIdentity()));
	}


	public gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser renewIFSUserCredentials(
		gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		return dorian.renewIFSUserCredentials(getCallerIdentity(), parameters);
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSFindUsersResponse findIFSUsers(
		gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		return new IFSFindUsersResponse(dorian.findIFSUsers(getCallerIdentity(), parameters));
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSUpdateUserResponse updateIFSUser(
		gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		dorian.updateIFSUser(getCallerIdentity(), parameters);
		return new IFSUpdateUserResponse();
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSRemoveUserResponse removeIFSUser(
		gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		dorian.removeIFSUser(getCallerIdentity(), parameters);
		return new IFSRemoveUserResponse();
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSFindTrustedIdPsResponse getTrustedIdPs(
		gov.nih.nci.cagrid.dorian.wsrf.IFSFindTrustedIdPs parameters) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidUserFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {
		return new IFSFindTrustedIdPsResponse(dorian.getTrustedIdPs(getCallerIdentity()));
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IFSCreateProxyResponse createProxy(IFSCreateProxy parameters)
		throws java.rmi.RemoteException, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidAssertionFault, PermissionDeniedFault,
		gov.nih.nci.cagrid.dorian.ifs.bean.UserPolicyFault, gov.nih.nci.cagrid.dorian.ifs.bean.InvalidProxyFault,
		DorianInternalFault {
		try {
			ProxyLifetime lifetime = parameters.getProxyLifetime();
			PublicKey key = KeyUtil.loadPublicKeyFromString(parameters.getPublicKey().getKeyAsString());
			SAMLAssertion saml = IOUtils.stringToSAMLAssertion(parameters.getSAMLAssertion().getXml());
			X509Certificate[] certs = dorian.createProxy(saml, key, lifetime);

			gov.nih.nci.cagrid.dorian.ifs.bean.X509Certificate[] certList = new gov.nih.nci.cagrid.dorian.ifs.bean.X509Certificate[certs.length];
			for (int i = 0; i < certs.length; i++) {
				certList[i] = new gov.nih.nci.cagrid.dorian.ifs.bean.X509Certificate();
				certList[i].setCertificateAsString(CertUtil.writeCertificateToString(certs[i]));
			}

			return new IFSCreateProxyResponse(certList);
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (gov.nih.nci.cagrid.dorian.ifs.bean.UserPolicyFault f) {
			throw f;
		} catch (gov.nih.nci.cagrid.dorian.ifs.bean.InvalidProxyFault f) {
			throw f;
		} catch (gov.nih.nci.cagrid.dorian.ifs.bean.InvalidAssertionFault f) {
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


	public gov.nih.nci.cagrid.dorian.bean.SAMLAssertion authenticateWithIdP(
		gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential auth) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {

		SAMLAssertion saml = dorian.authenticate(auth);
		try {
			String xml = IOUtils.samlAssertionToString(saml);
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


	public String registerWithIdP(Application a) throws RemoteException, InvalidUserPropertyFault, DorianInternalFault {
		return dorian.registerWithIdP(a);
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IdpFindUsersResponse findIdPUsers(
		gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter filter) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault, gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {

		IdPUser[] users = dorian.findIdPUsers(getCallerIdentity(), filter);
		return new IdpFindUsersResponse(users);
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IdpUpdateUserResponse updateIdPUser(
		gov.nih.nci.cagrid.dorian.idp.bean.IdPUser user) throws java.rmi.RemoteException,
		gov.nih.nci.cagrid.dorian.idp.bean.InvalidUserPropertyFault, gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault,
		gov.nih.nci.cagrid.dorian.idp.bean.NoSuchUserFault, gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {

		dorian.updateIdPUser(getCallerIdentity(), user);
		return new IdpUpdateUserResponse();
	}


	public gov.nih.nci.cagrid.dorian.wsrf.IdpRemoveUserResponse removeIdPUser(java.lang.String parameters)
		throws java.rmi.RemoteException, gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault,
		gov.nih.nci.cagrid.dorian.bean.DorianInternalFault {

		dorian.removeIdPUser(getCallerIdentity(), parameters);
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


	private DorianResource getResource() throws RemoteException {
		Object resource = null;
		try {
			resource = ResourceContext.getResourceContext().getResource();

		} catch (Exception e) {
			throw new RemoteException("Unable to access resource.", e);
		}

		DorianResource mathResource = (DorianResource) resource;
		return mathResource;
	}

}