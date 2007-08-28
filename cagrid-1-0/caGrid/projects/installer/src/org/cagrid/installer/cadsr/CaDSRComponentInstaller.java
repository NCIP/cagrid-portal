/**
 * 
 */
package org.cagrid.installer.cadsr;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.ServicePropertiesFileEditorStep;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CaDSRComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public CaDSRComponentInstaller() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.cadsr.title"), "", "cadsr"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_CADSR);
							}

						}));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editCaDSRSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("cadsr.edit.service.metadata.title"),
				model.getMessage("cadsr.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/cadsr/etc/serviceMetadata.xml";
			}
		};
		model.add(editCaDSRSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CADSR);
			}
		});

		DeployPropertiesFileEditorStep editcaDSRDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"cadsr", model
						.getMessage("cadsr.edit.deploy.properties.title"),
				model.getMessage("cadsr.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editcaDSRDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CADSR);
			}
		});

		ServicePropertiesFileEditorStep editCaDSRServicePropertiesStep = new ServicePropertiesFileEditorStep(
				"cadsr", model
						.getMessage("cadsr.edit.service.properties.title"),
				model.getMessage("cadsr.edit.service.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editCaDSRServicePropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CADSR);
			}
		});

	}

}
