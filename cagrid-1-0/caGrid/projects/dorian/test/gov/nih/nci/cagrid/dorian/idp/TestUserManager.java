package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.common.Crypt;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserFilter;
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

	private IdPProperties properties;

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
			UserManager um = new UserManager(db, properties);

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
				users[i].setPassword(Crypt.crypt(users[i].getPassword()));
				assertTrue(um.userExists(users[i].getEmail()));
				User u = um.getUser(users[i].getEmail());
				assertEquals(users[i], u);

				User[] list = um.getUsers();
				assertEquals(i + 1, list.length);
				UserFilter f = new UserFilter();
				f.setStatus(UserManager.ACTIVE);
				f.setRole(UserManager.NON_ADMINISTRATOR);
				assertEquals(activeNA, um.getUsers(f).length);

				f.setStatus(UserManager.PENDING);
				assertEquals(pendingNA, um.getUsers(f).length);

				f.setStatus(UserManager.REJECTED);
				assertEquals(rejectedNA, um.getUsers(f).length);

				f.setStatus(UserManager.SUSPENDED);
				assertEquals(suspendedNA, um.getUsers(f).length);

				f.setStatus(UserManager.ACTIVE);
				f.setRole(UserManager.ADMINISTRATOR);
				assertEquals(activeA, um.getUsers(f).length);

				f.setStatus(UserManager.PENDING);
				assertEquals(pendingA, um.getUsers(f).length);

				f.setStatus(UserManager.REJECTED);
				assertEquals(rejectedA, um.getUsers(f).length);

				f.setStatus(UserManager.SUSPENDED);
				assertEquals(suspendedA, um.getUsers(f).length);

			}

			int numberOfUsers = users.length;

			for (int i = 0; i < users.length; i++) {
				um.removeUser(users[i].getEmail());
				numberOfUsers = numberOfUsers - 1;
				if ((users[i].getStatus().equals(UserManager.ACTIVE))
						&& (users[i].getRole()
								.equals(UserManager.NON_ADMINISTRATOR))) {
					activeNA = activeNA - 1;
				} else if ((users[i].getStatus().equals(UserManager.PENDING))
						&& (users[i].getRole()
								.equals(UserManager.NON_ADMINISTRATOR))) {
					pendingNA = pendingNA - 1;
				}
				if ((users[i].getStatus().equals(UserManager.REJECTED))
						&& (users[i].getRole()
								.equals(UserManager.NON_ADMINISTRATOR))) {
					rejectedNA = rejectedNA - 1;
				}
				if ((users[i].getStatus().equals(UserManager.SUSPENDED))
						&& (users[i].getRole()
								.equals(UserManager.NON_ADMINISTRATOR))) {
					users[i] = makeUser(UserManager.NON_ADMINISTRATOR,
							UserManager.SUSPENDED);
					suspendedNA = suspendedNA - 1;
				} else if ((users[i].getStatus().equals(UserManager.ACTIVE))
						&& (users[i].getRole()
								.equals(UserManager.ADMINISTRATOR))) {
					activeA = activeA - 1;
				} else if ((users[i].getStatus().equals(UserManager.PENDING))
						&& (users[i].getRole()
								.equals(UserManager.ADMINISTRATOR))) {
					pendingA = pendingA - 1;
				} else if ((users[i].getStatus().equals(UserManager.REJECTED))
						&& (users[i].getRole()
								.equals(UserManager.ADMINISTRATOR))) {
					rejectedA = rejectedA - 1;
				} else if ((users[i].getStatus().equals(UserManager.SUSPENDED))
						&& (users[i].getRole()
								.equals(UserManager.ADMINISTRATOR))) {
					suspendedA = suspendedA - 1;
				}
				assertFalse(um.userExists(users[i].getEmail()));

				User[] list = um.getUsers(null);
				assertEquals(numberOfUsers, list.length);
				UserFilter f = new UserFilter();
				f.setStatus(UserManager.ACTIVE);
				f.setRole(UserManager.NON_ADMINISTRATOR);
				assertEquals(activeNA, um.getUsers(f).length);

				f.setStatus(UserManager.PENDING);
				assertEquals(pendingNA, um.getUsers(f).length);

				f.setStatus(UserManager.REJECTED);
				assertEquals(rejectedNA, um.getUsers(f).length);

				f.setStatus(UserManager.SUSPENDED);
				assertEquals(suspendedNA, um.getUsers(f).length);

				f.setStatus(UserManager.ACTIVE);
				f.setRole(UserManager.ADMINISTRATOR);
				assertEquals(activeA, um.getUsers(f).length);

				f.setStatus(UserManager.PENDING);
				assertEquals(pendingA, um.getUsers(f).length);

				f.setStatus(UserManager.REJECTED);
				assertEquals(rejectedA, um.getUsers(f).length);

				f.setStatus(UserManager.SUSPENDED);
				assertEquals(suspendedA, um.getUsers(f).length);

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

	public void testChangeStatus() {
		try {
			UserManager um = new UserManager(db, properties);
			User u1 = makeActiveUser();
			um.addUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertTrue(um.userExists(u1.getEmail()));
			User u2 = um.getUser(u1.getEmail());
			assertEquals(u1, u2);
			um.changeUserStatus(u1.getEmail(), UserManager.SUSPENDED);
			u2 = null;
			u2 = um.getUser(u1.getEmail());
			if (u1.equals(u2)) {
				assertTrue(false);
			}
			u1.setStatus(UserManager.SUSPENDED);
			assertEquals(u1, u2);

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

	public void testChangeRole() {
		try {
			UserManager um = new UserManager(db, properties);
			User u1 = makeActiveUser();
			um.addUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertTrue(um.userExists(u1.getEmail()));
			User u2 = um.getUser(u1.getEmail());
			assertEquals(u1, u2);
			um.changeUserRole(u1.getEmail(), UserManager.ADMINISTRATOR);
			u2 = null;
			u2 = um.getUser(u1.getEmail());
			if (u1.equals(u2)) {
				assertTrue(false);
			}
			u1.setRole(UserManager.ADMINISTRATOR);
			assertEquals(u1, u2);

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

	public void testChangePassword() {
		try {
			UserManager um = new UserManager(db, properties);
			User u1 = makeActiveUser();
			um.addUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertTrue(um.userExists(u1.getEmail()));
			User u2 = um.getUser(u1.getEmail());
			assertEquals(u1, u2);
			um.changeUserPassword(u1.getEmail(), "newpassword");
			u2 = null;
			u2 = um.getUser(u1.getEmail());
			if (u1.equals(u2)) {
				assertTrue(false);
			}
			u1.setPassword(Crypt.crypt("newpassword"));
			assertEquals(u1, u2);

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
			UserManager um = new UserManager(db, properties);
			User u1 = makeActiveUser();
			um.addUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertTrue(um.userExists(u1.getEmail()));
			User u2 = um.getUser(u1.getEmail());
			assertEquals(u1, u2);

			User[] list = um.getUsers();
			assertEquals(1, list.length);
			assertEquals(u1, list[0]);
			UserFilter f = new UserFilter();
			f.setStatus(UserManager.ACTIVE);
			f.setRole(UserManager.NON_ADMINISTRATOR);
			assertEquals(1, um.getUsers(f).length);
			f.setStatus(UserManager.PENDING);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserManager.REJECTED);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserManager.SUSPENDED);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserManager.ACTIVE);
			f.setRole(UserManager.ADMINISTRATOR);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserManager.PENDING);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserManager.REJECTED);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserManager.SUSPENDED);
			assertEquals(0, um.getUsers(f).length);
			um.removeUser(u1.getEmail());
			assertFalse(um.userExists(u1.getEmail()));

			try {
				um.getUser(u1.getEmail());
				assertTrue(false);
			} catch (NoSuchUserFault fs) {

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

	public void testFindUsers() {
		try {
			int size = 10;
			UserManager um = new UserManager(db, properties);
			for (int i = 0; i < size; i++) {
				um.addUser(makeUser(UserManager.NON_ADMINISTRATOR, UserManager.ACTIVE));
			}
			assertEquals(size, um.getUsers(getActiveUserFilter()).length);
			
			//test email address
			UserFilter f1 = getActiveUserFilter();
			f1.setEmail("@mail.com");
			assertEquals(size, um.getUsers(f1).length);
			f1.setEmail("@mail.");
			assertEquals(size, um.getUsers(f1).length);
			f1.setEmail("XX");
			assertEquals(0, um.getUsers(f1).length);
			
			//Test First Name and Last Name
			UserFilter f2 = getActiveUserFilter();
			f2.setFirstName("firs");
			assertEquals(size, um.getUsers(f2).length);
			f2.setLastName("ast");
			assertEquals(size, um.getUsers(f2).length);
			f2.setFirstName("XX");
			assertEquals(0, um.getUsers(f2).length);
			f2.setFirstName(null);
			assertEquals(size, um.getUsers(f2).length);
			

			//Test Organization
			UserFilter f0 = getActiveUserFilter();
			f0.setOrganization("org");
			assertEquals(size, um.getUsers(f0).length);
			f0.setOrganization("XX");
			assertEquals(0, um.getUsers(f0).length);

			
			//Test Address
			UserFilter f3 = getActiveUserFilter();
			f3.setAddress("address");
			assertEquals(size, um.getUsers(f3).length);
			f3.setAddress("XX");
			assertEquals(0, um.getUsers(f3).length);
			
			//Test Address 2
			UserFilter f4 = getActiveUserFilter();
			f4.setAddress2("address2");
			assertEquals(size, um.getUsers(f4).length);
			f4.setAddress2("XX");
			assertEquals(0, um.getUsers(f4).length);
			
			//Test City and State
			UserFilter f5 = getActiveUserFilter();
			f5.setCity("Columbus");
			assertEquals(size, um.getUsers(f5).length);
			f5.setState("OH");
			assertEquals(size, um.getUsers(f5).length);
			f5.setCity(null);
			assertEquals(size, um.getUsers(f5).length);
			f5.setState("XX");
			assertEquals(0, um.getUsers(f5).length);
			
			//Test Zip Code
			UserFilter f6 = getActiveUserFilter();
			f6.setZipcode("43210");
			assertEquals(size, um.getUsers(f6).length);
			f6.setZipcode("XX");
			assertEquals(0, um.getUsers(f6).length);
			
			//Test Phone Number
			UserFilter f7 = getActiveUserFilter();
			f7.setPhoneNumber("614-555-5555");
			assertEquals(size, um.getUsers(f7).length);
			f7.setPhoneNumber("XX");
			assertEquals(0, um.getUsers(f7).length);
		
			//test for each user
			
			for (int i = 0; i < size; i++) {
//				test email address
				UserFilter all = getActiveUserFilter();
				UserFilter u1 = getActiveUserFilter();
				u1.setEmail(i+"user@mail.com");
				all.setEmail(i+"user@mail.com");
				assertEquals(1, um.getUsers(u1).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test First Name
				UserFilter u2 = getActiveUserFilter();
				u2.setFirstName(i+"first");
				all.setFirstName(i+"first");
				assertEquals(1, um.getUsers(u2).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test Last Name
				UserFilter u3 = getActiveUserFilter();
				u3.setLastName(i+"last");
				all.setLastName(i+"last");
				assertEquals(1, um.getUsers(u3).length);
				assertEquals(1, um.getUsers(all).length);

				//Test Organization
				UserFilter u4 = getActiveUserFilter();
				u4.setOrganization(i+"organization");
				all.setOrganization(i+"organization");
				assertEquals(1, um.getUsers(u4).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test Address
				UserFilter u5 = getActiveUserFilter();
				u5.setAddress(i+"address");
				all.setAddress(i+"address");
				assertEquals(1, um.getUsers(u5).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test Address 2
				UserFilter u6 = getActiveUserFilter();
				u6.setAddress2(i+"address2");
				all.setAddress2(i+"address2");
				assertEquals(1, um.getUsers(u6).length);
				assertEquals(1, um.getUsers(all).length);
				
				all.setCity("Columbus");
				all.setState("OH");
				all.setZipcode("43210");
				all.setPhoneNumber("614-555-5555");
				assertEquals(1, um.getUsers(all).length);
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

	private UserFilter getActiveUserFilter() {
		UserFilter filter = new UserFilter();
		filter.setStatus(UserManager.ACTIVE);
		filter.setRole(UserManager.NON_ADMINISTRATOR);
		return filter;
	}

	private User makeActiveUser() {
		return makeUser(UserManager.NON_ADMINISTRATOR, UserManager.ACTIVE);
	}

	private User makeUser(UserRole role, UserStatus status) {
		User u = new User();
		u.setEmail(count+"user@mail.com");
		u.setPassword(count+"password");
		u.setFirstName(count+"first");
		u.setLastName(count+"last");
		u.setAddress(count+"address");
		u.setAddress2(count+"address2");
		u.setCity("Columbus");
		u.setState("OH");
		u.setZipcode("43210");
		u.setPhoneNumber("614-555-5555");
		u.setOrganization(count+"organization");
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
			properties = new IdPProperties(db);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
