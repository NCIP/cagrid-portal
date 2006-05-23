/*
 * Created on May 22, 2006
 */
package gov.nih.nci.cagrid.ntsec;

import java.net.UnknownHostException;

import javax.security.auth.login.LoginException;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

/**
 * NTAuthentication is an implementation of authenticating using NT 4.0 authentication.  It uses
 * the JCIFS library, which is a native Java implementation of NT 4.0 authentication.
 * @author Patrick McConnell
 */
public class NTAuthentication
{
	/**
	 * The IP or domain name of the machine that will perform the authentication
	 */
	private String domainController;
	
	/**
	 * Construct a new NTAuthentication object for performing NT 4.0 authentication
	 * 
	 * @param domainController the IP or domain name of the machine that will perform the 
	 * authentication
	 */
	public NTAuthentication(String domainController)
	{
		super();
		
		this.domainController = domainController;
	}
	
	/**
	 * Authenticates
	 * @param domain
	 * @param name
	 * @param password
	 * @return
	 * @throws LoginException
	 * @throws UnknownHostException
	 */
	public boolean authenticate(String domain, String name, String password) 
		throws LoginException, UnknownHostException
	{
//		UniAddress domainController = new UniAddress(
//			//NbtAddress.getLocalHost()
//			//NbtAddress.getByName("ccis1716.duhs.duke.edu")
//			//NbtAddress.getByName("152.16.195.32")
//			//NbtAddress.getByName("152.16.195.32", 'U', "00")
//			NbtAddress.getByName("127.0.0.1", 'U', "00")
//		);
		//UniAddress domainController = UniAddress.getByName("192.168.1.15");
		try {
			UniAddress domainController = UniAddress.getByName(this.domainController);
			NtlmPasswordAuthentication authentication =	new NtlmPasswordAuthentication(
				domain, name, password
			);
			SmbSession.logon(domainController, authentication);
		} catch(SmbAuthException sae ) {
		    // AUTHENTICATION FAILURE
		    return false;
		} catch(SmbException se) {
		    throw new LoginException(getMessage(se));
		}
		return true;

	}
	
	private String getMessage(SmbException se)
	{
		String msg = se.getMessage();
		if (msg != null && ! msg.equals("")) return msg;
		Throwable t = se.getRootCause();
		if (t == null) return msg;
		msg = t.getMessage();
		return msg;
	}

	public static void main(String[] args)
		throws Exception
	{
		try {
			boolean success = new NTAuthentication("152.16.7.56").authenticate("canctr", "mccon012", "");
			System.out.println(success);
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}
}
