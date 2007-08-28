/**
 * 
 */
package org.cagrid.installer.dorian;

import java.io.File;

import javax.swing.Icon;

import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureDorianHostCredentialsStep extends
		PropertyConfigurationStep {

	/**
	 * 
	 */
	public ConfigureDorianHostCredentialsStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureDorianHostCredentialsStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureDorianHostCredentialsStep(String name, String description,
			Icon icon) {
		super(name, description, icon);

	}

	public void applyState() throws InvalidStateException {
		super.applyState();
		String hostname = this.model.getProperty(
				Constants.SERVICE_HOSTNAME);
		String dorianHostCredsDir = this.model.getProperty(
				Constants.DORIAN_HOST_CRED_DIR);
		File serviceCertPath = new File(dorianHostCredsDir + "/" + hostname
				+ "-cert.pem");
		File serviceKeyPath = new File(dorianHostCredsDir + "/" + hostname
				+ "-key.pem");
		this.model.setProperty(Constants.SERVICE_CERT_PATH,
				serviceCertPath.getAbsolutePath());
		this.model.setProperty(Constants.SERVICE_KEY_PATH,
				serviceKeyPath.getAbsolutePath());
	}

}
