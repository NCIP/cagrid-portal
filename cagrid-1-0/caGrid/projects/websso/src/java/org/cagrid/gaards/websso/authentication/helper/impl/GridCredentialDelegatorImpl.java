package org.cagrid.gaards.websso.authentication.helper.impl;

import gov.nih.nci.cagrid.common.Utils;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.cds.client.DelegationUserClient;
import org.cagrid.gaards.cds.common.AllowedParties;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.common.ProxyLifetime;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.websso.authentication.helper.GridCredentialDelegator;
import org.cagrid.gaards.websso.beans.CredentialDelegationServiceInformation;
import org.cagrid.gaards.websso.exception.AuthenticationConfigurationException;
import org.globus.gsi.GlobusCredential;

public class GridCredentialDelegatorImpl implements GridCredentialDelegator
{

	private CredentialDelegationServiceInformation credentialDelegationServiceInformation = null;
	
	public GridCredentialDelegatorImpl(CredentialDelegationServiceInformation credentialDelegationServiceInformation)
	{
		super();
		this.credentialDelegationServiceInformation = credentialDelegationServiceInformation;
	}

	public String delegateGridCredential(GlobusCredential globusCredential, gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime credentialslifetime, List<String> hostIdentityList ) throws AuthenticationConfigurationException
	{

		ProxyLifetime credentialsCDSLifeTime = convertToCDSLifeTime(credentialslifetime);
		
		String[] hostIdentities = new String[hostIdentityList.size()];
		hostIdentityList.toArray(hostIdentities);
		
		AllowedParties allowedParties = new AllowedParties();
		allowedParties.setGridIdentity(hostIdentities);
		
		IdentityDelegationPolicy identityDelegationPolicy = new IdentityDelegationPolicy();
		identityDelegationPolicy.setAllowedParties(allowedParties);

		ProxyLifetime delegationProxyLifetime = new ProxyLifetime();
		delegationProxyLifetime.setHours(this.credentialDelegationServiceInformation.getDelegationLifetimeHours());
		delegationProxyLifetime.setMinutes(this.credentialDelegationServiceInformation.getDelegationLifetimeMinutes());
		delegationProxyLifetime.setSeconds(this.credentialDelegationServiceInformation.getDelegationLifetimeMinutes());
		
		DelegationUserClient client = null;
		try
		{
			client = new DelegationUserClient(this.credentialDelegationServiceInformation.getServiceURL(), globusCredential);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new AuthenticationConfigurationException("Error accessing the Delegation Service : " + e.getMessage());
		}

		DelegatedCredentialReference delegatedCredentialReference = null;
		try
		{
			delegatedCredentialReference = client.delegateCredential( delegationProxyLifetime, identityDelegationPolicy, credentialsCDSLifeTime);
		}
		catch (CDSInternalFault e)
		{
			throw new AuthenticationConfigurationException("Internal Error in the Delegation Service", e);
		}
		catch (DelegationFault e)
		{
			e.printStackTrace();
			throw new AuthenticationConfigurationException("Error accessing the Delegation Service, Unable to delegate credentials", e);
		}
		catch (PermissionDeniedFault e)
		{
			throw new AuthenticationConfigurationException("Error accessing the Delegation Service, Permission Denied", e);
		}
		catch (RemoteException e)
		{
			throw new AuthenticationConfigurationException("Error accessing the Delegation Service", e);
		}
		catch (MalformedURIException e)
		{
			throw new AuthenticationConfigurationException("Error accessing the Delegation Service, Please check the URL for Delegation Service ", e);
		}
		
		String serializedDelegatedCredentialReference = null;

		try
		{
			StringWriter stringWriter = new StringWriter();
			Utils.serializeObject(delegatedCredentialReference, new QName("http://cds.gaards.cagrid.org/CredentialDelegationService/DelegatedCredential/types", "DelegatedCredentialReference") , stringWriter, DelegationUserClient.class.getResourceAsStream("client-config.wsdd"));
			serializedDelegatedCredentialReference = stringWriter.toString();
		}
		catch (Exception e)
		{
			throw new AuthenticationConfigurationException("Unable to serialize the message Delegated Credentials", e);
		}

		return serializedDelegatedCredentialReference;
	}

	private ProxyLifetime convertToCDSLifeTime(gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime credentialslifetime)
	{
		return new ProxyLifetime(credentialslifetime.getHours(), credentialslifetime.getMinutes(), credentialslifetime.getSeconds());
	}

}
