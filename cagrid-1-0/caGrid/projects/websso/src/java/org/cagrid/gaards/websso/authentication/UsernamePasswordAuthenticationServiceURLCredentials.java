package org.cagrid.gaards.websso.authentication;

import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

public class UsernamePasswordAuthenticationServiceURLCredentials extends UsernamePasswordCredentials
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String authenticationServiceURL = null;


	public String getAuthenticationServiceURL()
	{
		return authenticationServiceURL;
	}


	public void setAuthenticationServiceURL(String authenticationServiceURL)
	{
		this.authenticationServiceURL = authenticationServiceURL;
	}


	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}


	@Override
	public boolean equals(Object obj)
	{
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }

        final UsernamePasswordAuthenticationServiceURLCredentials c = (UsernamePasswordAuthenticationServiceURLCredentials) obj;

        return this.getUsername().equals(c.getUsername())
            && this.getPassword().equals(c.getPassword())
            && this.authenticationServiceURL.equals(c.getAuthenticationServiceURL());
	}

	@Override
	public int hashCode()
	{
        return this.getUsername().hashCode() ^ this.getPassword().hashCode() ^ this.authenticationServiceURL.hashCode();
	}

}
