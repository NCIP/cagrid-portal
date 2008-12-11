/**
 * 
 */
package org.cagrid.installer.syncgts;

import org.cagrid.installer.component.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.service.DeployServiceTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SyncGTSComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public SyncGTSComponentInstaller() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel,
	 *      org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(
						new DeployServiceTask(model
								.getMessage("installing.sync.gts.title"), "",
								"syncgts"), new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_SYNC_GTS);
							}

						}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureSyncGTSStep configureSyncGTSStep = new ConfigureSyncGTSStep(
				model.getMessage("sync.gts.config.title"), model
						.getMessage("sync.gts.config.desc"));
		model.add(configureSyncGTSStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_SYNC_GTS);
			}
		});

		DeployPropertiesFileEditorStep editSyncGTSDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"syncgts", model
						.getMessage("sync.gts.edit.deploy.properties.title"),
				model.getMessage("sync.gts.edit.deploy.properties.desc"), model
						.getMessage("edit.properties.property.name"), model
						.getMessage("edit.properties.property.value"));
		model.add(editSyncGTSDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_SYNC_GTS);
			}
		});

	}

}
