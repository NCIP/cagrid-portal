package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.common.GUMSInternalException;
import gov.nih.nci.cagrid.gums.common.GUMSObject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.projectmobius.db.ConnectionManager;
import org.projectmobius.db.Query;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: Database.java,v 1.2 2005-09-27 19:16:38 langella Exp $
 */
public class Database extends GUMSObject {

	private ConnectionManager root;
	private ConnectionManager gums;
	private String database;
	private boolean dbBuilt = false;


	public Database(ConnectionManager rootConnectionManager,String database) throws GUMSInternalException {
		try {
			this.database = database;
			this.root = rootConnectionManager;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			throw new GUMSInternalException("Error initializing the Janus Database Manager.");
		}

	}
	
	public void createDatabaseIfNeeded() throws GUMSInternalException {
		try {
			if(!dbBuilt){
			if (!databaseExists(database)) {
				Query.update(this.root, "create database " +database);
			}
			gums = new ConnectionManager(database, root.getUrlPrefix(), root.getDriver(), root.getHost(), root
				.getPort(), root.getUsername(), root.getPassword());
			dbBuilt = true;
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			throw new GUMSInternalException("Error creating the Janus Database.");
		}
	}
	
	public void destroyDatabase() throws GUMSInternalException {
		try {
			if (databaseExists(database)) {
				Query.update(this.root, "drop database if exists " +database);
			}
			gums = null;
			dbBuilt = false;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			throw new GUMSInternalException("Error destroying the Janus Database.");
		}
	}
	
	


	public boolean tableExists(String tableName) throws GUMSInternalException {
		boolean exists = false;
		Connection c = null;
		try {
			c = gums.getConnection();
			DatabaseMetaData dbMetadata = c.getMetaData();
			String[] names = {"TABLE"};
			names[0] = tableName;
			ResultSet tables = dbMetadata.getTables(null, "%", tableName, names);
			if (tables.next()) {
				exists = true;
			}
			tables.close();
			gums.releaseConnection(c);
		} catch (Exception e) {
			gums.releaseConnection(c);
			logError(e.getMessage(), e);
			throw new GUMSInternalException("Error determining if the table " + tableName + " exists:" + e.getMessage(), e);
		}
		return exists;
	}


	public void update(String sql) throws GUMSInternalException {
		try {
			Query.update(gums, sql);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			throw new GUMSInternalException(e.getMessage(), e);
		}
	}


	public ConnectionManager getConnectionManager() {
		return this.gums;
	}


	public boolean databaseExists(String db) throws GUMSInternalException {
		boolean exists = false;
		Connection c = null;
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
			logError(e.getMessage(), e);
			this.root.releaseConnection(c);
			logError(e.getMessage(), e);
			throw new GUMSInternalException("Error determining if the database " + db + " exists.");
		}
		this.root.releaseConnection(c);
		return exists;
	}

}