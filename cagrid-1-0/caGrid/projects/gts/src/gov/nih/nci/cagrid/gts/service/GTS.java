package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
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


	public TrustedAuthority addTrustedAuthority(String gridIdentity, TrustedAuthority ta) throws GTSInternalFault,
		IllegalTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(gridIdentity);
		return trust.addTrustedAuthority(ta);
	}


	private void checkServiceAdministrator(String gridIdentity) throws GTSInternalFault, PermissionDeniedFault {
		PermissionFilter p = new PermissionFilter();
		p.setGridIdentity(gridIdentity);
		p.setRole(Role.TrustServiceAdmin);
		p.setTrustedAuthorityName("*");
		if (permissions.findPermissions(p).length == 0) {
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
