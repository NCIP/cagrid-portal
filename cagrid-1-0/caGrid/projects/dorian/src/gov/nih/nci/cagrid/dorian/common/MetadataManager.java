package gov.nih.nci.cagrid.dorian.common;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.bean.Metadata;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;

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
public class MetadataManager extends LoggingObject {
	private Database db;

	private boolean dbBuilt = false;

	private String table;

	public MetadataManager(Database db, String table) {
		this.db = db;
		this.table = table;
	}

	public boolean exists(String name) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
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
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("Unexpected Database Error, could not determine if the metadata "
							+ name + " exists.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}

	public synchronized void insert(Metadata metadata) throws DorianInternalFault {
		this.buildDatabase();
		if (!exists(metadata.getName())) {
			db.update("INSERT INTO " + table + " VALUES('" + metadata.getName()
					+ "','"+metadata.getDescription()+ "','" + metadata.getValue() + "')");
		} else {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Could not insert the metadata "
					+ metadata.getName() + " because it already exists.");
			throw fault;
		}
	}

	public synchronized void update(Metadata metadata) throws DorianInternalFault {
		this.buildDatabase();
		if (exists(metadata.getName())) {
			db.update("update " + table + " SET DESCRIPTION='"+metadata.getDescription()+"',VALUE='" + metadata.getValue()
					+ "' WHERE NAME='" + metadata.getName() + "'");
		} else {
			insert(metadata);
		}
	}

	public synchronized void remove(String name) throws DorianInternalFault {
		this.buildDatabase();
		db.update("DELETE FROM " + table + " WHERE NAME='" + name + "'");
	}

	public Metadata get(String name) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;

		String value = null;
		String description = null;
		try {
			c = db.getConnection();
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
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("Unexpected Database Error, obtain the metadata "
							+ name + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
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

	public void destroy() throws DorianInternalFault {
		db.update("DROP TABLE IF EXISTS " + table);
		dbBuilt = false;
	}

	private void buildDatabase() throws DorianInternalFault {
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