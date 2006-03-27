package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.common.GridTrustServiceI;
import gov.nih.nci.cagrid.gts.service.globus.resource.BaseResourceHome;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;

import java.io.File;
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


	public GridTrustServiceImpl() {

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


	private GTS getGTSHandle() throws GTSInternalFault {
		if (gts == null) {
			try {
				File f = new File(".");
				System.out.println(f.getAbsolutePath());
				EndpointReferenceType type = AddressingUtils.createEndpointReference(null);
				BaseResourceHome home = (BaseResourceHome) ResourceContext.getResourceContext().getResourceHome();
				SimpleResourceManager srm = new SimpleResourceManager(home.getGtsConfig());
				GTSConfiguration conf = (GTSConfiguration) srm.getResource(GTSConfiguration.RESOURCE);
				this.gts = new GTS(conf, type.getAddress().toString());
			} catch (Exception e) {
				FaultHelper.printStackTrace(e);
				GTSInternalFault fault = new GTSInternalFault();
				fault.setFaultString(Utils.getExceptionMessage(e));
				gov.nih.nci.cagrid.common.FaultHelper helper = new gov.nih.nci.cagrid.common.FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (GTSInternalFault) helper.getFault();
				throw fault;
			}
		}
		return gts;
	}


	     public void removeTrustedAuthority(java.lang.String trustedAuthorityName) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		getGTSHandle().removeTrustedAuthority(trustedAuthorityName,getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.TrustedAuthority addTrustedAuthority(gov.nih.nci.cagrid.gts.bean.TrustedAuthority ta) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		return getGTSHandle().addTrustedAuthority(ta,getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.TrustedAuthority[] findTrustedAuthorities(gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter f) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault {
		return getGTSHandle().findTrustAuthorities(f);
	}


	     public void addPermission(gov.nih.nci.cagrid.gts.bean.Permission permission) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		getGTSHandle().addPermission(permission,getCallerIdentity());
	}


	     public gov.nih.nci.cagrid.gts.bean.Permission[] findPermissions(gov.nih.nci.cagrid.gts.bean.PermissionFilter f) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		return getGTSHandle().findPermissions(f,getCallerIdentity());
	}


	     public void revokePermission(gov.nih.nci.cagrid.gts.bean.Permission permission) throws RemoteException, gov.nih.nci.cagrid.gts.stubs.GTSInternalFault, gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault, gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault {
		getGTSHandle().revokePermission(permission,getCallerIdentity());
	}

}
