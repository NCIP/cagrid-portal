package gov.nih.nci.cagrid.gums.common;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.Metadata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class MetadataManager extends GUMSObject {
	private Database db;

	private boolean dbBuilt = false;

	private String table;

	public MetadataManager(Database db, String table) {
		this.db = db;
		this.table = table;
	}

	public boolean exists(String name) throws GUMSInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + table
					+ " where name='" + name + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Database Error, could not determine if the metadata "
							+ name + " exists.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}

	public synchronized void insert(Metadata metadata) throws GUMSInternalFault {
		this.buildDatabase();
		if (!exists(metadata.getName())) {
			db.update("INSERT INTO " + table + " VALUES('" + metadata.getName()
					+ "','"+metadata.getDescription()+ "','" + metadata.getValue() + "')");
		} else {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Could not insert the metadata "
					+ metadata.getName() + " because it already exists.");
			throw fault;
		}
	}

	public synchronized void update(Metadata metadata) throws GUMSInternalFault {
		this.buildDatabase();
		if (exists(metadata.getName())) {
			db.update("update " + table + " SET DESCRIPTION='"+metadata.getDescription()+"',VALUE='" + metadata.getValue()
					+ "' WHERE NAME='" + metadata.getName() + "'");
		} else {
			insert(metadata);
		}
	}

	public synchronized void remove(String name) throws GUMSInternalFault {
		this.buildDatabase();
		db.update("DELETE FROM " + table + " WHERE NAME='" + name + "'");
	}

	public Metadata get(String name) throws GUMSInternalFault {
		this.buildDatabase();
		Connection c = null;

		String value = null;
		String description = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select DESCRIPTION,VALUE from " + table
					+ " where name='" + name + "'");
			if (rs.next()) {
				value = rs.getString("VALUE");
				description = rs.getString("DESCRIPTION");
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Database Error, obtain the metadata "
							+ name + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}

		if (value == null) {
			return null;
		} else {
			Metadata metadata = new Metadata();
			metadata.setName(name);
			metadata.setValue(value);
			metadata.setDescription(description);
			return metadata;
		}
	}

	public void destroy() throws GUMSInternalFault {
		db.update("DROP TABLE IF EXISTS " + table);
		dbBuilt = false;
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(table)) {
				String applications = "CREATE TABLE " + table + " ("
						+ "NAME VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "DESCRIPTION TEXT,"
						+ "VALUE TEXT NOT NULL,"
						+ "INDEX document_index (NAME));";
				db.update(applications);
			}
			this.dbBuilt = true;
		}
	}
}