package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.service.ca.CertificateAuthorityProperties;
import gov.nih.nci.cagrid.dorian.service.idp.AssertionCredentialsManager;
import gov.nih.nci.cagrid.dorian.service.idp.IdentityProvider;
import gov.nih.nci.cagrid.dorian.service.idp.IdentityProviderProperties;
import gov.nih.nci.cagrid.dorian.service.ifs.IdentityFederationProperties;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;

import org.cagrid.tools.database.Database;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.AbstractResource;

public class BeanUtils {

	private XmlBeanFactory factory;

	public BeanUtils(AbstractResource dorianConf,
			AbstractResource dorianProperties) throws DorianInternalFault {
		this.factory = new XmlBeanFactory(dorianConf);
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.setLocation(dorianProperties);
		cfg.postProcessBeanFactory(factory);
	}

	public DorianProperties getDorianProperties() throws Exception {
		DorianProperties props = (DorianProperties) factory
				.getBean(ConfigurationConstants.DORIAN_PROPERTIES_BEAN);
		return props;
	}

	public IdentityFederationProperties getIdentityFederationProperties()
			throws Exception {
		IdentityFederationProperties props = (IdentityFederationProperties) factory
				.getBean(ConfigurationConstants.IDENTITY_FEDERATION_PROPERTIES_BEAN);
		return props;
	}

	public IdentityProvider getIdentityProvider() throws Exception {
		return (IdentityProvider) factory
				.getBean(ConfigurationConstants.IDP_BEAN);
	}

	public IdentityProviderProperties getIdentityProviderProperties()
			throws Exception {
		return (IdentityProviderProperties) factory
				.getBean(ConfigurationConstants.IDP_PROPERTIES_BEAN);
	}

	public AssertionCredentialsManager getAssertionCredentialsManager()
			throws Exception {
		return (AssertionCredentialsManager) factory
				.getBean(ConfigurationConstants.IDP_ASSERTION_MANAGER_BEAN);
	}

	public gov.nih.nci.cagrid.dorian.service.idp.UserManager getIdPUserManager()
			throws Exception {
		return (gov.nih.nci.cagrid.dorian.service.idp.UserManager) factory
				.getBean(ConfigurationConstants.IDP_USER_MANAGER_BEAN);
	}

	public Database getDatabase() throws Exception {
		return (Database) factory.getBean(ConfigurationConstants.DATABASE_BEAN);
	}

	public CertificateAuthorityProperties getCertificateAuthorityProperties()
			throws Exception {
		return (CertificateAuthorityProperties) factory
				.getBean(ConfigurationConstants.CA_PROPERTIES_BEAN);

	}

	public CertificateAuthority getCertificateAuthority() throws Exception {
		return (CertificateAuthority) factory
				.getBean(ConfigurationConstants.CA_BEAN);
	}

}
