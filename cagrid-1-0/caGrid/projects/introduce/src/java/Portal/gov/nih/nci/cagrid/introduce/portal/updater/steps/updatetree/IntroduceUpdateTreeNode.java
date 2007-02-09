package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import java.awt.Font;
import java.util.StringTokenizer;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.software.IntroduceType;
import gov.nih.nci.cagrid.introduce.beans.software.SoftwareType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;
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
		if(introduce.getVersion().equals(CommonTools.getIntroduceVersion())){
			checkBox.setEnabled(false);
			checkBox.setSelected(true);
			installed = true;
			checkBox.setText(checkBox.getText() + " installed");
			checkBox.setFont(checkBox.getFont().deriveFont(Font.ITALIC));
		}
		if (model != null) {
			initialize();
		}
	}
	
	public boolean isInstalled(){
		return installed;
	}
	
	public Object getUserObject(){
		return checkBox;
	}

	public void initialize() {
		ExtensionType[] extensionVersions = this.software.getExtension();
		if (extensionVersions != null) {
			for (int j = 0; j < extensionVersions.length; j++) {
				ExtensionType extension = extensionVersions[j];
				if (extension.getCompatibleIntroduceVersions() != null) {
					if (isCompatibleExtension(extension
							.getCompatibleIntroduceVersions())) {
						ExtensionUpdateTreeNode node = new ExtensionUpdateTreeNode(
								extension.getDisplayName() + " ("
										+ extension.getVersion() + ")", getModel(),
								extension);
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
}
