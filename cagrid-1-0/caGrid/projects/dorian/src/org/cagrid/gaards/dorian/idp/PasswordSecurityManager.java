package org.cagrid.gaards.dorian.idp;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.tools.database.Database;

public class PasswordSecurityManager extends LoggingObject {

	private static final String TABLE = "idp_password_security";
	private static final String UID = "UID";
	private static final String CONSECUTIVE_INVALID_LOGINS = "CONSECUTIVE_INVALID_LOGINS";
	private static final String LOCK_OUT_EXPIRATION = "LOCK_OUT_EXPIRATION";
	private static final String TOTAL_INVALID_LOGINS = "TOTAL_INVALID_LOGINS";

	private Database db;

	private boolean dbBuilt = false;
	private PasswordSecurityPolicy policy;

	public PasswordSecurityManager(Database db, PasswordSecurityPolicy policy) {
		this.db = db;
		this.policy = policy;
	}

	public synchronized boolean entryExists(String uid)
			throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from "
					+ TABLE + " where UID= ?");
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

	private synchronized void insertEntry(String uid)
			throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c.prepareStatement("INSERT INTO " + TABLE
					+ " SET " + UID + " = ?, " + CONSECUTIVE_INVALID_LOGINS
					+ "= ?, " + TOTAL_INVALID_LOGINS + "= ?, "
					+ LOCK_OUT_EXPIRATION + "= ?");
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

	public void reportSuccessfulLoginAttempt(String uid)
			throws DorianInternalFault {
		PasswordSecurity entry = getEntry(uid);
		entry.setConsecutiveInvalidLogins(0);
		updateEntry(uid, entry);
	}

	public void reportInvalidLoginAttempt(String uid)
			throws DorianInternalFault {
		PasswordSecurity entry = getEntry(uid);
		long count = entry.getConsecutiveInvalidLogins() + 1;
		long total = entry.getTotalInvalidLogins() + 1;
		if (count >= policy.getConsecutiveInvalidLogins()) {
			entry.setConsecutiveInvalidLogins(0);
			entry.setTotalInvalidLogins(total);
			Calendar exp = new GregorianCalendar();
			exp.add(Calendar.HOUR_OF_DAY, policy.getLockout().getHours());
			exp.add(Calendar.MINUTE, policy.getLockout().getMinutes());
			exp.add(Calendar.SECOND, policy.getLockout().getSeconds());
			entry.setLockoutExpiration(exp.getTimeInMillis());
			updateEntry(uid, entry);
		} else {
			entry.setConsecutiveInvalidLogins(count);
			entry.setTotalInvalidLogins(total);
			updateEntry(uid, entry);
		}
	}

	public synchronized PasswordSecurity getEntry(String uid)
			throws DorianInternalFault {
		this.buildDatabase();
		if (!entryExists(uid)) {
			insertEntry(uid);
		}
		PasswordSecurity entry = null;
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + TABLE
					+ " where UID= ?");
			s.setString(1, uid);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				entry = new PasswordSecurity();
				entry.setConsecutiveInvalidLogins(rs
						.getLong(CONSECUTIVE_INVALID_LOGINS));
				entry.setTotalInvalidLogins(rs.getLong(TOTAL_INVALID_LOGINS));
				entry.setLockoutExpiration(rs.getLong(LOCK_OUT_EXPIRATION));
				if (entry.getTotalInvalidLogins() >= policy
						.getTotalInvalidLogins()) {
					entry.setPasswordStatus(PasswordStatus.LockedUntilChanged);
				} else {
					long curr = System.currentTimeMillis();
					long expires = entry.getLockoutExpiration();
					if (curr > expires) {
						entry.setPasswordStatus(PasswordStatus.Valid);
					} else {
						entry.setPasswordStatus(PasswordStatus.Locked);
					}
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

		if (entry == null) {
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("An unexpected error occurred in locating the password security entry for "
							+ uid + ".");
			throw fault;
		}

		return entry;
	}

	public PasswordStatus getPasswordStatus(String uid)
			throws DorianInternalFault {
		return getEntry(uid).getPasswordStatus();
	}

	private synchronized void updateEntry(String uid, PasswordSecurity entry)
			throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement ps = c.prepareStatement("UPDATE " + TABLE
					+ " SET " + CONSECUTIVE_INVALID_LOGINS + "= ?,"
					+ TOTAL_INVALID_LOGINS + "= ?," + LOCK_OUT_EXPIRATION
					+ "= ?  WHERE " + UID + "=?");
			ps.setLong(1, entry.getConsecutiveInvalidLogins());
			ps.setLong(2, entry.getTotalInvalidLogins());
			ps.setLong(3, entry.getLockoutExpiration());
			ps.setString(4, uid);
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
			PreparedStatement ps = c.prepareStatement("DELETE FROM " + TABLE
					+ " WHERE " + UID + " = ?");
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
			try {
				if (!this.db.tableExists(TABLE)) {
					String table = "CREATE TABLE " + TABLE + " (" + UID
							+ " VARCHAR(255) NOT NULL PRIMARY KEY,"
							+ CONSECUTIVE_INVALID_LOGINS + " BIGINT NOT NULL,"
							+ TOTAL_INVALID_LOGINS + " BIGINT NOT NULL,"
							+ LOCK_OUT_EXPIRATION + " BIGINT NOT NULL,"
							+ "INDEX document_index (UID));";
					db.update(table);

				}
				this.dbBuilt = true;
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

	public void clearDatabase() throws DorianInternalFault {
		this.buildDatabase();
		try {
			db.update("drop TABLE " + TABLE);
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
