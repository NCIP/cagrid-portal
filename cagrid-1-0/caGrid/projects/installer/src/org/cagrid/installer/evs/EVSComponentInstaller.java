/**
 * 
 */
package org.cagrid.installer.evs;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class EVSComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public EVSComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.evs.title"), "", "evs"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_EVS);
							}

						}));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editEVSSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("evs.edit.service.metadata.title"),
				model.getMessage("evs.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/evs/etc/serviceMetadata.xml";
			}
		};
		model.add(editEVSSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_EVS);
			}
		});

		DeployPropertiesFileEditorStep editEVSDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"evs", model
						.getMessage("evs.edit.deploy.properties.title"),
				model.getMessage("evs.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editEVSDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_EVS);
			}
		});

	}

}
