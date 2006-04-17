package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class GTS implements TrustLevelStatus, TrustLevelLookup {

	private GTSConfiguration conf;
	private String gtsURI;
	private TrustedAuthorityManager trust;
	private PermissionManager permissions;
	private TrustLevelManager levels;


	public GTS(GTSConfiguration conf, String gtsURI) {
		this.conf = conf;
		this.gtsURI = gtsURI;
		Database db = new Database(this.conf.getConnectionManager(), this.conf.getGTSInternalId());
		trust = new TrustedAuthorityManager(this.gtsURI, this, db);
		levels = new TrustLevelManager(this, db);
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


	public void updateTrustedAuthority(TrustedAuthority ta, String callerGridIdentity) throws GTSInternalFault,
		IllegalTrustedAuthorityFault, InvalidTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		trust.updateTrustedAuthority(ta);
	}


	public void removeTrustedAuthority(String name, String callerGridIdentity) throws GTSInternalFault,
		InvalidTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		trust.removeTrustedAuthority(name);
	}


	public void addTrustLevel(TrustLevel level, String callerGridIdentity) throws GTSInternalFault,
		IllegalTrustLevelFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		levels.addTrustLevel(level);
	}


	public void removeTrustLevel(String name, String callerGridIdentity) throws GTSInternalFault,
		InvalidTrustLevelFault, IllegalTrustLevelFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		levels.removeTrustLevel(name);
	}


	public void updateTrustLevel(TrustLevel level, String callerGridIdentity) throws GTSInternalFault,
		InvalidTrustLevelFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		levels.updateTrustLevel(level);
	}


	public TrustLevel[] getTrustLevels() throws GTSInternalFault {
		return levels.getTrustLevels();
	}


	public void addPermission(Permission p, String callerGridIdentity) throws GTSInternalFault, IllegalPermissionFault,
		PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		if ((p.getTrustedAuthorityName() != null)
			&& (!p.getTrustedAuthorityName().equals(gov.nih.nci.cagrid.gts.common.Constants.ALL_TRUST_AUTHORITIES))) {
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


	public void revokePermission(Permission p, String callerGridIdentity) throws GTSInternalFault,
		InvalidPermissionFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		permissions.revokePermission(p);
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
		levels.destroy();
	}


	public boolean isTrustLevelUsed(String name) throws GTSInternalFault {
		TrustedAuthorityFilter f = new TrustedAuthorityFilter();
		f.setTrustLevel(name);
		TrustedAuthority[] ta = this.findTrustAuthorities(f);
		if ((ta == null) || (ta.length == 0)) {
			return true;
		} else {
			return false;
		}
	}


	public boolean doesTrustLevelExist(String name) throws GTSInternalFault {
		if (levels.doesTrustedLevelExist(name)) {
			return true;
		} else {
			return false;
		}
	}

}
