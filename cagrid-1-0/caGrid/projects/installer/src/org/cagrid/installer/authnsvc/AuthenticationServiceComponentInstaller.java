/**
 * 
 */
package org.cagrid.installer.authnsvc;

import java.io.File;

import org.cagrid.installer.CaGridComponentInstaller;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.ConfigureServiceMetadataStep;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.DeployPropertiesFileEditorStep;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.RunTasksStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.FilePropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.PasswordPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption.LabelValuePair;
import org.cagrid.installer.tasks.ConditionalTask;
import org.cagrid.installer.util.InstallerUtils;
import org.cagrid.installer.validator.GenericDBConnectionValidator;
import org.cagrid.installer.validator.PathExistsValidator;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class AuthenticationServiceComponentInstaller implements
		CaGridComponentInstaller {

	/**
	 * 
	 */
	public AuthenticationServiceComponentInstaller() {

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
				new ConditionalTask(new DeployAuthenticationServiceTask(model
						.getMessage("deploying.authn.svc.title"), ""),
						new Condition() {

							public boolean evaluate(WizardModel m) {
								CaGridInstallerModel model = (CaGridInstallerModel) m;
								return model
										.isTrue(Constants.INSTALL_AUTHN_SVC);
							}

						}));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cagrid.installer.ComponentInstaller#addSteps(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	public void addSteps(CaGridInstallerModel model) {
		ConfigureServiceMetadataStep editAuthnSvcMetaStep = new ConfigureServiceMetadataStep(
				"", model.getMessage("authn.svc.edit.service.metadata.title"),
				model.getMessage("authn.svc.edit.service.metadata.desc")) {
			protected String getServiceMetadataPath() {
				return model.getServiceDestDir()
						+ "/authentication-service/etc/serviceMetadata.xml";
			}
		};
		model.add(editAuthnSvcMetaStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}
		});

		// Check if user wants to use the service credentials as the
		// asserting credentials
		PropertyConfigurationStep checkUseSvcCredsStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.use.svc.creds.title"), model
						.getMessage("authn.svc.use.svc.creds.desc")) {
			public void applyState() throws InvalidStateException {

				super.applyState();

				if (this.model.isTrue(Constants.AUTHN_SVC_USE_SVC_CREDS)) {
					this.model
							.setProperty(
									Constants.AUTH_SVC_SAML_PROVIDER_CERT_PATH,
									this.model
											.getProperty(Constants.SERVICE_CERT_PATH));
					this.model.setProperty(
							Constants.AUTH_SVC_SAML_PROVIDER_KEY_PATH,
							this.model.getProperty(Constants.SERVICE_KEY_PATH));
					this.model.setProperty(
							Constants.AUTH_SVC_SAML_PROVIDER_KEY_PWD, "");
				}

			}
		};
		checkUseSvcCredsStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.AUTHN_SVC_USE_SVC_CREDS, model
								.getMessage("yes"), false, false));
		model.add(checkUseSvcCredsStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}

		});
		
		//If can't find the service credentials, as user for location.
		PropertyConfigurationStep configAssertingSvcCredsStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.asserting.svc.creds.title"), model
						.getMessage("authn.svc.asserting.svc.creds.desc"));
		InstallerUtils.addCommonCertFields(model, configAssertingSvcCredsStep,
				Constants.AUTH_SVC_SAML_PROVIDER_CERT_PATH,
				Constants.AUTH_SVC_SAML_PROVIDER_KEY_PATH,
				null);
		model.add(configAssertingSvcCredsStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return !model.isAuthnSvcServiceCredentialsPresent()
						&& model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}

		});

		// If user doesn't want to use service credentials, ask for location
		// of asserting credentials
		PropertyConfigurationStep configAssertingCredsStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.asserting.creds.title"), model
						.getMessage("authn.svc.asserting.creds.desc"));
		InstallerUtils.addCommonCertFields(model, configAssertingCredsStep,
				Constants.AUTH_SVC_SAML_PROVIDER_CERT_PATH,
				Constants.AUTH_SVC_SAML_PROVIDER_KEY_PATH,
				Constants.AUTH_SVC_SAML_PROVIDER_KEY_PWD);
		model.add(configAssertingCredsStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return !model.isTrue(Constants.AUTHN_SVC_USE_SVC_CREDS)
						&& model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}

		});

		PropertyConfigurationStep overwriteJaasStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.overwrite.jaas.title"), model
						.getMessage("authn.svc.overwrite.jaas.desc"));
		overwriteJaasStep
				.getOptions()
				.add(
						new ListPropertyConfigurationOption(
								Constants.AUTHN_SVC_OVERWRITE_JAAS,
								model.getMessage("authn.svc.overwrite.jaas"),
								new LabelValuePair[] {
										new LabelValuePair(
												model
														.getMessage("authn.svc.overwrite.jaas.yes"),
												Constants.AUTHN_SVC_OVERWRITE_JAAS_YES),
										new LabelValuePair(
												model
														.getMessage("authn.svc.overwrite.jaas.no"),
												Constants.AUTHN_SVC_OVERWRITE_JAAS_NO) }));
		model.add(overwriteJaasStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return new File(System.getProperty("user.home")
						+ "/.java.login.config").exists()
						&& model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}

		});

		// Let's user select the type of credential provider
		PropertyConfigurationStep selectCredentialProviderStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.select.cred.provider.type.title"),
				model.getMessage("authn.svc.select.cred.provider.type.desc"));
		selectCredentialProviderStep
				.getOptions()
				.add(
						new ListPropertyConfigurationOption(
								Constants.AUTHN_SVC_CRED_PROVIDER_TYPE,
								model
										.getMessage("authn.svc.cred.provider.type"),
								new LabelValuePair[] {
										new LabelValuePair(
												model
														.getMessage("authn.svc.cred.provider.type.rdbms"),
												Constants.AUTHN_SVC_CRED_PROVIDER_TYPE_RDBMS),
										new LabelValuePair(
												model
														.getMessage("authn.svc.cred.provider.type.ldap"),
												Constants.AUTHN_SVC_CRED_PROVIDER_TYPE_LDAP) }));
		model.add(selectCredentialProviderStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}

		});

		// AuthenticationService RDBMS step
		PropertyConfigurationStep authnSvcRdbmsStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.rdbms.title"), model
						.getMessage("authn.svc.rdbms.desc"));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_CSM_CTX, model
								.getMessage("authn.svc.csm.ctx"), model
								.getProperty(Constants.AUTHN_SVC_CSM_CTX,
										"AUTHNSVC"), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_URL, model
								.getMessage("authn.svc.rdbms.url"),
						model.getProperty(Constants.AUTHN_SVC_RDBMS_URL,
								"jdbc:mysql://localhost:3306/authnsvc"), true));

		FilePropertyConfigurationOption driverJar = new FilePropertyConfigurationOption(
				Constants.AUTHN_SVC_RDBMS_DRIVER_JAR, model
						.getMessage("authn.svc.rdbms.driver.jar"),
				model.getProperty(Constants.AUTHN_SVC_RDBMS_DRIVER_JAR, System
						.getProperty("user.dir")
						+ "/lib/mysql-connector-java-3.0.16-ga-bin.jar"), true);
		driverJar.setBrowseLabel(model.getMessage("browse"));
		driverJar.setExtensions(new String[] { ".jar" });
		authnSvcRdbmsStep.getOptions().add(driverJar);

		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_DRIVER, model
								.getMessage("authn.svc.rdbms.driver"), model
								.getProperty(Constants.AUTHN_SVC_RDBMS_DRIVER,
										"org.gjt.mm.mysql.Driver"), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_USERNAME, model
								.getMessage("authn.svc.rdbms.username"), model
								.getProperty(
										Constants.AUTHN_SVC_RDBMS_USERNAME,
										"root"), true));
		authnSvcRdbmsStep.getOptions().add(
				new PasswordPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_PASSWORD, model
								.getMessage("authn.svc.rdbms.password"),
						model.getProperty(Constants.AUTHN_SVC_RDBMS_PASSWORD,
								""), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_TABLE_NAME, model
								.getMessage("authn.svc.rdbms.table.name"),
						model.getProperty(Constants.AUTHN_SVC_RDBMS_TABLE_NAME,
								"CSM_USER"), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_LOGIN_ID_COLUMN, model
								.getMessage("authn.svc.rdbms.login.id.column"),
						model.getProperty(
								Constants.AUTHN_SVC_RDBMS_LOGIN_ID_COLUMN,
								"LOGIN_NAME"), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_PASSWORD_COLUMN, model
								.getMessage("authn.svc.rdbms.password.column"),
						model.getProperty(
								Constants.AUTHN_SVC_RDBMS_PASSWORD_COLUMN,
								"PASSWORD"), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_FIRST_NAME_COLUMN,
						model.getMessage("authn.svc.rdbms.first.name.column"),
						model.getProperty(
								Constants.AUTHN_SVC_RDBMS_FIRST_NAME_COLUMN,
								"FIRST_NAME"), true));
		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_LAST_NAME_COLUMN,
						model.getMessage("authn.svc.rdbms.last.name.column"),
						model.getProperty(
								Constants.AUTHN_SVC_RDBMS_LAST_NAME_COLUMN,
								"LAST_NAME"), true));

		authnSvcRdbmsStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_EMAIL_ID_COLUMN, model
								.getMessage("authn.svc.rdbms.email.id.column"),
						model.getProperty(
								Constants.AUTHN_SVC_RDBMS_EMAIL_ID_COLUMN,
								"EMAIL_ID"), true));
		authnSvcRdbmsStep.getOptions().add(
				new BooleanPropertyConfigurationOption(
						Constants.AUTHN_SVC_RDBMS_ENCRYPTION_ENABLED,
						model.getMessage("authn.svc.rdbms.encryption.enabled"),
						false, false));
		authnSvcRdbmsStep
				.getValidators()
				.add(
						new PathExistsValidator(
								Constants.AUTHN_SVC_RDBMS_DRIVER_JAR,
								model
										.getMessage("authn.svc.rdbms.driver.jar.not.found")));
		authnSvcRdbmsStep.getValidators().add(
				new GenericDBConnectionValidator(
						Constants.AUTHN_SVC_RDBMS_USERNAME,
						Constants.AUTHN_SVC_RDBMS_PASSWORD, "select 1", model
								.getMessage("db.validation.failed"),
						Constants.AUTHN_SVC_RDBMS_DRIVER_JAR,
						Constants.AUTHN_SVC_RDBMS_URL,
						Constants.AUTHN_SVC_RDBMS_DRIVER));
		model.add(authnSvcRdbmsStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_AUTHN_SVC)
						&& model.isEqual(
								Constants.AUTHN_SVC_CRED_PROVIDER_TYPE_RDBMS,
								Constants.AUTHN_SVC_CRED_PROVIDER_TYPE);
			}
		});

		// AuthenticationService LDAP Setup
		PropertyConfigurationStep authnSvcLdapStep = new PropertyConfigurationStep(
				model.getMessage("authn.svc.ldap.title"), model
						.getMessage("authn.svc.ldap.desc"));
		authnSvcLdapStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_CSM_CTX, model
								.getMessage("authn.svc.csm.ctx"), model
								.getProperty(Constants.AUTHN_SVC_CSM_CTX,
										"AUTHNSVC"), true));
		authnSvcLdapStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_LDAP_HOSTNAME, model
								.getMessage("authn.svc.ldap.hostname"), model
								.getProperty(Constants.AUTHN_SVC_LDAP_HOSTNAME,
										"ldaps://cbioweb.nci.nih.gov:636"),
						true));
		authnSvcLdapStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_LDAP_SEARCH_BASE, model
								.getMessage("authn.svc.ldap.search.base"),
						model.getProperty(Constants.AUTHN_SVC_LDAP_SEARCH_BASE,
								"ou=nci,o=nih"), true));
		authnSvcLdapStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_LDAP_LOGIN_ID_ATTRIBUTE,
						model.getMessage("authn.svc.ldap.login.id.attribute"),
						model.getProperty(
								Constants.AUTHN_SVC_LDAP_LOGIN_ID_ATTRIBUTE,
								"cn"), true));
		authnSvcLdapStep
				.getOptions()
				.add(
						new TextPropertyConfigurationOption(
								Constants.AUTHN_SVC_LDAP_FIRST_NAME_ATTRIBUTE,
								model
										.getMessage("authn.svc.ldap.first.name.attribute"),
								model
										.getProperty(
												Constants.AUTHN_SVC_LDAP_FIRST_NAME_ATTRIBUTE,
												"givenName"), true));
		authnSvcLdapStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_LDAP_LAST_NAME_ATTRIBUTE,
						model.getMessage("authn.svc.ldap.last.name.attribute"),
						model.getProperty(
								Constants.AUTHN_SVC_LDAP_LAST_NAME_ATTRIBUTE,
								"sn"), true));
		authnSvcLdapStep.getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.AUTHN_SVC_LDAP_EMAIL_ID_ATTRIBUTE,
						model.getMessage("authn.svc.ldap.email.id.attribute"),
						model.getProperty(
								Constants.AUTHN_SVC_LDAP_EMAIL_ID_ATTRIBUTE,
								"mail"), true));
		// TODO: add validation
		model.add(authnSvcLdapStep, new Condition() {

			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_AUTHN_SVC)
						&& model.isEqual(
								Constants.AUTHN_SVC_CRED_PROVIDER_TYPE_LDAP,
								Constants.AUTHN_SVC_CRED_PROVIDER_TYPE);
			}
		});

		// Configure AuthenticationService deploy.properties
		DeployPropertiesFileEditorStep editAuthnDeployPropertiesStep = new DeployPropertiesFileEditorStep(
				"authentication-service", model
						.getMessage("authn.svc.edit.deploy.properties.title"),
				model.getMessage("authn.svc.edit.deploy.properties.desc"),
				model.getMessage("edit.properties.property.name"), model
						.getMessage("edit.properties.property.value"));
		model.add(editAuthnDeployPropertiesStep, new Condition() {
			public boolean evaluate(WizardModel m) {
				CaGridInstallerModel model = (CaGridInstallerModel) m;
				return model.isTrue(Constants.INSTALL_AUTHN_SVC);
			}
		});

	}
}
