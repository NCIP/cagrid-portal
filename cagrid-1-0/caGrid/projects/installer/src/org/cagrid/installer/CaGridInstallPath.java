package org.cagrid.installer;

import java.util.HashMap;
import java.util.Map;

import org.cagrid.installer.steps.FinishedStep;
import org.cagrid.installer.steps.IntroductionStep;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.BranchingPath;
import org.pietschy.wizard.models.Condition;
import org.pietschy.wizard.models.SimplePath;

public class CaGridInstallPath extends BranchingPath {

	public CaGridInstallPath(){
		Map dicomPropertiesMap = new HashMap();
		Map imagePropertiesMap = new HashMap();
		SimplePath finishPath = new SimplePath();
		finishPath.addStep(new FinishedStep());
		this.addStep(new IntroductionStep());
		this.addBranch(finishPath, new Condition() {
        
            public boolean evaluate(WizardModel arg0) {
               return true;
            }
        
        });
	}

}
