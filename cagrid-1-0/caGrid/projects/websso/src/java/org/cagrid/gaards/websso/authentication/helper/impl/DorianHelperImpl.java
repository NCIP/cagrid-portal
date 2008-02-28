package org.cagrid.gaards.websso.authentication.helper.impl;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.websso.authentication.helper.DorianHelper;
import org.cagrid.gaards.websso.beans.DorianInformation;
import org.cagrid.gaards.websso.exception.AuthenticationConfigurationException;
import org.cagrid.gaards.websso.exception.AuthenticationErrorException;
import org.globus.gsi.GlobusCredential;

public class DorianHelperImpl implements DorianHelper
{
		
	public DorianHelperImpl()
	{
		super();
	}

	
	public GlobusCredential obtainProxy(SAMLAssertion samlAssertion, DorianInformation dorianInformation) throws AuthenticationConfigurationException, AuthenticationErrorException
	{
		GlobusCredential globusCredential = null;
		
		IFSUserClient ifsUserClient = null;
		try
		{
			ifsUserClient = new IFSUserClient(dorianInformation.getDorianServiceURL());
		} catch (MalformedURIException e)
		{
			throw new AuthenticationConfigurationException("Invalid Dorian Service URL : " + e.getMessage());
		} 
		catch (RemoteException e)
		{
			throw new AuthenticationConfigurationException("Error accessing the Dorian Service : " + e.getMessage());
		}
		try
		{
			globusCredential = ifsUserClient.createProxy(samlAssertion, dorianInformation.getProxyLifeTime(), dorianInformation.getDelegationPathLength());
		} catch (DorianFault e)
		{
			throw new AuthenticationConfigurationException("Error accessing the Dorian Service : " + FaultUtil.printFaultToString(e));
		} 
		catch (DorianInternalFault e)
		{
			throw new AuthenticationConfigurationException("Error accessing the Dorian Service : " + FaultUtil.printFaultToString(e));
		} 
		catch (InvalidAssertionFault e)
		{
			throw new AuthenticationConfigurationException("Invalid SAML Assertion obtained from Authentication Service : " + FaultUtil.printFaultToString(e));
		} 
		catch (InvalidProxyFault e)
		{
			throw new AuthenticationConfigurationException("Error obtaining Proxy from Dorian : " + FaultUtil.printFaultToString(e));
		} 
		catch (UserPolicyFault e)
		{
			throw new AuthenticationConfigurationException("Policy Error occured obtaining Proxy from Dorian : " + FaultUtil.printFaultToString(e));
		} 
		catch (PermissionDeniedFault e)
		{
			throw new AuthenticationErrorException("Permission denied while obtaining Proxy from Dorian : " + FaultUtil.printFaultToString(e));
		}

		return globusCredential;

	}

}
