package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.ifs.bean.UserAttributeDescriptor;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.IllegalUserAttributeFault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserAttributeManager extends LoggingObject {

	public static String ATTRIBUTE_TABLE_PREFIX = "ATTRIBUTE_";

	private Database db;

	private boolean dbBuilt = false;

	private String tableName;

	private UserAttributeDescriptor des;


	public UserAttributeManager(Database db, long attributeId, UserAttributeDescriptor des) {
		this.db = db;
		this.des = des;
		this.tableName = ATTRIBUTE_TABLE_PREFIX + attributeId;
	}


	public String getDBTableName() {
		return tableName;
	}


	public boolean hasAttribute(String gridIdentity) throws DorianInternalFault {
		this.create();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			PreparedStatement ps = c.prepareStatement("select count(*) from " + tableName + " where GRID_IDENTITY = ?");
			ps.setString(1, gridIdentity);
			ResultSet rs = ps.executeQuery();
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
			fault.setFaultString("Unexpected Database Error: Error determining if the user " + gridIdentity
				+ " has the attribute " + des.getName() + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public String getAttribute(String gridIdentity) throws DorianInternalFault {
		this.create();
		Connection c = null;
		String value = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			PreparedStatement ps = c
				.prepareStatement("select ATT_VALUE from " + tableName + " where GRID_IDENTITY = ?");
			ps.setString(1, gridIdentity);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				value = rs.getString(1);
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error: Error getting the attribute " + des.getName()
				+ " for the user " + gridIdentity + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return value;
	}


	public void removeAttribute(String gridIdentity) throws DorianInternalFault {
		this.create();
		Connection c = null;
		try {
			c = db.getConnection();
			if (hasAttribute(gridIdentity)) {
				PreparedStatement ps = c.prepareStatement("delete from " + tableName + " where GRID_IDENTITY =?");
				ps.setString(1, gridIdentity);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not remove the attribute " + des.getName() + " for the user "
				+ gridIdentity + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			try {
				db.releaseConnection(c);
			} catch (Exception exception) {
				this.log.error(exception.getMessage(), exception);
			}
		}
	}


	private void validateAttribute(String value) throws IllegalUserAttributeFault {
		String expr = Utils.clean(des.getRegularExpressionValueRestriction());
		if (expr != null) {
			Pattern pattern = Pattern.compile(des.getRegularExpressionValueRestriction());
			Matcher matcher = pattern.matcher(value);
			if (!matcher.matches()) {
				IllegalUserAttributeFault fault = new IllegalUserAttributeFault();
				fault.setFaultString("The user attribute " + des.getName() + " does not match the required pattern, "
					+ des.getRegularExpressionValueRestriction() + ".");
				throw fault;
			}
		}
	}


	public void addUpdateAttribute(String gridIdentity, String value) throws DorianInternalFault,
		IllegalUserAttributeFault {
		this.create();
		Connection c = null;
		validateAttribute(value);
		try {
			c = db.getConnection();
			if (!hasAttribute(gridIdentity)) {
				PreparedStatement ps = c.prepareStatement("INSERT INTO " + tableName + " SET " + "GRID_IDENTITY"
					+ " = ?, " + "ATT_VALUE" + " = ?");
				ps.setString(1, gridIdentity);
				ps.setString(2, value);
				ps.executeUpdate();
			} else {
				PreparedStatement ps = c.prepareStatement("UPDATE " + tableName + " SET ATT_VALUE" + " = ?"+" WHERE GRID_IDENTITY=?");
				ps.setString(1, value);
				ps.setString(2, gridIdentity);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not add the attribute " + des.getName() + " for the user "
				+ gridIdentity + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			try {
				db.releaseConnection(c);
			} catch (Exception exception) {
				this.log.error(exception.getMessage(), exception);
			}
		}
	}


	private void create() throws DorianInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(tableName)) {
				String table = "CREATE TABLE " + tableName + " (" + "GRID_IDENTITY VARCHAR(255) NOT NULL PRIMARY KEY,"
					+ "ATT_VALUE TEXT NOT NULL," + "INDEX document_index (GRID_IDENTITY));";
				db.update(table);
			}
			this.dbBuilt = true;
		}
	}


	public void delete() throws DorianInternalFault {
		db.update("DROP TABLE IF EXISTS " + tableName);
		dbBuilt = false;
	}

}