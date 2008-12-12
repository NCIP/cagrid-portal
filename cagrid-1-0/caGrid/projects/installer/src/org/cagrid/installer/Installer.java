/**
 * 
 */
package org.cagrid.installer;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.component.AntComponentInstaller;
import org.cagrid.installer.component.CaGridSourceComponentInstaller;
import org.cagrid.installer.component.DownloadedComponentInstaller;
import org.cagrid.installer.component.GlobusComponentInstaller;
import org.cagrid.installer.component.JBossComponentInstaller;
import org.cagrid.installer.component.TomcatComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.model.CaGridInstallerModelImpl;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.InstallationCompleteStep;
import org.cagrid.installer.steps.PresentLicenseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.SelectInstallationTypeStep;
import org.cagrid.installer.steps.SpecifyPortsStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.SaveSettingsTask;
import org.cagrid.installer.tasks.installer.CopySelectedServicesToTempDirTask;
import org.cagrid.installer.tasks.service.DeployServiceTask;
import org.cagrid.installer.util.DownloadPropertiesUtils;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class Installer {

    private final Log logger;

    private CaGridInstallerModelImpl model;

    private SplashScreen screen;

    private final int TOTAL_INIT_STEPS = 100;

    private int initProgress = 0;

    private List<DownloadedComponentInstaller> dependenciesComponentInstallers = new ArrayList<DownloadedComponentInstaller>();


    public Installer() {

        File _basePath = new File(System.getProperty("user.home") + "/" + Constants.CAGRID_BASE_DIR_NAME);
        if (!_basePath.exists()) {
            _basePath.mkdir();
        }
        logger = LogFactory.getLog(Installer.class);
        logger.debug("Logger initialized");

        dependenciesComponentInstallers.add(new AntComponentInstaller());
        dependenciesComponentInstallers.add(new GlobusComponentInstaller());
        dependenciesComponentInstallers.add(new CaGridSourceComponentInstaller());

    }


    public static void main(String[] args) {
        Installer installer = new Installer();
        installer.initialize();
        installer.run();
    }


    private void splashScreenDestruct() {
        if (screen != null) {
            screen.setScreenVisible(false);
            screen.dispose();
        }
    }


    private void splashScreenInit() {
        ImageIcon myImage = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(
            "images/installer.gif"));
        screen = new SplashScreen(myImage);
        screen.setLocationRelativeTo(null);
        screen.setProgressMax(TOTAL_INIT_STEPS);
        screen.setScreenVisible(true);
    }


    public void initialize() {

        try {
            // Show the splash screen
            splashScreenInit();

            incrementProgress();

            Map<String, String> defaultState = new HashMap<String, String>();

            incrementProgress();

            // Load default properties
            Properties downloadedProps = DownloadPropertiesUtils.getDownloadedProps();
            incrementProgress();

            Enumeration e = downloadedProps.propertyNames();
            while (e.hasMoreElements()) {
                String propName = (String) e.nextElement();
                defaultState.put(propName, downloadedProps.getProperty(propName));
            }
            incrementProgress();

            // Set up temp dir
            String installerDir = InstallerUtils.buildInstallerDirPath(defaultState.get(Constants.CAGRID_VERSION));
            logger.info("installer dir: " + installerDir);
            String tempDir = installerDir + "/tmp";
            defaultState.put(Constants.TEMP_DIR_PATH, tempDir);
            File tempDirFile = new File(tempDir);
            if (tempDirFile.exists()) {
                tempDirFile.delete();
            }
            logger.info("Creating temporary directory '" + tempDir + "'");
            try {
                tempDirFile.mkdirs();
            } catch (Exception ex) {
                String msg = "Error creating temporary directory '" + tempDir + "': " + ex.getMessage();
                logger.error(msg, ex);
                throw new RuntimeException(msg, ex);
            }
            incrementProgress();

            // Check for presence of cagrid.installer.properties file
            String cagridInstallerFileName = System.getProperty(Constants.CAGRID_INSTALLER_PROPERTIES);
            if (cagridInstallerFileName != null) {
                logger.info("Custom installer properties file specified: '" + cagridInstallerFileName + "'");
            } else {
                cagridInstallerFileName = installerDir + "/" + Constants.CAGRID_INSTALLER_PROPERTIES;
                logger.info("Using default properties file: '" + cagridInstallerFileName + "'");
            }
            defaultState.put(Constants.CAGRID_INSTALLER_PROPERTIES, cagridInstallerFileName);
            File cagridInstallerFile = new File(cagridInstallerFileName);
            incrementProgress();

            // If cagrid.installer.properties found, load properties.
            logger.info("Looking for '" + cagridInstallerFileName + "'");
            if (cagridInstallerFile.exists()) {
                try {
                    logger.info("Loading '" + cagridInstallerFileName + "'");
                    Properties props = new Properties();
                    props.load(new FileInputStream(cagridInstallerFile));

                    // Downloaded properties have precedence
                    Enumeration e2 = props.propertyNames();
                    while (e2.hasMoreElements()) {
                        String propName = (String) e2.nextElement();
                        if (!defaultState.containsKey(propName)) {
                            defaultState.put(propName, props.getProperty(propName));
                        }
                    }
                } catch (Exception ex) {
                    String msg = "Could not load '" + cagridInstallerFileName + "': " + ex.getMessage();
                    logger.error(msg, ex);
                    throw new RuntimeException(msg, ex);
                }
            } else {
                logger.info("Did not find '" + cagridInstallerFileName + "'");
            }
            incrementProgress();

            InstallerUtils.assertCorrectJavaVersion(defaultState);

            initSteps(defaultState);

            while (this.initProgress < TOTAL_INIT_STEPS) {
                incrementProgress();
                try {
                    Thread.sleep(10);
                } catch (Exception ex) {

                }
            }

        } catch (Exception ex) {
            InstallerUtils.handleException("Error initializing: " + ex.getMessage(), ex);
        } finally {
            splashScreenDestruct();
        }
    }


    private void incrementProgress() {
        screen.setProgress("Initializing installer...", this.initProgress += 5);
    }


    private void initSteps(Map<String, String> defaultState) {

        // TODO: provide some factory method here
        this.model = new CaGridInstallerModelImpl(defaultState);

        clearFlags();

        // Initialize steps
        PresentLicenseStep licenseStep = new PresentLicenseStep(this.model.getMessage("accept.license.title"),
            this.model.getMessage("accept.license.desc"));
        this.model.add(licenseStep);
        incrementProgress();

        // Gives user choice to install caGrid, configure a container,
        // or install one or more services, or
        // any of the above.
        SelectInstallationTypeStep selectInstallStep = new SelectInstallationTypeStep(this.model
            .getMessage("select.install.title"), this.model.getMessage("select.install.desc"));
        selectInstallStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_CAGRID, this.model
                .getMessage("select.install.install.cagrid"), true, true));
        selectInstallStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.CONFIGURE_CONTAINER, this.model
                .getMessage("select.install.configure.container"), false, true));

        this.model.add(selectInstallStep);

        incrementProgress();
        addInstallcaGridSteps();
        incrementProgress();
        addConfigureContainerSteps();
        incrementProgress();
        addDeployContainerSteps();
        incrementProgress();

        this.model.add(new InstallationCompleteStep(this.model.getMessage("installation.complete.title"), ""));

    }


    private void addInstallcaGridSteps() {
        final RunTasksStep installDependenciesStep = new RunTasksStep(this.model
            .getMessage("install.dependencies.title"), this.model.getMessage("install.dependencies.desc"));
        for (DownloadedComponentInstaller installer : getDownloadedComponentInstallers()) {
            installer.addCheckInstallSteps(this.model);
            installer.addInstallDownloadedComponentTasks(this.model, installDependenciesStep);
            incrementProgress();
        }

        installDependenciesStep.getTasks().add(
            new ConditionalTask(new CopySelectedServicesToTempDirTask(this.model
                .getMessage("copying.selected.services"), ""), new Condition() {
                public boolean evaluate(WizardModel m) {
                    CaGridInstallerModel model = (CaGridInstallerModel) m;
                    return model.isTrue(Constants.INSTALL_SERVICES);
                }
            }));

        installDependenciesStep.getTasks()
            .add(new SaveSettingsTask(this.model.getMessage("saving.settings.title"), ""));

        incrementProgress();

        this.model.add(installDependenciesStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return installDependenciesStep.getTasksCount(model) > 0
                    || (model.isTrue(Constants.INSTALL_CAGRID) && installDependenciesStep.getTasksCount(model) > 0);
            }
        });
    }


    private void addDeployContainerSteps() {
        final RunTasksStep deployContainer = new RunTasksStep(this.model.getMessage("install.title"), this.model
            .getMessage("install.desc"));

        // for each container type make sure to add that container installer
        TomcatComponentInstaller tomcatComponentInstaller = new TomcatComponentInstaller();
        tomcatComponentInstaller.addCheckInstallSteps(this.model);
        tomcatComponentInstaller.addInstallDownloadedComponentTasks(this.model, deployContainer);

        JBossComponentInstaller jbossComponentInstaller = new JBossComponentInstaller();
        jbossComponentInstaller.addCheckInstallSteps(this.model);
        jbossComponentInstaller.addInstallDownloadedComponentTasks(this.model, deployContainer);

        incrementProgress();

        // deploy the syngGTS
        DeployServiceTask deploySyncGTS = new DeployServiceTask("", "", "syncGTS");
        deployContainer.getTasks().add(new ConditionalTask(deploySyncGTS, new Condition() {

            public boolean evaluate(WizardModel model) {
                return ((CaGridInstallerModel) model).isDeployGlobusRequired()
                    && ((CaGridInstallerModel) model).isConfigureContainerSelected();
            }
        }));

        deployContainer.getTasks().add(
            new ConditionalTask(new SaveSettingsTask(this.model.getMessage("saving.settings.title"), ""),
                new Condition() {
                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isConfigureContainerSelected();
                    }
                }));

        this.model.add(deployContainer, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return deployContainer.getTasksCount(model) > 0;
            }
        });

    }


    private void addConfigureContainerSteps() {

        PropertyConfigurationStep selectContainerStep = new PropertyConfigurationStep(this.model
            .getMessage("select.container.title"), this.model.getMessage("select.container.desc"));
        selectContainerStep.getOptions().add(
            new ListPropertyConfigurationOption(Constants.CONTAINER_TYPE, this.model.getMessage("container.type"),
                new String[]{this.model.getMessage("container.type.tomcat"),
                        this.model.getMessage("container.type.jboss")}, true));
        selectContainerStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.USE_SECURE_CONTAINER, this.model
                .getMessage("globus.check.secure.desc"), false, false));

        this.model.add(selectContainerStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isConfigureContainerSelected();

            }

        });

        // Allows user to specify ports and hostname
        SpecifyPortsStep containerConfigureStep = new SpecifyPortsStep(this.model.getMessage("specify.ports.title"),
            this.model.getMessage("specify.ports.desc"));
        containerConfigureStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.SERVICE_HOSTNAME, this.model.getMessage("service.hostname"),
                this.model.getProperty(Constants.SERVICE_HOSTNAME, "localhost"), true));
        containerConfigureStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.SHUTDOWN_PORT, this.model.getMessage("shutdown.port"),
                this.model.getProperty(Constants.SHUTDOWN_PORT, "8005"), false));
        containerConfigureStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.HTTP_PORT, this.model.getMessage("http.port"), this.model
                .getProperty(Constants.HTTP_PORT, "8080"), model.isTrue(Constants.USE_SECURE_CONTAINER)));
        containerConfigureStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.HTTPS_PORT, this.model.getMessage("https.port"), this.model
                .getProperty(Constants.HTTPS_PORT, "8443"), model.isTrue(Constants.USE_SECURE_CONTAINER)));

        // need to get credentials here from dorian if secure deployment is
        // required

        PropertyConfigurationStep checkDeployGlobusStep = new PropertyConfigurationStep(this.model
            .getMessage("globus.check.redeploy.title"), this.model.getMessage("globus.check.redeploy.desc"));
        checkDeployGlobusStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.REDEPLOY_GLOBUS, this.model.getMessage("yes"), false,
                false));
        this.model.add(checkDeployGlobusStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return (model.isTomcatContainer() || model.isJBossContainer()) && model.isGlobusDeployed()
                    && model.isConfigureContainerSelected();
            }

        });

        this.model.add(containerConfigureStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isConfigureContainerSelected();
            }
        });

    }


    private void clearFlags() {
        this.model.unsetProperty(Constants.CONFIGURE_CONTAINER);

        this.model.unsetProperty(Constants.INSTALL_SYNC_GTS);
        this.model.unsetProperty(Constants.INSTALL_SERVICES);

        this.model.unsetProperty(Constants.RECONFIGURE_CAGRID);
        this.model.unsetProperty(Constants.USE_SECURE_CONTAINER);
    }


    public void run() {
        ImageIcon myImage = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(
            "images/caGrid_small.png"));
        Wizard wizard = new Wizard(this.model);
        wizard.showInFrame("caGrid Installation Wizard", myImage.getImage());
    }


    public List<DownloadedComponentInstaller> getDownloadedComponentInstallers() {
        return dependenciesComponentInstallers;
    }


    public void setDownloadedComponentInstallers(List<DownloadedComponentInstaller> externalComponentInstallers) {
        this.dependenciesComponentInstallers = externalComponentInstallers;
    }

}
