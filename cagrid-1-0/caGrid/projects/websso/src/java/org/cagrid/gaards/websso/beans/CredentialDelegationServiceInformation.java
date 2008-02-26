package org.cagrid.gaards.websso.beans;

import java.io.Serializable;

public class CredentialDelegationServiceInformation implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	private String serviceURL = null;
	
	private int delegationLifetimeHours = 12;

	private int delegationLifetimeMinutes = 0;

	private int delegationLifetimeSeconds = 0;

	public String getServiceURL()
	{
		return serviceURL;
	}

	public void setServiceURL(String serviceURL)
	{
		this.serviceURL = serviceURL;
	}

	public int getDelegationLifetimeHours()
	{
		return delegationLifetimeHours;
	}

	public void setDelegationLifetimeHours(int delegationLifetimeHours)
	{
		this.delegationLifetimeHours = delegationLifetimeHours;
	}

	public int getDelegationLifetimeMinutes()
	{
		return delegationLifetimeMinutes;
	}

	public void setDelegationLifetimeMinutes(int delegationLifetimeMinutes)
	{
		this.delegationLifetimeMinutes = delegationLifetimeMinutes;
	}

	public int getDelegationLifetimeSeconds()
	{
		return delegationLifetimeSeconds;
	}

	public void setDelegationLifetimeSeconds(int delegationLifetimeSeconds)
	{
		this.delegationLifetimeSeconds = delegationLifetimeSeconds;
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

}
