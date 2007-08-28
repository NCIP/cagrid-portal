/**
 * 
 */
package org.cagrid.installer.portal;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DropServiceDatabaseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.syncgts.ConfigureSyncGTSStep;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PortalComponentInstaller implements CaGridComponentInstaller {

	/**
	 * 
	 */
	public PortalComponentInstaller() {

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.model.CaGridInstallerModel, org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model,
			RunTasksStep installStep) {
		Condition installPortal = new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_PORTAL);
			}

		};

		// Configure Portal Properties
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.portal.title"), "",
						"configure-portal-properties"), installPortal));

		// Configure Portal Index Svc URLs
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.portal.title"), "",
						"configure-portal-index-svc"), installPortal));

		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.portal.title"), "",
						"configure-portal-deactivate-syncgts"),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_PORTAL)
										&& (model
												.isTrue(Constants.INSTALL_SYNC_GTS) || model
												.isSyncGTSInstalled());
							}

						}));

		// Deploy Portal
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.portal.title"), "",
						"deploy-portal"), installPortal));

		// Deploy Portal Crypto Jars
		installStep.getTasks().add(
				new ConditionalTask(new CaGridInstallerAntTask(model
						.getMessage("installing.portal.title"), "",
						"deploy-portal-crypto-jars"), installPortal));

		CreatePortalDatabaseTask createPortalDB = new CreatePortalDatabaseTask(
				model.getMessage("installing.portal.title"), "");
		installStep.getTasks().add(
				new ConditionalTask(createPortalDB, installPortal));

		// Initialze Portal DB
		CaGridInstallerAntTask initPortalDB1 = new CaGridInstallerAntTask(
				model.getMessage("installing.portal.title"), "",
				"createPortalDatabase") {
			@Override
			protected String getBuildFilePath(CaGridInstallerModel model) {
				return model.getServiceDestDir() + "/portal/build.xml";
			}
		};
		installStep.getTasks().add(
				new ConditionalTask(initPortalDB1, installPortal));

		CaGridInstallerAntTask initPortalDB2 = new CaGridInstallerAntTask(
				model.getMessage("installing.portal.title"), "",
				"createPortalZipCodeSeedData") {
			@Override
			protected String getBuildFilePath(CaGridInstallerModel model) {
				return model.getServiceDestDir() + "/portal/build.xml";
			}
		};
		installStep.getTasks().add(
				new ConditionalTask(initPortalDB2, installPortal));


		CaGridInstallerAntTask initPortalDB4 = new CaGridInstallerAntTask(
				model.getMessage("installing.portal.title"), "",
				"createCaBIGWorkspaceSeedData") {
			@Override
			protected String getBuildFilePath(CaGridInstallerModel model) {
				return model.getServiceDestDir() + "/portal/build.xml";
			}
		};
		installStep.getTasks().add(
				new ConditionalTask(initPortalDB4, installPortal));

	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		Condition installPortal = new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_PORTAL);
			}

		};

		// Configure portal DB
		PropertyConfigurationStep portalDbStep = new PropertyConfigurationStep(
				model.getMessage("portal.db.config.title"), model
						.getMessage("portal.db.config.desc"));
		InstallerUtils.addDBConfigPropertyOptions(model, portalDbStep, "portal.", "portal");
		model.add(portalDbStep, installPortal);

		// Drop existing portal DB
		final DropServiceDatabaseStep dropPortalDbStep = new DropServiceDatabaseStep(
				model.getMessage("portal.db.drop.title"), model
						.getMessage("portal.db.drop.desc"), "portal.",
				"drop.portal.db");
		model.add(dropPortalDbStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_PORTAL)
						&& dropPortalDbStep.databaseExists(model);
			}
		});

		// Configure portal properties
		ConfigurePortalPropertiesStep portalPropsStep = new ConfigurePortalPropertiesStep(
				model.getMessage("portal.props.config.title"), model
						.getMessage("portal.props.config.desc"));
		model.add(portalPropsStep, installPortal);

		// Configure portal SyncGTS
		ConfigureSyncGTSStep portalSyncGTSStep = new ConfigureSyncGTSStep(
				model.getMessage("portal.syncgts.title"), model
						.getMessage("portal.syncgts.desc")) {
			protected boolean isShowPerformFirstSyncField() {
				return false;
			}

			protected String getSyncDescriptionFileName() {
				return this.model.getServiceDestDir()
						+ "/portal/ext/resources/sync-description.xml";
			}
		};
		model.add(portalSyncGTSStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_PORTAL)
						&& !(model.isTrue(Constants.INSTALL_SYNC_GTS) || model
								.isSyncGTSInstalled());
			}
		});

	}

}
