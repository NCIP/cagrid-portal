package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class TrustLevelManager {

	private static final String TRUST_LEVELS = "TRUST_LEVELS";

	private Logger logger;

	private boolean dbBuilt = false;

	private Database db;

	private TrustLevelStatus status;


	public TrustLevelManager(TrustLevelStatus status, Database db) {
		logger = Logger.getLogger(this.getClass().getName());
		this.db = db;
		this.status = status;
	}


	public synchronized void addTrustLevel(TrustLevel level) throws GTSInternalFault, IllegalTrustLevelFault {
		this.buildDatabase();
		if (Utils.clean(level.getName()) == null) {
			IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
			fault.setFaultString("Cannot add trust level, no name specified.");
			throw fault;
		}

		if (doesTrustedLevelExist(level.getName())) {
			IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
			fault.setFaultString("The Trust Level " + level.getName() + " cannot be added, it already exists.");
			throw fault;
		}

		if (level.getDescription() == null) {
			level.setDescription("");
		}

		StringBuffer insert = new StringBuffer();
		try {
			insert.append("INSERT INTO " + TRUST_LEVELS + " SET NAME='" + level.getName() + "',DESCRIPTION='"
				+ level.getDescription() + "'");
			db.update(insert.toString());
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in adding the Trust Level, "
				+ level.getName() + ", the following statement generated the error: \n" + insert.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error adding the Trust Level, " + level.getName() + "!!!");
			throw fault;
		}
	}


	public synchronized TrustLevel[] getTrustLevels() throws GTSInternalFault {
		this.buildDatabase();
		Connection c = null;
		List levels = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			sql.append("select * from " + TRUST_LEVELS);

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				TrustLevel level = new TrustLevel();
				level.setName(rs.getString("NAME"));
				level.setDescription(rs.getString("DESCRIPTION"));
				levels.add(level);
			}
			rs.close();
			s.close();

			TrustLevel[] list = new TrustLevel[levels.size()];
			for (int i = 0; i < list.length; i++) {
				list[i] = (TrustLevel) levels.get(i);
			}
			return list;

		} catch (Exception e) {
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in getting trust levels, the following statement generated the error: \n"
					+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in getting trust levels.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public synchronized TrustLevel getTrustLevel(String name) throws GTSInternalFault, InvalidTrustLevelFault {
		String sql = "select * from " + TRUST_LEVELS + " where NAME='" + name + "'";
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				TrustLevel level = new TrustLevel();
				level.setName(rs.getString("NAME"));
				level.setDescription(rs.getString("DESCRIPTION"));
				return level;
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in obtaining the trust level, " + name
				+ ", the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error obtaining the trust level " + name);
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		InvalidTrustLevelFault fault = new InvalidTrustLevelFault();
		fault.setFaultString("The trust level " + name + " does not exist.");
		throw fault;
	}


	public synchronized void updateTrustLevel(TrustLevel level) throws GTSInternalFault, InvalidTrustLevelFault {
		TrustLevel curr = this.getTrustLevel(level.getName());
		StringBuffer sql = new StringBuffer();
		boolean needsUpdate = false;
		if (level.getDescription() != null) {
			if ((Utils.clean(level.getDescription()) != null)
				&& (!level.getDescription().equals(curr.getDescription()))) {
				buildUpdate(needsUpdate, sql, "DESCRIPTION", level.getDescription());
				needsUpdate = true;
			}
		}

		try {
			if (!level.equals(curr)) {
				if (needsUpdate) {
					sql.append(" WHERE NAME='" + level.getName() + "'");
					db.update(sql.toString());
				}
			}
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in updating the trust level, "
				+ level.getName() + ", the following statement generated the error: \n" + sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in updating the trust level, " + level.getName() + ".");
			throw fault;
		}
	}


	public synchronized void removeTrustLevel(String name) throws GTSInternalFault, InvalidTrustLevelFault,
		IllegalTrustLevelFault {
		if (doesTrustedLevelExist(name)) {
			if (status.isTrustLevelUsed(name)) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The Trust Level, " + name
					+ " cannot be removed until all the Trusted Authorities referencing it are removed.");
				throw fault;
			}

			String sql = "delete FROM " + TRUST_LEVELS + " where NAME='" + name + "'";
			try {
				db.update(sql);
			} catch (Exception e) {
				this.logger.log(Level.SEVERE, "Unexpected database error incurred in removing the trust level, " + name
					+ ", the following statement generated the error: \n" + sql + "\n", e);
				GTSInternalFault fault = new GTSInternalFault();
				fault.setFaultString("Unexpected error removing the trust level " + name);
				throw fault;
			}
		} else {
			InvalidTrustLevelFault fault = new InvalidTrustLevelFault();
			fault.setFaultString("The trust level " + name + " does not exist.");
			throw fault;
		}
	}


	public synchronized boolean doesTrustedLevelExist(String name) throws GTSInternalFault {
		this.buildDatabase();
		String sql = "select count(*) from " + TRUST_LEVELS + " where NAME='" + name + "'";
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
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in determining if the TrustLevel " + name
				+ " exists, the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in determining if the Trust Level " + name + " exists.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public synchronized void buildDatabase() throws GTSInternalFault {
		if (!dbBuilt) {
			db.createDatabaseIfNeeded();
			if (!this.db.tableExists(TRUST_LEVELS)) {
				String trust = "CREATE TABLE " + TRUST_LEVELS + " (" + "NAME VARCHAR(255) NOT NULL PRIMARY KEY,"
					+ "DESCRIPTION TEXT, INDEX document_index (NAME));";
				db.update(trust);
				/*
				try {
					TrustLevel levelA = new TrustLevel();
					levelA.setName("Level A");
					levelA.setDescription("This is level A.");
					this.addTrustLevel(levelA);

					TrustLevel levelB = new TrustLevel();
					levelB.setName("Level B");
					levelB.setDescription("This is level B.");
					this.addTrustLevel(levelB);

					TrustLevel levelC = new TrustLevel();
					levelC.setName("Level C");
					levelC.setDescription("This is level C.");
					this.addTrustLevel(levelC);
				} catch (Exception e) {
					e.printStackTrace();
				}
				*/
			}
			dbBuilt = true;
		}
	}


	public void destroy() throws GTSInternalFault {
		buildDatabase();
		db.update("DROP TABLE IF EXISTS " + TRUST_LEVELS);
		dbBuilt = false;
	}


	private void buildUpdate(boolean needsUpdate, StringBuffer sql, String field, String value) {
		if (needsUpdate) {
			sql.append(",").append(field).append("='").append(value).append("'");
		} else {
			sql.append("UPDATE " + TRUST_LEVELS + " SET ");
			sql.append(field).append("='").append(value).append("'");
		}

	}

}
