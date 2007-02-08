package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import java.util.StringTokenizer;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.software.IntroduceType;
import gov.nih.nci.cagrid.introduce.beans.software.SoftwareType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class MainUpdateTreeNode extends UpdateTypeTreeNode {

	private SoftwareType software = null;

	public MainUpdateTreeNode(String displayName, DefaultTreeModel model,
			SoftwareType software) {
		super(displayName, model);
		this.software = software;
		if (model != null) {
			initialize();
		}
	}

	public void initialize() {
		if (this.software != null) {
			IntroduceType[] introduceVersions = this.software.getIntroduce();
			if (introduceVersions != null) {
				for (int i = 0; i < introduceVersions.length; i++) {
					IntroduceType introduce = introduceVersions[i];
					if (!isOlderVersion(CommonTools.getIntroduceVersion(),
							introduce.getVersion())) {
						// need to add this introduce version
						IntroduceUpdateTreeNode introduceNode = new IntroduceUpdateTreeNode(
								"Introduce (" + introduce.getVersion() + ")",
								this.getModel(), introduce, software);
						getModel().insertNodeInto(introduceNode, this,
								this.getChildCount());
					}
				}
			}
		}
	}

	public boolean isOlderVersion(String currentVersion, String proposedVersion) {
		StringTokenizer currentTokes = new StringTokenizer(currentVersion, ".",
				false);
		StringTokenizer proposedTokes = new StringTokenizer(proposedVersion,
				".", false);
		while (proposedTokes.hasMoreElements()) {
			if (!currentTokes.hasMoreElements()) {
				return false;
			}
			int proposedPartVersion = Integer.valueOf(
					(String) proposedTokes.nextToken()).intValue();
			int currentPartVersion = Integer.valueOf(
					(String) currentTokes.nextToken()).intValue();
			if (proposedPartVersion > currentPartVersion) {
				return false;
			}
			if (proposedPartVersion < currentPartVersion) {
				return true;
			}
		}
		return false;
	}

}
