package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserRole;
import gov.nih.nci.cagrid.gums.idp.bean.UserStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends GUMSObject {

	private static final String IDP_USERS_TABLE = "GUMS_IDP_USERS";

	public static final UserStatus ACTIVE = UserStatus.fromValue("Active");

	public static final UserStatus SUSPENDED = UserStatus
			.fromValue("Suspended");

	public static final UserStatus REJECTED = UserStatus.fromValue("Rejected");

	public static final UserStatus PENDING = UserStatus.fromValue("Pending");

	public static final UserRole ADMINISTRATOR = UserRole
			.fromValue("Administrator");

	public static final UserRole NON_ADMINISTRATOR = UserRole
			.fromValue("Non Administrator");

	private Database db;

	private boolean dbBuilt = false;

	public UserManager(Database db) throws GUMSInternalFault {
		this.db = db;
	}

	public synchronized void addUser(User user) throws GUMSInternalFault {
		this.buildDatabase();
		db.update("INSERT INTO " + IDP_USERS_TABLE + " VALUES('"
				+ user.getEmail() + "','" + user.getPassword() + "','"
				+ user.getFirstName() + "','" + user.getLastName() + "','"
				+ user.getOrganization() + "','" + user.getAddress() + "','"
				+ user.getAddress2() + "','" + user.getCity() + "','"
				+ user.getState() + "','" + user.getZipcode() + "','"
				+ user.getPhoneNumber() + "','" + user.getStatus().getValue()
				+ "','" + user.getRole().getValue() + "')");
	}

	public synchronized void removeUser(String email) throws GUMSInternalFault {
		this.buildDatabase();
		db.update("DELETE FROM " + IDP_USERS_TABLE + " WHERE EMAIL='" + email
				+ "'");
	}

	private StringBuffer appendWhereOrAnd(boolean firstAppended,
			StringBuffer sql) {
		if (firstAppended) {
			sql.append(" AND ");
		} else {
			sql.append(" WHERE");
		}
		return sql;
	}

	public User[] getUsers() throws GUMSInternalFault, NoSuchUserFault {
		return getUsers(null, null);
	}

	public User[] getUsers(UserStatus status) throws GUMSInternalFault,
			NoSuchUserFault {
		return getUsers(status, null);
	}

	public User[] getUsers(UserRole role) throws GUMSInternalFault,
			NoSuchUserFault {
		return getUsers(null, role);
	}

	public User[] getUsers(UserStatus status, UserRole role)
			throws GUMSInternalFault, NoSuchUserFault {
		this.buildDatabase();

		this.buildDatabase();
		Connection c = null;
		List users = new ArrayList();
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + IDP_USERS_TABLE);
			boolean firstAppended = false;
			if (status != null) {
				sql = appendWhereOrAnd(firstAppended, sql);
				firstAppended = true;
				sql.append(" STATUS='" + status.getValue() + "'");
			}

			if (role != null) {
				sql = appendWhereOrAnd(firstAppended, sql);
				firstAppended = true;
				sql.append(" ROLE='" + role.getValue() + "'");
			}

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				User user = new User();
				user.setEmail(rs.getString("EMAIL"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setFirstName(rs.getString("FIRST_NAME"));
				user.setLastName(rs.getString("LAST_NAME"));
				user.setOrganization(rs.getString("ORGANIZATION"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setAddress2(rs.getString("ADDRESS2"));
				user.setCity(rs.getString("CITY"));
				user.setState(rs.getString("STATE"));
				user.setZipcode(rs.getString("ZIP_CODE"));
				user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				user.setStatus(UserStatus.fromValue(rs.getString("STATUS")));
				user.setRole(UserRole.fromValue(rs.getString("ROLE")));
				users.add(user);
			}
			rs.close();
			s.close();

			User[] list = new User[users.size()];
			for (int i = 0; i < list.length; i++) {
				list[i] = (User) users.get(i);
			}
			return list;

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
	}

	public User getUser(String email) throws GUMSInternalFault, NoSuchUserFault {
		this.buildDatabase();
		User user = new User();
		Connection c = null;

		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + IDP_USERS_TABLE
					+ " where EMAIL='" + email + "'");
			if (rs.next()) {
				user.setEmail(email);
				user.setPassword(rs.getString("PASSWORD"));
				user.setFirstName(rs.getString("FIRST_NAME"));
				user.setLastName(rs.getString("LAST_NAME"));
				user.setOrganization(rs.getString("ORGANIZATION"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setAddress2(rs.getString("ADDRESS2"));
				user.setCity(rs.getString("CITY"));
				user.setState(rs.getString("STATE"));
				user.setZipcode(rs.getString("ZIP_CODE"));
				user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				user.setStatus(UserStatus.fromValue(rs.getString("STATUS")));
				user.setRole(UserRole.fromValue(rs.getString("ROLE")));
			} else {
				NoSuchUserFault fault = new NoSuchUserFault();
				fault.setFaultString("The user " + email + " does not exist.");
				throw fault;
			}
			rs.close();
			s.close();

		} catch (NoSuchUserFault f) {
			throw f;
		} catch (Exception e) {

			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain the user "
					+ email + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return user;
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(IDP_USERS_TABLE)) {
				String applications = "CREATE TABLE " + IDP_USERS_TABLE + " ("
						+ "EMAIL VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "PASSWORD VARCHAR(255) NOT NULL,"
						+ "FIRST_NAME VARCHAR(255) NOT NULL,"
						+ "LAST_NAME VARCHAR(255) NOT NULL,"
						+ "ORGANIZATION VARCHAR(255) NOT NULL,"
						+ "ADDRESS VARCHAR(255) NOT NULL,"
						+ "ADDRESS2 VARCHAR(255) NOT NULL,"
						+ "CITY VARCHAR(255) NOT NULL,"
						+ "STATE VARCHAR(20) NOT NULL,"
						+ "ZIP_CODE VARCHAR(20) NOT NULL,"
						+ "PHONE_NUMBER VARCHAR(20) NOT NULL,"
						+ "STATUS VARCHAR(20) NOT NULL,"
						+ "ROLE VARCHAR(20) NOT NULL,"
						+ "INDEX document_index (EMAIL));";
				db.update(applications);
			}
			this.dbBuilt = true;
		}
	}

	public synchronized void changeUserStatus(String email, UserStatus status)
			throws GUMSInternalFault, NoSuchUserFault {
		this.buildDatabase();
		if (userExists(email)) {
			db.update("update " + IDP_USERS_TABLE + " SET STATUS='"
					+ status.getValue() + "' where EMAIL='" + email + "'");
		} else {
			NoSuchUserFault fault = new NoSuchUserFault();
			fault.setFaultString("The user " + email + " does not exist.");
			throw fault;
		}
	}

	public synchronized void changeUserRole(String email, UserRole role)
			throws GUMSInternalFault, NoSuchUserFault {
		this.buildDatabase();
		if (userExists(email)) {
			db.update("update " + IDP_USERS_TABLE + " SET ROLE='"
					+ role.getValue() + "' where EMAIL='" + email + "'");
		} else {
			NoSuchUserFault fault = new NoSuchUserFault();
			fault.setFaultString("The user " + email + " does not exist.");
			throw fault;
		}
	}

	public synchronized void changeUserPassword(String email, String password)
			throws GUMSInternalFault, NoSuchUserFault {
		this.buildDatabase();
		if (userExists(email)) {
			db.update("update " + IDP_USERS_TABLE + " SET PASSWORD='"
					+ password + "' where EMAIL='" + email + "'");
		} else {
			NoSuchUserFault fault = new NoSuchUserFault();
			fault.setFaultString("The user " + email + " does not exist.");
			throw fault;
		}
	}

	public boolean userExists(String email) throws GUMSInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from "
					+ IDP_USERS_TABLE + " where EMAIL='" + email + "'");
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
					.setFaultString("Unexpected Database Error, could not determine if the user "
							+ email + " exists.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}

	public void destroy() throws GUMSInternalFault {
		db.update("DROP TABLE IF EXISTS " + IDP_USERS_TABLE);
		dbBuilt = false;
	}

}