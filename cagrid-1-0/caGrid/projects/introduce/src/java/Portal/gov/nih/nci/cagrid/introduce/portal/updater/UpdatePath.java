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

public class UpdatePath extends SimplePath {

	public UpdatePath(){
		Map globalPropertiesMap = new HashMap();

		this.addStep(new CheckForUpdatesStep());
		this.addStep(new FinishedStep());
		
	}

}
