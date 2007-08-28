/**
 * 
 */
package org.cagrid.installer.gme;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GMEComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public GMEComponentInstaller() {

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
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("configuring.gme.title"), "",
						"configure-gme-globus-config"), new Condition() {

					public boolean evaluate(WizardModel m) {
						CaGridInstallerModel model = (CaGridInstallerModel) m;
						return model.isTrue(Constants.INSTALL_GME);
					}

				}));

		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.gme.title"), "", "gme"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_GME);
							}

						}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editGMESvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("gme.edit.service.metadata.title"), model
						.getMessage("gme.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/gme/etc/serviceMetadata.xml";
			}
		};
		model.add(editGMESvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GME);
			}
		});

		PropertyConfigurationStep gmeDbInfoStep = new PropertyConfigurationStep(
				model.getMessage("gme.db.config.title"), model
						.getMessage("gme.db.config.desc"));
		InstallerUtils.addDBConfigPropertyOptions(model, gmeDbInfoStep, "gme.",
				"gme");
		model.add(gmeDbInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GME);
			}
		});

		DeployPropertiesGMEFileEditorStep editGMEDeployPropertiesStep = new DeployPropertiesGMEFileEditorStep(
				"gme", model.getMessage("gme.edit.deploy.properties.title"),
				model.getMessage("gme.edit.deploy.properties.desc"), model
						.getMessage("edit.properties.property.name"), model
						.getMessage("edit.properties.property.value"));
		model.add(editGMEDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GME);
			}
		});
	}

}
