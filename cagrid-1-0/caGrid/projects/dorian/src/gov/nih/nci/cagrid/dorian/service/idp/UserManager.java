package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.AddressValidator;
import gov.nih.nci.cagrid.dorian.common.Crypt;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.NoSuchUserFault;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends LoggingObject {

	private static final String IDP_USERS_TABLE = "IDP_USERS";

	private Database db;

	private boolean dbBuilt = false;

	private IdPConfiguration conf;


	public UserManager(Database db, IdPConfiguration conf) throws DorianInternalFault {
		this.db = db;
		this.conf = conf;
	}


	private void validateSpecifiedField(String fieldName, String name) throws InvalidUserPropertyFault {
		try{
			AddressValidator.validateField(fieldName, name);
		}catch(Exception e){
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString(e.getMessage());
			throw fault;
		}
	}


	private void validatePassword(IdPUser user) throws InvalidUserPropertyFault {
		String password = user.getPassword();
		if ((password == null) || (conf.getMinimumPasswordLength() > password.length())
			|| (conf.getMaximumPasswordLength() < password.length())) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString("Unacceptable password, the length of the password must be between "
				+ conf.getMinimumPasswordLength() + " and " + conf.getMaximumPasswordLength());
			throw fault;
		}
	}


	private void validateUserId(IdPUser user) throws InvalidUserPropertyFault {
		String uid = user.getUserId();
		if ((uid == null) || (conf.getMinimumUIDLength() > uid.length()) || (conf.getMaximumUIDLength() < uid.length())) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString("Unacceptable User ID, the length of the password must be between "
				+ conf.getMinimumUIDLength() + " and " + conf.getMaximumUIDLength());
			throw fault;
		}
	}


	private void validateUser(IdPUser user) throws InvalidUserPropertyFault {
		validateUserId(user);
		validatePassword(user);
		validateSpecifiedField("First Name", user.getFirstName());
		validateSpecifiedField("Last Name", user.getLastName());
		validateSpecifiedField("Address", user.getAddress());
		validateSpecifiedField("City", user.getCity());
		validateSpecifiedField("Organization", user.getOrganization());
		validateSpecifiedField("Zip Code", user.getZipcode());
		validateSpecifiedField("Phone", user.getPhoneNumber());

		try {
			AddressValidator.validateEmail(user.getEmail());
		} catch (IllegalArgumentException e) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString(e.getMessage());
			throw fault;
		}
	}


	public synchronized void addUser(IdPUser user) throws DorianInternalFault, InvalidUserPropertyFault {
		this.buildDatabase();
		this.validateUser(user);
		if (userExists(user.getUserId())) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString("The user " + user.getUserId() + " already exists.");
			throw fault;
		}
		db.update("INSERT INTO " + IDP_USERS_TABLE + " VALUES('" + user.getUserId() + "','" + user.getEmail() + "','"
			+ Crypt.crypt(user.getPassword()) + "','" + user.getFirstName() + "','" + user.getLastName() + "','"
			+ user.getOrganization() + "','" + user.getAddress() + "','" + user.getAddress2() + "','" + user.getCity()
			+ "','" + user.getState().getValue() + "','" + user.getZipcode() + "','" + user.getCountry().getValue()
			+ "','" + user.getPhoneNumber() + "','" + user.getStatus().getValue() + "','" + user.getRole().getValue()
			+ "')");
	}


	public synchronized void removeUser(String uid) throws DorianInternalFault {
		this.buildDatabase();
		db.update("DELETE FROM " + IDP_USERS_TABLE + " WHERE UID='" + uid + "'");
	}


	private StringBuffer appendWhereOrAnd(boolean firstAppended, StringBuffer sql) {
		if (firstAppended) {
			sql.append(" AND ");
		} else {
			sql.append(" WHERE");
		}
		return sql;
	}


	public IdPUser[] getUsers(IdPUserFilter filter) throws DorianInternalFault {
		return getUsers(filter, true);
	}


	public IdPUser[] getUsers(IdPUserFilter filter, boolean includePassword) throws DorianInternalFault {

		this.buildDatabase();
		Connection c = null;
		List users = new ArrayList();
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + IDP_USERS_TABLE);
			if (filter != null) {
				boolean firstAppended = false;

				if (filter.getUserId() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" UID LIKE '%" + filter.getUserId() + "%'");
				}

				if (filter.getFirstName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" FIRST_NAME LIKE '%" + filter.getFirstName() + "%'");
				}

				if (filter.getLastName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" LAST_NAME LIKE '%" + filter.getLastName() + "%'");
				}

				if (filter.getOrganization() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ORGANIZATION LIKE '%" + filter.getOrganization() + "%'");
				}

				if (filter.getAddress() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ADDRESS LIKE '%" + filter.getAddress() + "%'");
				}

				if (filter.getAddress2() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ADDRESS2 LIKE '%" + filter.getAddress2() + "%'");
				}

				if (filter.getCity() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" CITY LIKE '%" + filter.getCity() + "%'");
				}

				if (filter.getState() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" STATE LIKE '%" + filter.getState().getValue() + "%'");
				}

				if (filter.getCountry() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" COUNTRY LIKE '%" + filter.getCountry().getValue() + "%'");
				}

				if (filter.getZipcode() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ZIP_CODE LIKE '%" + filter.getZipcode() + "%'");
				}

				if (filter.getEmail() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" EMAIL LIKE '%" + filter.getEmail() + "%'");
				}

				if (filter.getPhoneNumber() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" PHONE_NUMBER LIKE '%" + filter.getPhoneNumber() + "%'");
				}

				if (filter.getStatus() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" STATUS='" + filter.getStatus() + "'");
				}

				if (filter.getRole() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ROLE='" + filter.getRole() + "'");
				}
			}

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				IdPUser user = new IdPUser();
				user.setUserId(rs.getString("UID"));
				user.setEmail(rs.getString("EMAIL"));
				if (includePassword) {
					user.setPassword(rs.getString("PASSWORD"));
				}
				user.setFirstName(rs.getString("FIRST_NAME"));
				user.setLastName(rs.getString("LAST_NAME"));
				user.setOrganization(rs.getString("ORGANIZATION"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setAddress2(rs.getString("ADDRESS2"));
				user.setCity(rs.getString("CITY"));
				user.setState(StateCode.fromValue(rs.getString("STATE")));
				user.setZipcode(rs.getString("ZIP_CODE"));
				user.setCountry(CountryCode.fromValue(rs.getString("COUNTRY")));
				user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				user.setStatus(IdPUserStatus.fromValue(rs.getString("STATUS")));
				user.setRole(IdPUserRole.fromValue(rs.getString("ROLE")));
				users.add(user);
			}
			rs.close();
			s.close();

			IdPUser[] list = new IdPUser[users.size()];
			for (int i = 0; i < list.length; i++) {
				list[i] = (IdPUser) users.get(i);
			}
			return list;

		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public IdPUser getUser(String uid) throws DorianInternalFault, NoSuchUserFault {
		return this.getUser(uid, true);
	}


	public IdPUser getUser(String uid, boolean includePassword) throws DorianInternalFault, NoSuchUserFault {
		this.buildDatabase();
		IdPUser user = new IdPUser();
		Connection c = null;

		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + IDP_USERS_TABLE + " where UID='" + uid + "'");
			if (rs.next()) {
				user.setUserId(uid);
				user.setEmail(rs.getString("EMAIL"));
				if (includePassword) {
					user.setPassword(rs.getString("PASSWORD"));
				}
				user.setFirstName(rs.getString("FIRST_NAME"));
				user.setLastName(rs.getString("LAST_NAME"));
				user.setOrganization(rs.getString("ORGANIZATION"));
				user.setAddress(rs.getString("ADDRESS"));
				user.setAddress2(rs.getString("ADDRESS2"));
				user.setCity(rs.getString("CITY"));
				user.setState(StateCode.fromValue(rs.getString("STATE")));
				user.setZipcode(rs.getString("ZIP_CODE"));
				user.setCountry(CountryCode.fromValue(rs.getString("COUNTRY")));
				user.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				user.setStatus(IdPUserStatus.fromValue(rs.getString("STATUS")));
				user.setRole(IdPUserRole.fromValue(rs.getString("ROLE")));
			} else {
				NoSuchUserFault fault = new NoSuchUserFault();
				fault.setFaultString("The user " + uid + " does not exist.");
				throw fault;
			}
			rs.close();
			s.close();

		} catch (NoSuchUserFault f) {
			throw f;
		} catch (Exception e) {

			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain the user " + uid + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return user;
	}


	private void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(IDP_USERS_TABLE)) {
				String applications = "CREATE TABLE " + IDP_USERS_TABLE + " ("
					+ "UID VARCHAR(255) NOT NULL PRIMARY KEY," + "EMAIL VARCHAR(255) NOT NULL,"
					+ "PASSWORD VARCHAR(255) NOT NULL," + "FIRST_NAME VARCHAR(255) NOT NULL,"
					+ "LAST_NAME VARCHAR(255) NOT NULL," + "ORGANIZATION VARCHAR(255) NOT NULL,"
					+ "ADDRESS VARCHAR(255) NOT NULL," + "ADDRESS2 VARCHAR(255) NOT NULL,"
					+ "CITY VARCHAR(255) NOT NULL," + "STATE VARCHAR(20) NOT NULL," + "ZIP_CODE VARCHAR(20) NOT NULL,"
					+ "COUNTRY VARCHAR(2) NOT NULL," + "PHONE_NUMBER VARCHAR(20) NOT NULL,"
					+ "STATUS VARCHAR(20) NOT NULL," + "ROLE VARCHAR(20) NOT NULL," + "INDEX document_index (EMAIL));";
				db.update(applications);
			}
			this.dbBuilt = true;
		}
	}


	public synchronized void updateUser(IdPUser u) throws DorianInternalFault, NoSuchUserFault,
		InvalidUserPropertyFault {
		this.buildDatabase();
		if (u.getUserId() == null) {
			NoSuchUserFault fault = new NoSuchUserFault();
			fault.setFaultString("Could not update user, the user " + u.getUserId() + " does not exist.");
			throw fault;
		} else if (userExists(u.getUserId())) {
			StringBuffer sb = new StringBuffer();
			sb.append("update " + IDP_USERS_TABLE + " SET ");
			int changes = 0;
			IdPUser curr = this.getUser(u.getUserId());
			if (u.getPassword() != null) {
				validatePassword(u);
				String newPass = Crypt.crypt(u.getPassword());
				if (!newPass.equals(curr.getPassword())) {
					if (changes > 0) {
						sb.append(",");
					}
					sb.append("PASSWORD='" + newPass + "'");
					changes = changes + 1;
				}
			}

			if ((u.getEmail() != null) && (!u.getEmail().equals(curr.getEmail()))) {
				try {
					AddressValidator.validateEmail(u.getEmail());
				} catch (IllegalArgumentException e) {
					InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
					fault.setFaultString(e.getMessage());
					throw fault;
				}
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("EMAIL='" + u.getEmail() + "'");
				changes = changes + 1;
			}

			if ((u.getFirstName() != null) && (!u.getFirstName().equals(curr.getFirstName()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("First Name", u.getFirstName());
				sb.append("FIRST_NAME='" + u.getFirstName() + "'");
				changes = changes + 1;
			}

			if ((u.getLastName() != null) && (!u.getLastName().equals(curr.getLastName()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("Last Name", u.getLastName());
				sb.append("LAST_NAME='" + u.getLastName() + "'");
				changes = changes + 1;
			}

			if ((u.getOrganization() != null) && (!u.getOrganization().equals(curr.getOrganization()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("Organization", u.getOrganization());
				sb.append("ORGANIZATION='" + u.getOrganization() + "'");
				changes = changes + 1;
			}

			if ((u.getAddress() != null) && (!u.getAddress().equals(curr.getAddress()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("Address", u.getAddress());
				sb.append("ADDRESS='" + u.getAddress() + "'");
				changes = changes + 1;
			}

			if ((u.getAddress2() != null) && (!u.getAddress2().equals(curr.getAddress2()))) {
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("ADDRESS2='" + u.getAddress2() + "'");
				changes = changes + 1;
			}

			if ((u.getCity() != null) && (!u.getCity().equals(curr.getCity()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("City", u.getCity());
				sb.append("CITY='" + u.getCity() + "'");
				changes = changes + 1;
			}

			if ((u.getState() != null) && (!u.getState().equals(curr.getState()))) {
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("STATE='" + u.getState().getValue() + "'");
				changes = changes + 1;
			}

			if ((u.getCountry() != null) && (!u.getCountry().equals(curr.getCountry()))) {
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("COUNTRY='" + u.getCountry().getValue() + "'");
				changes = changes + 1;
			}

			if ((u.getZipcode() != null) && (!u.getZipcode().equals(curr.getZipcode()))) {
				if (changes > 0) {
					sb.append(",");
				}

				validateSpecifiedField("Zip Code", u.getZipcode());

				sb.append("ZIP_CODE='" + u.getZipcode() + "'");
				changes = changes + 1;
			}

			if ((u.getPhoneNumber() != null) && (!u.getPhoneNumber().equals(curr.getPhoneNumber()))) {
				if (changes > 0) {
					sb.append(",");
				}

				validateSpecifiedField("Phone Number", u.getPhoneNumber());

				sb.append("PHONE_NUMBER='" + u.getPhoneNumber() + "'");
				changes = changes + 1;
			}

			if ((u.getStatus() != null) && (!u.getStatus().equals(curr.getStatus()))) {
				if (changes > 0) {
					sb.append(",");
				}

				if (accountCreated(curr.getStatus()) && !accountCreated(u.getStatus())) {
					InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
					fault.setFaultString("Error, cannot change " + u.getUserId()
						+ "'s status from a post-created account status (" + curr.getStatus()
						+ ") to a pre-created account status (" + u.getStatus() + ").");
					throw fault;
				}

				sb.append("STATUS='" + u.getStatus().getValue() + "'");
				changes = changes + 1;
			}

			if ((u.getRole() != null) && (!u.getRole().equals(curr.getRole()))) {
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("ROLE='" + u.getRole().getValue() + "'");
				changes = changes + 1;
			}
			sb.append(" where UID='" + u.getUserId() + "'");
			if (changes > 0) {
				db.update(sb.toString());
			}

		} else {
			NoSuchUserFault fault = new NoSuchUserFault();
			fault.setFaultString("Could not update user, the user " + u.getUserId() + " does not exist.");
			throw fault;
		}
	}


	private boolean accountCreated(IdPUserStatus status) {
		if (status.equals(IdPUserStatus.Suspended)) {
			return true;
		} else if (status.equals(IdPUserStatus.Active)) {
			return true;
		} else {
			return false;
		}
	}


	public boolean userExists(String uid) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + IDP_USERS_TABLE + " where UID='" + uid + "'");
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
			fault.setFaultString("Unexpected Database Error, could not determine if the user " + uid + " exists.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public void destroy() throws DorianInternalFault {
		db.update("DROP TABLE IF EXISTS " + IDP_USERS_TABLE);
		dbBuilt = false;
	}

}