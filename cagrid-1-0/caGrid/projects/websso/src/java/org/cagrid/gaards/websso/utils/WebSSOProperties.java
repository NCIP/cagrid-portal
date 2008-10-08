package org.cagrid.gaards.websso.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.cagrid.gaards.websso.beans.AuthenticationServiceInformation;
import org.cagrid.gaards.websso.beans.CredentialDelegationServiceInformation;
import org.cagrid.gaards.websso.beans.DelegatedApplicationInformation;
import org.cagrid.gaards.websso.beans.DorianInformation;
import org.cagrid.gaards.websso.beans.WebSSOServerInformation;
import org.cagrid.gaards.websso.exception.AuthenticationConfigurationException;
import org.jdom.Document;
import org.jdom.Element;

public class WebSSOProperties
{
	private Document propertiesFile = null;
	
	private List <AuthenticationServiceInformation> authenticationServiceInformationList = null;
	
	private List<DelegatedApplicationInformation> delegatedApplicationInformationList = null;
	
	private WebSSOServerInformation webSSOServerInformation = null;
	
	private CredentialDelegationServiceInformation credentialDelegationServiceInformation = null;
	
	public WebSSOProperties(final FileHelper fileHelper, final String propertiesFileName, final String schemaFileName) throws AuthenticationConfigurationException
	{
		this.propertiesFile = fileHelper.validateXMLwithSchema(propertiesFileName, schemaFileName);
		this.authenticationServiceInformationList = loadAuthenticationServices();
		this.delegatedApplicationInformationList = loadDelegatedApplicationInformationList();
		this.webSSOServerInformation = loadWebSSOServerInformation();
		this.credentialDelegationServiceInformation = loadCredentialDelegationServiceInformation();
	}
	
	public List<AuthenticationServiceInformation> getAuthenticationServiceInformationList()
	{
		return authenticationServiceInformationList;
	}
	
	public List<DelegatedApplicationInformation> getDelegatedApplicationInformationList()
	{
		return delegatedApplicationInformationList;
	}
	
	public WebSSOServerInformation getWebSSOServerInformation()
	{
		return webSSOServerInformation;
	}
	
	public CredentialDelegationServiceInformation getCredentialDelegationServiceInformation()
	{
		return credentialDelegationServiceInformation;
	}
	
	private List<AuthenticationServiceInformation> loadAuthenticationServices()
	{
		List<AuthenticationServiceInformation> authenticationServiceInformationList = new ArrayList<AuthenticationServiceInformation>();
		Element webssoProperties = propertiesFile.getRootElement();
		Element authenticationServiceList = webssoProperties.getChild("authentication-service-list");
		List<?> authenticationServices = authenticationServiceList.getChildren("authentication-service-information");
		Iterator<?> authenticationServicesIterator  = authenticationServices.iterator();
		while(authenticationServicesIterator.hasNext())
		{
		 	Element authenticationService = (Element)authenticationServicesIterator.next();
		 	Element serviceName = authenticationService.getChild("service-name");
		 	Element serviceURL = authenticationService.getChild("service-url");
		 	AuthenticationServiceInformation authenticationServiceInformation = new AuthenticationServiceInformation();
		 	authenticationServiceInformation.setAuthenticationServiceName(serviceName.getText().trim());
		 	authenticationServiceInformation.setAuthenticationServiceURL(serviceURL.getText().trim());
		 	authenticationServiceInformation.setDorianInformation(this.getDorianInformation(authenticationService));
		 	authenticationServiceInformationList.add(authenticationServiceInformation);
		}
		return authenticationServiceInformationList;
	}
	
	private List<DelegatedApplicationInformation> loadDelegatedApplicationInformationList()
	{
		List<DelegatedApplicationInformation> hostInformationList = new ArrayList<DelegatedApplicationInformation>();
		Element webssoProperties = propertiesFile.getRootElement();
		Element delegatedApplicationGroup = webssoProperties.getChild("delegated-applications-group");
		Element delegatedApplicationList = delegatedApplicationGroup.getChild("delegated-application-list");
		List<?> delegatedApplications = delegatedApplicationList.getChildren("delegated-application");
		Iterator<?> delegatedApplicationsIterator  = delegatedApplications.iterator();
		while(delegatedApplicationsIterator.hasNext())
		{
		 	Element delegatedApplication = (Element)delegatedApplicationsIterator.next();
		 	Element applicationName = delegatedApplication.getChild("application-name");
		 	Element hostIdentity = delegatedApplication.getChild("host-identity");
		 	DelegatedApplicationInformation hostInformation = new DelegatedApplicationInformation();
		 	hostInformation.setHostName(applicationName.getText().trim());
		 	hostInformation.setHostIdentity(hostIdentity.getText().trim());
		 	hostInformationList.add(hostInformation);
		}
		return hostInformationList;
	}
	
	private DorianInformation getDorianInformation(Element authenticationService)
	{
	 	DorianInformation dorianInformation = new DorianInformation();
	 	Element dorianInformationElement = authenticationService.getChild("dorian-information");
	 	dorianInformation.setDorianServiceURL(this.getDorianServiceURL(dorianInformationElement));
	 	dorianInformation.setProxyLifetimeHours(Integer.parseInt(this.getProxyLifeTimeHours(dorianInformationElement)));
	 	dorianInformation.setProxyLifetimeMinutes(Integer.parseInt(this.getProxyLifeTimeMinutes(dorianInformationElement)));
	 	dorianInformation.setProxyLifetimeSeconds(Integer.parseInt(this.getProxyLifeTimeSeconds(dorianInformationElement)));
	 	dorianInformation.setDelegationPathLength(Integer.parseInt(this.getProxyDelegationPathLength(dorianInformationElement)));
	 	return dorianInformation;
	}

	private String getDorianServiceURL(Element dorianInformationElement)
	{
		Element serviceURL = dorianInformationElement.getChild("service-url");
		return serviceURL.getText().trim();
	}
	
	private String getProxyLifeTimeHours(Element dorianInformationElement)
	{
		Element proxyLifetimeHours = dorianInformationElement.getChild("proxy-lifetime-hours");
		return proxyLifetimeHours.getText().trim();
	}
	
	private String getProxyLifeTimeMinutes(Element dorianInformationElement)
	{
		Element proxyLifetimeMinutes = dorianInformationElement.getChild("proxy-lifetime-minutes");
		return proxyLifetimeMinutes.getText().trim();
	}

	private String getProxyLifeTimeSeconds(Element dorianInformationElement)
	{
		Element proxyLifetimeSeconds = dorianInformationElement.getChild("proxy-lifetime-seconds");
		return proxyLifetimeSeconds.getText().trim();
	}

	private String getProxyDelegationPathLength(Element dorianInformationElement)
	{
		Element proxyDelegationPathLength = dorianInformationElement.getChild("proxy-delegation-path-length");
		return proxyDelegationPathLength.getText().trim();
	}
	
	private CredentialDelegationServiceInformation loadCredentialDelegationServiceInformation()
	{
		Element webssoProperties = propertiesFile.getRootElement();
		Element credentialDelegationServiceInformationElement = webssoProperties.getChild("credential-delegation-service-information");
		CredentialDelegationServiceInformation credentialDelegationServiceInformation = new CredentialDelegationServiceInformation();
		credentialDelegationServiceInformation.setServiceURL(this.getDelegationServiceURL(credentialDelegationServiceInformationElement));
		credentialDelegationServiceInformation.setDelegationLifetimeHours(Integer.parseInt(this.getDelegationLifeTimeHours(credentialDelegationServiceInformationElement)));
		credentialDelegationServiceInformation.setDelegationLifetimeMinutes(Integer.parseInt(this.getDelegationLifeTimeMinutes(credentialDelegationServiceInformationElement)));
		credentialDelegationServiceInformation.setDelegationLifetimeSeconds(Integer.parseInt(this.getDelegationLifeTimeSeconds(credentialDelegationServiceInformationElement)));
		credentialDelegationServiceInformation.setIssuedCredentialPathLength(Integer.parseInt(this.getIssuedCredentialPathLength(credentialDelegationServiceInformationElement)));
		return credentialDelegationServiceInformation;
	}
	
	
	private String getDelegationServiceURL(Element credentialDelegationServiceInformationElement)
	{
		Element serviceURL = credentialDelegationServiceInformationElement.getChild("service-url");
		return serviceURL.getText().trim();
	}
	
	private String getDelegationLifeTimeHours(Element credentialDelegationServiceInformationElement)
	{
		Element delegationLifetimeHours = credentialDelegationServiceInformationElement.getChild("delegation-lifetime-hours");
		return delegationLifetimeHours.getText().trim();
	}
	
	private String getDelegationLifeTimeMinutes(Element credentialDelegationServiceInformationElement)
	{
		Element delegationLifetimeMinutes = credentialDelegationServiceInformationElement.getChild("delegation-lifetime-minutes");
		return delegationLifetimeMinutes.getText().trim();
	}

	private String getDelegationLifeTimeSeconds(Element credentialDelegationServiceInformationElement)
	{
		Element delegationLifetimeSeconds = credentialDelegationServiceInformationElement.getChild("delegation-lifetime-seconds");
		return delegationLifetimeSeconds.getText().trim();
	}

	private String getIssuedCredentialPathLength(Element credentialDelegationServiceInformationElement) {
		Element issuedCredentialPathLength = credentialDelegationServiceInformationElement.getChild("issued-credential-path-length");
		return issuedCredentialPathLength.getText().trim();
	}
	
	private WebSSOServerInformation loadWebSSOServerInformation() {
		Element webssoProperties = propertiesFile.getRootElement();
		Element webSSOServerInformationElement = webssoProperties.getChild("websso-server-information");
		WebSSOServerInformation webSSOServerInformation = new WebSSOServerInformation();
		webSSOServerInformation.setStartAutoSyncGTS(this.getStartAutoSyncGTS(webSSOServerInformationElement));
		webSSOServerInformation.setTrustStorePath(this.getTrustStorePath(webSSOServerInformationElement));
		webSSOServerInformation.setHostCredentialCertificateFilePath(this.getHostCredentialCertificateFilePath(webSSOServerInformationElement));
		webSSOServerInformation.setHostCredentialKeyFilePath(this.getHostCredentialKeyFilePath(webSSOServerInformationElement));

		return webSSOServerInformation;
	}

	private String getStartAutoSyncGTS(Element webSSOServerInformationElement)
	{
		Element trustStorePath = webSSOServerInformationElement.getChild("start-auto-syncgts");
		return trustStorePath.getText().trim();
	}
	
	private String getTrustStorePath(Element webSSOServerInformationElement)
	{
		Element trustStorePath = webSSOServerInformationElement.getChild("trust-store-path");
		return trustStorePath.getText().trim();
	}

	private String getHostCredentialCertificateFilePath(Element webSSOServerInformationElement)
	{
		Element hostCredentialCertificateFilePath = webSSOServerInformationElement.getChild("host-credential-certificate-file-path");
		return hostCredentialCertificateFilePath.getText().trim();
	}

	private String getHostCredentialKeyFilePath(Element webSSOServerInformationElement)
	{
		Element hostCredentialKeyFilePath = webSSOServerInformationElement.getChild("host-credential-key-file-path");
		return hostCredentialKeyFilePath.getText().trim();
	}

}

