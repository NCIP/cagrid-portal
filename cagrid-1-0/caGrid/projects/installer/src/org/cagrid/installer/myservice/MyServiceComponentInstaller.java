/**
 * 
 */
package org.cagrid.installer.myservice;

import org.cagrid.installer.component.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MyServiceComponentInstaller implements CaGridComponentInstaller {
	
	public MyServiceComponentInstaller(){
		
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.my.service.title"), "", "") {

					protected String getBuildFilePath(CaGridInstallerModel model) {
						return model.getProperty(Constants.MY_SERVICE_DIR)
								+ "/build.xml";
					}

				}, new Condition() {

					public boolean evaluate(WizardModel m) {
						CaGridInstallerModel model = (CaGridInstallerModel) m;
						return model.isTrue(Constants.INSTALL_MY_SERVICE)
								&& model.isConfigureContainerSelected();
					}

				}));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		PropertyConfigurationStep selectMyServiceStep = new PropertyConfigurationStep(
				model.getMessage("select.my.service.title"), model
						.getMessage("select.my.service.desc"));
		FilePropertyConfigurationOption mySvcDir = new FilePropertyConfigurationOption(
				Constants.MY_SERVICE_DIR, model
						.getMessage("my.service.dir"), model.getProperty(
						Constants.MY_SERVICE_DIR, ""), true);
		mySvcDir.setBrowseLabel(model.getMessage("browse"));
		mySvcDir.setDirectoriesOnly(true);
		selectMyServiceStep.getOptions().add(mySvcDir);
		model.add(selectMyServiceStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_MY_SERVICE);
			}
		});

		ConfigureServiceMetadataStep editMySvcMetaStep = new ConfigureServiceMetadataStep(
				Constants.MY_SERVICE_DIR, model
						.getMessage("my.service.edit.service.metadata.title"),
				model.getMessage("my.service.edit.service.metadata.desc"));
		model.add(editMySvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_MY_SERVICE);
			}
		});

		final IntroduceServicePropertiesFileEditorStep mySvcDeployPropsStep = new IntroduceServicePropertiesFileEditorStep(
				Constants.MY_SERVICE_DIR,
				"deploy.properties",
				model
						.getMessage("my.service.edit.deploy.properties.title"),
				model.getMessage("my.service.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(mySvcDeployPropsStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return mySvcDeployPropsStep.evaluate(m) && model.isTrue(Constants.INSTALL_MY_SERVICE);
			}
		});

		final IntroduceServicePropertiesFileEditorStep mySvcServicePropsStep = new IntroduceServicePropertiesFileEditorStep(
				Constants.MY_SERVICE_DIR,
				"service.properties",
				model
						.getMessage("my.service.edit.service.properties.title"),
				model
						.getMessage("my.service.edit.service.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(mySvcServicePropsStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return mySvcServicePropsStep.evaluate(m) && model.isTrue(Constants.INSTALL_MY_SERVICE);
			}
		});

	}

}
