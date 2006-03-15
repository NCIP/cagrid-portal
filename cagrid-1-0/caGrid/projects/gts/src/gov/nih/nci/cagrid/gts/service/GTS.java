package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class GTS {

	private GTSConfiguration conf;
	private String gtsURI;
	private TrustedAuthorityManager trust;
	private PermissionManager permissions;


	public GTS(GTSConfiguration conf, String gtsURI) {
		this.conf = conf;
		this.gtsURI = gtsURI;
		Database db = new Database(this.conf.getConnectionManager(), this.conf.getGTSInternalId());
		trust = new TrustedAuthorityManager(this.gtsURI, db);
		permissions = new PermissionManager(db);
	}


	public TrustedAuthority addTrustedAuthority(TrustedAuthority ta, String callerGridIdentity)
		throws GTSInternalFault, IllegalTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		return trust.addTrustedAuthority(ta);
	}


	public TrustedAuthority[] findTrustAuthorities(TrustedAuthorityFilter filter) throws GTSInternalFault {
		return trust.findTrustAuthorities(filter);
	}


	public void addPermission(Permission p, String callerGridIdentity) throws GTSInternalFault, IllegalPermissionFault,
		PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		if (p.getTrustedAuthorityName() != null) {
			if (!trust.doesTrustedAuthorityExist(p.getTrustedAuthorityName())) {
				IllegalPermissionFault fault = new IllegalPermissionFault();
				fault.setFaultString("Cannot add permission, the Trusted Authority (" + p.getTrustedAuthorityName()
					+ ") specified does not exist.");
				throw fault;
			}
		}
		permissions.addPermission(p);
	}


	public Permission[] findPermissions(PermissionFilter filter, String callerGridIdentity) throws GTSInternalFault,
		PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		return permissions.findPermissions(filter);
	}


	private void checkServiceAdministrator(String gridIdentity) throws GTSInternalFault, PermissionDeniedFault {
		if (!permissions.isUserTrustServiceAdmin(gridIdentity)) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are not a trust service administrator!!!");
			throw fault;
		}
	}


	public void destroy() throws GTSInternalFault {
		trust.destroy();
		permissions.destroy();
	}

}
