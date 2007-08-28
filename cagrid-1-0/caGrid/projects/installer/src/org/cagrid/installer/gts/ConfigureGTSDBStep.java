/**
 * 
 */
package org.cagrid.installer.gts;

import javax.swing.Icon;

import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureGTSDBStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public ConfigureGTSDBStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureGTSDBStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureGTSDBStep(String name, String description, Icon icon) {
		super(name, description, icon);

	}
	
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		if(this.model.isTrue(Constants.INSTALL_GTS)){
			this.model.setProperty(Constants.USE_SECURE_CONTAINER, Constants.TRUE);
		}
		
	}

}
