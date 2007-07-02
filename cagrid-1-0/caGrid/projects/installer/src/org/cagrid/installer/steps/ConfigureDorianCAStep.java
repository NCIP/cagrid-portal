/**
 * 
 */
package org.cagrid.installer.steps;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pietschy.wizard.InvalidStateException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureDorianCAStep extends PropertyConfigurationStep {
	
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
		if ("true".equals(this.model.getState()
				.get(Constants.DORIAN_USE_GEN_CA))) {
			logger.debug("Setting default dorian cert path and key path to predefined values");

			JTextField certPathField = (JTextField) getOption(Constants.DORIAN_CA_CERT_PATH);
			String certPath = (String) this.model.getState().get(
					Constants.CA_CERT_PATH);
			logger.debug("Setting default for " + Constants.DORIAN_CA_CERT_PATH + " to " + certPath);
			certPathField.setText(certPath);
			
			JTextField keyPathField = (JTextField) getOption(Constants.DORIAN_CA_KEY_PATH);
			String keyPath = (String) this.model.getState().get(
					Constants.CA_KEY_PATH);
			logger.debug("Setting default for " + Constants.DORIAN_CA_KEY_PATH + " to " + keyPath);
			keyPathField.setText(keyPath);
			
			JTextField keyPwdField = (JTextField) getOption(Constants.DORIAN_CA_KEY_PWD);
			String keyPwd = (String) this.model.getState().get(
					Constants.CA_KEY_PWD);
			logger.debug("Setting default for " + Constants.DORIAN_CA_KEY_PWD + " to " + keyPwd);
			keyPwdField.setText(keyPwd);

		}
	}

	public void applyState() throws InvalidStateException {
		super.applyState();
		try {
			File f = new File((String) this.model.getState().get(
					Constants.DORIAN_CA_CERT_PATH));
			this.model.getState().put(Constants.DORIAN_CA_CERT_PATH,
					f.getAbsolutePath());
		} catch (Exception ex) {
			throw new InvalidStateException("Could not set certificate path: "
					+ ex.getMessage(), ex);
		}
		try {
			File f = new File((String) this.model.getState().get(
					Constants.DORIAN_CA_KEY_PATH));
			this.model.getState().put(Constants.DORIAN_CA_KEY_PATH,
					f.getAbsolutePath());
		} catch (Exception ex) {
			throw new InvalidStateException("Could not set key path: "
					+ ex.getMessage(), ex);
		}
	}

}
