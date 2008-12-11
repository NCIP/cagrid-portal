/**
 * 
 */
package org.cagrid.installer.steps;

import javax.swing.Icon;
import javax.swing.JCheckBox;

import org.pietschy.wizard.InvalidStateException;

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


    @Override
    public void applyState() throws InvalidStateException {
        // TODO Auto-generated method stub
        super.applyState();
    }
	
	

}
