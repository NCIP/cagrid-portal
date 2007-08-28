/**
 * 
 */
package org.cagrid.installer.browser;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;
import org.cagrid.installer.tasks.ConditionalTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class BrowserComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public BrowserComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		Condition installBrowser = new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_BROWSER);
			}

		};

		// Configure Browser Properties
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.browser.title"), "",
						"configure-browser"), installBrowser));
		
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.browser.title"), "",
						"deploy-browser"), installBrowser));
		
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.browser.title"), "",
						"deploy-browser-crypto-jars"), installBrowser));
		
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureBrowserPropertiesStep portalPropsStep = new ConfigureBrowserPropertiesStep(
				model.getMessage("browser.props.config.title"), model
						.getMessage("browser.props.config.desc"));
		model.add(portalPropsStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_BROWSER);
			}			
		});
	}

}
