package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;

import java.util.logging.Logger;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class PermissionManager {

	private static final String PERMISSIONS_TABLE = "PERMISSIONS";

	private Logger logger;

	private boolean dbBuilt = false;

	private Database db;


	public PermissionManager(Database db) {
		logger = Logger.getLogger(this.getClass().getName());
		this.db = db;
	}


	public synchronized void buildDatabase() throws GTSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(PERMISSIONS_TABLE)) {
				String trust = "CREATE TABLE " + PERMISSIONS_TABLE + " (" + "GRID_IDENTITY VARCHAR(255) NOT NULL,"
					+ "ROLE VARCHAR(50) NOT NULL," + "TRUSTED_AUTHORITY VARCHAR(255) NOT NULL,"
					+ "INDEX document_index (GRID_IDENTITY));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}


	public void destroy() throws GTSInternalFault {
		db.update("DROP TABLE IF EXISTS " + PERMISSIONS_TABLE);
		dbBuilt = false;
	}

}
