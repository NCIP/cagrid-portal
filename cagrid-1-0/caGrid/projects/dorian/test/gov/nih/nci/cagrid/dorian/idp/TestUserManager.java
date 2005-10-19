package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserRole;
import gov.nih.nci.cagrid.gums.idp.bean.UserStatus;

import java.io.File;

import junit.framework.TestCase;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestUserManager extends TestCase {
	private static final String DB = "TEST_GUMS";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";

	private Database db;

	private int count = 0;

	public void testMultipleUsers() {
		try {

			int userCount = 80;
			int activeNA = 0;
			int pendingNA = 0;
			int rejectedNA = 0;
			int suspendedNA = 0;

			int activeA = 0;
			int pendingA = 0;
			int rejectedA = 0;
			int suspendedA = 0;

			User[] users = new User[userCount];
			UserManager um = new UserManager(db);

			for (int i = 0; i < users.length; i++) {
				if ((i % 8) == 0) {
					users[i] = makeUser(UserManager.NON_ADMINISTRATOR,
							UserManager.ACTIVE);
					activeNA = activeNA + 1;
				} else if ((i % 8) == 1) {
					users[i] = makeUser(UserManager.NON_ADMINISTRATOR,
							UserManager.PENDING);
					pendingNA = pendingNA + 1;
				} else if ((i % 8) == 2) {
					users[i] = makeUser(UserManager.NON_ADMINISTRATOR,
							UserManager.REJECTED);
					rejectedNA = rejectedNA + 1;
				} else if ((i % 8) == 3) {
					users[i] = makeUser(UserManager.NON_ADMINISTRATOR,
							UserManager.SUSPENDED);
					suspendedNA = suspendedNA + 1;
				} else if ((i % 8) == 4) {
					users[i] = makeUser(UserManager.ADMINISTRATOR,
							UserManager.ACTIVE);
					activeA = activeA + 1;
				} else if ((i % 8) == 5) {
					users[i] = makeUser(UserManager.ADMINISTRATOR,
							UserManager.PENDING);
					pendingA = pendingA + 1;
				} else if ((i % 8) == 6) {
					users[i] = makeUser(UserManager.ADMINISTRATOR,
							UserManager.REJECTED);
					rejectedA = rejectedA + 1;
				} else if ((i % 8) == 7) {
					users[i] = makeUser(UserManager.ADMINISTRATOR,
							UserManager.SUSPENDED);
					suspendedA = suspendedA + 1;
				}

				um.addUser(users[i]);
				assertTrue(um.userExists(users[i].getEmail()));
				User u = um.getUser(users[i].getEmail());
				assertEquals(users[i], u);

				User[] list = um.getUsers();
				assertEquals(i+1, list.length);
				assertEquals(activeNA, um.getUsers(UserManager.ACTIVE,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(pendingNA, um.getUsers(UserManager.PENDING,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(rejectedNA, um.getUsers(UserManager.REJECTED,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(suspendedNA, um.getUsers(UserManager.SUSPENDED,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(activeA, um.getUsers(UserManager.ACTIVE,
						UserManager.ADMINISTRATOR).length);
				assertEquals(pendingA, um.getUsers(UserManager.PENDING,
						UserManager.ADMINISTRATOR).length);
				assertEquals(rejectedA, um.getUsers(UserManager.REJECTED,
						UserManager.ADMINISTRATOR).length);
				assertEquals(suspendedA, um.getUsers(UserManager.SUSPENDED,
						UserManager.ADMINISTRATOR).length);

			}
			
			int numberOfUsers = users.length;
			
			for (int i = 0; i < users.length; i++) {
				um.removeUser(users[i].getEmail());
				numberOfUsers = numberOfUsers - 1;
				if ((users[i].getStatus().equals(UserManager.ACTIVE)) &&
						(users[i].getRole().equals(UserManager.NON_ADMINISTRATOR))) {
					activeNA = activeNA - 1;
				} else if ((users[i].getStatus().equals(UserManager.PENDING)) &&
						(users[i].getRole().equals(UserManager.NON_ADMINISTRATOR))) {
					pendingNA = pendingNA - 1;
				} if ((users[i].getStatus().equals(UserManager.REJECTED)) &&
						(users[i].getRole().equals(UserManager.NON_ADMINISTRATOR))) {
					rejectedNA = rejectedNA - 1;
				} if ((users[i].getStatus().equals(UserManager.SUSPENDED)) &&
						(users[i].getRole().equals(UserManager.NON_ADMINISTRATOR))) {
					users[i] = makeUser(UserManager.NON_ADMINISTRATOR,
							UserManager.SUSPENDED);
					suspendedNA = suspendedNA - 1;
				} else if ((users[i].getStatus().equals(UserManager.ACTIVE)) &&
						(users[i].getRole().equals(UserManager.ADMINISTRATOR))) {
					activeA = activeA - 1;
				} else if ((users[i].getStatus().equals(UserManager.PENDING)) &&
						(users[i].getRole().equals(UserManager.ADMINISTRATOR))) {
					pendingA = pendingA - 1;
				} else if ((users[i].getStatus().equals(UserManager.REJECTED)) &&
						(users[i].getRole().equals(UserManager.ADMINISTRATOR))) {
					rejectedA = rejectedA - 1;
				} else if ((users[i].getStatus().equals(UserManager.SUSPENDED)) &&
						(users[i].getRole().equals(UserManager.ADMINISTRATOR))) {
					suspendedA = suspendedA - 1;
				}
				assertFalse(um.userExists(users[i].getEmail()));

				User[] list = um.getUsers();
				assertEquals(numberOfUsers, list.length);
				assertEquals(activeNA, um.getUsers(UserManager.ACTIVE,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(pendingNA, um.getUsers(UserManager.PENDING,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(rejectedNA, um.getUsers(UserManager.REJECTED,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(suspendedNA, um.getUsers(UserManager.SUSPENDED,
						UserManager.NON_ADMINISTRATOR).length);
				assertEquals(activeA, um.getUsers(UserManager.ACTIVE,
						UserManager.ADMINISTRATOR).length);
				assertEquals(pendingA, um.getUsers(UserManager.PENDING,
						UserManager.ADMINISTRATOR).length);
				assertEquals(rejectedA, um.getUsers(UserManager.REJECTED,
						UserManager.ADMINISTRATOR).length);
				assertEquals(suspendedA, um.getUsers(UserManager.SUSPENDED,
						UserManager.ADMINISTRATOR).length);

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testSingleUser() {
		try {
			UserManager um = new UserManager(db);
			User u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getEmail()));
			User u2 = um.getUser(u1.getEmail());
			assertEquals(u1, u2);

			User[] list = um.getUsers();
			assertEquals(1, list.length);
			assertEquals(u1, list[0]);
			assertEquals(1, um.getUsers(UserManager.ACTIVE,
					UserManager.NON_ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.PENDING,
					UserManager.NON_ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.REJECTED,
					UserManager.NON_ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.SUSPENDED,
					UserManager.NON_ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.ACTIVE,
					UserManager.ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.PENDING,
					UserManager.ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.REJECTED,
					UserManager.ADMINISTRATOR).length);
			assertEquals(0, um.getUsers(UserManager.SUSPENDED,
					UserManager.ADMINISTRATOR).length);

			um.removeUser(u1.getEmail());
			assertFalse(um.userExists(u1.getEmail()));

			try {
				um.getUser(u1.getEmail());
				assertTrue(false);
			} catch (NoSuchUserFault f) {

			}

			assertEquals(0, um.getUsers().length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private User makeActiveUser() {
		return makeUser(UserManager.NON_ADMINISTRATOR, UserManager.ACTIVE);
	}

	private User makeUser(UserRole role, UserStatus status) {
		User u = new User();
		u.setEmail("user" + count + "@mail.com");
		u.setPassword("password" + count);
		u.setFirstName("first" + count);
		u.setLastName("last" + count);
		u.setAddress("address" + count);
		u.setAddress2("address2" + count);
		u.setCity("Columbus");
		u.setState("Ohio");
		u.setZipcode("43210");
		u.setPhoneNumber("(614) 555-5555");
		u.setOrganization("organization" + count);
		u.setStatus(status);
		u.setRole(role);
		count = count + 1;
		return u;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			count = 0;
			Document doc = XMLUtilities.fileNameToDocument(DB_CONFIG);
			ConnectionManager cm = new ConnectionManager(doc.getRootElement());
			db = new Database(cm, DB);
			db.destroyDatabase();
			db.createDatabaseIfNeeded();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
