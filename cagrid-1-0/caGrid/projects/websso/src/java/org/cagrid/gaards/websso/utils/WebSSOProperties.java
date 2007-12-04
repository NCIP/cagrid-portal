package org.cagrid.gaards.websso.utils;

import java.util.List;
import java.util.Map;

import org.cagrid.gaards.websso.beans.AuthenticationServiceInformation;
import org.cagrid.gaards.websso.beans.HostInformation;
import org.cagrid.gaards.websso.exception.AuthenticationConfigurationException;
import org.jdom.Document;

public class WebSSOProperties
{
	Document propertiesFile = null;
	
	public WebSSOProperties(final FileHelper fileHelper, final String propertiesFileName, final String schemaFileName) throws AuthenticationConfigurationException
	{
		this.propertiesFile = fileHelper.validateXMLwithSchema(propertiesFileName, schemaFileName);
	}
	
	public List<AuthenticationServiceInformation> getAuthenticationServices()
	{
		return null;
	}
	
	public List<HostInformation> getHostInformationList()
	{
		return null;
	}

	
}
