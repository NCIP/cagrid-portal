/**
 * 
 */
package org.cagrid.installer.dorian;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigureNewDorianCAStep extends PropertyConfigurationStep {

	private static final Log logger = LogFactory
			.getLog(ConfigureNewDorianCAStep.class);

	/**
	 * 
	 */
	public ConfigureNewDorianCAStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureNewDorianCAStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureNewDorianCAStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	public void prepare() {
		try {
			if (this.model.isEqual(Constants.DORIAN_CA_TYPE_DBCA, Constants.DORIAN_CA_TYPE)) {
				JLabel label = getLabel(Constants.DORIAN_CA_ERACOM_SLOT);
				label.setVisible(false);
				JComboBox field = (JComboBox) getOption(Constants.DORIAN_CA_ERACOM_SLOT);
				field.setVisible(false);
			}
		} catch (Exception ex) {
			logger.error("Error configuring new dorian ca panel: "
					+ ex.getMessage(), ex);
		}
	}

}
