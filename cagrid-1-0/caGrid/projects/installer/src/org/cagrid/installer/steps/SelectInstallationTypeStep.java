/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectInstallationTypeStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public SelectInstallationTypeStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public SelectInstallationTypeStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public SelectInstallationTypeStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	protected void checkComplete() {

		if (this.requiredFields.containsKey(Constants.INSTALL_CAGRID)
				&& this.requiredFields.get(Constants.INSTALL_CAGRID)
				|| this.requiredFields.containsKey(Constants.INSTALL_SERVICES)
				&& this.requiredFields.get(Constants.INSTALL_SERVICES)) {
			setComplete(true);
		} else {
			setComplete(false);
		}
	}

}
