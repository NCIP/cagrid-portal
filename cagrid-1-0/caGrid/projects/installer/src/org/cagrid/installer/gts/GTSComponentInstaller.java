/**
 * 
 */
package org.cagrid.installer.gts;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.DropServiceDatabaseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GTSComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public GTSComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(new ConfigureGTSTask(model
						.getMessage("configuring.gts.title"), ""),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_GTS);
							}

						}));
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.gts.title"), "", "gts"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_GTS);
							}

						}));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editGTSSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("gts.edit.service.metadata.title"),
				model.getMessage("gts.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/gts/etc/serviceMetadata.xml";
			}
		};
		model.add(editGTSSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GTS);
			}
		});

		DeployPropertiesFileEditorStep editGTSDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"gts", model
						.getMessage("gts.edit.deploy.properties.title"),
				model.getMessage("gts.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editGTSDeployPropertiesStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GTS);
			}
		});

		// Configures the GTS database
		ConfigureGTSDBStep gtsDbInfoStep = new ConfigureGTSDBStep(model
				.getMessage("gts.db.config.title"), model
				.getMessage("gts.db.config.desc"));
		InstallerUtils.addDBConfigPropertyOptions(model, gtsDbInfoStep, "gts.", "gts");
		model.add(gtsDbInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GTS);
			}
		});

		final DropServiceDatabaseStep dropGtsDbStep = new DropServiceDatabaseStep(
				model.getMessage("gts.db.drop.title"), model
						.getMessage("gts.db.drop.desc"), "gts.", "gts.db.drop");
		model.add(dropGtsDbStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GTS)
						&& dropGtsDbStep.databaseExists(model);
			}
		});

		PropertyConfigurationStep gtsAddAdminStep = new PropertyConfigurationStep(
				model.getMessage("gts.add.admin.title"), model
						.getMessage("gts.add.admin.desc"));
		gtsAddAdminStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.GTS_ADMIN_IDENT,
								model.getMessage("gts.admin.ident"),
								model
										.getProperty(Constants.GTS_ADMIN_IDENT,
												"/O=org/OU=unit/OU=User Group/OU=Dorian IdP/CN=manager"),
								true));
		model.add(gtsAddAdminStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GTS);
			}
		});

	}

}
