/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;
import javax.swing.JCheckBox;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CheckSecureContainerStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public CheckSecureContainerStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public CheckSecureContainerStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public CheckSecureContainerStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}
	
	public void prepare(){
		if("true".equals(this.model.getState().get(Constants.INSTALL_DORIAN))){
			JCheckBox checkBox = (JCheckBox)getOption(Constants.USE_SECURE_CONTAINER);
			checkBox.setSelected(true);
		}
	}

}
