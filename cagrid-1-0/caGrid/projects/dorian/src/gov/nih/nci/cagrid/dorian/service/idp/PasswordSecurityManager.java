package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.conf.PasswordSecurityPolicy;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class PasswordSecurityManager extends LoggingObject {

	private static final String TABLE = "idp_password_security";
	private static final String UID = "UID";
	private static final String CONSECUTIVE_INVALID_LOGINS = "CONSECUTIVE_INVALID_LOGINS";
	private static final String LOCK_OUT_EXPIRATION = "LOCK_OUT_EXPIRATION";
	private static final String TOTAL_INVALID_LOGINS = "TOTAL_INVALID_LOGINS";
	public static final int VALID = 0;
	public static final int LOCKED = 1;
	public static final int LOCKED_UNTIL_CHANGED = 2;

	private Database db;

	private boolean dbBuilt = false;
	private PasswordSecurityPolicy policy;


	public PasswordSecurityManager(Database db, PasswordSecurityPolicy policy) {
		this.db = db;
		this.policy = policy;
	}


	public synchronized boolean entryExists(String uid) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from " + TABLE + " where UID= ?");
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
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	private synchronized void insertEntry(String uid) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c.prepareStatement("INSERT INTO " + TABLE + " SET " + UID + " = ?, "
				+ CONSECUTIVE_INVALID_LOGINS + "= ?, " + TOTAL_INVALID_LOGINS + "= ?, " + LOCK_OUT_EXPIRATION + "= ?");
			ps.setString(1, uid);
			ps.setLong(2, 0);
			ps.setLong(3, 0);
			ps.setLong(4, 0);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	public void reportSuccessfulLoginAttempt(String uid) throws DorianInternalFault {
		PasswordSecurityEntry entry = getEntry(uid);
		entry.setConsecutiveInvalidLogins(0);
		updateEntry(entry);
	}


	public void reportInvalidLoginAttempt(String uid) throws DorianInternalFault {
		PasswordSecurityEntry entry = getEntry(uid);
		long count = entry.getConsecutiveInvalidLogins() + 1;
		long total = entry.getTotalInvalidLogins() + 1;
		if (count >= policy.getMaxConsecutiveInvalidLogins()) {
			entry.setConsecutiveInvalidLogins(0);
			entry.setTotalInvalidLogins(total);
			Calendar exp = new GregorianCalendar();
			exp.add(Calendar.HOUR_OF_DAY, policy.getLockoutTime().getHours());
			exp.add(Calendar.MINUTE, policy.getLockoutTime().getMinutes());
			exp.add(Calendar.SECOND, policy.getLockoutTime().getSeconds());
			entry.setLockOutExpiration(exp.getTimeInMillis());
			updateEntry(entry);
		} else {
			entry.setConsecutiveInvalidLogins(count);
			entry.setTotalInvalidLogins(total);
			updateEntry(entry);
		}
	}


	public synchronized PasswordSecurityEntry getEntry(String uid) throws DorianInternalFault {
		this.buildDatabase();
		if (!entryExists(uid)) {
			insertEntry(uid);
		}
		PasswordSecurityEntry entry = null;
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + TABLE + " where UID= ?");
			s.setString(1, uid);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				entry = new PasswordSecurityEntry();
				entry.setUid(uid);
				entry.setConsecutiveInvalidLogins(rs.getLong(CONSECUTIVE_INVALID_LOGINS));
				entry.setTotalInvalidLogins(rs.getLong(TOTAL_INVALID_LOGINS));
				entry.setLockOutExpiration(rs.getLong(LOCK_OUT_EXPIRATION));
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

		if (entry == null) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred in locating the password security entry for " + uid
				+ ".");
			throw fault;
		}

		return entry;
	}


	public int getPasswordStatus(String uid) throws DorianInternalFault {
		PasswordSecurityEntry entry = getEntry(uid);
		if (entry.getTotalInvalidLogins() >= policy.getMaxTotalInvalidLogins()) {
			return LOCKED_UNTIL_CHANGED;
		} else {
			long curr = System.currentTimeMillis();
			long expires = entry.getLockOutExpiration();
			if (curr > expires) {
				return VALID;
			} else {
				return LOCKED;
			}
		}

	}


	private synchronized void updateEntry(PasswordSecurityEntry entry) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE " + TABLE + " SET " + CONSECUTIVE_INVALID_LOGINS + "= ?,"
				+ TOTAL_INVALID_LOGINS + "= ?," + LOCK_OUT_EXPIRATION + "= ?  WHERE " + UID + "=?");
			ps.setLong(1, entry.getConsecutiveInvalidLogins());
			ps.setLong(2, entry.getTotalInvalidLogins());
			ps.setLong(3, entry.getLockOutExpiration());
			ps.setString(4, entry.getUid());
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	public synchronized void deleteEntry(String uid) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c.prepareStatement("DELETE FROM " + TABLE + " WHERE " + UID + " = ?");
			ps.setString(1, uid);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	private void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(TABLE)) {
				String table = "CREATE TABLE " + TABLE + " (" + UID + " VARCHAR(255) NOT NULL PRIMARY KEY,"
					+ CONSECUTIVE_INVALID_LOGINS + " BIGINT NOT NULL," + TOTAL_INVALID_LOGINS + " BIGINT NOT NULL,"
					+ LOCK_OUT_EXPIRATION + " BIGINT NOT NULL," + "INDEX document_index (UID));";
				db.update(table);

			}
			this.dbBuilt = true;
		}
	}


	public void clearDatabase() throws DorianInternalFault {
		this.buildDatabase();
		db.update("drop TABLE " + TABLE);
	}
}
