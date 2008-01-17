/**
 * 
 */
package org.cagrid.installer.dorian;

import javax.swing.Icon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.ConfigureIdPServiceCAStep;
import org.cagrid.installer.steps.Constants;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureDorianCAStep extends ConfigureIdPServiceCAStep {
	
	private static final Log logger = LogFactory.getLog(ConfigureDorianCAStep.class);

	/**
	 * 
	 */
	public ConfigureDorianCAStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureDorianCAStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureDorianCAStep(String name, String description, Icon icon) {
		super(name, description, icon);

	}

	public void prepare() {
		prePopulateCommonCAFields(Constants.DORIAN_USE_GEN_CA, Constants.DORIAN_CA_CERT_PATH, Constants.DORIAN_CA_KEY_PATH, Constants.DORIAN_CA_KEY_PWD);
	}
	

	public void applyState() throws InvalidStateException {
		super.applyState();
		this.ensureAbsolutePaths(Constants.DORIAN_CA_CERT_PATH, Constants.DORIAN_CA_KEY_PATH);
	}

}
