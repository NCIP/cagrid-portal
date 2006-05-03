package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.gts.common.GridTrustServiceI;
import gov.nih.nci.cagrid.gts.service.globus.resource.BaseResourceHome;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;

import java.rmi.RemoteException;

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
public class GridTrustServiceImpl implements GridTrustServiceI {

	private GTS gts = null;


	public GridTrustServiceImpl() throws RemoteException {
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


	     public void removeTrustedAuthority(java.lang.String trustedAuthorityName) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.removeTrustedAuthority(trustedAuthorityName, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.TrustedAuthority addTrustedAuthority(gov.nih.nci.cagrid.gts.bean.TrustedAuthority ta) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		return gts.addTrustedAuthority(ta, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.TrustedAuthority[] findTrustedAuthorities(gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter f) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault {
		return gts.findTrustAuthorities(f);
	}


	     public void addPermission(gov.nih.nci.cagrid.gts.bean.Permission permission) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.addPermission(permission, getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.Permission[] findPermissions(gov.nih.nci.cagrid.gts.bean.PermissionFilter f) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		return gts.findPermissions(f, getCallerIdentity());
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


	     public void removeTrustLevel(java.lang.String string) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		gts.removeTrustLevel(string, getCallerIdentity());
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

}
