/**
 * 
 */
package org.cagrid.installer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.authnsvc.AuthenticationServiceComponentInstaller;
import org.cagrid.installer.cadsr.CaDSRComponentInstaller;
import org.cagrid.installer.cds.CDSComponentInstaller;
import org.cagrid.installer.dorian.DorianComponentInstaller;
import org.cagrid.installer.evs.EVSComponentInstaller;
import org.cagrid.installer.fqp.FQPComponentInstaller;
import org.cagrid.installer.gme.GMEComponentInstaller;
import org.cagrid.installer.gridgrouper.GridGrouperComponentInstaller;
import org.cagrid.installer.gts.GTSComponentInstaller;
import org.cagrid.installer.index.IndexServiceComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.model.CaGridInstallerModelImpl;
import org.cagrid.installer.myservice.MyServiceComponentInstaller;
import org.cagrid.installer.portal.PortalComponentInstaller;
import org.cagrid.installer.steps.CheckSecureContainerStep;
import org.cagrid.installer.steps.ConfigureCAStep;
import org.cagrid.installer.steps.ConfigureServiceCertStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.InstallationCompleteStep;
import org.cagrid.installer.steps.PresentLicenseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.SelectComponentStep;
import org.cagrid.installer.steps.SelectInstallationTypeStep;
import org.cagrid.installer.steps.SpecifyTomcatPortsStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.syncgts.SyncGTSComponentInstaller;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.ConfigureGlobusTask;
import org.cagrid.installer.tasks.ConfigureTomcatTask;
import org.cagrid.installer.tasks.CopySelectedServicesToTempDirTask;
import org.cagrid.installer.tasks.DeployGlobusToTomcatTask;
import org.cagrid.installer.tasks.GenerateCATask;
import org.cagrid.installer.tasks.GenerateServiceCredsTask;
import org.cagrid.installer.tasks.SaveSettingsTask;
import org.cagrid.installer.transfer.TransferComponentInstaller;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.workflow.WorkflowComponentInstaller;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class Installer {

    private static final Log logger = LogFactory.getLog(Installer.class);

    private CaGridInstallerModelImpl model;

    private SplashScreen screen;

    private final int TOTAL_INIT_STEPS = 100;

    private int initProgress = 0;

    private List<CaGridComponentInstaller> componentInstallers = new ArrayList<CaGridComponentInstaller>();

    private List<DownloadedComponentInstaller> downloadedComponentInstallers = new ArrayList<DownloadedComponentInstaller>();


    public Installer() {

        downloadedComponentInstallers.add(new AntComponentInstaller());
        downloadedComponentInstallers.add(new TomcatComponentInstaller());
        downloadedComponentInstallers.add(new GlobusComponentInstaller());
        downloadedComponentInstallers.add(new ActiveBPELComponentInstaller());
        downloadedComponentInstallers.add(new CaGridSourceComponentInstaller());
        downloadedComponentInstallers.add(new PortalSourceComponentInstaller());

        componentInstallers.add(new MyServiceComponentInstaller());
        componentInstallers.add(new SyncGTSComponentInstaller());
        componentInstallers.add(new DorianComponentInstaller());
        componentInstallers.add(new CDSComponentInstaller());
        componentInstallers.add(new GMEComponentInstaller());
        componentInstallers.add(new EVSComponentInstaller());
        componentInstallers.add(new CaDSRComponentInstaller());
        componentInstallers.add(new FQPComponentInstaller());
        componentInstallers.add(new WorkflowComponentInstaller());
        componentInstallers.add(new TransferComponentInstaller());
        componentInstallers.add(new GTSComponentInstaller());
        componentInstallers.add(new AuthenticationServiceComponentInstaller());
        componentInstallers.add(new GridGrouperComponentInstaller());
        componentInstallers.add(new PortalComponentInstaller());
        componentInstallers.add(new IndexServiceComponentInstaller());
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

            // Set up temp dir
            String tempDir = InstallerUtils.getInstallerTempDir();
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

            // Load default properties
            Properties downloadedProps = getDownloadedProps(tempDir);
            incrementProgress();

            Enumeration e = downloadedProps.propertyNames();
            while (e.hasMoreElements()) {
                String propName = (String) e.nextElement();
                defaultState.put(propName, downloadedProps.getProperty(propName));
            }
            incrementProgress();

            // Check for presence of cagrid.installer.properties file
            String cagridInstallerFileName = System.getProperty(Constants.CAGRID_INSTALLER_PROPERTIES);
            if (cagridInstallerFileName != null) {
                logger.info("Custom installer properties file specified: '" + cagridInstallerFileName + "'");
            } else {
                cagridInstallerFileName = InstallerUtils.getInstallerDir() + "/"
                    + Constants.CAGRID_INSTALLER_PROPERTIES;
                logger.info("Using default properties file: '" + cagridInstallerFileName + "'");
            }
            defaultState.put(Constants.CAGRID_INSTALLER_PROPERTIES, cagridInstallerFileName);
            File cagridInstallerFile = new File(cagridInstallerFileName);

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

            assertCorrectJavaVersion(defaultState);

            initSteps(defaultState);

            while (this.initProgress < TOTAL_INIT_STEPS) {
                incrementProgress();
                try {
                    Thread.sleep(50);
                } catch (Exception ex) {

                }
            }

        } catch (Exception ex) {
            handleException("Error initializing: " + ex.getMessage(), ex);
        } finally {
            splashScreenDestruct();
        }
    }


    private void assertCorrectJavaVersion(Map<String, String> defaultState) throws Exception {
        String versionPattern = defaultState.get(Constants.JAVA_VERSION_PATTERN);
        if (versionPattern == null) {
            throw new Exception("Couldn't find version pattern property.");
        }
        String home = InstallerUtils.getJavaHomePath();
        String version = InstallerUtils.getJavaVersion();
        logger.info("At '" + home + "', found Java version: " + version);
        if (!version.matches(versionPattern)) {
            throw new Exception("The version of Java found at '" + home + "' is not correct. Found '" + version
                + "'. Expected version to match '" + versionPattern + "'.\n"
                + "Set the JAVA_HOME environment variable to"
                + " point to where you have installed the correct version of" + " Java before running the installer.");
        }
    }


    private Properties getDownloadedProps(String tempDir) {

        Properties defaultProps = null;

        InputStream propsIn = null;
        String downloadPropsFileName = System.getProperty("download.properties");
        if (downloadPropsFileName != null) {
            try {
                propsIn = new FileInputStream(downloadPropsFileName);
            } catch (Exception ex) {
                handleException("Couldn't load download.properties file '" + downloadPropsFileName + "': "
                    + ex.getMessage(), ex);
            }
        } else {
            try {
                propsIn = Thread.currentThread().getContextClassLoader().getResourceAsStream("download.properties");
            } catch (Exception ex) {
                handleException("Error loading default download.properties resource: " + ex.getMessage(), ex);
            }
            if (propsIn == null) {
                handleException("Couldn't find download.properties resource.", null);
            }
        }
        String downloadUrl = null;

        Properties downloadProps = new Properties();
        try {
            downloadProps.load(propsIn);
            downloadUrl = downloadProps.getProperty(Constants.DOWNLOAD_URL);
        } catch (Exception ex) {
            handleException("Error loading download.properties", ex);
        }
        logger.info("Downloading default properties from: " + downloadUrl);

        String toFile = tempDir + "/downloaded.properties";
        DownloadPropsThread dpt = new DownloadPropsThread(downloadUrl, toFile);
        dpt.start();
        try {
            dpt.join(Constants.CONNECT_TIMEOUT);
        } catch (InterruptedException ex) {
            handleException("Download thread interrupted", ex);
        }

        if (dpt.getException() != null) {
            Exception ex = dpt.getException();
            String msg = "Error loading default properties: " + ex.getMessage();
            handleException(msg, ex);
        }

        if (!dpt.isFinished()) {
            String msg = "Download of default properties timed out.";
            handleException(msg, new Exception(msg));
        }
        try {
            defaultProps = new Properties();
            defaultProps.load(new FileInputStream(toFile));
        } catch (Exception ex) {
            handleException("Error loading default properties: " + ex.getMessage(), ex);
        }

        return defaultProps;
    }


    private void incrementProgress() {
        screen.setProgress("Initializing installer...", ++this.initProgress);
    }


    private void handleException(String msg, Exception ex) {

        String htmlMsg = "";
        if (!InstallerUtils.isEmpty(msg)) {
            htmlMsg = "<html><body>" + msg.replaceAll("\n", "<br>") + "</body></html>";
        }

        JLabel msgLabel = new JLabel(htmlMsg);

        JOptionPane.showMessageDialog(null, msgLabel, "Error", JOptionPane.ERROR_MESSAGE);
        if (ex != null) {
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        } else {
            logger.error(msg);
            throw new RuntimeException(msg);
        }
    }


    private void initSteps(Map<String, String> defaultState) {

        // TODO: provide some factory method here
        this.model = new CaGridInstallerModelImpl(defaultState);

        clearFlags();

        // Initialize steps
        PresentLicenseStep licenseStep = new PresentLicenseStep(this.model.getMessage("accept.license.title"),
            this.model.getMessage("accept.license.desc"));
        this.model.add(licenseStep);

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
        selectInstallStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_SERVICES, this.model
                .getMessage("select.install.install.services"), true, true));

        selectInstallStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_PORTAL, this.model
                .getMessage("select.install.install.portal"), false, true));

        this.model.add(selectInstallStep);

        incrementProgress();

        incrementProgress();

        // Presents list of services that can be installed
        SelectComponentStep selectServicesStep = new SelectComponentStep(
            this.model.getMessage("select.services.title"), this.model.getMessage("select.services.desc"));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_MY_SERVICE, "My Introduce Service", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_DORIAN, "Dorian", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_CDS, "Credential Delegation Service (CDS)", false,
                true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_GTS, "GTS", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_SYNC_GTS, "SyncGTS", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_AUTHN_SVC, "AuthenticationService", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_GRID_GROUPER, "GridGrouper", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_INDEX_SVC, "Index Service", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_GME, "GME", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_EVS, "EVS", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_CADSR, "caDSR", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_FQP, "FQP", false, true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_TRANSFER, "Transfer Service (Tomcat Only)", false,
                true));
        selectServicesStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.INSTALL_WORKFLOW, "Workflow", false, true));

        this.model.add(selectServicesStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTrue(Constants.INSTALL_SERVICES);
            }

        });
        incrementProgress();

        // Select which container to use (Tomcat or Globus)
        // these next twho steps are mutually exclusive. ony one of the other
        // will run
        // if someone selects a service which can only run in the tomcat
        // container than
        // this will enable them to make sure the drop down for container
        // selection is
        // set to only the options that will work, in this case, only tomcat.
        // as support for more container becomes available this should be
        // refacted to be
        // easier managed.
        PropertyConfigurationStep selectAllContainerStep = new PropertyConfigurationStep(this.model
            .getMessage("select.container.title"), this.model.getMessage("select.container.desc"));
        selectAllContainerStep.getOptions().add(
            new ListPropertyConfigurationOption(Constants.CONTAINER_TYPE, this.model.getMessage("container.type"),
                new String[]{this.model.getMessage("container.type.tomcat"),
                        this.model.getMessage("container.type.globus")}, true));
        PropertyConfigurationStep selectTomcatOnlyContainerStep = new PropertyConfigurationStep(this.model
            .getMessage("select.container.title"), this.model.getMessage("select.container.desc"));
        selectTomcatOnlyContainerStep.getOptions().add(
            new ListPropertyConfigurationOption(Constants.CONTAINER_TYPE, this.model.getMessage("container.type"),
                new String[]{this.model.getMessage("container.type.tomcat"),}, true));

        this.model.add(selectAllContainerStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isConfigureContainerSelected()
                    && !(model.isTrue(Constants.INSTALL_PORTAL) || model.isTrue(Constants.INSTALL_TRANSFER));

            }

        });

        this.model.add(selectTomcatOnlyContainerStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isConfigureContainerSelected()
                    && (model.isTrue(Constants.INSTALL_PORTAL) || model.isTrue(Constants.INSTALL_TRANSFER));
            }

        });

        incrementProgress();

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
                    return model.isTrue(Constants.INSTALL_SERVICES) || model.isTrue(Constants.INSTALL_PORTAL);
                }
            }));

        installDependenciesStep.getTasks()
            .add(new SaveSettingsTask(this.model.getMessage("saving.settings.title"), ""));

        this.model.add(installDependenciesStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return installDependenciesStep.getTasksCount(model) > 0;
            }
        });

        addConfigureContainerSteps();
        incrementProgress();

        addGenerateCredentialStep();
        incrementProgress();

        final RunTasksStep installStep = new RunTasksStep(this.model.getMessage("install.title"), this.model
            .getMessage("install.desc"));

        installStep.getTasks().add(
            new ConditionalTask(new DeployGlobusToTomcatTask(this.model.getMessage("deploying.globus.title"), ""),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isDeployGlobusRequired() && model.isConfigureContainerSelected();

                    }

                }));

        installStep.getTasks().add(
            new ConditionalTask(new ConfigureGlobusTask(this.model.getMessage("configuring.globus.title"), ""),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isConfigureGlobusRequired() && model.isConfigureContainerSelected();

                    }

                }));

        installStep.getTasks().add(
            new ConditionalTask(new ConfigureTomcatTask(this.model.getMessage("configuring.tomcat.title"), ""),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isTomcatConfigurationRequired() && model.isConfigureContainerSelected();
                    }

                }));

        for (CaGridComponentInstaller installer : getComponentInstallers()) {
            installer.addSteps(this.model);
            installer.addInstallTasks(this.model, installStep);
            incrementProgress();
        }

        installStep.getTasks().add(
            new ConditionalTask(new SaveSettingsTask(this.model.getMessage("saving.settings.title"), ""),
                new Condition() {
                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isConfigureContainerSelected();
                    }
                }));

        this.model.add(installStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return installStep.getTasksCount(model) > 0;
            }
        });
        incrementProgress();

        this.model.add(new InstallationCompleteStep(this.model.getMessage("installation.complete.title"), ""));

    }


    private void addGenerateCredentialStep() {
        // Generate credentials, if necessary
        final RunTasksStep generateCredsStep = new RunTasksStep(this.model.getMessage("generate.credentials.title"),
            this.model.getMessage("generate.credentials.desc"));

        generateCredsStep.getTasks().add(
            new ConditionalTask(new GenerateCATask(this.model.getMessage("generating.ca.cert.title"), ""),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isCAGenerationRequired();

                    }
                }));
        generateCredsStep.getTasks().add(
            new ConditionalTask(
                new GenerateServiceCredsTask(this.model.getMessage("generating.service.cert.title"), ""),
                new Condition() {

                    public boolean evaluate(WizardModel m) {
                        CaGridInstallerModel model = (CaGridInstallerModel) m;
                        return model.isServiceCertGenerationRequired();

                    }
                }));
        this.model.add(generateCredsStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return generateCredsStep.getTasksCount(model) > 0 && model.isConfigureContainerSelected();
            }
        });
    }


    private void addConfigureContainerSteps() {

        PropertyConfigurationStep checkDeployGlobusStep = new PropertyConfigurationStep(this.model
            .getMessage("globus.check.redeploy.title"), this.model.getMessage("globus.check.redeploy.desc"));
        checkDeployGlobusStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.REDEPLOY_GLOBUS, this.model.getMessage("yes"), false,
                false));
        this.model.add(checkDeployGlobusStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTomcatContainer() && model.isGlobusDeployed() && model.isConfigureContainerSelected();
            }

        });

        // Checks if secure container should be used
        CheckSecureContainerStep checkDeployGlobusSecureStep = new CheckSecureContainerStep(this.model
            .getMessage("globus.check.secure.title"), this.model.getMessage("globus.check.secure.desc"));
        checkDeployGlobusSecureStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.USE_SECURE_CONTAINER, this.model.getMessage("yes"), false,
                false));
        this.model.add(checkDeployGlobusSecureStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return !model.isTrue(Constants.USE_SECURE_CONTAINER) && model.isConfigureContainerSelected();
            }

        });

        PropertyConfigurationStep getHostnameStep = new PropertyConfigurationStep(this.model
            .getMessage("specify.service.hostname.title"), this.model.getMessage("specify.service.hostname.desc"));
        getHostnameStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.SERVICE_HOSTNAME, this.model.getMessage("service.hostname"),
                this.model.getProperty(Constants.SERVICE_HOSTNAME, "localhost"), true));
        this.model.add(getHostnameStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return (model.isDeployGlobusRequired() || model.isConfigureGlobusRequired())
                    && model.isConfigureContainerSelected();
            }

        });

        // Allows user to specify Tomcat ports
        SpecifyTomcatPortsStep tomcatPortsStep = new SpecifyTomcatPortsStep(this.model
            .getMessage("tomcat.specify.ports.title"), this.model.getMessage("tomcat.specify.ports.desc"));
        tomcatPortsStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.TOMCAT_SHUTDOWN_PORT, this.model
                .getMessage("tomcat.shutdown.port"), this.model.getProperty(Constants.TOMCAT_SHUTDOWN_PORT, "8005"),
                true));
        tomcatPortsStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.TOMCAT_HTTP_PORT, this.model.getMessage("tomcat.http.port"),
                this.model.getProperty(Constants.TOMCAT_HTTP_PORT, "8080"), true));
        tomcatPortsStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.TOMCAT_HTTPS_PORT,
                this.model.getMessage("tomcat.https.port"),
                this.model.getProperty(Constants.TOMCAT_HTTPS_PORT, "8443"), true));
        // TODO: add validation
        this.model.add(tomcatPortsStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isTomcatConfigurationRequired() && model.isConfigureContainerSelected();
            }
        });

        // Checks if service cert is present
        PropertyConfigurationStep checkServiceCertPresentStep = new PropertyConfigurationStep(this.model
            .getMessage("svc.cert.check.present.title"), this.model.getMessage("svc.cert.check.present.desc"));
        checkServiceCertPresentStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.SERVICE_CERT_PRESENT, this.model.getMessage("yes"), false,
                false));
        this.model.add(checkServiceCertPresentStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isSecurityConfigurationRequired() && !model.isTrue(Constants.INSTALL_DORIAN)
                    && model.isConfigureContainerSelected();
            }
        });

        // Checks if CA cert is present
        PropertyConfigurationStep checkCAPresentStep = new PropertyConfigurationStep(this.model
            .getMessage("ca.cert.check.present.title"), this.model.getMessage("ca.cert.check.present.desc"));
        checkCAPresentStep.getOptions().add(
            new BooleanPropertyConfigurationOption(Constants.CA_CERT_PRESENT, this.model.getMessage("yes"), false,
                false));
        this.model.add(checkCAPresentStep, new Condition() {
            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isSecurityConfigurationRequired() && !model.isTrue(Constants.INSTALL_DORIAN)
                    && !model.isTrue(Constants.SERVICE_CERT_PRESENT) && model.isConfigureContainerSelected();
            }
        });

        // Allows user to enter existing CA cert info
        ConfigureCAStep caCertInfoStep = new ConfigureCAStep(this.model.getMessage("ca.cert.info.title"), this.model
            .getMessage("ca.cert.info.desc"));
        InstallerUtils.addCommonCACertFields(this.model, caCertInfoStep, Constants.CA_CERT_PATH, Constants.CA_KEY_PATH,
            Constants.CA_KEY_PWD, true);
        this.model.add(caCertInfoStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isSecurityConfigurationRequired() && !model.isTrue(Constants.INSTALL_DORIAN)
                    && !model.isTrue(Constants.SERVICE_CERT_PRESENT) && model.isTrue(Constants.CA_CERT_PRESENT)
                    && model.isConfigureContainerSelected();
            }
        });

        // Allows user to enter info necessary to gen new CA cert
        ConfigureCAStep newCaCertInfoStep = new ConfigureCAStep(this.model.getMessage("ca.cert.new.info.title"),
            this.model.getMessage("ca.cert.new.info.desc"));
        InstallerUtils.addCommonNewCACertFields(this.model, newCaCertInfoStep, Constants.CA_CERT_PATH,
            Constants.CA_KEY_PATH, Constants.CA_KEY_PWD, Constants.CA_DN, Constants.CA_DAYS_VALID);
        this.model.add(newCaCertInfoStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isSecurityConfigurationRequired() && !model.isTrue(Constants.INSTALL_DORIAN)
                    && !model.isTrue(Constants.SERVICE_CERT_PRESENT) && !model.isTrue(Constants.CA_CERT_PRESENT)
                    && model.isConfigureContainerSelected();
            }
        });

        // Allows user to enter info necessary to gen new service cert
        ConfigureServiceCertStep newServiceCertInfoStep = new ConfigureServiceCertStep(this.model
            .getMessage("service.cert.new.info.title"), this.model.getMessage("service.cert.new.info.desc"));
        FilePropertyConfigurationOption nscPathOption = new FilePropertyConfigurationOption(
            Constants.SERVICE_CERT_PATH, this.model.getMessage("service.cert.info.cert.path"), this.model.getProperty(
                Constants.SERVICE_CERT_PATH, InstallerUtils.getInstallerDir() + "/certs/service.cert"), true);
        nscPathOption.setDirectoriesOnly(false);
        nscPathOption.setBrowseLabel(this.model.getMessage("browse"));
        newServiceCertInfoStep.getOptions().add(nscPathOption);
        FilePropertyConfigurationOption nskPathOption = new FilePropertyConfigurationOption(Constants.SERVICE_KEY_PATH,
            this.model.getMessage("service.cert.info.key.path"), this.model.getProperty(Constants.SERVICE_KEY_PATH,
                InstallerUtils.getInstallerDir() + "/certs/service.key"), true);
        nskPathOption.setDirectoriesOnly(false);
        nskPathOption.setBrowseLabel(this.model.getMessage("browse"));
        newServiceCertInfoStep.getOptions().add(nskPathOption);
        newServiceCertInfoStep.getOptions().add(
            new TextPropertyConfigurationOption(Constants.SERVICE_HOSTNAME, this.model
                .getMessage("service.cert.info.hostname"), this.model.getProperty(Constants.SERVICE_HOSTNAME,
                "localhost"), true));
        // TODO: add validation
        this.model.add(newServiceCertInfoStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isSecurityConfigurationRequired() && !model.isTrue(Constants.INSTALL_DORIAN)
                    && !model.isTrue(Constants.SERVICE_CERT_PRESENT) && model.isConfigureContainerSelected();
            }
        });

        ConfigureServiceCertStep serviceCertInfoStep = new ConfigureServiceCertStep(this.model
            .getMessage("service.cert.info.title"), this.model.getMessage("service.cert.info.desc"));
        InstallerUtils.addCommonCertFields(this.model, serviceCertInfoStep, Constants.SERVICE_CERT_PATH,
            Constants.SERVICE_KEY_PATH, null);
        this.model.add(serviceCertInfoStep, new Condition() {

            public boolean evaluate(WizardModel m) {
                CaGridInstallerModel model = (CaGridInstallerModel) m;
                return model.isSecurityConfigurationRequired() && !model.isTrue(Constants.INSTALL_DORIAN)
                    && model.isTrue(Constants.SERVICE_CERT_PRESENT) && model.isConfigureContainerSelected();
            }
        });

    }


    private void clearFlags() {
        this.model.unsetProperty(Constants.CONFIGURE_CONTAINER);
        this.model.unsetProperty(Constants.DORIAN_USE_GEN_CA);
        this.model.unsetProperty(Constants.DORIAN_CA_PRESENT);
        this.model.unsetProperty(Constants.INSTALL_ACTIVEBPEL);
        this.model.unsetProperty(Constants.INSTALL_AUTHN_SVC);
        this.model.unsetProperty(Constants.INSTALL_CADSR);
        this.model.unsetProperty(Constants.INSTALL_DORIAN);
        this.model.unsetProperty(Constants.INSTALL_CDS);
        this.model.unsetProperty(Constants.INSTALL_EVS);
        this.model.unsetProperty(Constants.INSTALL_FQP);
        this.model.unsetProperty(Constants.INSTALL_GME);
        this.model.unsetProperty(Constants.INSTALL_GRID_GROUPER);
        this.model.unsetProperty(Constants.INSTALL_GTS);
        this.model.unsetProperty(Constants.INSTALL_INDEX_SVC);
        this.model.unsetProperty(Constants.INSTALL_MY_SERVICE);
        this.model.unsetProperty(Constants.INSTALL_PORTAL);
        this.model.unsetProperty(Constants.INSTALL_TRANSFER);
        this.model.unsetProperty(Constants.INSTALL_SYNC_GTS);
        this.model.unsetProperty(Constants.INSTALL_SERVICES);
        this.model.unsetProperty(Constants.INSTALL_WORKFLOW);
        this.model.unsetProperty(Constants.RECONFIGURE_CAGRID);
        this.model.unsetProperty(Constants.USE_SECURE_CONTAINER);
    }


    public void run() {
        ImageIcon myImage = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(
            "images/caGrid_small.png"));
        Wizard wizard = new Wizard(this.model);
        wizard.showInFrame("caGrid Installation Wizard", myImage.getImage());
    }


    private class DownloadPropsThread extends Thread {

        private Exception ex;

        private String fromUrl;

        private String toFile;

        private boolean finished;


        DownloadPropsThread(String fromUrl, String toFile) {
            this.fromUrl = fromUrl;
            this.toFile = toFile;
            this.setDaemon(true);
        }


        Exception getException() {
            return this.ex;
        }


        public void run() {
            try {
                File to = new File(this.toFile);
                URL from = new URL(this.fromUrl);

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(to));
                InputStream in = from.openStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                in.close();

                this.finished = true;
            } catch (Exception ex) {
                this.ex = ex;
            }
        }


        public boolean isFinished() {
            return this.finished;
        }

    }


    public List<CaGridComponentInstaller> getComponentInstallers() {
        return componentInstallers;
    }


    public void setComponentInstallers(List<CaGridComponentInstaller> componentInstallers) {
        this.componentInstallers = componentInstallers;
    }


    public List<DownloadedComponentInstaller> getDownloadedComponentInstallers() {
        return downloadedComponentInstallers;
    }


    public void setDownloadedComponentInstallers(List<DownloadedComponentInstaller> externalComponentInstallers) {
        this.downloadedComponentInstallers = externalComponentInstallers;
    }

}
