/**
 *
 */
package org.cagrid.installer.portal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DropServiceDatabaseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.validator.MySqlDBConnectionValidator;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

import java.util.Map;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 *
 */
public class PortalComponentInstaller implements CaGridComponentInstaller {

    private static final Log logger = LogFactory
            .getLog(PortalComponentInstaller.class);

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
            }};

        CreatePortalDatabaseTask createPortalDB = new CreatePortalDatabaseTask(
                model.getMessage("installing.portal.title"), "");
        installStep.getTasks().add(
                new ConditionalTask(createPortalDB, installPortal));

        CreateLiferayDatabaseTask createLiferayDB = new CreateLiferayDatabaseTask(
                model.getMessage("installing.portal.title"), "");
        installStep.getTasks().add(
                new ConditionalTask(createLiferayDB, installPortal));

        model.setProperty(Constants.LIFERAY_JBOSS_DIR,model.getProperty(Constants.PORTAL_INSTALL_DIR_PATH)+"/" + Constants.PORTAL_LIFERAY_DIR_NAME);

        installStep.getTasks().add(
                new ConditionalTask(new CaGridInstallerAntTask(model
                        .getMessage("installing.portal.title"), "", "install") {

                    protected String getBuildFilePath(CaGridInstallerModel model) {
                        return model.getProperty(Constants.PORTAL_INSTALL_DIR_PATH)
                                + "/cagrid-portal/build.xml";
                    }

                },installPortal));
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
        portalDbStep.getOptions().add(new TextPropertyConfigurationOption(
                Constants.PORTAL_DB_URL,model.getMessage("portal.db.url"),
                model.getProperty(Constants.PORTAL_DB_URL,"jdbc:mysql://localhost:3306/portal"),
                true));
        portalDbStep.getOptions().add(new TextPropertyConfigurationOption(
                Constants.PORTAL_DB_USERNAME,model.getMessage("portal.db.username"),
                model.getProperty(Constants.PORTAL_DB_USERNAME,"root"),
                true));
        portalDbStep.getOptions().add(new PasswordPropertyConfigurationOption(
                Constants.PORTAL_DB_PASSWORD,model.getMessage("portal.db.password"),
                model.getProperty(Constants.PORTAL_DB_PASSWORD,""),
                false));

        portalDbStep.getValidators().add(
                new MySqlDBConnectionValidator("", "",
                        Constants.PORTAL_DB_USERNAME,
                        Constants.PORTAL_DB_PASSWORD, "select 1",
                        model.getMessage("db.validation.failed")) {

                    protected String getJdbcUrl(Map state) {
                        String url = (String) state
                                .get(Constants.PORTAL_DB_URL);
                        return InstallerUtils.getJdbcBaseFromJdbcUrl(url)
                                + "/mysql";
                    }

                });
        model.add(portalDbStep, installPortal);

        // Drop existing portal DB
        final DropServiceDatabaseStep dropPortalDbStep = new DropServiceDatabaseStep(
                model.getMessage("portal.db.drop.title"), model
                .getMessage("portal.db.drop.desc"), "portal.",
                "drop.portal.db"){
            protected String getJdbcUrl(CaGridInstallerModel model) {
                logger.debug("Portal DB URL " + model.getProperty(Constants.PORTAL_DB_URL));
                return model.getProperty(Constants.PORTAL_DB_URL);
            }

            protected String getPassword(CaGridInstallerModel model) {
                return model.getProperty(Constants.PORTAL_DB_PASSWORD);
            }

            protected String getUsername(CaGridInstallerModel model) {
                return model.getProperty(Constants.PORTAL_DB_USERNAME);
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

        model.add(dropPortalDbStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTrue(Constants.INSTALL_PORTAL)
                        && dropPortalDbStep.databaseExists(model);
            }});


        // Configure portal liferay DB
        PropertyConfigurationStep liferayDbStep = new PropertyConfigurationStep(
                model.getMessage("liferay.db.config.title"), model
                .getMessage("liferay.db.config.desc"));
        InstallerUtils.addDBConfigPropertyOptions(model, liferayDbStep, "liferay.", "liferay");

        logger.debug("Setting Liferay DB Name same as the ID");
        model.add(liferayDbStep, installPortal);

        // Drop existing portal DB
        final DropServiceDatabaseStep dropLiferayDbStep = new DropServiceDatabaseStep(
                model.getMessage("liferay.db.drop.title"), model
                .getMessage("liferay.db.drop.desc"), "liferay.",
                "drop.liferay.db");
        model.add(dropLiferayDbStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTrue(Constants.INSTALL_PORTAL)
                        && dropLiferayDbStep.databaseExists(model);
            }
        });

        // Configure portal misc properties
        ConfigurePortalMiscPropertiesStep portalMiscPropsStep = new ConfigurePortalMiscPropertiesStep(
                model.getMessage("portal.props.config.title"), model
                .getMessage("portal.props.config.desc"));
        model.add(portalMiscPropsStep, installPortal);

        // Configure portal grid properties
        ConfigurePortalGridPropertiesStep portalGridPropsStep = new ConfigurePortalGridPropertiesStep(
                model.getMessage("portal.props.config.title"), model
                .getMessage("portal.props.config.desc"));
        model.add(portalGridPropsStep, installPortal);

    }


}
