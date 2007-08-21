package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.conf.LockoutTime;
import gov.nih.nci.cagrid.dorian.conf.PasswordSecurityPolicy;
import gov.nih.nci.cagrid.dorian.idp.bean.PasswordSecurity;
import gov.nih.nci.cagrid.dorian.idp.bean.PasswordStatus;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.test.Utils;
import junit.framework.TestCase;


public class TestPasswordSecurityManager extends TestCase {
	private Database db;


	public void testGetAndDeleteEntry() {
		PasswordSecurityManager psm = null;
		try {
			psm = new PasswordSecurityManager(db, getPolicy());
			String uid = "user";
			assertEquals(false, psm.entryExists(uid));
			PasswordSecurity entry = psm.getEntry(uid);
			assertEquals(true, psm.entryExists(uid));
			validateEntry(entry, 0, 0, false,PasswordStatus.Valid);
			assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));
			psm.deleteEntry(uid);
			assertEquals(false, psm.entryExists(uid));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				psm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testSuspendedPassword() {
		PasswordSecurityManager psm = null;
		try {
			PasswordSecurityPolicy policy = getPolicy();
			psm = new PasswordSecurityManager(db, policy);
			for (int j = 0; j < 2; j++) {
				String uid = "user" + j;
				assertEquals(false, psm.entryExists(uid));
				validateEntry(psm.getEntry(uid), 0, 0, false,PasswordStatus.Valid);
				assertEquals(true, psm.entryExists(uid));
				assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));

				int localCount = 0;
				boolean expiredOnce = false;
				for (int i = 1; i <= (policy.getMaxTotalInvalidLogins() + 1); i++) {
					psm.reportInvalidLoginAttempt(uid);
					localCount = localCount + 1;
					if (i >= policy.getMaxTotalInvalidLogins()) {
						if (localCount == policy.getMaxConsecutiveInvalidLogins()) {
							localCount = 0;
						}
						validateEntry(psm.getEntry(uid), localCount, i, expiredOnce,PasswordStatus.LockedUntilChanged);
						assertEquals(PasswordStatus.LockedUntilChanged, psm.getPasswordStatus(uid));
					} else if (localCount != policy.getMaxConsecutiveInvalidLogins()) {
						validateEntry(psm.getEntry(uid), localCount, i, expiredOnce,PasswordStatus.Valid);
						assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));
					} else {
						localCount = 0;
						expiredOnce = true;
						validateEntry(psm.getEntry(uid), localCount, i, expiredOnce,PasswordStatus.Locked);
						assertEquals(PasswordStatus.Locked, psm.getPasswordStatus(uid));

						psm.reportSuccessfulLoginAttempt(uid);
						validateEntry(psm.getEntry(uid), localCount, i, expiredOnce,PasswordStatus.Locked);
						assertEquals(PasswordStatus.Locked, psm.getPasswordStatus(uid));

						Thread.sleep((policy.getLockoutTime().getSeconds() * 1000) + 100);
						assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));
						validateEntry(psm.getEntry(uid), localCount, i, expiredOnce,PasswordStatus.Valid);
					}

				}
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				psm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void testResetPassword() {
		PasswordSecurityManager psm = null;
		try {
			PasswordSecurityPolicy policy = getPolicy();
			psm = new PasswordSecurityManager(db, policy);
			String uid = "user";
			assertEquals(false, psm.entryExists(uid));
			validateEntry(psm.getEntry(uid), 0, 0, false,PasswordStatus.Valid);
			assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));

			psm.reportInvalidLoginAttempt(uid);
			validateEntry(psm.getEntry(uid),  1, 1, false,PasswordStatus.Valid);
			assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));

			psm.reportInvalidLoginAttempt(uid);
			validateEntry(psm.getEntry(uid),  2, 2, false,PasswordStatus.Valid);
			assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));

			psm.reportSuccessfulLoginAttempt(uid);
			validateEntry(psm.getEntry(uid),  0, 2, false,PasswordStatus.Valid);
			assertEquals(PasswordStatus.Valid, psm.getPasswordStatus(uid));

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				psm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	protected void validateEntry(PasswordSecurity entry, long count, long totalCount, boolean expired, PasswordStatus status) {
		assertEquals(count, entry.getConsecutiveInvalidLogins());
		assertEquals(totalCount, entry.getTotalInvalidLogins());
		assertEquals(status, entry.getPasswordStatus());
		if (expired) {
			if (entry.getLockoutExpiration() <= 0) {
				fail("Password should be locked.");
			}
		} else {
			assertEquals(0, entry.getLockoutExpiration());
		}
	}


	private PasswordSecurityPolicy getPolicy() {
		PasswordSecurityPolicy policy = new PasswordSecurityPolicy();
		LockoutTime time = new LockoutTime();
		time.setHours(0);
		time.setMinutes(0);
		time.setSeconds(3);
		policy.setLockoutTime(time);
		policy.setMaxConsecutiveInvalidLogins(3);
		policy.setMaxTotalInvalidLogins(8);
		return policy;
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {

			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
}
