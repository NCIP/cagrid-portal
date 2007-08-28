/**
 * 
 */
package org.cagrid.installer.dorian;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.DropServiceDatabaseStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.validator.DorianIdpInfoValidator;
import org.cagrid.installer.validator.KeyAccessValidator;
import org.cagrid.installer.validator.PathExistsValidator;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DorianComponentInstaller implements CaGridComponentInstaller {

	private static final int NUM_ERACOM_SLOTS = 16;
	
	
	
	/**
	 * 
	 */
	public DorianComponentInstaller() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addInstallTasks(org.cagrid.installer.steps.RunTasksStep)
	 */
	public void addInstallTasks(CaGridInstallerModel model, RunTasksStep installStep) {
		installStep.getTasks().add(
				new ConditionalTask(new ConfigureDorianTask(model
						.getMessage("configuring.dorian.title"), ""),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_DORIAN);
							}

						}));

		installStep.getTasks().add(
				new ConditionalTask(
						new DeployDorianTask(model
								.getMessage("installing.dorian.title"), "",
								model), new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model.isTrue(Constants.INSTALL_DORIAN);
							}

						}));
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		// TODO Auto-generated method stub
		ConfigureServiceMetadataStep editDorianSvcMetaStep = new ConfigureServiceMetadataStep(
				"",
				model.getMessage("dorian.edit.service.metadata.title"),
				model.getMessage("dorian.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/dorian/etc/serviceMetadata.xml";
			}
		};
		model.add(editDorianSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});

		// Allows user to edit Dorian deploy.properties
		DeployPropertiesFileEditorStep editDorianDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"dorian", model
						.getMessage("dorian.edit.deploy.properties.title"),
				model.getMessage("dorian.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"),
				model.getMessage("edit.properties.property.value"));
		model.add(editDorianDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});

		// Allows user to specify Dorian DB information.
		ConfigureDorianDBStep dorianDbInfoStep = new ConfigureDorianDBStep(
				model.getMessage("dorian.db.config.title"), model
						.getMessage("dorian.db.config.desc"));
		InstallerUtils.addDBConfigPropertyOptions(model, dorianDbInfoStep, "dorian.", "dorian");
		model.add(dorianDbInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});

		final DropServiceDatabaseStep dropDorianDbStep = new DropServiceDatabaseStep(
				model.getMessage("dorian.db.drop.title"), model
						.getMessage("dorian.db.drop.desc"), "dorian.",
				"dorian.db.drop");
		model.add(dropDorianDbStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN)
						&& dropDorianDbStep.databaseExists(model);
			}
		});

		// Dorian IdP config
		PropertyConfigurationStep dorianIdpInfoStep = new PropertyConfigurationStep(
				model.getMessage("dorian.idp.config.title"), model
						.getMessage("dorian.idp.config.desc"));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(Constants.DORIAN_IDP_NAME,
						model.getMessage("dorian.idp.name"), model
								.getProperty(Constants.DORIAN_IDP_NAME,
										"Dorian IdP"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_UID_MIN, model
								.getMessage("dorian.idp.uid.min"),
						model.getProperty(Constants.DORIAN_IDP_UID_MIN,
								"4"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_UID_MAX, model
								.getMessage("dorian.idp.uid.max"),
						model.getProperty(Constants.DORIAN_IDP_UID_MAX,
								"15"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_PWD_MIN, model
								.getMessage("dorian.idp.pwd.min"),
						model.getProperty(Constants.DORIAN_IDP_PWD_MIN,
								"10"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_PWD_MAX, model
								.getMessage("dorian.idp.pwd.max"),
						model.getProperty(Constants.DORIAN_IDP_PWD_MAX,
								"20"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_PWD_LOCKOUT_HOURS, model
								.getMessage(Constants.DORIAN_IDP_PWD_LOCKOUT_HOURS),
						model.getProperty(Constants.DORIAN_IDP_PWD_LOCKOUT_HOURS,
								"4"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_PWD_LOCKOUT_MINUTES, model
								.getMessage(Constants.DORIAN_IDP_PWD_LOCKOUT_MINUTES),
						model.getProperty(Constants.DORIAN_IDP_PWD_LOCKOUT_MINUTES,
								"0"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_PWD_LOCKOUT_SECONDS, model
								.getMessage(Constants.DORIAN_IDP_PWD_LOCKOUT_SECONDS),
						model.getProperty(Constants.DORIAN_IDP_PWD_LOCKOUT_SECONDS,
								"0"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_MAX_CONSEC_INVALID_LOGINS, model
								.getMessage(Constants.DORIAN_IDP_MAX_CONSEC_INVALID_LOGINS),
						model.getProperty(Constants.DORIAN_IDP_MAX_CONSEC_INVALID_LOGINS,
								"5"), true));
		dorianIdpInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IDP_MAX_TOTAL_INVALID_LOGINS, model
								.getMessage(Constants.DORIAN_IDP_MAX_TOTAL_INVALID_LOGINS),
						model.getProperty(Constants.DORIAN_IDP_MAX_TOTAL_INVALID_LOGINS,
								"500"), true));		
		
		dorianIdpInfoStep
				.getOptions()
				.add(
						new ListPropertyConfigurationOption(
								Constants.DORIAN_IDP_REGPOLICY,
								model.getMessage("dorian.idp.regpolicy"),
								new String[] {
										model
												.getMessage("dorian.idp.regpolicy.manual"),
										model
												.getMessage("dorian.idp.regpolicy.auto") },
								true));
		dorianIdpInfoStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.DORIAN_IDP_SAML_AUTORENEW, model
								.getMessage("dorian.idp.saml.autorenew"), true,
						false));
		dorianIdpInfoStep.getOptions().add(
				new PasswordPropertyConfigurationOption(
						Constants.DORIAN_IDP_SAML_KEYPWD, model
								.getMessage("dorian.idp.saml.keypwd"),
						model.getProperty(
								Constants.DORIAN_IDP_SAML_KEYPWD, "idpkey"),
						true));
		dorianIdpInfoStep.getValidators().add(
				new DorianIdpInfoValidator(model, model
						.getMessage("error.nan")));
		

		model.add(dorianIdpInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});

		// Dorian Ifs Config
		PropertyConfigurationStep dorianIfsInfoStep = new PropertyConfigurationStep(
				model.getMessage("dorian.ifs.config.title"), model
						.getMessage("dorian.ifs.config.desc"));
		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_IDPNAME_MIN, model
								.getMessage("dorian.ifs.idpname.min"),
						model.getProperty(
								Constants.DORIAN_IFS_IDPNAME_MIN, "3"), true));
		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_IDPNAME_MAX, model
								.getMessage("dorian.ifs.idpname.max"),
						model.getProperty(
								Constants.DORIAN_IFS_IDPNAME_MAX, "50"), true));
		dorianIfsInfoStep
				.getOptions()
				.add(
						new ListPropertyConfigurationOption(
								Constants.DORIAN_IFS_IDPOLICY,
								model.getMessage("dorian.ifs.idpolicy"),
								new String[] {
										model
												.getMessage("dorian.ifs.idpolicy.name"),
										model
												.getMessage("dorian.ifs.idpolicy.id") },
								true));
		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_CREDLIFETIME_YEARS, model
								.getMessage("dorian.ifs.credlifetime.years"),
						model.getProperty(
								Constants.DORIAN_IFS_CREDLIFETIME_YEARS, "1"),
						true));
		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_CREDLIFETIME_MONTHS, model
								.getMessage("dorian.ifs.credlifetime.months"),
						model.getProperty(
								Constants.DORIAN_IFS_CREDLIFETIME_MONTHS, "0"),
						true));
		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_CREDLIFETIME_DAYS, model
								.getMessage("dorian.ifs.credlifetime.days"),
						model.getProperty(
								Constants.DORIAN_IFS_CREDLIFETIME_DAYS, "0"),
						true));
		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_CREDLIFETIME_HOURS, model
								.getMessage("dorian.ifs.credlifetime.hours"),
						model.getProperty(
								Constants.DORIAN_IFS_CREDLIFETIME_HOURS, "0"),
						true));
		dorianIfsInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_IFS_CREDLIFETIME_MINUTES,
								model
										.getMessage("dorian.ifs.credlifetime.minutes"),
								model
										.getProperty(
												Constants.DORIAN_IFS_CREDLIFETIME_MINUTES,
												"0"), true));
		dorianIfsInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_IFS_CREDLIFETIME_SECONDS,
								model
										.getMessage("dorian.ifs.credlifetime.seconds"),
								model
										.getProperty(
												Constants.DORIAN_IFS_CREDLIFETIME_SECONDS,
												"0"), true));
		dorianIfsInfoStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.DORIAN_IFS_HOSTCERT_AUTOAPPROVE, model
								.getMessage("dorian.ifs.hostcert.autoapprove"),
						true, false));

		dorianIfsInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_IFS_PROXYLIFETIME_HOURS,
								model
										.getMessage("dorian.ifs.proxylifetime.hours"),
								model
										.getProperty(
												Constants.DORIAN_IFS_PROXYLIFETIME_HOURS,
												"12"), true));
		dorianIfsInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_IFS_PROXYLIFETIME_MINUTES,
								model
										.getMessage("dorian.ifs.proxylifetime.minutes"),
								model
										.getProperty(
												Constants.DORIAN_IFS_PROXYLIFETIME_MINUTES,
												"0"), true));
		dorianIfsInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_IFS_PROXYLIFETIME_SECONDS,
								model
										.getMessage("dorian.ifs.proxylifetime.seconds"),
								model
										.getProperty(
												Constants.DORIAN_IFS_PROXYLIFETIME_SECONDS,
												"0"), true));

		dorianIfsInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_IFS_GTS_URL, model
								.getMessage("dorian.ifs.gts.url"), model
								.getProperty(Constants.DORIAN_IFS_GTS_URL, ""),
						false));
		// TODO: figure out why this hangs
		// dorianIfsInfoStep.getValidators().add(
		// new DorianIfsInfoValidator(model, model
		// .getMessage("error.nan"), model
		// .getMessage("error.bad.url")));
		model.add(dorianIfsInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});

		// Set the Dorian CA type
		PropertyConfigurationStep selectDorianCATypeStep = new PropertyConfigurationStep(
				model.getMessage("dorian.ca.type.title"), model
						.getMessage("dorian.ca.type.desc"));
		selectDorianCATypeStep
				.getOptions()
				.add(
						new ListPropertyConfigurationOption(
								Constants.DORIAN_CA_TYPE,
								model.getMessage("dorian.ca.type"),
								new String[] { Constants.DORIAN_CA_TYPE_DBCA,
										Constants.DORIAN_CA_TYPE_ERACOM,
										Constants.DORIAN_CA_TYPE_ERACOM_HYBRID },
								true));
		model.add(selectDorianCATypeStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});

		// Checks if user will supply Dorian CA
		PropertyConfigurationStep checkDorianCAPresentStep = new PropertyConfigurationStep(
				model.getMessage("dorian.check.ca.present.title"),
				model.getMessage("dorian.check.ca.present.desc"));
		checkDorianCAPresentStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.DORIAN_CA_PRESENT, model
								.getMessage("yes"), false, false));
		model.add(checkDorianCAPresentStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;

				return model.isTrue(Constants.INSTALL_DORIAN)
						&& model.isEqual(Constants.DORIAN_CA_TYPE_DBCA,
								Constants.DORIAN_CA_TYPE);
			}

		});

		// Configure existing Dorian CA
		ConfigureDorianCAStep dorianCaCertInfoStep = new ConfigureDorianCAStep(
				model.getMessage("dorian.ca.cert.info.title"), model
						.getMessage("dorian.ca.cert.info.desc"));
		dorianCaCertInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_CA_CERT_PATH, model
								.getMessage("dorian.ca.cert.info.cert.path"),
						model.getProperty(Constants.DORIAN_CA_CERT_PATH),
						true));
		dorianCaCertInfoStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.DORIAN_CA_KEY_PATH, model
								.getMessage("dorian.ca.cert.info.key.path"),
						model.getProperty(Constants.DORIAN_CA_KEY_PATH),
						true));
		InstallerUtils.addCommonDorianCAConfigFields(model, dorianCaCertInfoStep);
		dorianCaCertInfoStep.getValidators().add(
				new PathExistsValidator(Constants.DORIAN_CA_CERT_PATH,
						model.getMessage("error.cert.file.not.found")));
		dorianCaCertInfoStep.getValidators().add(
				new PathExistsValidator(Constants.DORIAN_CA_KEY_PATH,
						model.getMessage("error.key.file.not.found")));
		dorianCaCertInfoStep.getValidators().add(
				new KeyAccessValidator(Constants.DORIAN_CA_KEY_PATH,
						Constants.DORIAN_CA_KEY_PWD, model
								.getMessage("error.key.no.access")));
		model.add(dorianCaCertInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN)
						&& model.isTrue(Constants.DORIAN_CA_PRESENT);
			}
		});

		// Configure new Dorian CA
		ConfigureNewDorianCAStep dorianCaNewCertInfoStep = new ConfigureNewDorianCAStep(
				model.getMessage("dorian.ca.new.cert.info.title"),
				model.getMessage("dorian.ca.new.cert.info.desc"));

		InstallerUtils.addCommonDorianCAConfigFields(model, dorianCaNewCertInfoStep);

		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_SUBJECT,
								model
										.getMessage("dorian.ca.cert.info.subject"),
								model
										.getProperty(
												Constants.DORIAN_CA_SUBJECT,
												"C=US,O=abc,OU=xyz,OU=caGrid,OU=Users,CN=caGrid Dorian CA"),
								true));

		dorianCaNewCertInfoStep.getOptions().add(
				new ListPropertyConfigurationOption(
						Constants.DORIAN_CA_CAKEY_SIZE, model
								.getMessage("dorian.ca.cert.info.cakey.size"),
						new String[] { String.valueOf(2048),
								String.valueOf(1024), String.valueOf(512) },
						true));
		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_LIFETIME_YEARS,
								model
										.getMessage("dorian.ca.cert.info.lifetime.years"),
								model.getProperty(
										Constants.DORIAN_CA_LIFETIME_YEARS,
										"10"), true));
		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_LIFETIME_MONTHS,
								model
										.getMessage("dorian.ca.cert.info.lifetime.months"),
								model.getProperty(
										Constants.DORIAN_CA_LIFETIME_MONTHS,
										"0"), true));
		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_LIFETIME_DAYS,
								model
										.getMessage("dorian.ca.cert.info.lifetime.days"),
								model.getProperty(
										Constants.DORIAN_CA_LIFETIME_DAYS, "0"),
								true));
		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_LIFETIME_HOURS,
								model
										.getMessage("dorian.ca.cert.info.lifetime.hours"),
								model
										.getProperty(
												Constants.DORIAN_CA_LIFETIME_HOURS,
												"0"), true));
		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_LIFETIME_MINUTES,
								model
										.getMessage("dorian.ca.cert.info.lifetime.minutes"),
								model.getProperty(
										Constants.DORIAN_CA_LIFETIME_MINUTES,
										"0"), true));
		dorianCaNewCertInfoStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.DORIAN_CA_LIFETIME_SECONDS,
								model
										.getMessage("dorian.ca.cert.info.lifetime.seconds"),
								model.getProperty(
										Constants.DORIAN_CA_LIFETIME_SECONDS,
										"0"), true));

		String[] slots = new String[NUM_ERACOM_SLOTS];
		for (int i = 0; i < slots.length; i++) {
			slots[i] = String.valueOf(i);
		}
		dorianCaNewCertInfoStep.getOptions().add(
				new ListPropertyConfigurationOption(
						Constants.DORIAN_CA_ERACOM_SLOT, model
								.getMessage("dorian.ca.eracom.slot"), slots,
						false));
		// TODO: add more validation
		model.add(dorianCaNewCertInfoStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN)
						&& !model.isTrue(Constants.DORIAN_CA_PRESENT);
			}
		});

		ConfigureDorianHostCredentialsStep configDorianSvcCertStep = new ConfigureDorianHostCredentialsStep(
				model.getMessage("dorian.host.cred.title"), model
						.getMessage("dorian.host.cred.desc"));
		configDorianSvcCertStep.getOptions().add(
				new TextPropertyConfigurationOption(Constants.SERVICE_HOSTNAME,
						model.getMessage("service.cert.info.hostname"),
						model.getProperty(Constants.SERVICE_HOSTNAME,
								"localhost"), true));
		FilePropertyConfigurationOption dorianHostCredDir = new FilePropertyConfigurationOption(
				Constants.DORIAN_HOST_CRED_DIR, model
						.getMessage("dorian.host.cred.dir"), model
						.getProperty(Constants.DORIAN_HOST_CRED_DIR,
								InstallerUtils.getInstallerDir() + "/certs"),
				true);
		dorianHostCredDir.setBrowseLabel(model.getMessage("browse"));
		dorianHostCredDir.setDirectoriesOnly(true);
		configDorianSvcCertStep.getOptions().add(dorianHostCredDir);
		model.add(configDorianSvcCertStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_DORIAN);
			}
		});
	}

}
