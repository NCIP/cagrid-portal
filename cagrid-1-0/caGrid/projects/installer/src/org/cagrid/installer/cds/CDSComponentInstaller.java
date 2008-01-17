/**
 * 
 */
package org.cagrid.installer.cds;

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
public class CDSComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public CDSComponentInstaller() {

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
				new ConditionalTask(new ConfigureCDSTask(model
						.getMessage("configuring.cds.title"), ""),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_CDS);
							}

						}));
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("installing.cds.title"), "", "cds"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_CDS);
							}

						}));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editCDSSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("cds.edit.service.metadata.title"), model
						.getMessage("cds.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/cds/etc/serviceMetadata.xml";
			}
		};
		model.add(editCDSSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CDS);
			}
		});

		DeployPropertiesFileEditorStep editCDSDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"cds", model.getMessage("cds.edit.deploy.properties.title"),
				model.getMessage("cds.edit.deploy.properties.desc"), model
						.getMessage("edit.properties.property.name"), model
						.getMessage("edit.properties.property.value"));
		model.add(editCDSDeployPropertiesStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CDS);
			}
		});

		// Configures the CDS database
		ConfigureCDSDBStep cdsDbInfoStep = new ConfigureCDSDBStep(model
				.getMessage("cds.db.config.title"), model
				.getMessage("cds.db.config.desc"));
		InstallerUtils.addDBConfigPropertyOptions(model, cdsDbInfoStep, "cds.",
				"cds");
		model.add(cdsDbInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CDS);
			}
		});

		final DropServiceDatabaseStep dropCDSDbStep = new DropServiceDatabaseStep(
				model.getMessage("cds.db.drop.title"), model
						.getMessage("cds.db.drop.desc"), "cds.", "cds.db.drop");
		model.add(dropCDSDbStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CDS)
						&& dropCDSDbStep.databaseExists(model);
			}
		});

		PropertyConfigurationStep cdsGeneralConfig = new PropertyConfigurationStep(
				model.getMessage("cds.config.title"), model
						.getMessage("cds.config.desc"));
		cdsGeneralConfig.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.CDS_MAX_DELEGATION_PATH_LENGTH, model
								.getMessage("cds.max.delegation.path.length"),
						model.getProperty(
								Constants.CDS_MAX_DELEGATION_PATH_LENGTH, "0"),
						true));
		cdsGeneralConfig
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.CDS_DB_KEY_MANAGER_PASSWORD,
								model
										.getMessage("cds.dbkeymanager.key.encyption.password"),
								model.getProperty(
										Constants.CDS_DB_KEY_MANAGER_PASSWORD,
										"changeme"), true));
		model.add(cdsGeneralConfig, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CDS);
			}
		});

		PropertyConfigurationStep cdsAddAdminStep = new PropertyConfigurationStep(
				model.getMessage("cds.add.admin.title"), model
						.getMessage("cds.add.admin.desc"));
		cdsAddAdminStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.CDS_ADMIN_IDENT,
								model.getMessage("cds.admin.ident"),
								model
										.getProperty(Constants.CDS_ADMIN_IDENT,
												"/O=org/OU=unit/OU=User Group/OU=Dorian IdP/CN=manager"),
								true));
		model.add(cdsAddAdminStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_CDS);
			}
		});

	}

}
