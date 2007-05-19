package org.cagrid.installer;

import org.cagrid.common.wizard.CaGridWizardModel;
import org.cagrid.installer.path.MasterInstallerPath;
import org.pietschy.wizard.Wizard;


public class CaGridInstallerWizard {

    public static void main(String[] args) {
        MasterInstallerPath paths = new MasterInstallerPath();
        CaGridWizardModel model = new CaGridWizardModel(paths, new CaGridOverviewPanel());
        Wizard wizard = new Wizard(model);
        wizard.setOverviewVisible(true);
        wizard.showInFrame("caGrid Version 1.1 Installer");
    }
}
