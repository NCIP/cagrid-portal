/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;

import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureDorianDBStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public ConfigureDorianDBStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureDorianDBStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureDorianDBStep(String name, String description, Icon icon) {
		super(name, description, icon);

	}
	
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		if("true".equals(this.model.getState().get(Constants.INSTALL_DORIAN))){
			this.model.getState().put(Constants.USE_SECURE_CONTAINER, "true");
		}
		
	}

}
