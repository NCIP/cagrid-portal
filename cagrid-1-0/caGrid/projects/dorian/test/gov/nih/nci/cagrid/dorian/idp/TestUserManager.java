package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.common.Crypt;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.idp.bean.CountryCode;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.idp.bean.StateCode;
import gov.nih.nci.cagrid.gums.idp.bean.User;
import gov.nih.nci.cagrid.gums.idp.bean.UserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.UserRole;
import gov.nih.nci.cagrid.gums.idp.bean.UserStatus;
import gov.nih.nci.cagrid.gums.test.TestResourceManager;
import gov.nih.nci.cagrid.gums.test.TestUtils;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestUserManager extends TestCase {
	
	public static String IDP_CONFIG = "resources" + File.separator
	+ "general-test" + File.separator + "idp-config.xml";

	private Database db;

	private int count = 0;
	
	private IdPConfiguration conf;

	public void testMultipleUsers() {
		try {

			int userCount = 20;
			int activeNA = 0;
			int pendingNA = 0;
			int rejectedNA = 0;
			int suspendedNA = 0;

			int activeA = 0;
			int pendingA = 0;
			int rejectedA = 0;
			int suspendedA = 0;

			User[] users = new User[userCount];
			UserManager um = new UserManager(db, conf);

			for (int i = 0; i < users.length; i++) {
				if ((i % 8) == 0) {
					users[i] = makeUser(UserRole.Non_Administrator,
							UserStatus.Active);
					activeNA = activeNA + 1;
				} else if ((i % 8) == 1) {
					users[i] = makeUser(UserRole.Non_Administrator,
							UserStatus.Pending);
					pendingNA = pendingNA + 1;
				} else if ((i % 8) == 2) {
					users[i] = makeUser(UserRole.Non_Administrator,
							UserStatus.Rejected);
					rejectedNA = rejectedNA + 1;
				} else if ((i % 8) == 3) {
					users[i] = makeUser(UserRole.Non_Administrator,
							UserStatus.Suspended);
					suspendedNA = suspendedNA + 1;
				} else if ((i % 8) == 4) {
					users[i] = makeUser(UserRole.Administrator,
							UserStatus.Active);
					activeA = activeA + 1;
				} else if ((i % 8) == 5) {
					users[i] = makeUser(UserRole.Administrator,
							UserStatus.Pending);
					pendingA = pendingA + 1;
				} else if ((i % 8) == 6) {
					users[i] = makeUser(UserRole.Administrator,
							UserStatus.Rejected);
					rejectedA = rejectedA + 1;
				} else if ((i % 8) == 7) {
					users[i] = makeUser(UserRole.Administrator,
							UserStatus.Suspended);
					suspendedA = suspendedA + 1;
				}

				um.addUser(users[i]);
				users[i].setPassword(Crypt.crypt(users[i].getPassword()));
				assertTrue(um.userExists(users[i].getUserId()));
				User u = um.getUser(users[i].getUserId());
				assertEquals(users[i], u);

				User[] list = um.getUsers(null);
				assertEquals(i + 1, list.length);
				UserFilter f = new UserFilter();
				f.setStatus(UserStatus.Active);
				f.setRole(UserRole.Non_Administrator);
				assertEquals(activeNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Pending);
				assertEquals(pendingNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Rejected);
				assertEquals(rejectedNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Suspended);
				assertEquals(suspendedNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Active);
				f.setRole(UserRole.Administrator);
				assertEquals(activeA, um.getUsers(f).length);

				f.setStatus(UserStatus.Pending);
				assertEquals(pendingA, um.getUsers(f).length);

				f.setStatus(UserStatus.Rejected);
				assertEquals(rejectedA, um.getUsers(f).length);

				f.setStatus(UserStatus.Suspended);
				assertEquals(suspendedA, um.getUsers(f).length);

			}

			int numberOfUsers = users.length;

			for (int i = 0; i < users.length; i++) {
				um.removeUser(users[i].getUserId());
				numberOfUsers = numberOfUsers - 1;
				if ((users[i].getStatus().equals(UserStatus.Active))
						&& (users[i].getRole()
								.equals(UserRole.Non_Administrator))) {
					activeNA = activeNA - 1;
				} else if ((users[i].getStatus().equals(UserStatus.Pending))
						&& (users[i].getRole()
								.equals(UserRole.Non_Administrator))) {
					pendingNA = pendingNA - 1;
				}
				if ((users[i].getStatus().equals(UserStatus.Rejected))
						&& (users[i].getRole()
								.equals(UserRole.Non_Administrator))) {
					rejectedNA = rejectedNA - 1;
				}
				if ((users[i].getStatus().equals(UserStatus.Suspended))
						&& (users[i].getRole()
								.equals(UserRole.Non_Administrator))) {
					users[i] = makeUser(UserRole.Non_Administrator,
							UserStatus.Suspended);
					suspendedNA = suspendedNA - 1;
				} else if ((users[i].getStatus().equals(UserStatus.Active))
						&& (users[i].getRole()
								.equals(UserRole.Administrator))) {
					activeA = activeA - 1;
				} else if ((users[i].getStatus().equals(UserStatus.Pending))
						&& (users[i].getRole()
								.equals(UserRole.Administrator))) {
					pendingA = pendingA - 1;
				} else if ((users[i].getStatus().equals(UserStatus.Rejected))
						&& (users[i].getRole()
								.equals(UserRole.Administrator))) {
					rejectedA = rejectedA - 1;
				} else if ((users[i].getStatus().equals(UserStatus.Suspended))
						&& (users[i].getRole()
								.equals(UserRole.Administrator))) {
					suspendedA = suspendedA - 1;
				}
				assertFalse(um.userExists(users[i].getEmail()));

				User[] list = um.getUsers(null);
				assertEquals(numberOfUsers, list.length);
				UserFilter f = new UserFilter();
				f.setStatus(UserStatus.Active);
				f.setRole(UserRole.Non_Administrator);
				assertEquals(activeNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Pending);
				assertEquals(pendingNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Rejected);
				assertEquals(rejectedNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Suspended);
				assertEquals(suspendedNA, um.getUsers(f).length);

				f.setStatus(UserStatus.Active);
				f.setRole(UserRole.Administrator);
				assertEquals(activeA, um.getUsers(f).length);

				f.setStatus(UserStatus.Pending);
				assertEquals(pendingA, um.getUsers(f).length);

				f.setStatus(UserStatus.Rejected);
				assertEquals(rejectedA, um.getUsers(f).length);

				f.setStatus(UserStatus.Suspended);
				assertEquals(suspendedA, um.getUsers(f).length);

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	public void testChangeStatus() {
		try {
			UserManager um = new UserManager(db, conf);
			User u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setStatus(UserStatus.Suspended);
			um.updateUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			User u2 = um.getUser(u1.getUserId());
			assertEquals(u1, u2);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testChangeRole() {
		try {
			UserManager um = new UserManager(db, conf);
			User u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setRole(UserRole.Administrator);
			um.updateUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			User u2 = um.getUser(u1.getUserId());
			assertEquals(u1, u2);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	public void testChangePassword() {
		try {
			UserManager um = new UserManager(db, conf);
			User u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setPassword("npassword");
			um.updateUser(u1);
			User u2 = um.getUser(u1.getUserId());
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertEquals(u1, u2);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 

	}
	
	public void testUpdateUser() {
		try {
			UserManager um = new UserManager(db, conf);
			User u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setPassword("cpassword");
			u1.setFirstName("changedfirst");
			u1.setLastName("changedlast");
			u1.setAddress("changedaddress");
			u1.setAddress2("changedaddress2");
			u1.setCity("New York");
			u1.setState(StateCode.NY);
			u1.setCountry(CountryCode.AG);
			u1.setZipcode("11776");
			u1.setPhoneNumber("718-555-5555");
			u1.setOrganization("changedorganization");
			u1.setStatus(UserStatus.Suspended);
			u1.setRole(UserRole.Administrator);
			um.updateUser(u1);
			User u2 = um.getUser(u1.getUserId());
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertEquals(u1, u2);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 

	}
	

	public void testSingleUser() {
		try {
			UserManager um = new UserManager(db, conf);
			User u1 = makeActiveUser();
			um.addUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertTrue(um.userExists(u1.getUserId()));
			User u2 = um.getUser(u1.getUserId());
			assertEquals(u1, u2);

			User[] list = um.getUsers(null);
			assertEquals(1, list.length);
			assertEquals(u1, list[0]);
			UserFilter f = new UserFilter();
			f.setStatus(UserStatus.Active);
			f.setRole(UserRole.Non_Administrator);
			assertEquals(1, um.getUsers(f).length);
			f.setStatus(UserStatus.Pending);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserStatus.Rejected);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserStatus.Suspended);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserStatus.Active);
			f.setRole(UserRole.Administrator);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserStatus.Pending);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserStatus.Rejected);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(UserStatus.Suspended);
			assertEquals(0, um.getUsers(f).length);
			um.removeUser(u1.getUserId());
			assertFalse(um.userExists(u1.getEmail()));

			try {
				um.getUser(u1.getEmail());
				assertTrue(false);
			} catch (NoSuchUserFault fs) {

			}

			assertEquals(0, um.getUsers(null).length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	public void testFindUsers() {
		try {
			int size = 10;
			UserManager um = new UserManager(db, conf);
			for (int i = 0; i < size; i++) {
				um.addUser(makeUser(UserRole.Non_Administrator, UserStatus.Active));
			}
			assertEquals(size, um.getUsers(getActiveUserFilter()).length);
			
//			test email address
			UserFilter fid = getActiveUserFilter();
			fid.setUserId("user");
			assertEquals(size, um.getUsers(fid).length);
			fid.setUserId("XX");
			assertEquals(0, um.getUsers(fid).length);
			
			
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
			f5.setState(StateCode.OH);
			assertEquals(size, um.getUsers(f5).length);
			f5.setCity(null);
			assertEquals(size, um.getUsers(f5).length);
			f5.setState(null);
			assertEquals(size, um.getUsers(f5).length);
			
			//Test Zip Code
			UserFilter f6 = getActiveUserFilter();
			f6.setZipcode("43210");
			assertEquals(size, um.getUsers(f6).length);
			f6.setZipcode("XX");
			assertEquals(0, um.getUsers(f6).length);
			
//			Test country
			UserFilter cf = getActiveUserFilter();
			cf.setCountry(CountryCode.US);
			assertEquals(size, um.getUsers(cf).length);
		
			
			
			
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
				
				UserFilter uid = getActiveUserFilter();
				uid.setUserId(i+"user");
				all.setUserId(i+"user");
				assertEquals(1, um.getUsers(uid).length);
				assertEquals(1, um.getUsers(all).length);
				
				
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
				all.setState(StateCode.OH);
				all.setCountry(CountryCode.US);
				all.setZipcode("43210");
				all.setPhoneNumber("614-555-5555");
				assertEquals(1, um.getUsers(all).length);
			}
			
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	private UserFilter getActiveUserFilter() {
		UserFilter filter = new UserFilter();
		filter.setStatus(UserStatus.Active);
		filter.setRole(UserRole.Non_Administrator);
		return filter;
	}

	private User makeActiveUser() {
		return makeUser(UserRole.Non_Administrator, UserStatus.Active);
	}

	private User makeUser(UserRole role, UserStatus status) {
		User u = new User();
		u.setUserId(count+"user");
		u.setEmail(count+"user@mail.com");
		u.setPassword(count+"password");
		u.setFirstName(count+"first");
		u.setLastName(count+"last");
		u.setAddress(count+"address");
		u.setAddress2(count+"address2");
		u.setCity("Columbus");
		u.setState(StateCode.OH);
		u.setZipcode("43210");
		u.setCountry(CountryCode.US);
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
		    db = TestUtils.getDB();
		    assertEquals(0,db.getUsedConnectionCount());
		    TestResourceManager trm = new TestResourceManager(IDP_CONFIG);
		    this.conf = (IdPConfiguration)trm.getResource(IdPConfiguration.RESOURCE);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0,db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
}
