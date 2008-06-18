package org.cagrid.gaards.dorian.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.cagrid.gaards.dorian.common.AddressValidator;
import org.cagrid.gaards.dorian.common.Crypt;
import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault;
import org.cagrid.gaards.dorian.stubs.types.NoSuchUserFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.tools.database.Database;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends LoggingObject {

	public static String INVALID_PASSWORD_MESSAGE = "Invalid password, a valid password CANNOT contain a dictionary word and MUST contain at least one upper case letter, at least one lower case letter, at least one number, and at least one symbol (~!@#$%^&*()_-+={}[]|:;<>,.?)";

	public static String ADMIN_USER_ID = "dorian";

	public static String ADMIN_PASSWORD = "DorianAdmin$1";

	private static final String IDP_USERS_TABLE = "idp_users";

	private Database db;

	private boolean dbBuilt = false;

	private IdentityProviderProperties conf;

	private PasswordSecurityManager passwordSecurityManager;


	public UserManager(Database db, IdentityProviderProperties conf) throws DorianInternalFault {
		this.db = db;
		this.conf = conf;
		this.passwordSecurityManager = new PasswordSecurityManager(db, conf.getPasswordSecurityPolicy());
	}


	public IdPUser authenticateAndVerifyUser(BasicAuthCredential credential) throws DorianInternalFault,
		PermissionDeniedFault {
		try {
			IdPUser u = getUser(credential.getUserId());

			PasswordStatus status = this.passwordSecurityManager.getPasswordStatus(u.getUserId());

			if (status.equals(PasswordStatus.Valid)) {
				if (!u.getPassword().equals(Crypt.crypt(credential.getPassword()))) {
					passwordSecurityManager.reportInvalidLoginAttempt(u.getUserId());
					PermissionDeniedFault fault = new PermissionDeniedFault();
					fault.setFaultString("The uid or password is incorrect.");
					throw fault;
				} else {
					passwordSecurityManager.reportSuccessfulLoginAttempt(u.getUserId());
				}
			} else if (status.equals(PasswordStatus.LockedUntilChanged)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
					.setFaultString("This account has been locked because the maximum number of invalid logins has been exceeded, please contact an administrator to have your password reset.");
				throw fault;
			} else if (status.equals(PasswordStatus.Locked)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault
					.setFaultString("This account has been temporarily locked because the maximum number of consecutive invalid logins has been exceeded.");
				throw fault;
			} else {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("Unexpected security status code received.");
				throw fault;
			}
			verifyUser(u);
			return u;
		} catch (NoSuchUserFault e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("User Id or password is incorrect");
			throw fault;
		}

	}


	public void verifyUser(IdPUser u) throws DorianInternalFault, PermissionDeniedFault {

		if (!u.getStatus().equals(IdPUserStatus.Active)) {
			if (u.getStatus().equals(IdPUserStatus.Suspended)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The account has been suspended.");
				throw fault;

			} else if (u.getStatus().equals(IdPUserStatus.Rejected)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The application for the account was rejected.");
				throw fault;

			} else if (u.getStatus().equals(IdPUserStatus.Pending)) {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("The application for this account has not yet been reviewed.");
				throw fault;
			} else {
				PermissionDeniedFault fault = new PermissionDeniedFault();
				fault.setFaultString("Unknown Reason");
				throw fault;
			}
		}

	}


	private void validateSpecifiedField(String fieldName, String name) throws InvalidUserPropertyFault {
		try {
			AddressValidator.validateField(fieldName, name);
		} catch (Exception e) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString(e.getMessage());
			throw fault;
		}
	}


	private void validatePassword(IdPUser user) throws DorianInternalFault, InvalidUserPropertyFault {
		String password = user.getPassword();
		if ((password == null) || (conf.getPasswordSecurityPolicy().getMinPasswordLength() > password.length())
			|| (conf.getPasswordSecurityPolicy().getMaxPasswordLength() < password.length())) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString("Unacceptable password, the length of the password must be between "
				+ conf.getPasswordSecurityPolicy().getMinPasswordLength() + " and " + conf.getPasswordSecurityPolicy().getMaxPasswordLength() + " characters.");
			throw fault;
		} else {
			boolean hasDictionaryWord = true;
			try {
				hasDictionaryWord = DictionaryCheck.doesStringContainDictionaryWord(password);
			} catch (IOException e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault
					.setFaultString("Unexpected error validating the user's password, please contact an administrator.");
				throw fault;
			}
			boolean hasCapital = PasswordUtils.hasCapitalLetter(password);
			boolean hasLowerCase = PasswordUtils.hasLowerCaseLetter(password);
			boolean hasNumber = PasswordUtils.hasNumber(password);
			boolean hasSymbol = PasswordUtils.hasSymbol(password);
			if ((!hasCapital) || (!hasLowerCase) || (!hasNumber) || (!hasSymbol) || (hasDictionaryWord)) {
				InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
				fault.setFaultString(INVALID_PASSWORD_MESSAGE);
				throw fault;
			}

		}

	}


	private void validateUserId(IdPUser user) throws InvalidUserPropertyFault {
		String uid = user.getUserId();
		if ((uid == null) || (conf.getMinUserIdLength() > uid.length())
			|| (conf.getMaxUserIdLength() < uid.length())) {
			InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
			fault.setFaultString("Unacceptable User ID, the length of the user id must be between "
				+ conf.getMinUserIdLength() + " and " + conf.getMaxUserIdLength() + " characters.");
			throw fault;
		}
	}


	private void validateUser(IdPUser user) throws DorianInternalFault, InvalidUserPropertyFault {
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
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c
				.prepareStatement("INSERT INTO "
					+ IDP_USERS_TABLE
					+ " SET UID = ?, EMAIL= ?, PASSWORD= ?, FIRST_NAME= ?, LAST_NAME= ?, ORGANIZATION= ?, ADDRESS= ?, ADDRESS2= ?,CITY= ?, STATE= ?, ZIP_CODE= ?, COUNTRY= ?, PHONE_NUMBER= ?, STATUS= ?, ROLE= ?");
			ps.setString(1, user.getUserId());
			ps.setString(2, user.getEmail());
			ps.setString(3, Crypt.crypt(user.getPassword()));
			ps.setString(4, user.getFirstName());
			ps.setString(5, user.getLastName());
			ps.setString(6, user.getOrganization());
			ps.setString(7, user.getAddress());
			if (Utils.clean(user.getAddress2()) == null) {
				ps.setString(8, "");
			} else {
				ps.setString(8, user.getAddress2());
			}
			ps.setString(9, user.getCity());
			ps.setString(10, user.getState().getValue());
			ps.setString(11, user.getZipcode());
			ps.setString(12, user.getCountry().getValue());
			ps.setString(13, user.getPhoneNumber());
			ps.setString(14, user.getStatus().getValue());
			ps.setString(15, user.getRole().getValue());
			ps.executeUpdate();
			ps.close();
			user.setPasswordSecurity(this.passwordSecurityManager.getEntry(user.getUserId()));
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, Could not add user!!!");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public synchronized void removeUser(String uid) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM " + IDP_USERS_TABLE + " WHERE UID= ?");
			ps.setString(1, uid);
			ps.executeUpdate();
			ps.close();
			this.passwordSecurityManager.deleteEntry(uid);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, Could not delete user!!!");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
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
			PreparedStatement ps = null;
			if (filter != null) {
				ps = c
					.prepareStatement("select * from "
						+ IDP_USERS_TABLE
						+ " WHERE UID LIKE ? AND EMAIL LIKE ? AND FIRST_NAME LIKE ? AND LAST_NAME LIKE ? AND ORGANIZATION LIKE ? AND ADDRESS LIKE ? AND ADDRESS2 LIKE ? AND CITY LIKE ? AND STATE LIKE ? AND ZIP_CODE LIKE ? AND COUNTRY LIKE ? AND PHONE_NUMBER LIKE ? AND STATUS LIKE ? AND ROLE LIKE ?");

				if (filter.getUserId() != null) {
					ps.setString(1, "%" + filter.getUserId() + "%");
				} else {
					ps.setString(1, "%");
				}

				if (filter.getEmail() != null) {
					ps.setString(2, "%" + filter.getEmail() + "%");
				} else {
					ps.setString(2, "%");
				}

				if (filter.getFirstName() != null) {
					ps.setString(3, "%" + filter.getFirstName() + "%");
				} else {
					ps.setString(3, "%");
				}

				if (filter.getLastName() != null) {
					ps.setString(4, "%" + filter.getLastName() + "%");
				} else {
					ps.setString(4, "%");
				}

				if (filter.getOrganization() != null) {
					ps.setString(5, "%" + filter.getOrganization() + "%");
				} else {
					ps.setString(5, "%");
				}

				if (filter.getAddress() != null) {
					ps.setString(6, "%" + filter.getAddress() + "%");
				} else {
					ps.setString(6, "%");
				}

				if (filter.getAddress2() != null) {
					ps.setString(7, "%" + filter.getAddress2() + "%");
				} else {
					ps.setString(7, "%");
				}

				if (filter.getCity() != null) {
					ps.setString(8, "%" + filter.getCity() + "%");
				} else {
					ps.setString(8, "%");
				}

				if (filter.getState() != null) {
					ps.setString(9, "%" + filter.getState() + "%");
				} else {
					ps.setString(9, "%");
				}

				if (filter.getZipcode() != null) {
					ps.setString(10, "%" + filter.getZipcode() + "%");
				} else {
					ps.setString(10, "%");
				}

				if (filter.getCountry() != null) {
					ps.setString(11, "%" + filter.getCountry() + "%");
				} else {
					ps.setString(11, "%");
				}

				if (filter.getPhoneNumber() != null) {
					ps.setString(12, "%" + filter.getPhoneNumber() + "%");
				} else {
					ps.setString(12, "%");
				}

				if (filter.getStatus() != null) {
					ps.setString(13, filter.getStatus().getValue());
				} else {
					ps.setString(13, "%");
				}

				if (filter.getRole() != null) {
					ps.setString(14, filter.getRole().getValue());
				} else {
					ps.setString(14, "%");
				}
			} else {
				ps = c.prepareStatement("select * from " + IDP_USERS_TABLE);
			}
			// System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
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
				user.setPasswordSecurity(this.passwordSecurityManager.getEntry(user.getUserId()));
				users.add(user);
			}
			rs.close();
			ps.close();

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
			PreparedStatement s = c.prepareStatement("select * from " + IDP_USERS_TABLE + " where UID= ?");
			s.setString(1, uid);
			ResultSet rs = s.executeQuery();
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
				user.setPasswordSecurity(this.passwordSecurityManager.getEntry(uid));
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
			try {
				if (!this.db.tableExists(IDP_USERS_TABLE)) {
					String applications = "CREATE TABLE " + IDP_USERS_TABLE + " ("
						+ "UID VARCHAR(255) NOT NULL PRIMARY KEY," + "EMAIL VARCHAR(255) NOT NULL,"
						+ "PASSWORD VARCHAR(255) NOT NULL," + "FIRST_NAME VARCHAR(255) NOT NULL,"
						+ "LAST_NAME VARCHAR(255) NOT NULL," + "ORGANIZATION VARCHAR(255) NOT NULL,"
						+ "ADDRESS VARCHAR(255) NOT NULL," + "ADDRESS2 VARCHAR(255)," + "CITY VARCHAR(255) NOT NULL,"
						+ "STATE VARCHAR(20) NOT NULL," + "ZIP_CODE VARCHAR(20) NOT NULL,"
						+ "COUNTRY VARCHAR(2) NOT NULL," + "PHONE_NUMBER VARCHAR(20) NOT NULL,"
						+ "STATUS VARCHAR(20) NOT NULL," + "ROLE VARCHAR(20) NOT NULL,"
						+ "INDEX document_index (EMAIL));";
					db.update(applications);
					try {
						IdPUser u = new IdPUser();
						u.setUserId(ADMIN_USER_ID);
						u.setPassword(ADMIN_PASSWORD);
						u.setEmail("dorian@dorian.org");
						u.setFirstName("Mr.");
						u.setLastName("Administrator");
						u.setOrganization("caBIG");
						u.setAddress("3184 Graves Hall");
						u.setAddress2("333 W. Tenth Avenue");
						u.setCity("Columbus");
						u.setState(StateCode.OH);
						u.setZipcode("43210");
						u.setCountry(CountryCode.US);
						u.setPhoneNumber("555-555-5555");
						u.setStatus(IdPUserStatus.Active);
						u.setRole(IdPUserRole.Administrator);
						this.addUser(u);
					} catch (Exception e) {
						logError(e.getMessage(), e);
						DorianInternalFault fault = new DorianInternalFault();
						fault.setFaultString("Unexpected Error, Could not add initial IdP user!!!");
						FaultHelper helper = new FaultHelper(fault);
						helper.addFaultCause(e);
						fault = (DorianInternalFault) helper.getFault();
						throw fault;
					}
				}
				this.dbBuilt = true;

			} catch (DorianInternalFault e) {
				throw e;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("An unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
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
			boolean passwordChanged = false;
			if (u.getPassword() != null) {

				String newPass = Crypt.crypt(u.getPassword());
				if (!newPass.equals(curr.getPassword())) {
					validatePassword(u);
					curr.setPassword(newPass);
					passwordChanged = true;
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
				curr.setEmail(u.getEmail());
			}

			if ((u.getFirstName() != null) && (!u.getFirstName().equals(curr.getFirstName()))) {
				validateSpecifiedField("First Name", u.getFirstName());
				curr.setFirstName(u.getFirstName());
			}

			if ((u.getLastName() != null) && (!u.getLastName().equals(curr.getLastName()))) {
				validateSpecifiedField("Last Name", u.getLastName());
				curr.setLastName(u.getLastName());
			}

			if ((u.getOrganization() != null) && (!u.getOrganization().equals(curr.getOrganization()))) {
				validateSpecifiedField("Organization", u.getOrganization());
				curr.setOrganization(u.getOrganization());
			}

			if ((u.getAddress() != null) && (!u.getAddress().equals(curr.getAddress()))) {
				validateSpecifiedField("Address", u.getAddress());
				curr.setAddress(u.getAddress());
			}

			if ((u.getAddress2() != null) && (!u.getAddress2().equals(curr.getAddress2()))) {
				curr.setAddress2(u.getAddress2());
			}

			if ((u.getCity() != null) && (!u.getCity().equals(curr.getCity()))) {
				validateSpecifiedField("City", u.getCity());
				curr.setCity(u.getCity());
			}

			if ((u.getState() != null) && (!u.getState().equals(curr.getState()))) {
				curr.setState(u.getState());
			}

			if ((u.getCountry() != null) && (!u.getCountry().equals(curr.getCountry()))) {
				curr.setCountry(u.getCountry());
			}

			if ((u.getZipcode() != null) && (!u.getZipcode().equals(curr.getZipcode()))) {

				validateSpecifiedField("Zip Code", u.getZipcode());
				curr.setZipcode(u.getZipcode());
			}

			if ((u.getPhoneNumber() != null) && (!u.getPhoneNumber().equals(curr.getPhoneNumber()))) {
				validateSpecifiedField("Phone Number", u.getPhoneNumber());
				curr.setPhoneNumber(u.getPhoneNumber());
			}

			if ((u.getStatus() != null) && (!u.getStatus().equals(curr.getStatus()))) {
				if (accountCreated(curr.getStatus()) && !accountCreated(u.getStatus())) {
					InvalidUserPropertyFault fault = new InvalidUserPropertyFault();
					fault.setFaultString("Error, cannot change " + u.getUserId()
						+ "'s status from a post-created account status (" + curr.getStatus()
						+ ") to a pre-created account status (" + u.getStatus() + ").");
					throw fault;
				}

				curr.setStatus(u.getStatus());
			}

			if ((u.getRole() != null) && (!u.getRole().equals(curr.getRole()))) {
				if (changes > 0) {
					sb.append(",");
				}
				curr.setRole(u.getRole());
			}

			Connection c = null;
			try {
				c = db.getConnection();
				PreparedStatement ps = c
					.prepareStatement("UPDATE "
						+ IDP_USERS_TABLE
						+ " SET UID = ?, EMAIL= ?, PASSWORD= ?, FIRST_NAME= ?, LAST_NAME= ?, ORGANIZATION= ?, ADDRESS= ?, ADDRESS2= ?,CITY= ?, STATE= ?, ZIP_CODE= ?, COUNTRY= ?, PHONE_NUMBER= ?, STATUS= ?, ROLE= ? WHERE UID = ?");
				ps.setString(1, curr.getUserId());
				ps.setString(2, curr.getEmail());
				ps.setString(3, curr.getPassword());
				ps.setString(4, curr.getFirstName());
				ps.setString(5, curr.getLastName());
				ps.setString(6, curr.getOrganization());
				ps.setString(7, curr.getAddress());
				ps.setString(8, curr.getAddress2());
				ps.setString(9, curr.getCity());
				ps.setString(10, curr.getState().getValue());
				ps.setString(11, curr.getZipcode());
				ps.setString(12, curr.getCountry().getValue());
				ps.setString(13, curr.getPhoneNumber());
				ps.setString(14, curr.getStatus().getValue());
				ps.setString(15, curr.getRole().getValue());
				ps.setString(16, curr.getUserId());
				ps.executeUpdate();
				ps.close();

				if (passwordChanged) {
					this.passwordSecurityManager.deleteEntry(curr.getUserId());
				}
			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("Unexpected Error, Could not update user!!!");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			} finally {
				db.releaseConnection(c);
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
			PreparedStatement s = c.prepareStatement("select count(*) from " + IDP_USERS_TABLE + " where UID= ?");
			s.setString(1, uid);
			ResultSet rs = s.executeQuery();
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


	public void clearDatabase() throws DorianInternalFault {
		this.buildDatabase();
		try {
			db.update("drop TABLE " + IDP_USERS_TABLE);
			this.passwordSecurityManager.clearDatabase();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

}