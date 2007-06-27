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
public class CheckServiceCertPresentStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public CheckServiceCertPresentStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public CheckServiceCertPresentStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public CheckServiceCertPresentStep(String name, String description,
			Icon icon) {
		super(name, description, icon);
	}
	
	public void applyState() throws InvalidStateException{
		super.applyState();
//		if(!"true".equals(this.model.getState().get(Constants.SERVICE_CERT_PRESENT))){
//			this.model.getState().put(Constants.GENERATE_SERVICE_CERT, "true");
//		}else{
//			this.model.getState().put(Constants.GENERATE_SERVICE_CERT, "false");
//		}
	}

}
