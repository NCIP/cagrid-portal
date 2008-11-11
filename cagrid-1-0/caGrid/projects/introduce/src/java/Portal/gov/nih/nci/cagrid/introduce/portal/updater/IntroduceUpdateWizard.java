package gov.nih.nci.cagrid.introduce.portal.updater;

import org.cagrid.grape.GridApplication;
import org.pietschy.wizard.Wizard;

public class IntroduceUpdateWizard {
    
    public IntroduceUpdateWizard() {
        showUpdateWizard();
    }
    
	public static void showUpdateWizard(){
		UpdatePath paths = new UpdatePath();
		UpdateWizardModel model = new UpdateWizardModel(paths);
		Wizard wizard = new Wizard(model);
		wizard.setOverviewVisible(true);
		wizard.showInDialog("Introduce Update Manager",GridApplication.getContext().getApplication(),true);
	}

	public static void main(String[] args) {
		IntroduceUpdateWizard.showUpdateWizard();
	}
}
