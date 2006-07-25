package gov.nih.nci.cagrid.gridgrouper.service;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Stem;
import edu.internet2.middleware.grouper.StemFinder;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.grouper.SubjectFinder;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouper {

	public static final String GROUPER_SUPER_USER = "GrouperSystem";
	public static final String GROUPER_ADMIN_STEM_NAME = "grouperadministration";
	public static final String GROUPER_ADMIN_STEM_DISPLAY_NAME = "Grouper Administration";
	public static final String GROUPER_ADMIN_GROUP_NAME_EXTENTION = "gridgrouperadministrators";
	public static final String GROUPER_ADMIN_GROUP_DISPLAY_NAME_EXTENTION = "Grid Grouper Administrators";
	public static final String GROUPER_ADMIN_GROUP_NAME = "grouperadministration:gridgrouperadministrators";

	private Group adminGroup;


	public GridGrouper() throws Exception {
		GrouperSession session = GrouperSession.start(SubjectFinder.findById(GROUPER_SUPER_USER));
		Stem adminStem = null;
		try {
			adminStem = StemFinder.findByName(session, GROUPER_ADMIN_STEM_NAME);
		} catch (StemNotFoundException e) {
			Stem root = StemFinder.findRootStem(session);
			adminStem = root.addChildStem(GROUPER_ADMIN_STEM_NAME, GROUPER_ADMIN_STEM_DISPLAY_NAME);
		}
		try {
			adminGroup = GroupFinder.findByName(session, GROUPER_ADMIN_GROUP_NAME);
		} catch (GroupNotFoundException gne) {
			adminGroup = adminStem.addChildGroup(GROUPER_ADMIN_GROUP_NAME_EXTENTION,
				GROUPER_ADMIN_GROUP_DISPLAY_NAME_EXTENTION);
		}
	}


	public Group getAdminGroup() {
		return adminGroup;
	}

}
