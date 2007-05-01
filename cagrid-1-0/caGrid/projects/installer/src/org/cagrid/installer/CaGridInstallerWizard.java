package org.cagrid.installer;

import org.pietschy.wizard.Wizard;

public class CaGridInstallerWizard {

	public static void main(String[] args) {
		CaGridInstallPath paths = new CaGridInstallPath();
		CaGridWizardModel model = new CaGridWizardModel(paths);
		Wizard wizard = new Wizard(model);
		wizard.setOverviewVisible(true);
		wizard.showInFrame("caGrid Version 1.1 Installer");
	}
}
