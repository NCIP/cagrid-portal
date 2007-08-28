/**
 * 
 */
package org.cagrid.installer.dorian;

import javax.swing.Icon;

import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
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
		
		if(this.model.isTrue(Constants.INSTALL_DORIAN)){
			this.model.setProperty(Constants.USE_SECURE_CONTAINER, Constants.TRUE);
		}
		
	}

}
