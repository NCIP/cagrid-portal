package gov.nih.nci.cagrid.gridgrouper.service;

import edu.internet2.middleware.grouper.AccessPrivilege;
import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Member;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.RegistryReset;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.service.tools.GridGrouperBootstrapper;
import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;

import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGroups extends TestCase {

	private GridGrouper grouper = null;

	private String SUPER_USER = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=super admin";

	private String ADMIN_USER = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=admin";

	private String USER_A = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user a";

	private String USER_B = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user b";

	public void testAddGroupAndMembers() {
		try {
			GridGrouperBootstrapper.addAdminMember(SUPER_USER);

			assertTrue(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID, Utils
							.getRootStemIdentifier(), SUPER_USER,
					StemPrivilegeType.stem));
			assertTrue(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID, Utils
							.getRootStemIdentifier(), SUPER_USER,
					StemPrivilegeType.create));

			StemDescriptor root = grouper.getStem(SUPER_USER, Utils
					.getRootStemIdentifier());
			assertNotNull(root);
			assertEquals(root.getName(), Utils.getRootStemIdentifier()
					.getStemName());

			String testStem = "TestStem";
			StemDescriptor test = grouper.addChildStem(SUPER_USER, Utils
					.getRootStemIdentifier(), testStem, testStem);

			final String groupExtension = "mygroup";
			final String groupDisplayExtension = "My Group";

			GroupDescriptor grp = grouper.addChildGroup(SUPER_USER, Utils
					.getStemIdentifier(test), groupExtension,
					groupDisplayExtension);
			assertEquals(groupExtension, grp.getExtension());
			assertEquals(groupDisplayExtension, grp.getDisplayExtension());
			GroupDescriptor[] grps = grouper.getChildGroups(SUPER_USER, Utils
					.getStemIdentifier(test));
			assertEquals(1, grps.length);
			assertEquals(groupExtension, grps[0].getExtension());
			assertEquals(groupDisplayExtension, grps[0].getDisplayExtension());

			final String subGroupExtension = "mysubgroup";
			final String subGroupDisplayExtension = "My Sub Group";

			GroupDescriptor subgrp = grouper.addChildGroup(SUPER_USER, Utils
					.getStemIdentifier(test), subGroupExtension,
					subGroupDisplayExtension);
			assertEquals(subGroupExtension, subgrp.getExtension());
			assertEquals(subGroupDisplayExtension, subgrp.getDisplayExtension());

			assertEquals(2, grouper.getChildGroups(SUPER_USER, Utils
					.getStemIdentifier(test)).length);

			// TODO: Finish this on down

			Subject subject = SubjectUtils.getSubject(SUPER_USER);
			GrouperSession session = GrouperSession.start(subject);
			Group group = GroupFinder.findByName(session, grp.getName());

			group.addMember(SubjectUtils.getSubject(USER_A));
			group.grantPriv(SubjectUtils.getSubject(ADMIN_USER),
					AccessPrivilege.ADMIN);

			Group subGroup = GroupFinder.findByName(session, subgrp.getName());

			subGroup.addMember(SubjectUtils.getSubject(USER_B));

			printMembers(group);
			printMembers(subGroup);
			group.addMember(subGroup.toSubject());
			printMembers(group);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	private void printMembers(Group group) throws Exception{
		System.out.println();
		System.out.println("All Members of " + group.getName());
		System.out.println("-------------------------------------------------");
		System.out.println();
		Set set = group.getMembers();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Member m = (Member) itr.next();
			System.out.println(m.getSubject().getName());
		}
		set = null;
		itr = null;

		System.out.println();
		System.out.println("Immediate Members of " + group.getName());
		System.out.println("-------------------------------------------------");
		System.out.println();
		set = group.getImmediateMembers();
		itr = set.iterator();
		while (itr.hasNext()) {
			Member m = (Member) itr.next();
			System.out.println(m.getSubjectId());
		}

		set = null;
		itr = null;

		System.out.println();
		System.out.println("Effective Members of " + group.getName());
		System.out.println("-------------------------------------------------");
		System.out.println();
		set = group.getEffectiveMembers();
		itr = set.iterator();
		while (itr.hasNext()) {
			Member m = (Member) itr.next();
			System.out.println(m.getSubjectId());
		}
	}

	private void printMemberships(Group group) throws Exception {
		System.out.println();
		System.out.println("All Memberships of " + group.getName());
		System.out.println("-------------------------------------------------");
		System.out.println();
		Set set = group.getMemberships();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Membership m = (Membership) itr.next();
			System.out.println(m.getMember().getSubject().getId());
		}
	}

	protected void setUp() throws Exception {
		super.setUp();
		RegistryReset.reset();
		this.grouper = new GridGrouper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		RegistryReset.reset();
	}

}
