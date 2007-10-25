package org.cagrid.gaards.cds.testutils;

import org.cagrid.gaards.cds.service.CDS;
import org.cagrid.gaards.cds.service.ConfigurationConstants;
import org.cagrid.gaards.cds.service.DelegatedCredentialManager;
import org.cagrid.gaards.cds.service.KeyManager;
import org.cagrid.gaards.cds.service.PropertyManager;
import org.cagrid.gaards.cds.service.policy.IdentityPolicyHandler;
import org.cagrid.tools.database.Database;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class Utils {

	public static XmlBeanFactory loadConfiguration() throws Exception {

		ClassPathResource cpr = new ClassPathResource(
				Constants.CDS_CONFIGURATION);
		XmlBeanFactory factory = new XmlBeanFactory(cpr);
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.setLocation(new ClassPathResource(Constants.CDS_PROPERTIES));
		cfg.postProcessBeanFactory(factory);
		return factory;
	}

	public static CDS getCDS() throws Exception {
		XmlBeanFactory factory = loadConfiguration();
		return (CDS) factory
				.getBean(ConfigurationConstants.CDS_BEAN);
	}

	public static DelegatedCredentialManager getDelegatedCredentialManager()
			throws Exception {
		XmlBeanFactory factory = loadConfiguration();
		return (DelegatedCredentialManager) factory
				.getBean(ConfigurationConstants.DELEGATED_CREDENTIAL_MANAGER_CONFIGURATION_BEAN);
	}

	public static KeyManager getKeyManager() throws Exception {
		XmlBeanFactory factory = loadConfiguration();
		return (KeyManager) factory
				.getBean(ConfigurationConstants.KEY_MANAGER_CONFIGURATION_BEAN);
	}

	public static IdentityPolicyHandler getIdentityPolicyHandler()
			throws Exception {
		XmlBeanFactory factory = loadConfiguration();
		return (IdentityPolicyHandler) factory
				.getBean(ConfigurationConstants.IDENTITY_POLICY_HANDLER);
	}

	public static Database getDatabase() throws Exception {
		XmlBeanFactory factory = loadConfiguration();
		return (Database) factory
				.getBean(ConfigurationConstants.DATABASE_CONFIGURATION_BEAN);
	}

	public static PropertyManager getPropertyManager() throws Exception {
		XmlBeanFactory factory = loadConfiguration();
		return (PropertyManager) factory
				.getBean(ConfigurationConstants.PROPERTY_MANAGER_CONFIGURATION_BEAN);
	}

}
