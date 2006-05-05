package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.common.Constants;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class PermissionManager {

	private static final String PERMISSIONS_TABLE = "PERMISSIONS";

	private Log log;

	private boolean dbBuilt = false;

	private Database db;


	public PermissionManager(Database db) {
		log = LogFactory.getLog(this.getClass().getName());
		this.db = db;
	}


	public synchronized void addPermission(Permission p) throws GTSInternalFault, IllegalPermissionFault {

		// This method assumes that any Trusted Authorites associated with a
		// permission is valid
		this.buildDatabase();
		if (p.getTrustedAuthorityName() == null) {
			p.setTrustedAuthorityName(Constants.ALL_TRUST_AUTHORITIES);
		}

		if (p.getGridIdentity() == null) {
			IllegalPermissionFault fault = new IllegalPermissionFault();
			fault.setFaultString("The permission " + formatPermission(p) + " no grid identity specified.");
			throw fault;
		}

		if (p.getRole() == null) {
			IllegalPermissionFault fault = new IllegalPermissionFault();
			fault.setFaultString("The permission " + formatPermission(p) + " no role specified.");
			throw fault;
		}

		if ((p.getTrustedAuthorityName().equals(Constants.ALL_TRUST_AUTHORITIES))
			&& (!p.getRole().equals(Role.TrustServiceAdmin))) {
			IllegalPermissionFault fault = new IllegalPermissionFault();
			fault.setFaultString("The permission " + formatPermission(p) + " must specify a specific Trust Authority.");
			throw fault;
		}
		if ((!p.getTrustedAuthorityName().equals(Constants.ALL_TRUST_AUTHORITIES))
			&& (p.getRole().equals(Role.TrustServiceAdmin))) {
			IllegalPermissionFault fault = new IllegalPermissionFault();
			fault.setFaultString("The permission " + formatPermission(p)
				+ " cannot specify a specific Trust Authority.");
			throw fault;
		}

		if (this.doesPermissionExist(p)) {
			IllegalPermissionFault fault = new IllegalPermissionFault();
			fault.setFaultString("The permission " + formatPermission(p) + " cannot be added, it already exists.");
			throw fault;
		}

		StringBuffer insert = new StringBuffer();
		try {
			insert.append("INSERT INTO " + PERMISSIONS_TABLE + " SET GRID_IDENTITY='" + p.getGridIdentity()
				+ "',ROLE='" + p.getRole().getValue() + "',TRUSTED_AUTHORITY='" + p.getTrustedAuthorityName() + "'");

			db.update(insert.toString());

		} catch (Exception e) {
			this.log.error("Unexpected database error incurred in adding the permission " + formatPermission(p)
				+ ", the following statement generated the error: \n" + insert.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error adding the permission " + formatPermission(p) + "!!!");
			throw fault;
		}
	}


	public synchronized void revokePermission(Permission p) throws GTSInternalFault, InvalidPermissionFault {
		buildDatabase();
		if (!doesPermissionExist(p)) {
			InvalidPermissionFault fault = new InvalidPermissionFault();
			fault.setFaultString("Could not revoke " + formatPermission(p) + ", the permission does not exist!!!");
			throw fault;
		}

		String sql = "delete from " + PERMISSIONS_TABLE + " where GRID_IDENTITY='" + p.getGridIdentity()
			+ "' AND ROLE='" + p.getRole().getValue() + "' AND TRUSTED_AUTHORITY='" + p.getTrustedAuthorityName() + "'";
		try {
			db.update(sql);
		} catch (Exception e) {
			String perm = formatPermission(p);
			this.log.error("Unexpected database error incurred in removing the permission " + perm
				+ " exists, the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in removing the permission " + perm + " exists.");
			throw fault;
		}

	}


	public synchronized boolean doesPermissionExist(Permission p) throws GTSInternalFault {
		String sql = "select count(*) from " + PERMISSIONS_TABLE + " where GRID_IDENTITY='" + p.getGridIdentity()
			+ "' AND ROLE='" + p.getRole().getValue() + "' AND TRUSTED_AUTHORITY='" + p.getTrustedAuthorityName() + "'";
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
			String perm = formatPermission(p);
			this.log.error("Unexpected database error incurred in determining if the permission " + perm
				+ " exists, the following statement generated the error: \n" + sql + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in determining if the permission " + perm + " exists.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public boolean isUserTrustServiceAdmin(String gridIdentity) throws GTSInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean isAdmin = false;
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			sql.append("select count(*) from " + PERMISSIONS_TABLE);
			sql.append(" WHERE GRID_IDENTITY ='" + gridIdentity + "' AND ");
			sql.append(" ROLE='" + Role.TrustServiceAdmin + "' AND");
			sql.append(" TRUSTED_AUTHORITY = '" + Constants.ALL_TRUST_AUTHORITIES + "'");

			ResultSet rs = s.executeQuery(sql.toString());
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					isAdmin = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			this.log.error("Unexpected database error incurred in determining whether or not the user " + gridIdentity
				+ "  is a trust service administrator, the following statement generated the error: \n"
				+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in determining whether or not the user " + gridIdentity
				+ "  is a trust service administrator.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return isAdmin;
	}


	public synchronized Permission[] findPermissions(PermissionFilter filter) throws GTSInternalFault {

		this.buildDatabase();
		Connection c = null;
		List permissions = new ArrayList();
		StringBuffer sql = new StringBuffer();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			sql.append("select * from " + PERMISSIONS_TABLE);
			if (filter != null) {
				boolean firstAppended = false;

				if (filter.getGridIdentity() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" GRID_IDENTITY LIKE '%" + filter.getGridIdentity() + "%'");
				}

				if (filter.getRole() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ROLE='" + filter.getRole() + "'");
				}

				if (filter.getTrustedAuthorityName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" TRUSTED_AUTHORITY LIKE '%" + filter.getTrustedAuthorityName() + "%'");
				}

			}

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				Permission p = new Permission();
				p.setGridIdentity(rs.getString("GRID_IDENTITY"));
				p.setRole(Role.fromValue(rs.getString("ROLE")));
				p.setTrustedAuthorityName(clean(rs.getString("TRUSTED_AUTHORITY")));
				permissions.add(p);
			}
			rs.close();
			s.close();

			Permission[] list = new Permission[permissions.size()];
			for (int i = 0; i < list.length; i++) {
				list[i] = (Permission) permissions.get(i);
			}
			return list;

		} catch (Exception e) {
			this.log.error(
				"Unexpected database error incurred in finding permissions, the following statement generated the error: \n"
					+ sql.toString() + "\n", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error occurred in finding permissions.");
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	private String clean(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}


	private StringBuffer appendWhereOrAnd(boolean firstAppended, StringBuffer sql) {
		if (firstAppended) {
			sql.append(" AND ");
		} else {
			sql.append(" WHERE");
		}
		return sql;
	}


	private String formatPermission(Permission p) {
		String role = null;
		if (p.getRole() != null) {
			role = p.getRole().getValue();
		}
		return "[" + p.getGridIdentity() + "," + role + "," + p.getTrustedAuthorityName() + "]";
	}


	public synchronized void buildDatabase() throws GTSInternalFault {
		if (!dbBuilt) {
			try {
				db.createDatabase();
				if (!this.db.tableExists(PERMISSIONS_TABLE)) {
					String trust = "CREATE TABLE " + PERMISSIONS_TABLE + " (" + "GRID_IDENTITY VARCHAR(255) NOT NULL,"
						+ "ROLE VARCHAR(50) NOT NULL," + "TRUSTED_AUTHORITY VARCHAR(255) NOT NULL,"
						+ "INDEX document_index (GRID_IDENTITY));";
					db.update(trust);
				}
				dbBuilt = true;
			} catch (Exception e) {
				this.log.error("Unexpected error in creating the database.", e);
				GTSInternalFault fault = new GTSInternalFault();
				fault.setFaultString("Unexpected error in creating the database.");
				throw fault;
			}
		}
	}


	public void destroy() throws GTSInternalFault {
		try {
			buildDatabase();
			db.update("DROP TABLE IF EXISTS " + PERMISSIONS_TABLE);
			dbBuilt = false;
		} catch (Exception e) {
			this.log.error("Unexpected error in removing the database.", e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected error in removing the database.");
			throw fault;
		}
	}

}
