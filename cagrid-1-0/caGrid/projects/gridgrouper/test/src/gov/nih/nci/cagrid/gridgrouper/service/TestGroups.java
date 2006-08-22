package gov.nih.nci.cagrid.gridgrouper.service;

import edu.internet2.middleware.grouper.RegistryReset;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupCompositeType;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilege;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupUpdate;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberType;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.service.tools.GridGrouperBootstrapper;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault;
import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;

import java.util.HashMap;
import java.util.HashSet;
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

	private String USER_A = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user a";

	private String USER_B = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user b";

	private String USER_C = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user c";

	private String USER_D = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user d";

	public void testViewReadPrivilege() {
		try {
			Map memberExpected = new HashMap();
			HashSet userExpected = new HashSet();
			HashSet privsExpected = new HashSet();
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

			GroupDescriptor grp = createAndCheckGroup(test, groupExtension,
					groupDisplayExtension, 1);
			GroupIdentifier gid = Utils.getGroupIdentifier(grp);

			userExpected.clear();
			userExpected.add(GroupPrivilegeType.admin);
			userExpected.add(GroupPrivilegeType.view);
			userExpected.add(GroupPrivilegeType.read);
			verifyUserPrivileges(grp, SUPER_USER, userExpected);

			// Test Default Privileges
			userExpected.clear();
			userExpected.add(GroupPrivilegeType.view);
			userExpected.add(GroupPrivilegeType.read);
			verifyUserPrivileges(grp, USER_A, userExpected);

			// TODO: Should this pass Should we be able to remove a default
			// privilege?

			// We want to test doing everything

			String description = "This is a test group";
			GroupUpdate update = new GroupUpdate();
			update.setDescription(description);
			grouper.updateGroup(SUPER_USER, gid, update);
			grouper.addMember(SUPER_USER, gid, USER_B);
			memberExpected.clear();
			memberExpected.put(USER_B, getGridMember(USER_B));
			verifyMembers(grp, MemberFilter.All, memberExpected);

			grouper.grantGroupPrivilege(SUPER_USER, gid, USER_C,
					GroupPrivilegeType.update);

			userExpected.clear();
			userExpected.add(GroupPrivilegeType.update);
			userExpected.add(GroupPrivilegeType.view);
			userExpected.add(GroupPrivilegeType.read);
			verifyUserPrivileges(grp, USER_C, userExpected);

			privsExpected.clear();
			privsExpected.add(USER_C);
			verifyPrivileges(grp, GroupPrivilegeType.update, privsExpected);

			// Reading Description

			GroupDescriptor g = grouper.getGroup(USER_A, gid);
			assertEquals(grp.getName(), g.getName());
			assertEquals(description, g.getDescription());

			// Reading Members
			memberExpected.clear();
			memberExpected.put(USER_B, getGridMember(USER_B));
			verifyMembers(USER_A, grp, MemberFilter.All, memberExpected);

			// Reading Privileges

			userExpected.clear();
			userExpected.add(GroupPrivilegeType.update);
			userExpected.add(GroupPrivilegeType.view);
			userExpected.add(GroupPrivilegeType.read);
			verifyUserPrivileges(USER_A, grp, USER_C, userExpected);

			// TODO: READ/VIEW users should be able to do this
			// privsExpected.clear();
			// privsExpected.add(USER_C);
			// verifyPrivileges(USER_A,grp, GroupPrivilegeType.update,
			// privsExpected);

			// Adding members
			try {
				grouper.addMember(USER_A, gid, USER_D);
				fail("Should not be able to add member!!!");
			} catch (InsufficientPrivilegeFault f) {

			}

			// Updating
			//TODO: This should throw Insufficient Privilege Fault
			try {
				GroupUpdate u = new GroupUpdate();
				u.setDescription("New Description");
				grouper.updateGroup(USER_A, gid, u);
				fail("Should not be able to update!!!");
			} catch (GroupModifyFault f) {

			}

			// Adding privileges
			try {
				grouper.grantGroupPrivilege(USER_A, gid, USER_D,
						GroupPrivilegeType.admin);
				fail("Should not be able to add privilege!!!");
			} catch (InsufficientPrivilegeFault f) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}

	private void printUsersWithPrivilege(GroupDescriptor des,
			GroupPrivilegeType priv) throws Exception {
		String[] subs = grouper.getSubjectsWithGroupPrivilege(SUPER_USER, Utils
				.getGroupIdentifier(des), priv);
		System.out.println("Users with the Privilege, " + priv.getValue()
				+ " on the group " + des.getName() + ":");
		System.out.println("");
		for (int i = 0; i < subs.length; i++) {
			System.out.println(subs[i]);
		}
		System.out.println("");
	}

	private void printPrivilegesForUser(GroupDescriptor des, String user)
			throws Exception {
		GroupPrivilege[] privs = grouper.getGroupPrivileges(SUPER_USER, Utils
				.getGroupIdentifier(des), user);
		System.out.println("Privileges for " + user + ", on the group "
				+ des.getName() + ":");
		System.out.println("");
		for (int i = 0; i < privs.length; i++) {
			System.out.println(privs[i].getPrivilegeType().getValue());
		}
		System.out.println("");
	}

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
			Map expected = new HashMap();
			final String groupExtension = "mygroup";
			final String groupDisplayExtension = "My Group";

			GroupDescriptor grp = createAndCheckGroup(test, groupExtension,
					groupDisplayExtension, 1);

			final String subGroupExtension = "mysubgroup";
			final String subGroupDisplayExtension = "My Sub Group";

			GroupDescriptor subgrp = createAndCheckGroup(test,
					subGroupExtension, subGroupDisplayExtension, 2);

			grouper
					.addMember(SUPER_USER, Utils.getGroupIdentifier(grp),
							USER_A);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			verifyMembers(grp, MemberFilter.All, expected);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			verifyMembers(grp, MemberFilter.ImmediateMembers, expected);

			expected.clear();
			verifyMembers(grp, MemberFilter.EffectiveMembers, expected);

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(subgrp),
					USER_B);

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(subgrp, MemberFilter.All, expected);

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(subgrp, MemberFilter.ImmediateMembers, expected);

			expected.clear();
			verifyMembers(subgrp, MemberFilter.EffectiveMembers, expected);

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grp), subgrp
					.getUUID());

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_B, getGridMember(USER_B));
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.All, expected);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.ImmediateMembers, expected);

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(grp, MemberFilter.EffectiveMembers, expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(subgrp),
					USER_B);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.All, expected);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.ImmediateMembers, expected);

			expected.clear();
			verifyMembers(grp, MemberFilter.EffectiveMembers, expected);

			expected.clear();
			verifyMembers(subgrp, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(subgrp, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(subgrp, MemberFilter.ImmediateMembers, expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grp),
					USER_A);

			expected.clear();
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.All, expected);

			expected.clear();
			expected.put(subgrp.getUUID(), getGroupMember(subgrp.getUUID()));
			verifyMembers(grp, MemberFilter.ImmediateMembers, expected);

			expected.clear();
			verifyMembers(grp, MemberFilter.EffectiveMembers, expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grp),
					subgrp.getUUID());

			expected.clear();
			verifyMembers(grp, MemberFilter.All, expected);

			expected.clear();
			verifyMembers(grp, MemberFilter.ImmediateMembers, expected);

			expected.clear();
			verifyMembers(grp, MemberFilter.EffectiveMembers, expected);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}

	public void testUnionComposite() {
		try {
			Map expected = new HashMap();
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

			final String groupExtensionX = "mygroupx";
			final String groupDisplayExtensionX = "My Group X";

			GroupDescriptor grpx = createAndCheckGroup(test, groupExtensionX,
					groupDisplayExtensionX, 1);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_A);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_B);

			final String groupExtensionY = "mygroupy";
			final String groupDisplayExtensionY = "My Group Y";

			GroupDescriptor grpy = createAndCheckGroup(test, groupExtensionY,
					groupDisplayExtensionY, 2);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_B);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_C);

			final String compositeGroupExtension = "compositegroup";
			final String compositeGroupDisplayExtension = "Composite Group";

			// Create Composite Union Group
			GroupDescriptor composite = createAndCheckGroup(test,
					compositeGroupExtension, compositeGroupDisplayExtension, 3);
			assertFalse(composite.isHasComposite());
			composite = grouper.addCompositeMember(SUPER_USER,
					GroupCompositeType.Union, Utils
							.getGroupIdentifier(composite), Utils
							.getGroupIdentifier(grpx), Utils
							.getGroupIdentifier(grpy));
			assertTrue(composite.isHasComposite());
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());
			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertTrue(grpx.isIsComposite());
			assertTrue(grpy.isIsComposite());

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_B, getGridMember(USER_B));
			expected.put(USER_C, getGridMember(USER_C));
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_B, getGridMember(USER_B));
			expected.put(USER_C, getGridMember(USER_C));
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			// TODO: Possible Grouper BUG: Make sure that the Membership is
			// working as intended.
			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, composite.getName(),
					null, 0));
			expected.put(USER_B, getGridMembership(USER_B, composite.getName(),
					null, 0));
			expected.put(USER_C, getGridMembership(USER_C, composite.getName(),
					null, 0));
			verifyMemberships(composite, MemberFilter.All, 3, expected);
			expected.clear();
			verifyMemberships(composite, MemberFilter.EffectiveMembers, 0,
					expected);
			expected.clear();
			verifyMemberships(composite, MemberFilter.ImmediateMembers, 0,
					expected);
			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, composite.getName(),
					null, 0));
			expected.put(USER_B, getGridMembership(USER_B, composite.getName(),
					null, 0));
			expected.put(USER_C, getGridMembership(USER_C, composite.getName(),
					null, 0));
			verifyMemberships(composite, MemberFilter.CompositeMembers, 3,
					expected);

			// Test Remove the shared user
			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_B);
			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_B, getGridMember(USER_B));
			expected.put(USER_C, getGridMember(USER_C));
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_B, getGridMember(USER_B));
			expected.put(USER_C, getGridMember(USER_C));
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_B);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_C, getGridMember(USER_C));
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			expected.put(USER_C, getGridMember(USER_C));
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grouper.deleteCompositeMember(SUPER_USER, Utils
					.getGroupIdentifier(composite));

			expected.clear();
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}

	public void testIntersectionComposite() {
		try {
			Map expected = new HashMap();
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

			final String groupExtensionX = "mygroupx";
			final String groupDisplayExtensionX = "My Group X";

			GroupDescriptor grpx = createAndCheckGroup(test, groupExtensionX,
					groupDisplayExtensionX, 1);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_A);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_B);

			final String groupExtensionY = "mygroupy";
			final String groupDisplayExtensionY = "My Group Y";

			GroupDescriptor grpy = createAndCheckGroup(test, groupExtensionY,
					groupDisplayExtensionY, 2);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_B);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_C);

			final String compositeGroupExtension = "compositegroup";
			final String compositeGroupDisplayExtension = "Composite Group";

			GroupDescriptor composite = createAndCheckGroup(test,
					compositeGroupExtension, compositeGroupDisplayExtension, 3);
			assertFalse(composite.isHasComposite());
			composite = grouper.addCompositeMember(SUPER_USER,
					GroupCompositeType.Intersection, Utils
							.getGroupIdentifier(composite), Utils
							.getGroupIdentifier(grpx), Utils
							.getGroupIdentifier(grpy));
			assertTrue(composite.isHasComposite());
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());
			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertTrue(grpx.isIsComposite());
			assertTrue(grpy.isIsComposite());

			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			expected.put(USER_B, getGridMember(USER_B));
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			// TODO: Possible Grouper BUG: Make sure that the Membership is
			// working as intended.
			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, composite.getName(),
					null, 0));
			verifyMemberships(composite, MemberFilter.All, 1, expected);
			expected.clear();
			verifyMemberships(composite, MemberFilter.EffectiveMembers, 0,
					expected);
			expected.clear();
			verifyMemberships(composite, MemberFilter.ImmediateMembers, 0,
					expected);
			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, composite.getName(),
					null, 0));
			verifyMemberships(composite, MemberFilter.CompositeMembers, 1,
					expected);

			// Test Remove the shared user
			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_B);
			expected.clear();
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grouper.deleteCompositeMember(SUPER_USER, Utils
					.getGroupIdentifier(composite));

			expected.clear();
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}

	public void testComplementComposite() {
		try {
			Map expected = new HashMap();
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

			final String groupExtensionX = "mygroupx";
			final String groupDisplayExtensionX = "My Group X";

			GroupDescriptor grpx = createAndCheckGroup(test, groupExtensionX,
					groupDisplayExtensionX, 1);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_A);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_B);

			final String groupExtensionY = "mygroupy";
			final String groupDisplayExtensionY = "My Group Y";

			GroupDescriptor grpy = createAndCheckGroup(test, groupExtensionY,
					groupDisplayExtensionY, 2);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_B);
			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grpy),
					USER_C);

			final String compositeGroupExtension = "compositegroup";
			final String compositeGroupDisplayExtension = "Composite Group";

			GroupDescriptor composite = createAndCheckGroup(test,
					compositeGroupExtension, compositeGroupDisplayExtension, 3);
			assertFalse(composite.isHasComposite());
			composite = grouper.addCompositeMember(SUPER_USER,
					GroupCompositeType.Complement, Utils
							.getGroupIdentifier(composite), Utils
							.getGroupIdentifier(grpx), Utils
							.getGroupIdentifier(grpy));
			assertTrue(composite.isHasComposite());
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());
			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertTrue(grpx.isIsComposite());
			assertTrue(grpy.isIsComposite());
			// Utils.printMemberships(composite);

			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			expected.put(USER_A, getGridMember(USER_A));
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			// TODO: Possible Grouper BUG: Make sure that the Membership is
			// working as intended.
			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, composite.getName(),
					null, 0));
			verifyMemberships(composite, MemberFilter.All, 1, expected);
			expected.clear();
			verifyMemberships(composite, MemberFilter.EffectiveMembers, 0,
					expected);
			expected.clear();
			verifyMemberships(composite, MemberFilter.ImmediateMembers, 0,
					expected);
			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, composite.getName(),
					null, 0));
			verifyMemberships(composite, MemberFilter.CompositeMembers, 1,
					expected);

			// Test Remove the shared user
			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grpx),
					USER_A);
			expected.clear();
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grouper.deleteCompositeMember(SUPER_USER, Utils
					.getGroupIdentifier(composite));

			expected.clear();
			verifyMembers(composite, MemberFilter.All, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.EffectiveMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.ImmediateMembers, expected);
			expected.clear();
			verifyMembers(composite, MemberFilter.CompositeMembers, expected);

			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}

	public void testNegativeComposites() {
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

			final String groupExtensionX = "mygroupx";
			final String groupDisplayExtensionX = "My Group X";

			GroupDescriptor grpx = createAndCheckGroup(test, groupExtensionX,
					groupDisplayExtensionX, 1);

			final String groupExtensionY = "mygroupy";
			final String groupDisplayExtensionY = "My Group Y";

			GroupDescriptor grpy = createAndCheckGroup(test, groupExtensionY,
					groupDisplayExtensionY, 2);

			final String compositeGroupExtension = "compositegroup";
			final String compositeGroupDisplayExtension = "Composite Group";

			// Create Composite Union Group
			GroupDescriptor composite = createAndCheckGroup(test,
					compositeGroupExtension, compositeGroupDisplayExtension, 3);
			assertFalse(composite.isHasComposite());
			composite = grouper.addCompositeMember(SUPER_USER,
					GroupCompositeType.Union, Utils
							.getGroupIdentifier(composite), Utils
							.getGroupIdentifier(grpx), Utils
							.getGroupIdentifier(grpy));
			assertTrue(composite.isHasComposite());
			assertFalse(grpx.isIsComposite());
			assertFalse(grpy.isIsComposite());
			grpx = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpx));
			grpy = grouper.getGroup(SUPER_USER, Utils.getGroupIdentifier(grpy));
			assertTrue(grpx.isIsComposite());
			assertTrue(grpy.isIsComposite());

			// Negative Tests.
			try {
				composite = grouper.addCompositeMember(SUPER_USER,
						GroupCompositeType.Intersection, Utils
								.getGroupIdentifier(composite), Utils
								.getGroupIdentifier(grpx), Utils
								.getGroupIdentifier(grpy));
				fail("Should not be able to add composite membership to group with composite membership.");
			} catch (MemberAddFault e) {

			}

			try {
				grouper.addMember(SUPER_USER, Utils
						.getGroupIdentifier(composite), USER_D);
				fail("Should not be able to add a member to group with composite membership.");
			} catch (MemberAddFault e) {

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}

	private GroupDescriptor createAndCheckGroup(StemDescriptor stem,
			String extension, String displayExtension, int childGroupCount)
			throws Exception {
		GroupDescriptor grp = grouper.addChildGroup(SUPER_USER, Utils
				.getStemIdentifier(stem), extension, displayExtension);
		assertEquals(extension, grp.getExtension());
		assertEquals(displayExtension, grp.getDisplayExtension());
		assertEquals(childGroupCount, grouper.getChildGroups(SUPER_USER, Utils
				.getStemIdentifier(stem)).length);
		assertFalse(grp.isHasComposite());
		Map expected = new HashMap();
		expected.clear();
		verifyMembers(grp, MemberFilter.All, expected);
		expected.clear();
		verifyMembers(grp, MemberFilter.EffectiveMembers, expected);
		expected.clear();
		verifyMembers(grp, MemberFilter.ImmediateMembers, expected);
		expected.clear();
		verifyMembers(grp, MemberFilter.CompositeMembers, expected);

		expected.clear();
		verifyMemberships(grp, MemberFilter.All, 0, expected);
		expected.clear();
		verifyMemberships(grp, MemberFilter.EffectiveMembers, 0, expected);
		expected.clear();
		verifyMemberships(grp, MemberFilter.ImmediateMembers, 0, expected);
		expected.clear();
		verifyMemberships(grp, MemberFilter.CompositeMembers, 0, expected);
		return grp;
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

			Map expected = new HashMap();
			final String groupExtension = "mygroup";
			final String groupDisplayExtension = "My Group";

			GroupDescriptor grp = createAndCheckGroup(test, groupExtension,
					groupDisplayExtension, 1);

			final String subGroupExtension = "mysubgroup";
			final String subGroupDisplayExtension = "My Sub Group";

			GroupDescriptor subgrp = createAndCheckGroup(test,
					subGroupExtension, subGroupDisplayExtension, 2);

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

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(subgrp),
					USER_B);

			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, subgrp.getName(),
					null, 0));
			verifyMemberships(subgrp, MemberFilter.All, 1, expected);

			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, subgrp.getName(),
					null, 0));
			verifyMemberships(subgrp, MemberFilter.ImmediateMembers, 1,
					expected);

			expected.clear();
			verifyMemberships(subgrp, MemberFilter.EffectiveMembers, 0,
					expected);

			grouper.addMember(SUPER_USER, Utils.getGroupIdentifier(grp), subgrp
					.getUUID());

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			expected.put(USER_B, getGridMembership(USER_B, grp.getName(),
					subgrp.getName(), 1));
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(),
					grp.getName(), null, 0));
			verifyMemberships(grp, MemberFilter.All, 3, expected);

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(),
					grp.getName(), null, 0));
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 2, expected);

			expected.clear();
			expected.put(USER_B, getGridMembership(USER_B, grp.getName(),
					subgrp.getName(), 1));
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 1, expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(subgrp),
					USER_B);

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(),
					grp.getName(), null, 0));
			verifyMemberships(grp, MemberFilter.All, 2, expected);

			expected.clear();
			expected.put(USER_A, getGridMembership(USER_A, grp.getName(), null,
					0));
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(),
					grp.getName(), null, 0));
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 2, expected);

			expected.clear();
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 0, expected);

			verifyMemberships(subgrp, MemberFilter.All, 0, expected);
			verifyMemberships(subgrp, MemberFilter.EffectiveMembers, 0,
					expected);
			verifyMemberships(subgrp, MemberFilter.ImmediateMembers, 0,
					expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grp),
					USER_A);

			expected.clear();
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(),
					grp.getName(), null, 0));
			verifyMemberships(grp, MemberFilter.All, 1, expected);

			expected.clear();
			expected.put(subgrp.getUUID(), getGroupMembership(subgrp.getUUID(),
					grp.getName(), null, 0));
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 1, expected);

			expected.clear();
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 0, expected);

			grouper.deleteMember(SUPER_USER, Utils.getGroupIdentifier(grp),
					subgrp.getUUID());

			verifyMemberships(grp, MemberFilter.All, 0, expected);
			verifyMemberships(grp, MemberFilter.EffectiveMembers, 0, expected);
			verifyMemberships(grp, MemberFilter.ImmediateMembers, 0, expected);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}

	}

	private void verifyMembers(GroupDescriptor grp, MemberFilter filter,
			Map expected) {
		verifyMembers(AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID, grp,
				filter, expected);
	}

	private void verifyMembers(String caller, GroupDescriptor grp,
			MemberFilter filter, Map expected) {
		try {
			int expectedCount = expected.size();
			assertEquals(expectedCount, expected.size());
			MemberDescriptor[] members = grouper.getMembers(caller, Utils
					.getGroupIdentifier(grp), filter);
			assertEquals(expectedCount, members.length);

			for (int i = 0; i < expectedCount; i++) {
				if (expected.containsKey(members[i].getSubjectId())) {
					MemberCaddy caddy = (MemberCaddy) expected
							.remove(members[i].getSubjectId());
					assertEquals(caddy.getMemberId(), members[i].getSubjectId());
					assertEquals(caddy.getMemberType(), members[i]
							.getMemberType());
					if (!filter.equals(MemberFilter.CompositeMembers)) {
						assertTrue(grouper
								.isMemberOf(
										AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
										Utils.getGroupIdentifier(grp), caddy
												.getMemberId(), filter));
					}
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
					if (!filter.equals(MemberFilter.CompositeMembers)) {
						assertTrue(grouper
								.isMemberOf(
										AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
										Utils.getGroupIdentifier(grp), caddy
												.getMemberId(), filter));
					}
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

	private void verifyUserPrivileges(GroupDescriptor grp, String user,
			HashSet expected) {
		verifyUserPrivileges(SUPER_USER, grp, user, expected);
	}

	private void verifyUserPrivileges(String caller, GroupDescriptor grp,
			String user, HashSet expected) {
		try {
			GroupPrivilege[] privs = grouper.getGroupPrivileges(caller, Utils
					.getGroupIdentifier(grp), user);
			assertEquals(expected.size(), privs.length);
			for (int i = 0; i < privs.length; i++) {
				if (expected.contains(privs[i].getPrivilegeType())) {
					assertEquals(user, privs[i].getSubject());
					expected.remove(privs[i].getPrivilegeType());
				} else {
					fail("The privilege "
							+ privs[i].getPrivilegeType().getValue()
							+ " was not expected!!!");
				}
			}
			assertEquals(0, expected.size());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Error verifying members");
		}

	}

	private void verifyPrivileges(GroupDescriptor grp, GroupPrivilegeType priv,
			HashSet expected) {
		verifyPrivileges(SUPER_USER, grp, priv, expected);
	}

	private void verifyPrivileges(String caller, GroupDescriptor grp,
			GroupPrivilegeType priv, HashSet expected) {
		try {
			String[] users = grouper.getSubjectsWithGroupPrivilege(caller,
					Utils.getGroupIdentifier(grp), priv);
			assertEquals(expected.size(), users.length);
			for (int i = 0; i < users.length; i++) {
				if (expected.contains(users[i])) {
					expected.remove(users[i]);
				} else {
					fail("The privilege " + priv.getValue()
							+ " was not expected for the user " + users[i]
							+ "!!!");
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
