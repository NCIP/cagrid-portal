package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.software.IntroduceType;
import gov.nih.nci.cagrid.introduce.beans.software.SoftwareType;

import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultTreeModel;

public class IntroduceUpdateTreeNode extends UpdateTypeTreeNode {

	private SoftwareType software;

	private IntroduceType introduce;

	private JCheckBox checkBox;

	private boolean installed = false;

	public IntroduceUpdateTreeNode(String displayName, DefaultTreeModel model,
			IntroduceType introduce, SoftwareType software) {
		super(displayName, model);
		this.software = software;
		this.introduce = introduce;
		checkBox = new JCheckBox(displayName);
		if (model != null) {
			initialize();
		}
	}

	public boolean isSelected() {
		return getCheckBox().isSelected();
	}

	public JCheckBox getCheckBox() {
		return checkBox;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public boolean isInstalled() {
		return installed;
	}

	public Object getUserObject() {
		return checkBox;
	}

	public void initialize() {
		ExtensionType[] extensionVersions = software.getExtension();
		if (extensionVersions != null) {
			for (int j = 0; j < extensionVersions.length; j++) {
				ExtensionType extension = extensionVersions[j];
				if (extension.getCompatibleIntroduceVersions() != null) {
					if (isCompatibleExtension(extension
							.getCompatibleIntroduceVersions())) {
						ExtensionUpdateTreeNode node = new ExtensionUpdateTreeNode(
								extension.getDisplayName() + " ("
										+ extension.getVersion() + ")",
								getModel(), extension);
						getModel().insertNodeInto(node, this,
								this.getChildCount());
					}
				}
			}
		}
	}

	private boolean isCompatibleExtension(String extensionIntroduceVersions) {
		StringTokenizer strtok = new StringTokenizer(
				extensionIntroduceVersions, ",", false);
		while (strtok.hasMoreElements()) {
			String extensionIntroduceVersion = strtok.nextToken();
			if (extensionIntroduceVersion.equals(introduce.getVersion())) {
				return true;
			}
		}

		return false;
	}

	public IntroduceType getIntroduce() {
		return introduce;
	}

	public void setIntroduce(IntroduceType introduce) {
		this.introduce = introduce;
	}
}
