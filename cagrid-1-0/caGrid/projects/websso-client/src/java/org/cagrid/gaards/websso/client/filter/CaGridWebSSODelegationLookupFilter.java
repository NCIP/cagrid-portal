package org.cagrid.gaards.websso.client.filter;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.cagrid.gaards.cds.client.DelegatedCredentialUserClient;
import org.cagrid.gaards.cds.delegated.stubs.types.DelegatedCredentialReference;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

public class CaGridWebSSODelegationLookupFilter implements Filter
{

	private static final String IS_SESSION_ATTRIBUTES_LOADED = "IS_SESSION_ATTRIBUTES_LOADED";
	private static final String IS_GRID_CREDENTIAL_LOADED = "IS_GRID_CREDENTIAL_LOADED";
	
	private static final String CAGRID_SSO_DELEGATION_SERVICE_EPR = "CAGRID_SSO_DELEGATION_SERVICE_EPR";
	private static final String CAGRID_SSO_GRID_CREDENTIAL = "CAGRID_SSO_GRID_CREDENTIAL";
	
	private static final String CERTIFICATE_FILE_PATH = "certificate-file-path";
	private static final String KEY_FILE_PATH = "key-file-path";
	
	private String certificateFilePath = null;
	private String keyFilePath = null;
	
	public void destroy()
	{
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
	{
		HttpSession session = ((HttpServletRequest)request).getSession();
		Boolean isGridCredentialLoaded = (Boolean) session.getAttribute(IS_GRID_CREDENTIAL_LOADED);
		if (null == isGridCredentialLoaded || isGridCredentialLoaded == Boolean.FALSE)
		{
			Boolean isSessionLoaded = (Boolean) session.getAttribute(IS_SESSION_ATTRIBUTES_LOADED);
			GlobusCredential hostCredential = null;
			if (null == isSessionLoaded || isSessionLoaded == Boolean.FALSE)
			{
				throw new ServletException("WebSSO Attributes are not loaded in the Session");
			}
			else
			{
				String delegationEPR = (String)session.getAttribute(CAGRID_SSO_DELEGATION_SERVICE_EPR);
				try
				{
					hostCredential = new GlobusCredential(certificateFilePath,keyFilePath);
				}
				catch (GlobusCredentialException e)
				{
					throw new ServletException("Unable to create Host Credentials from the Certificate and Key File", e);
				}
				DelegatedCredentialReference delegatedCredentialReference = null;
				try
				{
					delegatedCredentialReference = (DelegatedCredentialReference) ObjectDeserializer.deserialize(new InputSource(new StringReader(delegationEPR)), DelegatedCredentialReference.class);
				}
				catch (DeserializationException e)
				{
					throw new ServletException("Unable to deserialize the Delegation Reference", e);
				}
				DelegatedCredentialUserClient delegatedCredentialUserClient = null;
				try
				{
					delegatedCredentialUserClient = new DelegatedCredentialUserClient(delegatedCredentialReference, hostCredential);
				}
				catch (Exception e)
				{
					throw new ServletException("Unable to Initialize the Delegation Lookup Client", e);
				}
				GlobusCredential userCredential;
				try
				{
					userCredential = delegatedCredentialUserClient.getDelegatedCredential();
				}
				catch (CDSInternalFault e)
				{
					throw new ServletException("Error retrieve the Delegated Credentials", e);
				}
				catch (DelegationFault e)
				{
					throw new ServletException("Error retrieve the Delegated Credentials", e);
				}
				catch (PermissionDeniedFault e)
				{
					throw new ServletException("Error retrieve the Delegated Credentials", e);
				}
				session.setAttribute(CAGRID_SSO_GRID_CREDENTIAL, userCredential);
				session.setAttribute(IS_GRID_CREDENTIAL_LOADED, Boolean.TRUE);
			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.certificateFilePath = filterConfig.getInitParameter(CERTIFICATE_FILE_PATH);
		this.keyFilePath = filterConfig.getInitParameter(KEY_FILE_PATH);
		
	}

}
