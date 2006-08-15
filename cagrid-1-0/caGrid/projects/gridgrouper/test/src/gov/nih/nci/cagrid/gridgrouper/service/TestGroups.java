package gov.nih.nci.cagrid.gridgrouper.service;

import edu.internet2.middleware.grouper.RegistryReset;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.service.tools.GridGrouperBootstrapper;
import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;
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

	// private String USER_A =
	// "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=cagrid05/OU=IdP [1]/CN=user a";

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

			grouper.addChildGroup(SUPER_USER, Utils.getStemIdentifier(test),
					groupExtension, groupDisplayExtension);
			GroupDescriptor[] grps = grouper.getChildGroups(SUPER_USER, Utils
					.getStemIdentifier(test));
			assertEquals(1, grps.length);
			assertEquals(groupExtension, grps[0].getExtension());
			assertEquals(groupDisplayExtension, grps[0].getDisplayExtension());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
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
