package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;

import java.util.logging.Logger;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class GTS {
	private Logger logger;
	private GTSConfiguration conf;
	private String gtsURI;
	private TrustedAuthorityManager trust;
	private PermissionManager permissions;

	public GTS(GTSConfiguration conf, String gtsURI) {
		logger = Logger.getLogger(this.getClass().getName());
		this.conf = conf;
		this.gtsURI = gtsURI;
		Database db = new Database(conf.getConnectionManager(), conf.getGTSInternalId());
		trust = new TrustedAuthorityManager(gtsURI, db);
		permissions = new PermissionManager(db);
	}
	
	public synchronized TrustedAuthority addTrustedAuthority(String gridIdentity, TrustedAuthority ta)
	throws GTSInternalFault, IllegalTrustedAuthorityFault, PermissionDeniedFault {
		
		return null;
	}

}
