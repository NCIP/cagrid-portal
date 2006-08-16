package gov.nih.nci.cagrid.gridgrouper.service;

import edu.internet2.middleware.grouper.RegistryReset;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberType;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.service.tools.GridGrouperBootstrapper;
import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;

import java.util.HashMap;
import java.util.Map;

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

	// private String ADMIN_USER =
	// "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=admin";

	private String USER_A = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user a";

	private String USER_B = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user b";

	public void testMembers() {
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

			Map expected = new HashMap();
			expected.clear();
			// Test Members before any are added
			verifyMembers(grp, MemberFilter.All, 0, expected);
			verifyMembers(grp, MemberFilter.EffectiveMembers, 0, expected);
			verifyMembers(grp, MemberFilter.ImmediateMembers, 0, expected);

			grouper
					.addMember(SUPER_USER, Utils.getGroupIdentifier(grp),
							USER_A);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			verifyMembers(grp, MemberFilter.All, 1, expected);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			verifyMembers(grp, MemberFilter.ImmediateMembers, 1, expected);

			expected.clear();
			verifyMembers(grp, MemberFilter.EffectiveMembers, 0, expected);

			expected.clear();
			verifyMembers(subgrp, MemberFilter.All, 0, expected);
			expected.clear();
			verifyMembers(subgrp, MemberFilter.EffectiveMembers, 0, expected);
			expected.clear();
			verifyMembers(subgrp, MemberFilter.ImmediateMembers, 0, expected);

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(subgrp),
					USER_B);

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(subgrp, MemberFilter.All, 1, expected);

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(subgrp, MemberFilter.ImmediateMembers, 1, expected);

			expected.clear();
			verifyMembers(subgrp, MemberFilter.EffectiveMembers, 0, expected);

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grp), subgrp
					.getUUID());

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_B, getGridMember(USER_B));
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.All, 3, expected);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.ImmediateMembers, 2, expected);

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(grp, MemberFilter.EffectiveMembers, 1, expected);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	public void testMemberships() {
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

			Map expected = new HashMap();
			expected.clear();
			// Test Members before any are added
			verifyMemberships(grp, MemberFilter.All, 0, expected);
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 0, expected);
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 0, expected);
			grouper
					.addMember(SUPER_USER, Utils.getGroupIdentifier(grp),
							USER_A);

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			verifyMemberships(grp, MemberFilter.All, 1, expected);

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 1, expected);

			expected.clear();
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 0, expected);
			

			verifyMemberships(subgrp, MemberFilter.All, 0, expected);
			verifyMemberships(subgrp, MemberFilter.EffectiveMembers, 0,
					expected);
			verifyMemberships(subgrp, MemberFilter.ImmediateMembers, 0,
					expected);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(subgrp),
					USER_B);


			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, subgrp.getName(), null,
					0));
			verifyMemberships(subgrp, MemberFilter.All, 1, expected);

			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, subgrp.getName(), null,
					0));
			verifyMemberships(subgrp, MemberFilter.ImmediateMembers, 1, expected);

			expected.clear();
			verifyMemberships(subgrp, MemberFilter.EffectiveMembers, 0, expected);

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grp), subgrp
					.getUUID());
			
			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			expected.put(USER_B, getGridMembership(USER_B, grp.getName(), subgrp.getName(),
					1));
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(), grp.getName(), null,
					0));
			verifyMemberships(grp, MemberFilter.All, 3, expected);

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(), grp.getName(), null,
					0));
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 2, expected);

			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, grp.getName(), subgrp.getName(),
					1));
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 1, expected);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	

	private void verifyMembers(GroupDescriptor grp, MemberFilter filter,
			int expectedCount, Map expected) {
		try {
			assertEquals(expectedCount, expected.size());
			MemberDescriptor[] members = grouper.getMembers(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID, Utils
							.getGroupIdentifier(grp), filter);
			assertEquals(expectedCount, members.length);

			for (int i = 0; i < expectedCount; i++) {
				if (expected.containsKey(members[i].getSubjectId())) {
					MemberCaddy caddy = (MemberCaddy) expected
							.remove(members[i].getSubjectId());
					assertEquals(caddy.getMemberId(), members[i].getSubjectId());
					assertEquals(caddy.getMemberType(), members[i]
							.getMemberType());
					assertTrue(grouper.isMemberOf(
							AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
							Utils.getGroupIdentifier(grp), caddy.getMemberId(),
							filter));
				} else {
					fail("Member " + members[i].getSubjectId()
							+ " not expected!!!");
				}
			}
			assertEquals(0, expected.size());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Error verifying members");
		}

	}

	private void verifyMemberships(GroupDescriptor grp, MemberFilter filter,
			int expectedCount, Map expected) {
		try {
			assertEquals(expectedCount, expected.size());
			MembershipDescriptor[] members = grouper.getMemberships(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID, Utils
							.getGroupIdentifier(grp), filter);
			assertEquals(expectedCount, members.length);

			for (int i = 0; i < expectedCount; i++) {
				if (expected.containsKey(members[i].getMember().getSubjectId())) {
					MembershipCaddy caddy = (MembershipCaddy) expected
							.remove(members[i].getMember().getSubjectId());
					assertEquals(caddy.getMemberId(), members[i].getMember()
							.getSubjectId());
					assertEquals(caddy.getMemberType(), members[i].getMember()
							.getMemberType());
					assertEquals(caddy.getDepth(), members[i].getDepth());
			
					assertEquals(caddy.getGroupName(), members[i].getGroup()
							.getName());
					String viaGN = null;
					if (members[i].getViaGroup() != null) {
						viaGN = members[i].getViaGroup().getName();
					}
					assertEquals(caddy.getViaGroupName(), viaGN);
					assertTrue(grouper.isMemberOf(
							AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
							Utils.getGroupIdentifier(grp), caddy.getMemberId(),
							filter));
				} else {
					fail("Membership " + members[i].getMember().getSubjectId()
							+ " not expected!!!");
				}
			}
			assertEquals(0, expected.size());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Error verifying members");
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

	private MembershipCaddy getGridMembership(String name, String group,
			String viaGroup, int depth) {
		return new MembershipCaddy(name, group, viaGroup, depth,
				MemberType.Grid);
	}

	private MembershipCaddy getGroupMembership(String name, String group,
			String viaGroup, int depth) {
		return new MembershipCaddy(name, group, viaGroup, depth,
				MemberType.GrouperGroup);
	}

	private MemberCaddy getGridMember(String name) {
		return new MemberCaddy(name, MemberType.Grid);
	}

	private MemberCaddy getGroupMember(String name) {
		return new MemberCaddy(name, MemberType.GrouperGroup);
	}

	private class MemberCaddy {
		private String memberId;

		private MemberType memberType;

		public MemberCaddy(String id, MemberType type) {
			this.memberId = id;
			this.memberType = type;
		}

		public String getMemberId() {
			return memberId;
		}

		public MemberType getMemberType() {
			return memberType;
		}

	}

	private class MembershipCaddy {
		private String memberId;

		private String groupName;

		private String viaGroupName;

		private int depth;

		private MemberType memberType;

		public MembershipCaddy(String id, String groupName,
				String viaGroupName, int depth, MemberType type) {
			this.memberId = id;
			this.memberType = type;
			this.groupName = groupName;
			this.viaGroupName = viaGroupName;
			this.depth = depth;
		}

		public String getMemberId() {
			return memberId;
		}

		public MemberType getMemberType() {
			return memberType;
		}

		public int getDepth() {
			return depth;
		}

		public String getGroupName() {
			return groupName;
		}

		public String getViaGroupName() {
			return viaGroupName;
		}

	}

}
