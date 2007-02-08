package gov.nih.nci.cagrid.introduce.portal.updater;

import gov.nih.nci.cagrid.introduce.portal.updater.steps.CheckForUpdatesStep;
import gov.nih.nci.cagrid.introduce.portal.updater.steps.FinishedStep;
import gov.nih.nci.cagrid.introduce.portal.updater.steps.InstallOrUpdateChoiceStep;
import gov.nih.nci.cagrid.introduce.portal.updater.steps.IntroductionStep;

import java.util.HashMap;
import java.util.Map;

import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.BranchingPath;
import org.pietschy.wizard.models.Condition;
import org.pietschy.wizard.models.SimplePath;

public class UpdatePath extends BranchingPath {

	public UpdatePath(){
		Map globalPropertiesMap = new HashMap();
		SimplePath finishPath = new SimplePath();
		finishPath.addStep(new FinishedStep());
		
		//path for searching for new features.....
		SimplePath findNewFeaturesPath = new SimplePath();
		findNewFeaturesPath.setNextPath(finishPath);
		
		//path for updating.....
		SimplePath checkForUpdatesPath = new SimplePath();
		checkForUpdatesPath.addStep(new CheckForUpdatesStep());
		checkForUpdatesPath.setNextPath(finishPath);
		
		this.addStep(new IntroductionStep());
		final InstallOrUpdateChoiceStep updateTypeSelector = new InstallOrUpdateChoiceStep();
		this.addStep(updateTypeSelector);
		
		this.addBranch(checkForUpdatesPath, new Condition() {
			public boolean evaluate(WizardModel arg0) {
				return updateTypeSelector.isCheckForUpdateOption();
			}
		});
		
		this.addBranch(findNewFeaturesPath, new Condition() {
			public boolean evaluate(WizardModel arg0) {
				return updateTypeSelector.isFindNewFeaturesOption();
			}
		});
	}

}
