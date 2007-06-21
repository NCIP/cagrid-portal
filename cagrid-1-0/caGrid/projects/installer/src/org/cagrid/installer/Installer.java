/**
 * 
 */
package org.cagrid.installer;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.model.DynamicStatefulWizardModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DisplayMessageStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.SelectInstallationTypeStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.tasks.SaveSettingsTask;
import org.cagrid.installer.tasks.UnzipInstallTask;
import org.cagrid.installer.validator.CreateFilePermissionValidator;
import org.pietschy.wizard.Wizard;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class Installer {

	private static final Log logger = LogFactory.getLog(Installer.class);

	private DynamicStatefulWizardModel model;

	public Installer() {

	}

	public void initialize() {
		
		Map defaultState = new HashMap();
		
		//Load default properties
		logger.info("Loading default properties");
		try{
			Properties props = new Properties();
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("cagrid.installer.properties"));
			defaultState.putAll(props);
		}catch(Exception ex){
			String msg = "Error loading default properties: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}

		// Check for presence of .cagrid.installer file
		String cagridInstallerFileName = System
				.getProperty(Constants.CAGRID_INSTALLER_PROPERTIES);
		if (cagridInstallerFileName != null) {
			logger.info("Custom installer properties file specified: '"
					+ cagridInstallerFileName + "'");
		} else {
			cagridInstallerFileName = System.getProperty("user.home") + "/"
					+ Constants.CAGRID_INSTALLER_PROPERTIES;
			logger.info("Using default properties file: '"
					+ cagridInstallerFileName + "'");
		}
		defaultState.put(Constants.CAGRID_INSTALLER_PROPERTIES, cagridInstallerFileName);
		File cagridInstallerFile = new File(cagridInstallerFileName);

		// If .cagrid.installer found, load properties.
		logger.info("Looking for '" + cagridInstallerFileName + "'");
		if (cagridInstallerFile.exists()) {
			try {
				logger.info("Loading '" + cagridInstallerFileName + "'");
				Properties props = new Properties();
				props.load(new FileInputStream(
						cagridInstallerFile));
				defaultState.putAll(props);
			} catch (Exception ex) {
				String msg = "Could not load '" + cagridInstallerFileName
						+ "': " + ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg, ex);
			}
		} else {
			logger.info("Did not find '" + cagridInstallerFileName + "'");
		}

		this.model = new DynamicStatefulWizardModel(defaultState);

		// Check for the presence of Ant
		checkInstalled("Ant", "ANT_HOME", Constants.ANT_HOME,
				Constants.ANT_INSTALLED, this.model.getState());
		// TODO: check Ant version

		// Check for presence of Tomcat
		checkInstalled("Tomcat", "CATALINA_HOME", Constants.TOMCAT_HOME,
				Constants.TOMCAT_INSTALLED, this.model.getState());
		// TODO: check Tomcat version

		// Check for presence of Globus
		checkInstalled("Globus", "GLOBUS_LOCATION", Constants.GLOBUS_HOME,
				Constants.GLOBUS_INSTALLED, this.model.getState());
		// TODO: check Globus version

		// Check for presence of caGrid
		checkInstalled("caGrid", null, Constants.CAGRID_HOME,
				Constants.CAGRID_INSTALLED, this.model.getState());
		// TODO: check caGrid version

		// Set up temp dir
		String tempDir = (String) this.model.getState().get(
				Constants.TEMP_DIR_PATH);
		if (tempDir == null) {
			tempDir = System.getProperty("user.home") + "/cagrid-installer-"
					+ Math.random();
			this.model.getState().put(Constants.TEMP_DIR_PATH, tempDir);
		}
		File tempDirFile = new File(tempDir);
		if (!tempDirFile.exists()) {
			logger.info("Creating temporary directory '" + tempDir + "'");
			try {
				tempDirFile.mkdirs();
			} catch (Exception ex) {
				String msg = "Error creating temporary directory '" + tempDir
						+ "': " + ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg, ex);
			}
		}

		// Initialize steps

		// Gives user choice to install caGrid, or one or more services, or
		// both.
		SelectInstallationTypeStep selectInstallStep = new SelectInstallationTypeStep(
				this.model.getMessage("select.install.title"), this.model
						.getMessage("select.install.desc"));
		selectInstallStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.INSTALL_CAGRID, this.model
								.getMessage("select.install.install.cagrid"),
						true, true));
		selectInstallStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.INSTALL_SERVICES, this.model
								.getMessage("select.install.install.services"),
						true, true));
		model.add(selectInstallStep);

		// Asks user if Ant should be installed.
		addCheckInstallStep(model, "ant.check.reinstall.title",
				"ant.check.reinstall.desc", Constants.INSTALL_ANT,
				Constants.ANT_INSTALLED);

		// Allows user to specify where Ant should be installed
		addInstallInfoStep(model, Constants.ANT_HOME, "ant",
				"ant.home.title", "ant.home.desc",
				Constants.ANT_INSTALL_DIR_PATH, Constants.INSTALL_ANT);

		// Asks user if Tomcat should be installed
		addCheckInstallStep(model, "tomcat.check.reinstall.title",
				"tomcat.check.reinstall.desc", Constants.INSTALL_TOMCAT,
				Constants.TOMCAT_INSTALLED);

		// Allows user to specify where Tomcat should be installed
		addInstallInfoStep(model, Constants.TOMCAT_HOME, "tomcat",
				"tomcat.home.title", "tomcat.home.desc",
				Constants.TOMCAT_INSTALL_DIR_PATH, Constants.INSTALL_TOMCAT);

		// Asks user if Globus should be installed
		addCheckInstallStep(model, "globus.check.reinstall.title",
				"globus.check.reinstall.desc", Constants.INSTALL_GLOBUS,
				Constants.GLOBUS_INSTALLED);

		// Allows user to specify where Globus should be installed
		addInstallInfoStep(model, Constants.GLOBUS_HOME, "globus",
				"globus.home.title", "globus.home.desc",
				Constants.GLOBUS_INSTALL_DIR_PATH, Constants.INSTALL_GLOBUS);

		// Asks user if caGrid should be installed
		addCheckInstallStep(model, "cagrid.check.reinstall.title",
				"cagrid.check.reinstall.desc", Constants.INSTALL_CAGRID,
				Constants.CAGRID_INSTALLED);

		// Allows user to specify where caGrid should be installed
		addInstallInfoStep(model, Constants.CAGRID_HOME, "cagrid",
				"cagrid.home.title", "cagrid.home.desc",
				Constants.CAGRID_INSTALL_DIR_PATH, Constants.INSTALL_CAGRID);
		
		

		// Performs the installation
		RunTasksStep installStep = new RunTasksStep(this.model
				.getMessage("install.title"), this.model
				.getMessage("install.desc"));
		addUnzipInstallTask(installStep, "Installing Ant", "",
				Constants.ANT_DOWNLOAD_URL, Constants.ANT_TEMP_FILE_NAME,
				Constants.ANT_INSTALL_DIR_PATH, Constants.ANT_DIR_NAME,
				Constants.ANT_HOME, Constants.INSTALL_ANT);
		addUnzipInstallTask(installStep, "Installing Tomcat", "",
				Constants.TOMCAT_DOWNLOAD_URL, Constants.TOMCAT_TEMP_FILE_NAME,
				Constants.TOMCAT_INSTALL_DIR_PATH, Constants.TOMCAT_DIR_NAME,
				Constants.TOMCAT_HOME, Constants.INSTALL_TOMCAT);
		addUnzipInstallTask(installStep, "Installing Globus", "",
				Constants.GLOBUS_DOWNLOAD_URL, Constants.GLOBUS_TEMP_FILE_NAME,
				Constants.GLOBUS_INSTALL_DIR_PATH, Constants.GLOBUS_DIR_NAME,
				Constants.GLOBUS_HOME, Constants.INSTALL_GLOBUS);
		addUnzipInstallTask(installStep, "Installing caGrid", "",
				Constants.CAGRID_DOWNLOAD_URL, Constants.CAGRID_TEMP_FILE_NAME,
				Constants.CAGRID_INSTALL_DIR_PATH, Constants.CAGRID_DIR_NAME,
				Constants.CAGRID_HOME, Constants.INSTALL_CAGRID);
		
		
		installStep.getTasks().add(new SaveSettingsTask("Saving Settings", ""));
		
		
		model.add(installStep);

		model.add(new DisplayMessageStep("Finished", "Finished",
				"caGrid has been installed."));

	}

	private void addUnzipInstallTask(RunTasksStep installStep, String name,
			String desc, String downloadUrlProp, String tempFileNameProp,
			String installDirPathProp, String dirNameProp, String homeProp,
			final String installProp) {
		installStep.getTasks().add(
				new ConditionalTask(new UnzipInstallTask(name, desc,
						downloadUrlProp, tempFileNameProp, installDirPathProp,
						dirNameProp, homeProp), new Condition() {

					public boolean evaluate(WizardModel m) {
						CaGridInstallerModel model = (CaGridInstallerModel) m;
						return "true".equals(model.getState().get(installProp));
					}

				}));
	}

	private void addInstallInfoStep(DynamicStatefulWizardModel m,
			String homeProp, String defaultDirName, String titleProp,
			String descProp, String installDirPath, final String installProp) {

		File homeFile = null;
		String home = (String) m.getState().get(homeProp);
		if (home != null) {
			homeFile = new File(home);
		} else {
			homeFile = new File(System.getProperty("user.home")
					+ File.separator + "packages" + File.separator
					+ defaultDirName);
		}
		PropertyConfigurationStep installInfoStep = new PropertyConfigurationStep(
				m.getMessage(titleProp), m.getMessage(descProp));
		installInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(installDirPath, m
						.getMessage("directory"), homeFile.getParentFile()
						.getAbsolutePath(), true));
		installInfoStep.getValidators().add(
				new CreateFilePermissionValidator(homeProp, m
						.getMessage("error.permission.directory.create")));
		m.add(installInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return "true".equals(model.getState().get(installProp));
			}

		});
	}

	private void addCheckInstallStep(DynamicStatefulWizardModel m,
			String titleProp, String descProp, String configProp,
			final String installedProp) {

		PropertyConfigurationStep checkInstallStep = new PropertyConfigurationStep(
				m.getMessage(titleProp), m.getMessage(descProp));
		checkInstallStep.getOptions().add(
				new BooleanPropertyConfigurationOption(configProp, m
						.getMessage("yes"), false, false));
		m.add(checkInstallStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return "true".equals(model.getState().get(installedProp));
			}

		});
	}

	public void run() {
		Wizard wizard = new Wizard(this.model);
		wizard.showInFrame("caGrid Installation Wizard");
	}

	public static boolean checkInstalled(String progName, String envName,
			String propName, String propInstalledName, Map props) {
		boolean installed = false;
		logger.info("Checking if " + progName + " is installed");
		String home = (String)props.get(propName);
		if (home == null) {
			if (envName != null) {
				logger
						.info(progName
								+ " was not found in initial properties. Checking environment variable: "
								+ envName);
				home = System.getenv(envName);
			}
		} else {
			logger.info(progName + " found at '" + home + "'");
		}
		if (home != null) {
			File f = new File(home);
			if (!f.exists()) {
				logger.info(home + " does not exist");
				home = null;
			}
		}
		if (home != null) {
			logger.info(progName + " found at '" + home + "'");
			props.put(propName, home);
			props.put(propInstalledName, "true");
			installed = true;
		} else {
			logger.info(progName + " is not installed");
			props.put(propInstalledName, "false");
		}
		return installed;
	}

}
