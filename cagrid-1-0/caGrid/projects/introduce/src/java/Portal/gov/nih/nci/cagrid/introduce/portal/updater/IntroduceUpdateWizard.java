package gov.nih.nci.cagrid.introduce.portal.updater;

import org.pietschy.wizard.Wizard;
import org.projectmobius.portal.PortalResourceManager;

public class IntroduceUpdateWizard {
	
	public static void showUpdateWizard(){
		UpdatePath paths = new UpdatePath();
		UpdateWizardModel model = new UpdateWizardModel(paths);
		Wizard wizard = new Wizard(model);
		wizard.setOverviewVisible(true);
		wizard.showInDialog("Introduce Update Manager",PortalResourceManager.getInstance().getGridPortal(),true);
	}

	public static void main(String[] args) {
		IntroduceUpdateWizard.showUpdateWizard();
	}
}
