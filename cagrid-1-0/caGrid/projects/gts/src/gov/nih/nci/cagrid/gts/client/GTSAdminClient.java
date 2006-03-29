package gov.nih.nci.cagrid.gts.client;

import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;

import java.rmi.RemoteException;

import org.globus.gsi.GlobusCredential;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class GTSAdminClient {

	GridTrustServiceClient client;


	public GTSAdminClient(String url, GlobusCredential proxy) {
		this.client = new GridTrustServiceClient(url, proxy);
	}


	public TrustedAuthority addTrustedAuthority(TrustedAuthority ta) throws RemoteException, GTSInternalFault,
		IllegalTrustedAuthorityFault, PermissionDeniedFault {
		return client.addTrustedAuthority(ta);
	}


	public void updateTrustedAuthority(TrustedAuthority ta) throws RemoteException, GTSInternalFault,
		IllegalTrustedAuthorityFault, InvalidTrustedAuthorityFault, PermissionDeniedFault {
		client.updateTrustedAuthority(ta);
	}


	public void removeTrustedAuthority(java.lang.String trustedAuthorityName) throws RemoteException, GTSInternalFault,
		InvalidTrustedAuthorityFault, PermissionDeniedFault {
		client.removeTrustedAuthority(trustedAuthorityName);
	}


	public Permission[] findPermissions(PermissionFilter f) throws RemoteException, GTSInternalFault,
		PermissionDeniedFault {
		return client.findPermissions(f);
	}


	public void addPermission(Permission permission) throws RemoteException, GTSInternalFault, IllegalPermissionFault,
		PermissionDeniedFault {
		client.addPermission(permission);
	}


	public void revokePermission(Permission permission) throws RemoteException, GTSInternalFault,
		InvalidPermissionFault, PermissionDeniedFault {
		client.revokePermission(permission);
	}

}
