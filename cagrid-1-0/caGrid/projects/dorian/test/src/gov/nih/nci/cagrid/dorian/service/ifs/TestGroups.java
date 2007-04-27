package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.test.Utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class TestGroups extends TestCase {

	private Database db;
	private static final int MEMBER_COUNT = 5;
	private static final String MEMBER_PREFIX = "member";


	public void testAddExistingGroup() {
		GroupManager gm = new GroupManager(db);
		try {
			Group grp = validateAddingGroupAndMembers(gm, "mygroup");
			try {
				gm.addGroup(grp.getName());
				fail("Should not be able to add the group because it already exists.");
			} catch (DorianInternalFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				gm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public void testAddExistingMembers() {
		GroupManager gm = new GroupManager(db);
		try {
			Group grp = validateAddingGroupAndMembers(gm, "mygroup");
			try {
				grp.addMember(MEMBER_PREFIX+"0");
				fail("Should not be able to add member because the membership already exists.");
			} catch (DorianInternalFault f) {

			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				gm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}



	public void testMultipleGroups() {
		GroupManager gm = new GroupManager(db);
		int groupCount = 3;
		try {
			String grpPrefix = "mygroup";
			List groups = new ArrayList();
			for (int i = 0; i < groupCount; i++) {
				String name = grpPrefix + i;
				groups.add(validateAddingGroupAndMembers(gm, name));
			}

			for (int i = 0; i < groupCount; i++) {
				validateRemovingGroupAndMembers(gm, (Group) groups.get(i));
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				gm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private Group validateAddingGroupAndMembers(GroupManager gm, String name) throws Exception {
		gm.addGroup(name);
		assertTrue(gm.groupExists(name));
		Group grp = gm.getGroup(name);
		assertEquals(name, grp.getName());
		assertTrue(gm.groupExists(grp.getGroupId()));
		Group grpx = gm.getGroup(grp.getGroupId());
		assertEquals(grp.getGroupId(), grpx.getGroupId());
		assertEquals(grp.getName(), grpx.getName());
		for (int i = 0; i < MEMBER_COUNT; i++) {
			String member = MEMBER_PREFIX + i;
			assertFalse(grp.isMember(member));
			grp.addMember(member);
			assertTrue(grp.isMember(member));
			List members = grp.getMembers();
			assertEquals((i + 1), members.size());
			boolean found = false;
			for (int j = 0; j <= i; j++) {
				String mid = (String) members.get(j);
				if (member.equals(mid)) {
					found = true;
				}
			}
			if (!found) {
				fail("The member " + member + " should be a member of the group but is not.");
			}
		}
		return grp;
	}


	private void validateRemovingGroupAndMembers(GroupManager gm, Group grp) throws Exception {
		for (int i = 0; i < MEMBER_COUNT - 2; i++) {
			String member = MEMBER_PREFIX + i;
			assertTrue(grp.isMember(member));
			grp.removeMember(member);
			assertFalse(grp.isMember(member));
			List members = grp.getMembers();
			assertEquals((MEMBER_COUNT - (i + 1)), members.size());
		}
		gm.removeGroup(grp);
		assertFalse(gm.groupExists(grp.getGroupId()));
		assertFalse(gm.groupExists(grp.getName()));
		assertEquals(0, grp.getMembers().size());
	}


	public void testGroup() {
		GroupManager gm = new GroupManager(db);
		try {
			Group grp = validateAddingGroupAndMembers(gm, "mygroup");
			validateRemovingGroupAndMembers(gm, grp);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				gm.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
