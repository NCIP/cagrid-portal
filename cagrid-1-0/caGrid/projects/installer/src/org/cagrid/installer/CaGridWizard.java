/**
 * 
 */
package org.cagrid.installer;

import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CaGridWizard extends Wizard {
	
	private String frameTitle;

	/**
	 * @param model
	 */
	public CaGridWizard(WizardModel model) {
		super(model);
		
	}
	
	public void show(){
		showInFrame(getFrameTitle());
	}

	public String getFrameTitle() {
		return frameTitle;
	}

	public void setFrameTitle(String frameTitle) {
		this.frameTitle = frameTitle;
	}

}
