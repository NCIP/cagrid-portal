package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.AuthorityPrioritySpecification;
import gov.nih.nci.cagrid.gts.bean.AuthorityPriorityUpdate;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityTimeToLive;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidAuthorityFault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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


	public synchronized AuthorityGTS getAuthority(String gtsURI) throws GTSInternalFault, InvalidAuthorityFault {
		this.buildDatabase();
		String sql = "select * from " + GTS_AUTHORITIES + " where GTS_URI='" + gtsURI + "'";
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()) {
				AuthorityGTS gts = new AuthorityGTS();
				gts.setServiceURI(rs.getString("GTS_URI"));
				gts.setPerformAuthorization(rs.getBoolean("PERFORM_AUTH"));
				gts.setPriority(rs.getInt("PRIORITY"));
				gts.setServiceIdentity(Utils.clean(rs.getString("GTS_IDENTITY")));
				gts.setSyncTrustLevels(rs.getBoolean("SYNC_TRUST_LEVELS"));
				TrustedAuthorityTimeToLive ttl = new TrustedAuthorityTimeToLive();
				ttl.setHours(rs.getInt("TTL_HOURS"));
				ttl.setMinutes(rs.getInt("TTL_MINUTES"));
				ttl.setSeconds(rs.getInt("TTL_SECONDS"));
				gts.setTrustedAuthorityTimeToLive(ttl);
				return gts;
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in obtaining the authority, " + gtsURI
				+ ", the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error obtaining the authority " + gtsURI);
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		InvalidAuthorityFault fault = new InvalidAuthorityFault();
		fault.setFaultString("The authority " + gtsURI + " does not exist.");
		throw fault;
	}


	public synchronized void updateAuthorityPriorities(AuthorityPriorityUpdate update) throws GTSInternalFault,
		IllegalAuthorityFault {

		AuthorityGTS[] auths = this.getAuthorities();
		// Create HashMap
		Map map = new HashMap();
		for (int i = 0; i < auths.length; i++) {
			map.put(auths[i].getServiceURI(), auths[i]);
		}
		// Verfiy that all authorities are accounted for
		AuthorityPrioritySpecification[] specs = update.getAuthorityPrioritySpecification();
		for (int i = 0; i < specs.length; i++) {
			map.remove(specs[i].getServiceURI());
		}

		if (map.size() > 0) {
			StringBuffer error = new StringBuffer();
			error
				.append("Cannot update the authority priorities, an incomplete authority list was provided.\n The provided list was missing the following authorities:\n");
			Iterator itr = map.values().iterator();
			while (itr.hasNext()) {
				error.append((String) itr.next() + "\n");
			}
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString(error.toString());
			throw fault;
		}
		// Validate priorities
		int count = this.getAuthorityCount();
		for (int i = 1; i <= count; i++) {
			int found = 0;
			for (int j = 0; j < specs.length; j++) {
				if (i == specs[j].getPriority()) {
					found = found + 1;
				}
			}
			if (found < 1) {
				IllegalAuthorityFault fault = new IllegalAuthorityFault();
				fault
					.setFaultString("Cannot update the authority priorities, no authority specified with the priority "
						+ i + ", each authority must be assigned a unique priority between 1 and " + count + "!!!");
				throw fault;
			} else if (found > 1) {
				IllegalAuthorityFault fault = new IllegalAuthorityFault();
				fault
					.setFaultString("Cannot update the authority priorities, multiple authorities specified with the priority "
						+ i + ", each authority must be assigned a unique priority between 1 and " + count + "!!!");
				throw fault;
			}
		}

		Connection c = null;
		try {
			c = db.getConnection();
			c.setAutoCommit(false);
			for (int i = 0; i < specs.length; i++) {
				updateAuthorityPriority(c, specs[i].getServiceURI(), specs[i].getPriority());
			}
			c.commit();
		} catch (Exception e) {
			if (c != null) {
				try {
					c.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in updating the authority priorities!!!",
				e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in updating the authority priorities!!!");
			throw fault;
		} finally {
			try {
				if (c != null) {
					c.setAutoCommit(true);
				}
			} catch (Exception e) {

			}
			db.releaseConnection(c);
		}

	}


	protected synchronized void updateAuthorityPriority(Connection c, String uri, int priority)
		throws GTSInternalFault, InvalidAuthorityFault {
		this.buildDatabase();
		if (!doesAuthorityExist(uri)) {

		} else {
			try {
				PreparedStatement update = c.prepareStatement("UPDATE " + GTS_AUTHORITIES
					+ " SET PRIORITY = ? WHERE GTS_URI = ?");
				update.setInt(1, priority);
				update.setString(2, uri);
				update.executeUpdate();

			} catch (Exception e) {
				this.logger.log(Level.SEVERE,
					"Unexpected database error incurred in updating the priority for the authority, " + uri + ".", e);
				GTSInternalFault fault = new GTSInternalFault();
				fault.setFaultString("Unexpected error occurred in updating the priority for the authority, " + uri
					+ ".");
				throw fault;
			}
		}

	}


	public synchronized void updateAuthority(AuthorityGTS gts) throws GTSInternalFault, IllegalAuthorityFault,
		InvalidAuthorityFault {
		this.buildDatabase();
		if (Utils.clean(gts.getServiceURI()) == null) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority cannot be updated, no service URI specified!!!");
			throw fault;
		}

		if (gts.getTrustedAuthorityTimeToLive() == null) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority, " + gts.getServiceURI()
				+ " cannot be updated, no time to live specified!!!");
			throw fault;
		}

		if ((gts.isPerformAuthorization()) && (gts.getServiceIdentity() == null)) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority, " + gts.getServiceURI()
				+ " cannot be updated, when authorization is required a service identity must be specified!!!");
			throw fault;
		}

		AuthorityGTS curr = getAuthority(gts.getServiceURI());
		if (curr.getPriority() != gts.getPriority()) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault
				.setFaultString("The Authority, "
					+ gts.getServiceURI()
					+ " cannot be updated, priorities cannot be updated using this method, use the update priorities method!!!");
			throw fault;
		}

		Connection c = null;
		try {
			c = db.getConnection();
			if (!gts.equals(curr)) {
				PreparedStatement update = c
					.prepareStatement("UPDATE "
						+ GTS_AUTHORITIES
						+ " SET PRIORITY = ?, SYNC_TRUST_LEVELS = ?, TTL_HOURS = ?, TTL_MINUTES = ?, TTL_SECONDS = ?, PERFORM_AUTH = ?, GTS_IDENTITY = ? WHERE GTS_URI = ?");
				update.setInt(1, gts.getPriority());
				update.setString(2, String.valueOf(gts.isSyncTrustLevels()));
				update.setInt(3, gts.getTrustedAuthorityTimeToLive().getHours());
				update.setInt(4, gts.getTrustedAuthorityTimeToLive().getMinutes());
				update.setInt(5, gts.getTrustedAuthorityTimeToLive().getSeconds());
				update.setString(6, String.valueOf(gts.isPerformAuthorization()));
				update.setString(7, gts.getServiceIdentity());
				update.setString(8, gts.getServiceURI());
				update.executeUpdate();
			}
		} catch (Exception e) {

			this.logger.log(Level.SEVERE, "Unexpected database error incurred in updating the authority "
				+ gts.getServiceURI() + "!!!", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in updating the authority " + gts.getServiceURI() + "!!!");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	private synchronized AuthorityGTS[] getAuthoritiesEqualToOrAfter(int priority) throws GTSInternalFault {
		this.buildDatabase();

		Connection c = null;
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			sql
				.append("select GTS_URI from " + GTS_AUTHORITIES + " WHERE PRIORITY>=" + priority
					+ " ORDER BY PRIORITY");
			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				list.add(rs.getString("GTS_URI"));
			}
			rs.close();
			s.close();

			AuthorityGTS[] gts = new AuthorityGTS[list.size()];
			for (int i = 0; i < gts.length; i++) {
				String uri = (String) list.get(i);
				gts[i] = this.getAuthority(uri);
			}
			return gts;

		} catch (Exception e) {
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in getting the authorities, the following statement generated the error: \n"
					+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in getting the authorities.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	public synchronized AuthorityGTS[] getAuthorities() throws GTSInternalFault {
		this.buildDatabase();

		Connection c = null;
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			sql.append("select GTS_URI from " + GTS_AUTHORITIES + " ORDER BY PRIORITY");
			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				list.add(rs.getString("GTS_URI"));
			}
			rs.close();
			s.close();

			AuthorityGTS[] gts = new AuthorityGTS[list.size()];
			for (int i = 0; i < gts.length; i++) {
				String uri = (String) list.get(i);
				gts[i] = this.getAuthority(uri);
			}
			return gts;

		} catch (Exception e) {
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in getting the authorities, the following statement generated the error: \n"
					+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in getting the authorities.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	public synchronized int getAuthorityCount() throws GTSInternalFault {
		this.buildDatabase();
		Connection c = null;
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			sql.append("select COUNT(*) from " + GTS_AUTHORITIES);
			ResultSet rs = s.executeQuery(sql.toString());
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			s.close();
			return count;
		} catch (Exception e) {
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in getting the authority count, the following statement generated the error: \n"
					+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in getting the authority count.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

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

		// Validate the Priority (greater than 1 and not more than the count)
		int count = this.getAuthorityCount() + 1;
		if ((gts.getPriority() < 1) || (gts.getPriority() > count)) {
			IllegalAuthorityFault fault = new IllegalAuthorityFault();
			fault.setFaultString("The Authority, " + gts.getServiceURI()
				+ " cannot be added, invalid priority specified the priority must be between 1 and " + count + "!!!");
			throw fault;
		}

		Connection c = null;
		try {
			c = db.getConnection();
			c.setAutoCommit(false);
			// Get the current list of Authorities
			AuthorityGTS[] list = this.getAuthoritiesEqualToOrAfter(gts.getPriority());
			for (int i = 0; i < list.length; i++) {
				this.updateAuthorityPriority(c, list[i].getServiceURI(), (list[i].getPriority() + 1));
			}

			PreparedStatement insert = c
				.prepareStatement("INSERT INTO "
					+ GTS_AUTHORITIES
					+ " SET GTS_URI = ?, PRIORITY = ?, SYNC_TRUST_LEVELS = ?, TTL_HOURS = ?, TTL_MINUTES = ?, TTL_SECONDS = ?, PERFORM_AUTH = ?, GTS_IDENTITY = ?");
			insert.setString(1, gts.getServiceURI());
			insert.setInt(2, gts.getPriority());
			insert.setString(3, String.valueOf(gts.isSyncTrustLevels()));
			insert.setInt(4, gts.getTrustedAuthorityTimeToLive().getHours());
			insert.setInt(5, gts.getTrustedAuthorityTimeToLive().getMinutes());
			insert.setInt(6, gts.getTrustedAuthorityTimeToLive().getSeconds());
			insert.setString(7, String.valueOf(gts.isPerformAuthorization()));
			insert.setString(8, gts.getServiceIdentity());
			insert.executeUpdate();
			c.commit();
		} catch (Exception e) {
			if (c != null) {
				try {
					c.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			this.logger.log(Level.SEVERE, "Unexpected database error incurred in adding the authority "
				+ gts.getServiceURI() + "!!!", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in adding the authority " + gts.getServiceURI() + "!!!");
			throw fault;
		} finally {
			try {
				if (c != null) {
					c.setAutoCommit(true);
				}
			} catch (Exception e) {

			}
			db.releaseConnection(c);
		}

	}


	public synchronized void removeAuthority(String uri) throws GTSInternalFault, InvalidAuthorityFault {
		this.buildDatabase();
		AuthorityGTS gts = getAuthority(uri);
		Connection c = null;
		try {
			c = db.getConnection();
			c.setAutoCommit(false);
			// Get the current list of Authorities
			AuthorityGTS[] list = this.getAuthoritiesEqualToOrAfter(gts.getPriority());
			for (int i = 0; i < list.length; i++) {
				this.updateAuthorityPriority(c, list[i].getServiceURI(), (list[i].getPriority() - 1));
			}

			PreparedStatement ps = c.prepareStatement("DELETE FROM " + GTS_AUTHORITIES + " WHERE GTS_URI = ?");
			ps.setString(1, uri);
			ps.executeUpdate();
			c.commit();
		} catch (Exception e) {
			if (c != null) {
				try {
					c.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			this.logger.log(Level.SEVERE,
				"Unexpected database error incurred in deleting the authority " + uri + "!!!", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in deleting the authority " + uri + "!!!");
			throw fault;
		} finally {
			try {
				if (c != null) {
					c.setAutoCommit(true);
				}
			} catch (Exception e) {

			}
			db.releaseConnection(c);
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
