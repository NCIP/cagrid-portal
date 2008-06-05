package gov.nih.nci.cagrid.introduce.portal.updater.steps.updatetree;

import gov.nih.nci.cagrid.introduce.beans.software.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.software.IntroduceRevType;
import gov.nih.nci.cagrid.introduce.beans.software.IntroduceType;
import gov.nih.nci.cagrid.introduce.beans.software.SoftwareType;
import gov.nih.nci.cagrid.introduce.common.IntroducePropertiesManager;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.awt.Font;
import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultTreeModel;


public class IntroduceUpdateTreeNode extends UpdateTypeTreeNode {

    private SoftwareType software;

    private IntroduceType introduce;

    private JCheckBox checkBox;

    private boolean installed = false;


    public IntroduceUpdateTreeNode(String displayName, DefaultTreeModel model, IntroduceType introduce,
        SoftwareType software) {
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
        this.introduce.setIsInstalled(new Boolean(installed));
    }


    public boolean isInstalled() {
        return installed;
    }


    public Object getUserObject() {
        return checkBox;
    }


    public void initialize() {
        if (introduce.getIntroduceRev() != null) {
            IntroduceRevType[] revs = introduce.getIntroduceRev();
            if (revs.length > 0) {
                IntroduceRevType latestRev = revs[0];
                for (int i = 1; i < revs.length; i++) {
                    IntroduceRevType tryRev = revs[i];
                    if (tryRev.getPatchVersion() >= latestRev.getPatchVersion()) {
                        latestRev = tryRev;
                    }
                }
                int currentRev = Integer.parseInt(IntroducePropertiesManager.getIntroducePatchVersion());

                if (currentRev < latestRev.getPatchVersion()) {
                    IntroduceRevUpdateTreeNode revNode = new IntroduceRevUpdateTreeNode(
                        "Introduce Cummulative Patch Update " + latestRev.getPatchVersion(), getModel(), latestRev,
                        software);
                    getModel().insertNodeInto(revNode, this, this.getChildCount());
                } else if (currentRev == latestRev.getPatchVersion()) {
                    IntroduceRevUpdateTreeNode revNode = new IntroduceRevUpdateTreeNode(
                        "Introduce Cummulative Patch Update " + latestRev.getPatchVersion(), getModel(), latestRev,
                        software);
                    revNode.getCheckBox().setEnabled(false);
                    revNode.getCheckBox().setSelected(false);
                    revNode.setInstalled(true);
                    revNode.getCheckBox().setText(revNode.getCheckBox().getText() + " installed");
                    revNode.getCheckBox().setFont(revNode.getCheckBox().getFont().deriveFont(Font.ITALIC));
                    getModel().insertNodeInto(revNode, this, this.getChildCount());
                }
                introduce.setIntroduceRev(new IntroduceRevType[]{latestRev});
            }
        }

        ExtensionType[] extensionVersions = software.getExtension();
        if (extensionVersions != null) {
            for (int j = 0; j < extensionVersions.length; j++) {
                ExtensionType extension = extensionVersions[j];
                if (extension.getCompatibleIntroduceVersions() != null) {
                    if (isCompatibleExtension(extension.getCompatibleIntroduceVersions())
                        && isInstalledOrExtensionNewer(extension)) {
                        ExtensionUpdateTreeNode node = null;
                        if (extension.getVersion() != null) {
                            node = new ExtensionUpdateTreeNode(extension.getDisplayName() + " ("
                                + extension.getVersion() + ")", getModel(), extension);
                        } else {
                            node = new ExtensionUpdateTreeNode(extension.getDisplayName() + " (" + "initial version"
                                + ")", getModel(), extension);
                        }
                        if (isExtensionInstalled(extension)) {
                            node.getCheckBox().setEnabled(false);
                            node.getCheckBox().setSelected(false);
                            node.setInstalled(true);
                            node.getCheckBox().setText(node.getCheckBox().getText() + " installed");
                            node.getCheckBox().setFont(node.getCheckBox().getFont().deriveFont(Font.ITALIC));
                        }
                        getModel().insertNodeInto(node, this, this.getChildCount());
                    }
                }
            }
        }
    }


    private boolean isExtensionInstalled(ExtensionType extension) {
        boolean extensionInstalled = false;

        if (ExtensionsLoader.getInstance().getExtension(extension.getName()) != null) {
            if (extension.getVersion() == null) {
                if (ExtensionsLoader.getInstance().getExtension(extension.getName()).getVersion() == null) {
                    extensionInstalled = true;
                }
            } else {
                if (ExtensionsLoader.getInstance().getExtension(extension.getName()).getVersion() != null
                    && ExtensionsLoader.getInstance().getExtension(extension.getName()).getVersion().equals(
                        extension.getVersion())) {
                    extensionInstalled = true;
                }
            }
        }
        return extensionInstalled;
    }


    private boolean isInstalledOrExtensionNewer(ExtensionType extension) {
        boolean newer = false;
        if (ExtensionsLoader.getInstance().getExtension(extension.getName()) != null) {
            String installedVersion = ExtensionsLoader.getInstance().getExtension(extension.getName()).getVersion();
            if (installedVersion == null && extension.getVersion() != null) {
                newer = true;
            } else if (installedVersion != null && extension.getVersion() != null) {
                if (!isOlderVersion(installedVersion, extension.getVersion()) || isExtensionInstalled(extension)) {
                    newer = true;
                }
            } else if ((installedVersion == null && extension.getVersion() == null)
                || (installedVersion != null && extension.getVersion() != null && installedVersion.equals(extension
                    .getVersion()))) {
                newer = true;
            } else {
                newer = false;
            }
        } else {
            newer = true;
        }
        return newer;
    }


    private boolean isOlderVersion(String currentVersion, String proposedVersion) {
        StringTokenizer currentTokes = new StringTokenizer(currentVersion, ".", false);
        StringTokenizer proposedTokes = new StringTokenizer(proposedVersion, ".", false);
        while (proposedTokes.hasMoreElements()) {
            if (!currentTokes.hasMoreElements()) {
                return false;
            }
            int proposedPartVersion = Integer.valueOf(proposedTokes.nextToken()).intValue();
            int currentPartVersion = Integer.valueOf(currentTokes.nextToken()).intValue();
            if (proposedPartVersion > currentPartVersion) {
                return false;
            }
            if (proposedPartVersion < currentPartVersion) {
                return true;
            }
        }
        return false;
    }


    private boolean isCompatibleExtension(String extensionIntroduceVersions) {
        StringTokenizer strtok = new StringTokenizer(extensionIntroduceVersions, ",", false);
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
