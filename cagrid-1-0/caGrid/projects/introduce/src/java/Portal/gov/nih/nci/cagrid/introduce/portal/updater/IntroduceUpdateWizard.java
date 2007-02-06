package gov.nih.nci.cagrid.introduce.portal.updater;

import org.pietschy.wizard.Wizard;

public class IntroduceUpdateWizard {
	
	public static void showUpdateWizard(){
		UpdatePath paths = new UpdatePath();
		UpdateWizardModel model = new UpdateWizardModel(paths);
		Wizard wizard = new Wizard(model);
		wizard.setOverviewVisible(true);
		wizard.showInFrame("Introduce Update Manager");
	}

	public static void main(String[] args) {
		IntroduceUpdateWizard.showUpdateWizard();
	}
}
