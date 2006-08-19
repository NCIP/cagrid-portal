package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class StemNodeMenu extends GridGrouperTreeNodeMenu {

	public StemNodeMenu(GroupManagementBrowser browser, GridGrouperTree tree) {
		super(browser, tree);
	}

	private StemTreeNode getStemNode() {
		return (StemTreeNode) getGridGrouperTree().getCurrentNode();
	}

	public void removeNode() {
		StemTreeNode node = getStemNode();
		if (node.getChildCount() > 0) {
			PortalUtils
					.showErrorMessage("Cannot remove stem with child stems or child groups!!!");
			return;
		}

		if (node.isRootStem()) {
			PortalUtils.showErrorMessage("Cannot remove root stem!!!");
			return;
		}
		int id = getBrowser().getProgress()
				.startEvent("Removing the stem.... ");
		try {
			node.getStem().delete();
			getBrowser().getContentManager().removeStem(node);
			((GridGrouperBaseTreeNode) node.getParent()).refresh();
			getBrowser().getProgress().stopEvent(id,
					"Successfully removed the stem !!!");
		} catch (Exception e) {
			getBrowser().getProgress().stopEvent(id,
					"Error removing the stem !!!");
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}
	}
}
