/**
 * 
 */
package org.cagrid.installer.path;

import java.util.ArrayList;
import java.util.List;

import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.SimplePath;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MultiStepPath extends SimplePath implements ChainablePath, InitializingBean {

	private List<PanelWizardStep> wizardSteps = new ArrayList<PanelWizardStep>();
	
	public List<PanelWizardStep> getWizardSteps() {
		return this.wizardSteps;
	}

	public void setWizardSteps(List<PanelWizardStep> steps) {
		this.wizardSteps = steps;
	}

	/**
	 * 
	 */
	public MultiStepPath() {

	}

	public void afterPropertiesSet() throws Exception {
		for(PanelWizardStep step : getWizardSteps()){
			addStep(step);
		}
	}

}
