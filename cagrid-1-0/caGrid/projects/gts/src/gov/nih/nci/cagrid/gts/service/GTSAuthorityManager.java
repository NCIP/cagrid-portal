package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class GTSAuthorityManager {

	private static final String GTS_AUTHORITIES = "GTS_AUTHORITIES";

	private Logger logger;

	private boolean dbBuilt = false;

	private Database db;


	public GTSAuthorityManager(Database db) {
		logger = Logger.getLogger(this.getClass().getName());
		this.db = db;
	}


	public synchronized void addAuthority(AuthorityGTS gts) throws GTSInternalFault, IllegalAuthorityFault {
		this.buildDatabase();
		if (Utils.clean(gts.getServiceURI()) == null) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority cannot be added, no service URI specified!!!");
			throw fault;
		}

		if (gts.getTrustedAuthorityTimeToLive() == null) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority, " + gts.getServiceURI()
				+ " cannot be added, no time to live specified!!!");
			throw fault;
		}

		if ((gts.isPerformAuthorization()) && (gts.getServiceIdentity() == null)) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority, " + gts.getServiceURI()
				+ " cannot be added, when authorization is required a service identity must be specified!!!");
			throw fault;
		}

		if (doesAuthorityExist(gts.getServiceURI())) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority, " + gts.getServiceURI() + " cannot be added, it already exists!!!");
			throw fault;
		}
	}


	public synchronized boolean doesAuthorityExist(String gtsURI) throws GTSInternalFault {
		this.buildDatabase();
		String sql = "select count(*) from " + GTS_AUTHORITIES + " where GTS_URI='" + gtsURI + "'";
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in determining if the Authority GTS "
				+ gtsURI + " exists, the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in determining if the Authority GTS " + gtsURI + " exists.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public void destroy() throws GTSInternalFault {
		buildDatabase();
		db.update("DROP TABLE IF EXISTS " + GTS_AUTHORITIES);
		dbBuilt = false;
	}


	public synchronized void buildDatabase() throws GTSInternalFault {
		if (!dbBuilt) {
			db.createDatabaseIfNeeded();
			if (!this.db.tableExists(GTS_AUTHORITIES)) {
				String trust = "CREATE TABLE " + GTS_AUTHORITIES + " (" + "GTS_URI VARCHAR(255) NOT NULL PRIMARY KEY,"
					+ "PRIORITY INT NOT NULL, SYNC_TRUST_LEVELS VARCHAR(5) NOT NULL, "
					+ "TTL_HOURS INT NOT NULL, TTL_MINUTES INT NOT NULL,TTL_SECONDS INT NOT NULL, "
					+ "PERFORM_AUTH VARCHAR(5) NOT NULL, GTS_IDENTITY VARCHAR(255),"
					+ " INDEX document_index (GTS_URI));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}

}
