/**
 * 
 */
package org.cagrid.installer.steps;

import java.io.File;

import javax.swing.Icon;

import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureCAStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public ConfigureCAStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureCAStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureCAStep(String name, String description, Icon icon) {
		super(name, description, icon);

	}
	
	public void applyState() throws InvalidStateException {
		super.applyState();
		try{
			File f = new File((String)this.model.getState().get(Constants.CA_CERT_PATH));
			this.model.getState().put(Constants.CA_CERT_PATH, f.getAbsolutePath());
		}catch(Exception ex){
			throw new InvalidStateException("Could not set certificate path: " + ex.getMessage(), ex);
		}
		try{
			File f = new File((String)this.model.getState().get(Constants.CA_KEY_PATH));
			this.model.getState().put(Constants.CA_KEY_PATH, f.getAbsolutePath());
		}catch(Exception ex){
			throw new InvalidStateException("Could not set key path: " + ex.getMessage(), ex);
		}
	}

}
