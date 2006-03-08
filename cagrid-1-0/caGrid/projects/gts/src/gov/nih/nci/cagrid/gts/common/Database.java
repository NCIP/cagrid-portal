package gov.nih.nci.cagrid.gts.common;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.projectmobius.db.ConnectionManager;
import org.projectmobius.db.DatabaseException;
import org.projectmobius.db.Query;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: Database.java,v 1.1 2006-03-08 16:22:58 langella Exp $
 */
public class Database{

	private ConnectionManager root;

	private ConnectionManager gts = null;

	private String database;
	
	private Logger logger;

	private boolean dbBuilt = false;

	public Database(ConnectionManager rootConnectionManager, String database) {
		this.database = database;
		this.root = rootConnectionManager;
		logger = Logger.getLogger(this.getClass().getName());
	}

	public void createDatabaseIfNeeded() throws GTSInternalFault {

		try {
			if (!dbBuilt) {
				if (!databaseExists(database)) {
					Query.update(this.root, "create database " + database);
				}
				if (gts == null) {
					gts = new ConnectionManager(database, root
							.getUrlPrefix(), root.getDriver(), root.getHost(),
							root.getPort(), root.getUsername(), root
									.getPassword());
				}
				dbBuilt = true;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(), e);
			GTSInternalFault fault = new GTSInternalFault();
			fault
					.setFaultString("An error occured while trying to create the Dorian database ("
							+ database + ")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GTSInternalFault) helper.getFault();
			throw fault;
		}

	}

	public void destroyDatabase() throws GTSInternalFault {
		try {
			if (databaseExists(database)) {
				Query.update(this.root, "drop database if exists " + database);
			}
			if (gts != null) {
				gts.destroy();
			}
			if (root != null) {
				root.closeAllUnusedConnections();
			}
			gts = null;
			dbBuilt = false;
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(), e);
			GTSInternalFault fault = new GTSInternalFault();
			fault
					.setFaultString("An error occured while trying to destroy the Dorian database ("
							+ database + ")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GTSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public boolean tableExists(String tableName) throws GTSInternalFault {
		boolean exists = false;
		Connection c = null;
		try {
			c = gts.getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();
			String[] names = { "TABLE" };
			names[0] = tableName;
			ResultSet tables = dbMetadata
					.getTables(null, "%", tableName, names);
			if (tables.next()) {
				exists = true;
			}
			tables.close();
			gts.releaseConnection(c);
		} catch (Exception e) {
			gts.releaseConnection(c);
			logger.log(Level.SEVERE,e.getMessage(), e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GTSInternalFault) helper.getFault();
			throw fault;
		}
		return exists;
	}

	public void update(String sql) throws GTSInternalFault {
		try {
			Query.update(gts, sql);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(), e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw (GTSInternalFault) helper.getFault();
		}
	}

	public long insertGetId(String sql) throws GTSInternalFault {
		try {
			return Query.insertGetId(gts, sql);
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage(), e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			throw (GTSInternalFault) helper.getFault();
		}
	}

	public void releaseConnection(Connection c) {
		this.gts.releaseConnection(c);
	}

	public Connection getConnection() throws DatabaseException {
		return this.gts.getConnection();
	}

	private boolean databaseExists(String db) throws GTSInternalFault {
		boolean exists = false;
		Connection c = null;
		if (gts == root) {
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
			logger.log(Level.SEVERE,e.getMessage(), e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GTSInternalFault) helper.getFault();
			throw fault;
		}
		this.root.releaseConnection(c);
		return exists;
	}

	public int getUsedConnectionCount() {
		return this.gts.getUsedConnectionCount();
	}

	public int getRootUsedConnectionCount() {
		return this.root.getUsedConnectionCount();
	}

}