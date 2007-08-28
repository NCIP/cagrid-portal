/**
 * 
 */
package org.cagrid.installer.gridgrouper;

import java.util.Map;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.DropServiceDatabaseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.validator.MySqlDBConnectionValidator;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GridGrouperComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public GridGrouperComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(
						new CreateGridGrouperDatabaseTask(model
								.getMessage("creating.grid.grouper.db.title"),
								""), new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model
										.isTrue(Constants.INSTALL_GRID_GROUPER);
							}

						}));
		installStep.getTasks().add(
				new ConditionalTask(new ConfigureGridGrouperTask(model
						.getMessage("configuring.grid.grouper.title"), ""),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model
										.isTrue(Constants.INSTALL_GRID_GROUPER);
							}

						}));
		installStep.getTasks().add(
				new ConditionalTask(new DeployServiceTask(model
						.getMessage("deploying.grid.grouper.title"), "",
						"gridgrouper"), new Condition() {

					public boolean evaluate(WizardModel m) {
						CaGridInstallerModel model = (CaGridInstallerModel) m;
						return model.isTrue(Constants.INSTALL_GRID_GROUPER);
					}

				}));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editGridGrouperSvcMetaStep = new ConfigureServiceMetadataStep(
				"",
				model
						.getMessage("grid.grouper.edit.service.metadata.title"),
				model
						.getMessage("grid.grouper.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/gridgrouper/etc/GridGrouper_serviceMetadata.xml";
			}
		};
		model.add(editGridGrouperSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GRID_GROUPER);
			}
		});

		PropertyConfigurationStep gridGrouperConfigStep = new PropertyConfigurationStep(
				model.getMessage("grid.grouper.config.title"), model
						.getMessage("grid.grouper.config.desc"));
		gridGrouperConfigStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.GRID_GROUPER_ADMIN_IDENT,
								model
										.getMessage("grid.grouper.admin.ident"),
								model
										.getProperty(
												Constants.GRID_GROUPER_ADMIN_IDENT,
												"/O=org/OU=unit/OU=User Group/OU=Dorian IdP/CN=manager"),
								true));
		gridGrouperConfigStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.GRID_GROUPER_DB_URL, model
								.getMessage("grid.grouper.db.url"), model
								.getProperty(Constants.GRID_GROUPER_DB_URL,
										"jdbc:mysql://localhost:3306/grouper"),
						true));
		gridGrouperConfigStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.GRID_GROUPER_DB_USERNAME, model
								.getMessage("grid.grouper.db.username"),
						model.getProperty(
								Constants.GRID_GROUPER_DB_USERNAME, "root"),
						true));
		gridGrouperConfigStep
				.getOptions()
				.add(
						new PasswordPropertyConfigurationOption(
								Constants.GRID_GROUPER_DB_PASSWORD,
								model
										.getMessage("grid.grouper.db.password"),
								model.getProperty(
										Constants.GRID_GROUPER_DB_PASSWORD, ""),
								false));
		gridGrouperConfigStep.getValidators().add(
				new MySqlDBConnectionValidator("", "",
						Constants.GRID_GROUPER_DB_USERNAME,
						Constants.GRID_GROUPER_DB_PASSWORD, "select 1",
						model.getMessage("db.validation.failed")) {

					protected String getJdbcUrl(Map state) {
						String url = (String) state
								.get(Constants.GRID_GROUPER_DB_URL);
						return InstallerUtils.getJdbcBaseFromJdbcUrl(url)
								+ "/mysql";
					}

				});
		model.add(gridGrouperConfigStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GRID_GROUPER);
			}
		});

		final DropServiceDatabaseStep dropGridGrouperDbStep = new DropServiceDatabaseStep(
				model.getMessage("grid.grouper.db.drop.title"), model
						.getMessage("grid.grouper.db.drop.desc"),
				"grid.grouper.", "drop.grid.grouper.db") {
			protected String getJdbcUrl(CaGridInstallerModel model) {
				return model.getProperty(Constants.GRID_GROUPER_DB_URL);
			}

			protected String getDatabase(CaGridInstallerModel model) {
				String db = null;
				String url = getJdbcUrl(model);
				if(url != null){
					db = InstallerUtils.getDbNameFromJdbcUrl(url); 
				}
				return db;
			}
		};
		model.add(dropGridGrouperDbStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GRID_GROUPER)
						&& dropGridGrouperDbStep.databaseExists(model);
			}
		});

		DeployPropertiesFileEditorStep editGridGrouperDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"gridgrouper",
				model
						.getMessage("grid.grouper.edit.deploy.properties.title"),
				model
						.getMessage("grid.grouper.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editGridGrouperDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_GRID_GROUPER);
			}
		});

	}

}
