package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.gts.service.globus.ServiceConfiguration;
import gov.nih.nci.cagrid.gts.service.globus.resource.BaseResourceHome;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;
import org.globus.wsrf.utils.AddressingUtils;


/**
 * gov.nih.nci.cagrid.gtsI TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 */
public class GTSImpl {
	private ServiceConfiguration configuration;
	private GTS gts = null;


	public GTSImpl() throws RemoteException {
		try {
			EndpointReferenceType type = AddressingUtils.createEndpointReference(null);
			BaseResourceHome home = (BaseResourceHome) ResourceContext.getResourceContext().getResourceHome();
			SimpleResourceManager srm = new SimpleResourceManager(home.getGtsConfig());
			GTSConfiguration conf = (GTSConfiguration) srm.getResource(GTSConfiguration.RESOURCE);
			this.gts = new GTS(conf, type.getAddress().toString());
		} catch (Exception e) {
			FaultHelper.printStackTrace(e);
			throw new RemoteException("Error configuring Grid Trust Service");
		}
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


	     public gov.nih.nci.cagrid.gts.bean.TrustedAuthority addTrustedAuthority(gov.nih.nci.cagrid.gts.bean.TrustedAuthority ta) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		return gts.addTrustedAuthority(ta, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.TrustedAuthority[] findTrustedAuthorities(gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter filter) throws RemoteException {
		return gts.findTrustAuthorities(filter);
	}


	     public void removeTrustedAuthority(java.lang.String trustedAuthorityName) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.removeTrustedAuthority(trustedAuthorityName, getCallerIdentity());
	}


	     public void addPermission(gov.nih.nci.cagrid.gts.bean.Permission permission) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.addPermission(permission, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.Permission[] findPermissions(gov.nih.nci.cagrid.gts.bean.PermissionFilter filter) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		return gts.findPermissions(filter, getCallerIdentity());
	}


	     public void revokePermission(gov.nih.nci.cagrid.gts.bean.Permission permission) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.revokePermission(permission, getCallerIdentity());
	}


	     public void updateTrustedAuthority(gov.nih.nci.cagrid.gts.bean.TrustedAuthority ta) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.updateTrustedAuthority(ta, getCallerIdentity());
	}


	     public void addTrustLevel(gov.nih.nci.cagrid.gts.bean.TrustLevel trustLevel) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.addTrustLevel(trustLevel, getCallerIdentity());
	}


	     public void updateTrustLevel(gov.nih.nci.cagrid.gts.bean.TrustLevel trustLevel) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.updateTrustLevel(trustLevel, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.TrustLevel[] getTrustLevels() throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault {
		return gts.getTrustLevels();
	}


	     public void removeTrustLevel(java.lang.String trustLevelName) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.removeTrustLevel(trustLevelName, getCallerIdentity());
	}


	     public void addAuthority(gov.nih.nci.cagrid.gts.bean.AuthorityGTS authorityGTS) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.addAuthority(authorityGTS, getCallerIdentity());
	}


	     public void updateAuthority(gov.nih.nci.cagrid.gts.bean.AuthorityGTS authorityGTS) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault, gov.nih.nci.cagrid.gts.stubs.InvalidAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.updateAuthority(authorityGTS, getCallerIdentity());
	}


	     public void updateAuthorityPriorities(gov.nih.nci.cagrid.gts.bean.AuthorityPriorityUpdate authorityPriorityUpdate) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.updateAuthorityPriorities(authorityPriorityUpdate, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.AuthorityGTS[] getAuthorities() throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault {
		return gts.getAuthorities();
	}


	     public void removeAuthority(java.lang.String serviceURI) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.removeAuthority(serviceURI, getCallerIdentity());
	}


	     public void updateCRL(java.lang.String trustedAuthorityName,gov.nih.nci.cagrid.gts.bean.X509CRL crl) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.updateCRL(trustedAuthorityName, crl, getCallerIdentity());
	}


	     public boolean validate(gov.nih.nci.cagrid.gts.bean.X509Certificate[] chain,gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter filter) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.CertificateValidationFault {
		return gts.validate(chain, filter);
	}

}
