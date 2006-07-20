package gov.nih.nci.cagrid.dorian.common;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.projectmobius.db.ConnectionManager;
import org.projectmobius.db.DatabaseException;
import org.projectmobius.db.Query;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: Database.java,v 1.14 2006-07-20 18:44:26 langella Exp $
 */
public class Database extends LoggingObject {

	private ConnectionManager root;

	private ConnectionManager dorian = null;

	private String database;

	private boolean dbBuilt = false;

	public Database(ConnectionManager rootConnectionManager, String database) {
		this.database = database;
		this.root = rootConnectionManager;
	}

	public void createDatabaseIfNeeded() throws DorianInternalFault {

		try {
			if (!dbBuilt) {
				if (!databaseExists(database)) {
					Query.update(this.root, "create database " + database);
				}
				if (dorian == null) {
					dorian = new ConnectionManager(database, root
							.getUrlPrefix(), root.getDriver(), root.getHost(),
							root.getPort(), root.getUsername(), root
									.getPassword());
				}
				dbBuilt = true;
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An error occured while trying to create the Dorian database ("
							+ database + ")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}

	}

	public void destroyDatabase() throws DorianInternalFault {
		try {
			if (databaseExists(database)) {
				Query.update(this.root, "drop database if exists " + database);
			}
			if (dorian != null) {
				dorian.destroy();
			}
			if (root != null) {
				root.closeAllUnusedConnections();
			}
			dorian = null;
			dbBuilt = false;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An error occured while trying to destroy the Dorian database ("
							+ database + ")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

	public boolean tableExists(String tableName) throws DorianInternalFault {
		boolean exists = false;
		Connection c = null;
		try {
			c = dorian.getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();
			String[] names = { "TABLE" };
			names[0] = tableName;
			ResultSet tables = dbMetadata
					.getTables(null, "%", tableName, names);
			if (tables.next()) {
				exists = true;
			}
			tables.close();
			dorian.releaseConnection(c);
		} catch (Exception e) {
			dorian.releaseConnection(c);
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
		return exists;
	}

	public void update(String sql) throws DorianInternalFault {
		try {
			Query.update(dorian, sql);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw (DorianInternalFault) helper.getFault();
		}
	}

	public long insertGetId(String sql) throws DorianInternalFault {
		try {
			return Query.insertGetId(dorian, sql);
		} catch (Exception e) {
			// logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw (DorianInternalFault) helper.getFault();
		}
	}

	public void releaseConnection(Connection c) {
		this.dorian.releaseConnection(c);
	}

	public Connection getConnection() throws DatabaseException {
		return this.dorian.getConnection();
	}

	private boolean databaseExists(String db) throws DorianInternalFault {
		boolean exists = false;
		Connection c = null;
		if (dorian == root) {
			return true;
		}
		try {
			c = this.root.getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();

			ResultSet dbs = dbMetadata.getCatalogs();
			while (dbs.next()) {
				if (dbs.getString(1).equalsIgnoreCase(db)) {
					exists = true;
				}
			}
			dbs.close();
		} catch (Exception e) {
			this.root.releaseConnection(c);
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
		this.root.releaseConnection(c);
		return exists;
	}

	public int getUsedConnectionCount() {
		return this.dorian.getUsedConnectionCount();
	}

	public int getRootUsedConnectionCount() {
		return this.root.getUsedConnectionCount();
	}

}