package gov.nih.nci.cagrid.gridgrouper.service;

import edu.internet2.middleware.grouper.RegistryReset;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.service.tools.GridGrouperBootstrapper;
import gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;
import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestStems extends TestCase {

	private GridGrouper grouper = null;

	private String ADMIN_USER = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=admin";

	private String USER_A = "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user a";

	public void testRootStem() {
		try {
			StemDescriptor root = grouper.getStem(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getRootStemIdentifier());
			assertNotNull(root);
			assertEquals(root.getName(), getRootStemIdentifier().getStemName());
			String displayExtension = root.getDisplayExtension();
			String description = root.getDescription();
			assertNotNull(displayExtension);
			assertNotNull(description);
			String updatedDisplayExtension = displayExtension + " Update";
			String updatedDescription = displayExtension
					+ " Description Update";

			try {
				grouper.updateStemDisplayExtension(
						AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
						getStemIdentifier(root), updatedDisplayExtension);
				fail("Should have failed, insufficient privilege!!!");
			} catch (InsufficientPrivilegeFault f) {

			}

			try {
				grouper.updateStemDescription(
						AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
						getStemIdentifier(root), updatedDescription);
				fail("Should have failed, insufficient privilege!!!");
			} catch (InsufficientPrivilegeFault f) {

			}

			checkStem(grouper.getStem(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getRootStemIdentifier()), displayExtension, description);

			// Now create an admin user and do the update
			GridGrouperBootstrapper.addAdminMember(ADMIN_USER);
			assertTrue(grouper
					.hasStemPrivilege(
							AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
							getStemIdentifier(root), ADMIN_USER,
							StemPrivilegeType.stem));
			assertTrue(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), ADMIN_USER,
					StemPrivilegeType.create));
			grouper.updateStemDisplayExtension(ADMIN_USER,
					getStemIdentifier(root), updatedDisplayExtension);

			grouper.updateStemDescription(ADMIN_USER, getStemIdentifier(root),
					updatedDescription);

			checkStem(grouper.getStem(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getRootStemIdentifier()), updatedDisplayExtension,
					updatedDescription);

			grouper.updateStemDisplayExtension(ADMIN_USER,
					getStemIdentifier(root), displayExtension);

			grouper.updateStemDescription(ADMIN_USER, getStemIdentifier(root),
					description);

			checkStem(grouper.getStem(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getRootStemIdentifier()), displayExtension, description);

			// Now try with another user

			assertFalse(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A, StemPrivilegeType.stem));
			assertFalse(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A, StemPrivilegeType.create));

			try {
				grouper
						.grantStemPrivilege(
								AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
								getStemIdentifier(root), USER_A,
								StemPrivilegeType.stem);
				fail("Should have failed, insufficient privilege!!!");
			} catch (InsufficientPrivilegeFault e) {

			}

			assertFalse(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A, StemPrivilegeType.stem));
			assertFalse(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A, StemPrivilegeType.create));

			grouper.grantStemPrivilege(ADMIN_USER, getStemIdentifier(root),
					USER_A, StemPrivilegeType.stem);

			assertTrue(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A, StemPrivilegeType.stem));
			assertFalse(grouper.hasStemPrivilege(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A, StemPrivilegeType.create));

			// TODO: Start here
			grouper.updateStemDisplayExtension(USER_A, getStemIdentifier(root),
					updatedDisplayExtension);

			grouper.updateStemDescription(USER_A, getStemIdentifier(root),
					updatedDescription);

			checkStem(grouper.getStem(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getRootStemIdentifier()), updatedDisplayExtension,
					updatedDescription);

			grouper.updateStemDisplayExtension(USER_A, getStemIdentifier(root),
					displayExtension);

			grouper.updateStemDescription(USER_A, getStemIdentifier(root),
					description);

			checkStem(grouper.getStem(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getRootStemIdentifier()), displayExtension, description);

			StemPrivilege[] privs = grouper.getStemPrivileges(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), USER_A);
			assertNotNull(privs);
			assertEquals(1, privs.length);
			assertEquals(USER_A, privs[0].getSubject());
			assertEquals(StemPrivilegeType.stem, privs[0].getPrivilegeType());
			assertEquals(root.getName(), privs[0].getStemName());
			
			StemPrivilege[] privs2 = grouper.getStemPrivileges(
					AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID,
					getStemIdentifier(root), ADMIN_USER);
			assertNotNull(privs2);
			assertEquals(0, privs2.length);
			
			//TODO: Should I be able to call with anon user?

			String[] subs1 = grouper.getSubjectsWithStemPrivilege(
					ADMIN_USER,
					getStemIdentifier(root), StemPrivilegeType.stem);
			assertNotNull(subs1);
			assertEquals(1, subs1.length);
			assertEquals(USER_A, subs1[0]);
			
			//TODO: Should I be able to call with anon user?
			
			String[] subs2 = grouper.getSubjectsWithStemPrivilege(
					ADMIN_USER,
					getStemIdentifier(root), StemPrivilegeType.create);
			assertNotNull(subs2);
			assertEquals(0, subs2.length);
			
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}

	}

	private void checkStem(StemDescriptor des, String displayExtension,
			String description) {
		assertEquals(displayExtension, des.getDisplayExtension());
		assertEquals(description, des.getDescription());
	}

	private StemIdentifier getStemIdentifier(StemDescriptor des) {
		StemIdentifier id = new StemIdentifier();
		id.setStemName(des.getName());
		return id;
	}

	private StemIdentifier getRootStemIdentifier() {
		StemIdentifier id = new StemIdentifier();
		id
				.setStemName(gov.nih.nci.cagrid.gridgrouper.client.GridGrouper.ROOT_STEM);
		return id;
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
