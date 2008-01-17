/**
 * 
 */
package org.cagrid.installer.cds;

import javax.swing.Icon;

import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureCDSDBStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public ConfigureCDSDBStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureCDSDBStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureCDSDBStep(String name, String description, Icon icon) {
		super(name, description, icon);

	}
	
	public void applyState() throws InvalidStateException {
		super.applyState();
		
		if(this.model.isTrue(Constants.INSTALL_CDS)){
			this.model.setProperty(Constants.USE_SECURE_CONTAINER, Constants.TRUE);
		}
		
	}

}
