/**
 * 
 */
package org.cagrid.installer.steps;

import org.pietschy.wizard.InvalidStateException;

import javax.swing.*;

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

		if (isSelected(Constants.INSTALL_CAGRID)
				|| isSelected(Constants.CONFIGURE_CONTAINER)
				|| isSelected(Constants.INSTALL_SERVICES)
				|| isSelected(Constants.INSTALL_PORTAL)){
			setComplete(true);
		} else {
			setComplete(false);
		}
	}

	private boolean isSelected(String fieldName) {
		return this.requiredFields.containsKey(fieldName)
				&& this.requiredFields.get(fieldName);
	}

	public void applyState() throws InvalidStateException {
		super.applyState();
        if (this.model.isTrue(Constants.INSTALL_PORTAL)) {
			this.model.setProperty(Constants.CONTAINER_TYPE, this.model
					.getMessage("container.type.jboss"));
		}

    }

}
