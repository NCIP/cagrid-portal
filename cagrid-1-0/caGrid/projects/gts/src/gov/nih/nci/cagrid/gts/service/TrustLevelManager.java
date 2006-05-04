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
import java.util.Calendar;
import java.util.GregorianCalendar;
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

	private TrustedAuthorityLevelRemover status;

	private String gtsURI;


	public TrustLevelManager(String gtsURI, TrustedAuthorityLevelRemover status, Database db) {
		logger = Logger.getLogger(this.getClass().getName());
		this.db = db;
		this.status = status;
		this.gtsURI = gtsURI;
	}


	public synchronized void addTrustLevel(TrustLevel level) throws GTSInternalFault, IllegalTrustLevelFault {
		addTrustLevel(level, true);
	}


	public synchronized void addTrustLevel(TrustLevel level, boolean internal) throws GTSInternalFault,
		IllegalTrustLevelFault {
		this.buildDatabase();
		if (Utils.clean(level.getName()) == null) {
			IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
			fault.setFaultString("Cannot add trust level, no name specified.");
			throw fault;
		}

		if (doesTrustLevelExist(level.getName())) {
			IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
			fault.setFaultString("The Trust Level " + level.getName() + " cannot be added, it already exists.");
			throw fault;
		}

		if (level.getDescription() == null) {
			level.setDescription("");
		}

		if (internal) {
			level.setIsAuthority(Boolean.TRUE);
			level.setAuthorityTrustService(gtsURI);
			level.setSourceTrustService(gtsURI);

		} else {
			if ((level.getIsAuthority() == null)) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The trust level " + level.getName()
					+ ", cannot be added because it does not specify whether or not this GTS is the authority of it.");
				throw fault;
			}

			if (level.getAuthorityTrustService() == null) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The trust level " + level.getName()
					+ ", cannot be added because it does not specify an authority trust service.");
				throw fault;

			}

			if ((level.getIsAuthority().booleanValue()) && (!level.getAuthorityTrustService().equals(gtsURI))) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The trust level " + level.getName()
					+ " cannot be added, a conflict was detected, this gts (" + gtsURI
					+ ") was specified as its authority, however the URI of another GTS ( "
					+ level.getAuthorityTrustService() + ") was specified.");
				throw fault;
			}

			if (level.getSourceTrustService() == null) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The trust level " + level.getName()
					+ ", cannot be added because it does not specify a source trust service.");
				throw fault;
			}
		}
		Calendar c = new GregorianCalendar();
		level.setLastUpdated(c.getTimeInMillis());
		StringBuffer insert = new StringBuffer();
		try {
			insert.append("INSERT INTO " + TRUST_LEVELS + " SET NAME='" + level.getName() + "',DESCRIPTION='"
				+ level.getDescription() + "', IS_AUTHORITY='" + level.getIsAuthority() + "', AUTHORITY_GTS='"
				+ level.getAuthorityTrustService() + "', SOURCE_GTS='" + level.getSourceTrustService()
				+ "', LAST_UPDATED=" + level.getLastUpdated());
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
				level.setIsAuthority(new Boolean(rs.getBoolean("IS_AUTHORITY")));
				level.setAuthorityTrustService(rs.getString("AUTHORITY_GTS"));
				level.setSourceTrustService(rs.getString("SOURCE_GTS"));
				level.setLastUpdated(rs.getLong("LAST_UPDATED"));
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


	public synchronized TrustLevel[] getTrustLevels(String gtsSourceURI) throws GTSInternalFault {
		this.buildDatabase();
		Connection c = null;
		List levels = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			sql.append("select * from " + TRUST_LEVELS + " WHERE SOURCE_GTS='" + gtsSourceURI + "'");

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				TrustLevel level = new TrustLevel();
				level.setName(rs.getString("NAME"));
				level.setDescription(rs.getString("DESCRIPTION"));
				level.setIsAuthority(new Boolean(rs.getBoolean("IS_AUTHORITY")));
				level.setAuthorityTrustService(rs.getString("AUTHORITY_GTS"));
				level.setSourceTrustService(rs.getString("SOURCE_GTS"));
				level.setLastUpdated(rs.getLong("LAST_UPDATED"));
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
				level.setIsAuthority(new Boolean(rs.getBoolean("IS_AUTHORITY")));
				level.setAuthorityTrustService(rs.getString("AUTHORITY_GTS"));
				level.setSourceTrustService(rs.getString("SOURCE_GTS"));
				level.setLastUpdated(rs.getLong("LAST_UPDATED"));
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


	public synchronized void updateTrustLevel(TrustLevel level) throws GTSInternalFault, InvalidTrustLevelFault,
		IllegalTrustLevelFault {
		updateTrustLevel(level, true);
	}


	public synchronized void updateTrustLevel(TrustLevel level, boolean internal) throws GTSInternalFault,
		InvalidTrustLevelFault, IllegalTrustLevelFault {
		TrustLevel curr = this.getTrustLevel(level.getName());
		StringBuffer sql = new StringBuffer();
		boolean needsUpdate = false;
		if (internal) {
			if (!curr.getAuthorityTrustService().equals(gtsURI)) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The trust level cannot be updated, the GTS (" + gtsURI
					+ ") is not its authority!!!");
				throw fault;
			}

			if ((Utils.clean(level.getAuthorityTrustService()) != null)
				&& (!level.getAuthorityTrustService().equals(curr.getAuthorityTrustService()))) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The authority trust service for a trust level cannot be changed");
				throw fault;
			}

			if ((Utils.clean(level.getSourceTrustService()) != null)
				&& (!level.getSourceTrustService().equals(curr.getSourceTrustService()))) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The source trust service for a trust level cannot be changed");
				throw fault;
			}

		} else {

			if ((curr.getIsAuthority().booleanValue()) && (!level.getAuthorityTrustService().equals(gtsURI))) {
				IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
				fault.setFaultString("The trust level " + level.getName()
					+ " cannot be updated, a conflict was detected, this gts (" + gtsURI
					+ ") was specified as its authority, however the URI of another GTS ( "
					+ level.getAuthorityTrustService() + ") was specified.");
				throw fault;
			}

			if (!curr.getAuthorityTrustService().equals(level.getAuthorityTrustService())) {
				buildUpdate(needsUpdate, sql, "AUTHORITY_GTS", level.getAuthorityTrustService());
				needsUpdate = true;
			}

			if (!curr.getSourceTrustService().equals(level.getSourceTrustService())) {
				buildUpdate(needsUpdate, sql, "SOURCE_GTS", level.getSourceTrustService());
				needsUpdate = true;
			}
		}

		if ((level.getIsAuthority() != null) && (!level.getIsAuthority().equals(curr.getIsAuthority()))) {
			IllegalTrustLevelFault fault = new IllegalTrustLevelFault();
			fault.setFaultString("Whether or not this GTS is the authority for a level cannot be changed!!!");
			throw fault;
		}

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
					Calendar c = new GregorianCalendar();
					level.setLastUpdated(c.getTimeInMillis());
					buildUpdate(needsUpdate, sql, "LAST_UPDATED", level.getLastUpdated());
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
		if (doesTrustLevelExist(name)) {
			this.status.removeAssociatedTrustedAuthorities(name);
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


	public synchronized boolean doesTrustLevelExist(String name) throws GTSInternalFault {
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
					+ "DESCRIPTION TEXT, " + "IS_AUTHORITY VARCHAR(5) NOT NULL,"
					+ "AUTHORITY_GTS VARCHAR(255) NOT NULL,"
					+ "SOURCE_GTS VARCHAR(255) NOT NULL, LAST_UPDATED BIGINT NOT NULL,"
					+ "INDEX document_index (NAME));";
				db.update(trust);
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


	private void buildUpdate(boolean needsUpdate, StringBuffer sql, String field, long value) {
		if (needsUpdate) {
			sql.append(",").append(field).append("=").append(value).append("");
		} else {
			sql.append("UPDATE " + TRUST_LEVELS + " SET ");
			sql.append(field).append("=").append(value);
		}

	}

}
