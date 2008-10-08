package org.cagrid.gaards.websso.authentication;

import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.cagrid.gaards.websso.authentication.helper.AuthenticationServiceHelper;
import org.cagrid.gaards.websso.authentication.helper.DorianHelper;
import org.cagrid.gaards.websso.authentication.helper.GridCredentialDelegator;
import org.cagrid.gaards.websso.authentication.helper.ProxyValidator;
import org.cagrid.gaards.websso.authentication.helper.SAMLToAttributeMapper;
import org.cagrid.gaards.websso.beans.AuthenticationServiceInformation;
import org.cagrid.gaards.websso.beans.DelegatedApplicationInformation;
import org.cagrid.gaards.websso.beans.DorianInformation;
import org.cagrid.gaards.websso.beans.WebSSOServerInformation;
import org.cagrid.gaards.websso.exception.AuthenticationConfigurationException;
import org.cagrid.gaards.websso.utils.WebSSOConstants;
import org.cagrid.gaards.websso.utils.WebSSOProperties;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationManager;
import org.jasig.cas.authentication.MutableAuthentication;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CaGridAuthenticationManager
 * 
 * @author oster
 * @created Oct 2, 2007 12:40:18 PM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
@Service
public class CaGridAuthenticationManager implements AuthenticationManager {
	private WebSSOProperties webSSOProperties = null;
	private AuthenticationServiceHelper authenticationServiceHelper;
	private DorianHelper dorianHelper = null;
	private ProxyValidator proxyValidator=null;
	private SAMLToAttributeMapper samlToAttributeMapper=null;
	private GridCredentialDelegator gridCredentialDelegator=null;
	
	private List<String> hostIdentities = null;
	
	public CaGridAuthenticationManager() {
	}
	
	@Autowired
	public CaGridAuthenticationManager(
			AuthenticationServiceHelper authenticationServiceHelper,
			DorianHelper dorianHelper,
			GridCredentialDelegator gridCredentialDelegator,
			ProxyValidator proxyValidator,
			SAMLToAttributeMapper samlToAttributeMapper,
			WebSSOProperties webSSOProperties) {
		super();
		this.authenticationServiceHelper = authenticationServiceHelper;
		this.dorianHelper = dorianHelper;
		this.gridCredentialDelegator = gridCredentialDelegator;
		this.proxyValidator = proxyValidator;
		this.samlToAttributeMapper = samlToAttributeMapper;
		this.webSSOProperties = webSSOProperties;
	}
	
	public Authentication authenticate(Credentials credentials)
			throws AuthenticationException {
		if (null == webSSOProperties) {
			throw new AuthenticationConfigurationException(
					"Error Initializing Authentication Manager properties");
		}
		// Authenticate the user credentials and retrieve 
		SAMLAssertion samlAssertion = authenticationServiceHelper.authenticate( ((UsernamePasswordAuthenticationServiceURLCredentials)credentials).getAuthenticationServiceURL() , ((UsernamePasswordAuthenticationServiceURLCredentials)credentials).getUsername(), ((UsernamePasswordAuthenticationServiceURLCredentials)credentials).getPassword());
		// Obtained the GlobusCredential for the Authenticated User
		GlobusCredential globusCredential = dorianHelper.obtainProxy(samlAssertion, this.getDorianInformation(((UsernamePasswordAuthenticationServiceURLCredentials)credentials).getAuthenticationServiceURL()));
		// Validate the Proxy
		proxyValidator.validate(globusCredential);
		// Delegate the Globus Credentials
		String serializedDelegatedCredentialReference = gridCredentialDelegator.delegateGridCredential(globusCredential, this.getHostIdentities());

		HashMap<String, String> attributesMap = samlToAttributeMapper.convertSAMLtoHashMap(samlAssertion);

		// Adding the serialed Delegated Credentials Reference and Grid Identity to the 
		attributesMap.put(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR, serializedDelegatedCredentialReference);
		attributesMap.put(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY, globusCredential.getIdentity());
		
		// Creating the Principal from the grid identity
		Principal p = new SimplePrincipal(this.constructPrincipal(attributesMap));

		// Create a new Authentication Object using the Principal
		MutableAuthentication mutableAuthentication = new MutableAuthentication(p);
		return mutableAuthentication;
	}
	
	private DorianInformation getDorianInformation(
			String authenticationServiceURL) throws AuthenticationConfigurationException {
		AuthenticationServiceInformation authenticationServiceInformation = new AuthenticationServiceInformation();
		authenticationServiceInformation
				.setAuthenticationServiceURL(authenticationServiceURL);
		List<AuthenticationServiceInformation> authenticationServiceInformationList = webSSOProperties
				.getAuthenticationServiceInformationList();
		AuthenticationServiceInformation authenticationServiceInformationMatched = authenticationServiceInformationList
				.get(authenticationServiceInformationList.indexOf(authenticationServiceInformation));
		return authenticationServiceInformationMatched.getDorianInformation();
	}
	
	private List<String> getHostIdentities()
			throws AuthenticationConfigurationException {
		if (null == hostIdentities) {
			List<DelegatedApplicationInformation> delegatedApplicationInformationList = webSSOProperties
					.getDelegatedApplicationInformationList();

			if (delegatedApplicationInformationList.size() == 0)
				throw new AuthenticationConfigurationException("None Host Identities configured for Delegation ");

			hostIdentities = new ArrayList<String>();
			for (DelegatedApplicationInformation delegatedApplicationInformation : delegatedApplicationInformationList){
				hostIdentities.add(delegatedApplicationInformation.getHostIdentity());
			}
			hostIdentities = addWebSSOServerHostIdentity(hostIdentities,
					webSSOProperties.getWebSSOServerInformation());
		}
		return hostIdentities;
	}
	
	private String constructPrincipal(HashMap<String, String> attributeMap) {
		String principalName = new String();
		Set<String> keySet = attributeMap.keySet();
		for (String key : keySet) {
			String value = attributeMap.get(key);
			principalName = principalName.concat(key
					+ WebSSOConstants.KEY_VALUE_PAIR_DELIMITER + value
					+ WebSSOConstants.ATTRIBUTE_DELIMITER);
		}
		principalName = principalName.substring(0, principalName.lastIndexOf(WebSSOConstants.ATTRIBUTE_DELIMITER));
		return principalName;
	}
	
	private List<String> addWebSSOServerHostIdentity(
			List<String> hostIdentities,
			WebSSOServerInformation webSSOServerInformation)
			throws AuthenticationConfigurationException {
		try {
			GlobusCredential globusCredential = new GlobusCredential(
					webSSOServerInformation.getHostCredentialCertificateFilePath(),
					webSSOServerInformation.getHostCredentialKeyFilePath());
			hostIdentities.add(globusCredential.getIdentity());
		} catch (GlobusCredentialException e) {
			throw new AuthenticationConfigurationException(
					"Unable to create the WebSSO Host Credentials using the configuration provided",e);
		}
		return hostIdentities;
	}
}
