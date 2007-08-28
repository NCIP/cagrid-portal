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
		ensureAbsolutePaths(Constants.CA_CERT_PATH, Constants.CA_KEY_PATH);
	}
	
	protected void ensureAbsolutePaths(String caCertPathProp, String caKeyPathProp) throws InvalidStateException {
		try{
			File f = new File(this.model.getProperty(caCertPathProp));
			this.model.setProperty(caCertPathProp, f.getAbsolutePath());
		}catch(Exception ex){
			throw new InvalidStateException("Could not set certificate path: " + ex.getMessage(), ex);
		}
		try{
			File f = new File(this.model.getProperty(caKeyPathProp));
			this.model.setProperty(caKeyPathProp, f.getAbsolutePath());
		}catch(Exception ex){
			throw new InvalidStateException("Could not set key path: " + ex.getMessage(), ex);
		}
	}

}
