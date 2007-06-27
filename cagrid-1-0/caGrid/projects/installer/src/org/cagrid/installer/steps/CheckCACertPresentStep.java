/**
 * 
 */
package org.cagrid.installer.steps;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CheckCACertPresentStep extends PropertyConfigurationStep implements
		PropertyChangeListener {

	/**
	 * 
	 */
	public CheckCACertPresentStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public CheckCACertPresentStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public CheckCACertPresentStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	public void applyState() throws InvalidStateException {
		super.applyState();
//		if (!"true"
//				.equals(this.model.getState().get(Constants.CA_CERT_PRESENT))) {
//			this.model.getState().put(Constants.GENERATE_CA_CERT, "true");
//		} else {
//			this.model.getState().put(Constants.GENERATE_CA_CERT, "false");
//		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
//		if (Constants.GENERATE_SERVICE_CERT.equals(evt.getPropertyName())) {
//			this.model.getState().put(Constants.CA_CERT_PRESENT, "false");
//		}
	}

}
