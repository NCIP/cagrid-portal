package gov.nih.nci.cagrid.dorian.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.dorian.common.Crypt;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.idp.bean.CountryCode;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserRole;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserStatus;
import gov.nih.nci.cagrid.dorian.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.idp.bean.StateCode;
import gov.nih.nci.cagrid.dorian.test.Utils;

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

			IdPUser[] users = new IdPUser[userCount];
			UserManager um = new UserManager(db, conf);

			for (int i = 0; i < users.length; i++) {
				if ((i % 8) == 0) {
					users[i] = makeUser(IdPUserRole.Non_Administrator,
							IdPUserStatus.Active);
					activeNA = activeNA + 1;
				} else if ((i % 8) == 1) {
					users[i] = makeUser(IdPUserRole.Non_Administrator,
							IdPUserStatus.Pending);
					pendingNA = pendingNA + 1;
				} else if ((i % 8) == 2) {
					users[i] = makeUser(IdPUserRole.Non_Administrator,
							IdPUserStatus.Rejected);
					rejectedNA = rejectedNA + 1;
				} else if ((i % 8) == 3) {
					users[i] = makeUser(IdPUserRole.Non_Administrator,
							IdPUserStatus.Suspended);
					suspendedNA = suspendedNA + 1;
				} else if ((i % 8) == 4) {
					users[i] = makeUser(IdPUserRole.Administrator,
							IdPUserStatus.Active);
					activeA = activeA + 1;
				} else if ((i % 8) == 5) {
					users[i] = makeUser(IdPUserRole.Administrator,
							IdPUserStatus.Pending);
					pendingA = pendingA + 1;
				} else if ((i % 8) == 6) {
					users[i] = makeUser(IdPUserRole.Administrator,
							IdPUserStatus.Rejected);
					rejectedA = rejectedA + 1;
				} else if ((i % 8) == 7) {
					users[i] = makeUser(IdPUserRole.Administrator,
							IdPUserStatus.Suspended);
					suspendedA = suspendedA + 1;
				}

				um.addUser(users[i]);
				users[i].setPassword(Crypt.crypt(users[i].getPassword()));
				assertTrue(um.userExists(users[i].getUserId()));
				IdPUser u = um.getUser(users[i].getUserId());
				assertEquals(users[i], u);

				IdPUser[] list = um.getUsers(null);
				assertEquals(i + 1, list.length);
				IdPUserFilter f = new IdPUserFilter();
				f.setStatus(IdPUserStatus.Active);
				f.setRole(IdPUserRole.Non_Administrator);
				assertEquals(activeNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Pending);
				assertEquals(pendingNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Rejected);
				assertEquals(rejectedNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Suspended);
				assertEquals(suspendedNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Active);
				f.setRole(IdPUserRole.Administrator);
				assertEquals(activeA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Pending);
				assertEquals(pendingA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Rejected);
				assertEquals(rejectedA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Suspended);
				assertEquals(suspendedA, um.getUsers(f).length);

			}

			int numberOfUsers = users.length;

			for (int i = 0; i < users.length; i++) {
				um.removeUser(users[i].getUserId());
				numberOfUsers = numberOfUsers - 1;
				if ((users[i].getStatus().equals(IdPUserStatus.Active))
						&& (users[i].getRole()
								.equals(IdPUserRole.Non_Administrator))) {
					activeNA = activeNA - 1;
				} else if ((users[i].getStatus().equals(IdPUserStatus.Pending))
						&& (users[i].getRole()
								.equals(IdPUserRole.Non_Administrator))) {
					pendingNA = pendingNA - 1;
				}
				if ((users[i].getStatus().equals(IdPUserStatus.Rejected))
						&& (users[i].getRole()
								.equals(IdPUserRole.Non_Administrator))) {
					rejectedNA = rejectedNA - 1;
				}
				if ((users[i].getStatus().equals(IdPUserStatus.Suspended))
						&& (users[i].getRole()
								.equals(IdPUserRole.Non_Administrator))) {
					users[i] = makeUser(IdPUserRole.Non_Administrator,
							IdPUserStatus.Suspended);
					suspendedNA = suspendedNA - 1;
				} else if ((users[i].getStatus().equals(IdPUserStatus.Active))
						&& (users[i].getRole()
								.equals(IdPUserRole.Administrator))) {
					activeA = activeA - 1;
				} else if ((users[i].getStatus().equals(IdPUserStatus.Pending))
						&& (users[i].getRole()
								.equals(IdPUserRole.Administrator))) {
					pendingA = pendingA - 1;
				} else if ((users[i].getStatus().equals(IdPUserStatus.Rejected))
						&& (users[i].getRole()
								.equals(IdPUserRole.Administrator))) {
					rejectedA = rejectedA - 1;
				} else if ((users[i].getStatus().equals(IdPUserStatus.Suspended))
						&& (users[i].getRole()
								.equals(IdPUserRole.Administrator))) {
					suspendedA = suspendedA - 1;
				}
				assertFalse(um.userExists(users[i].getEmail()));

				IdPUser[] list = um.getUsers(null);
				assertEquals(numberOfUsers, list.length);
				IdPUserFilter f = new IdPUserFilter();
				f.setStatus(IdPUserStatus.Active);
				f.setRole(IdPUserRole.Non_Administrator);
				assertEquals(activeNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Pending);
				assertEquals(pendingNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Rejected);
				assertEquals(rejectedNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Suspended);
				assertEquals(suspendedNA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Active);
				f.setRole(IdPUserRole.Administrator);
				assertEquals(activeA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Pending);
				assertEquals(pendingA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Rejected);
				assertEquals(rejectedA, um.getUsers(f).length);

				f.setStatus(IdPUserStatus.Suspended);
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
			IdPUser u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setStatus(IdPUserStatus.Suspended);
			um.updateUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			IdPUser u2 = um.getUser(u1.getUserId());
			assertEquals(u1, u2);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testChangeRole() {
		try {
			UserManager um = new UserManager(db, conf);
			IdPUser u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setRole(IdPUserRole.Administrator);
			um.updateUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			IdPUser u2 = um.getUser(u1.getUserId());
			assertEquals(u1, u2);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	public void testChangePassword() {
		try {
			UserManager um = new UserManager(db, conf);
			IdPUser u1 = makeActiveUser();
			um.addUser(u1);
			assertTrue(um.userExists(u1.getUserId()));
			u1.setPassword("npassword");
			um.updateUser(u1);
			IdPUser u2 = um.getUser(u1.getUserId());
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
			IdPUser u1 = makeActiveUser();
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
			u1.setStatus(IdPUserStatus.Suspended);
			u1.setRole(IdPUserRole.Administrator);
			um.updateUser(u1);
			IdPUser u2 = um.getUser(u1.getUserId());
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
			IdPUser u1 = makeActiveUser();
			um.addUser(u1);
			u1.setPassword(Crypt.crypt(u1.getPassword()));
			assertTrue(um.userExists(u1.getUserId()));
			IdPUser u2 = um.getUser(u1.getUserId());
			assertEquals(u1, u2);

			IdPUser[] list = um.getUsers(null);
			assertEquals(1, list.length);
			assertEquals(u1, list[0]);
			IdPUserFilter f = new IdPUserFilter();
			f.setStatus(IdPUserStatus.Active);
			f.setRole(IdPUserRole.Non_Administrator);
			assertEquals(1, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Pending);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Rejected);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Suspended);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Active);
			f.setRole(IdPUserRole.Administrator);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Pending);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Rejected);
			assertEquals(0, um.getUsers(f).length);
			f.setStatus(IdPUserStatus.Suspended);
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
				um.addUser(makeUser(IdPUserRole.Non_Administrator, IdPUserStatus.Active));
			}
			assertEquals(size, um.getUsers(getActiveUserFilter()).length);
			
//			test email address
			IdPUserFilter fid = getActiveUserFilter();
			fid.setUserId("user");
			assertEquals(size, um.getUsers(fid).length);
			fid.setUserId("XX");
			assertEquals(0, um.getUsers(fid).length);
			
			
			//test email address
			IdPUserFilter f1 = getActiveUserFilter();
			f1.setEmail("@mail.com");
			assertEquals(size, um.getUsers(f1).length);
			f1.setEmail("@mail.");
			assertEquals(size, um.getUsers(f1).length);
			f1.setEmail("XX");
			assertEquals(0, um.getUsers(f1).length);
			
			//Test First Name and Last Name
			IdPUserFilter f2 = getActiveUserFilter();
			f2.setFirstName("firs");
			assertEquals(size, um.getUsers(f2).length);
			f2.setLastName("ast");
			assertEquals(size, um.getUsers(f2).length);
			f2.setFirstName("XX");
			assertEquals(0, um.getUsers(f2).length);
			f2.setFirstName(null);
			assertEquals(size, um.getUsers(f2).length);
			

			//Test Organization
			IdPUserFilter f0 = getActiveUserFilter();
			f0.setOrganization("org");
			assertEquals(size, um.getUsers(f0).length);
			f0.setOrganization("XX");
			assertEquals(0, um.getUsers(f0).length);

			
			//Test Address
			IdPUserFilter f3 = getActiveUserFilter();
			f3.setAddress("address");
			assertEquals(size, um.getUsers(f3).length);
			f3.setAddress("XX");
			assertEquals(0, um.getUsers(f3).length);
			
			//Test Address 2
			IdPUserFilter f4 = getActiveUserFilter();
			f4.setAddress2("address2");
			assertEquals(size, um.getUsers(f4).length);
			f4.setAddress2("XX");
			assertEquals(0, um.getUsers(f4).length);
			
			//Test City and State
			IdPUserFilter f5 = getActiveUserFilter();
			f5.setCity("Columbus");
			assertEquals(size, um.getUsers(f5).length);
			f5.setState(StateCode.OH);
			assertEquals(size, um.getUsers(f5).length);
			f5.setCity(null);
			assertEquals(size, um.getUsers(f5).length);
			f5.setState(null);
			assertEquals(size, um.getUsers(f5).length);
			
			//Test Zip Code
			IdPUserFilter f6 = getActiveUserFilter();
			f6.setZipcode("43210");
			assertEquals(size, um.getUsers(f6).length);
			f6.setZipcode("XX");
			assertEquals(0, um.getUsers(f6).length);
			
//			Test country
			IdPUserFilter cf = getActiveUserFilter();
			cf.setCountry(CountryCode.US);
			assertEquals(size, um.getUsers(cf).length);
		
			
			
			
			//Test Phone Number
			IdPUserFilter f7 = getActiveUserFilter();
			f7.setPhoneNumber("614-555-5555");
			assertEquals(size, um.getUsers(f7).length);
			f7.setPhoneNumber("XX");
			assertEquals(0, um.getUsers(f7).length);
		
			//test for each user
			
			for (int i = 0; i < size; i++) {
				
				
//				test email address
				IdPUserFilter all = getActiveUserFilter();
				
				IdPUserFilter uid = getActiveUserFilter();
				uid.setUserId(i+"user");
				all.setUserId(i+"user");
				assertEquals(1, um.getUsers(uid).length);
				assertEquals(1, um.getUsers(all).length);
				
				
				IdPUserFilter u1 = getActiveUserFilter();
				u1.setEmail(i+"user@mail.com");
				all.setEmail(i+"user@mail.com");
				assertEquals(1, um.getUsers(u1).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test First Name
				IdPUserFilter u2 = getActiveUserFilter();
				u2.setFirstName(i+"first");
				all.setFirstName(i+"first");
				assertEquals(1, um.getUsers(u2).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test Last Name
				IdPUserFilter u3 = getActiveUserFilter();
				u3.setLastName(i+"last");
				all.setLastName(i+"last");
				assertEquals(1, um.getUsers(u3).length);
				assertEquals(1, um.getUsers(all).length);

				//Test Organization
				IdPUserFilter u4 = getActiveUserFilter();
				u4.setOrganization(i+"organization");
				all.setOrganization(i+"organization");
				assertEquals(1, um.getUsers(u4).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test Address
				IdPUserFilter u5 = getActiveUserFilter();
				u5.setAddress(i+"address");
				all.setAddress(i+"address");
				assertEquals(1, um.getUsers(u5).length);
				assertEquals(1, um.getUsers(all).length);
				
				//Test Address 2
				IdPUserFilter u6 = getActiveUserFilter();
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

	private IdPUserFilter getActiveUserFilter() {
		IdPUserFilter filter = new IdPUserFilter();
		filter.setStatus(IdPUserStatus.Active);
		filter.setRole(IdPUserRole.Non_Administrator);
		return filter;
	}

	private IdPUser makeActiveUser() {
		return makeUser(IdPUserRole.Non_Administrator, IdPUserStatus.Active);
	}

	private IdPUser makeUser(IdPUserRole role, IdPUserStatus status) {
		IdPUser u = new IdPUser();
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
		    db = Utils.getDB();
		    assertEquals(0,db.getUsedConnectionCount());
		    SimpleResourceManager trm = new SimpleResourceManager(IDP_CONFIG);
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
