package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSObject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.globus.wsrf.utils.FaultHelper;
import org.projectmobius.db.ConnectionManager;
import org.projectmobius.db.Query;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: Database.java,v 1.3 2005-09-28 20:19:09 langella Exp $
 */
public class Database extends GUMSObject {

	private ConnectionManager root;
	private ConnectionManager gums;
	private String database;
	private boolean dbBuilt = false;


	public Database(ConnectionManager rootConnectionManager,String database){
			this.database = database;
			this.root = rootConnectionManager;
	}
	
	public void createDatabaseIfNeeded() throws GUMSInternalFault{
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
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("An error occured while trying to create the GUMS database ("+database+")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		}
	}
	
	public void destroyDatabase() throws GUMSInternalFault {
		try {
			if (databaseExists(database)) {
				Query.update(this.root, "drop database if exists " +database);
			}
			gums = null;
			dbBuilt = false;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("An error occured while trying to destroy the GUMS database ("+database+")");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		}
	}
	
	


	public boolean tableExists(String tableName) throws GUMSInternalFault {
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
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		}
		return exists;
	}


	public void update(String sql) throws GUMSInternalFault {
		try {
			Query.update(gums, "yoyoyoyo"+sql);
		} catch (Exception e) {
			//logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);	
			throw (GUMSInternalFault)helper.getFault();
		}
	}


	public ConnectionManager getConnectionManager() {
		return this.gums;
	}


	public boolean databaseExists(String db) throws GUMSInternalFault {
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
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		}
		this.root.releaseConnection(c);
		return exists;
	}

}