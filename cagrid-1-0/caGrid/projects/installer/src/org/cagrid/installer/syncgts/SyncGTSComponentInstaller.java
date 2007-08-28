/**
 * 
 */
package org.cagrid.installer.syncgts;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
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

		PropertyConfigurationStep checkReplaceDefaultGTSCAStep = new PropertyConfigurationStep(
				model.getMessage("check.replace.default.gts.ca.title"), model
						.getMessage("check.replace.default.gts.ca.desc"));
		checkReplaceDefaultGTSCAStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.REPLACE_DEFAULT_GTS_CA, model
								.getMessage("yes"), false, false));
		model.add(checkReplaceDefaultGTSCAStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_SYNC_GTS);
			}
		});

		ReplaceDefaultGTSCAStep specifyDefaultGTSCAStep = new ReplaceDefaultGTSCAStep(
				model.getMessage("specify.default.gts.ca.title"), model
						.getMessage("specify.default.gts.ca.desc"));
		FilePropertyConfigurationOption repCaPath = new FilePropertyConfigurationOption(
				Constants.REPLACEMENT_GTS_CA_CERT_PATH, model
						.getMessage("replacement.gts.ca.cert.path"),
				model.getProperty(Constants.REPLACEMENT_GTS_CA_CERT_PATH, ""),
				true);
		repCaPath.setBrowseLabel(model.getMessage("browse"));
		repCaPath.setDirectoriesOnly(false);
		specifyDefaultGTSCAStep.getOptions().add(repCaPath);
		model.add(specifyDefaultGTSCAStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_SYNC_GTS)
						&& model.isTrue(Constants.REPLACE_DEFAULT_GTS_CA);
			}
		});
	}

}
