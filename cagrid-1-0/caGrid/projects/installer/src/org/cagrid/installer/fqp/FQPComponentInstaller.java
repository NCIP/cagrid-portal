/**
 * 
 */
package org.cagrid.installer.fqp;

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
public class FQPComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public FQPComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.fqp.title"), "", "fqp"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_FQP);
							}

						}));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editFQPSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("fqp.edit.service.metadata.title"),
				model.getMessage("fqp.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/fqp/etc/serviceMetadata.xml";
			}
		};
		model.add(editFQPSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_FQP);
			}
		});

		ServicePropertiesFileEditorStep editFQPServicePropertiesStep = new ServicePropertiesFileEditorStep(
				"fqp", model
						.getMessage("fqp.edit.service.properties.title"),
				model.getMessage("fqp.edit.service.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editFQPServicePropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_FQP);
			}
		});

		DeployPropertiesFileEditorStep editFQPDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"fqp", model
						.getMessage("fqp.edit.deploy.properties.title"),
				model.getMessage("fqp.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editFQPDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_FQP);
			}
		});

	}

}
