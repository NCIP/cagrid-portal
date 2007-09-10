/**
 * 
 */
package org.cagrid.installer.workflow;

import javax.swing.Icon;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureBPELAdminSecurityStep extends PropertyConfigurationStep {

	/**
	 * 
	 */
	public ConfigureBPELAdminSecurityStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigureBPELAdminSecurityStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigureBPELAdminSecurityStep(String name, String description,
			Icon icon) {
		super(name, description, icon);

	}
	
	public void init(WizardModel m){
		
		CaGridInstallerModel model = (CaGridInstallerModel)m;
		
		getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.BPEL_ADMIN_USERNAME, model
								.getMessage(Constants.BPEL_ADMIN_USERNAME), model
								.getProperty(Constants.BPEL_ADMIN_USERNAME,
										""), true));
		
		getOptions().add(
				new PasswordPropertyConfigurationOption(
						Constants.BPEL_ADMIN_PASSWORD, model
								.getMessage(Constants.BPEL_ADMIN_PASSWORD), model
								.getProperty(Constants.BPEL_ADMIN_PASSWORD,
										""), true));
		
		getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.BPEL_ADMIN_ROLE, model
								.getMessage(Constants.BPEL_ADMIN_ROLE), model
								.getProperty(Constants.BPEL_ADMIN_ROLE,
										"admin"), true));
		
		super.init(m);
	}

}
