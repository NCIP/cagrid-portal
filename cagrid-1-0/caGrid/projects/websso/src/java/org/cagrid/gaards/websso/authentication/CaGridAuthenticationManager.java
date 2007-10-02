package org.cagrid.gaards.websso.authentication;

import gov.nih.nci.cagrid.authentication.bean.BasicAuthenticationCredential;
import gov.nih.nci.cagrid.authentication.bean.Credential;
import gov.nih.nci.cagrid.authentication.client.AuthenticationClient;
import gov.nih.nci.cagrid.dorian.client.IFSUserClient;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.globus.gsi.GlobusCredential;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationManager;
import org.jasig.cas.authentication.MutableAuthentication;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

/**
 * CaGridAuthenticationManager
 * 
 * @author oster
 * @created Oct 2, 2007 12:40:18 PM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class CaGridAuthenticationManager implements AuthenticationManager {

	public static final int DEFAULT_LIFETIME_HOURS = 12;

	public static final int DEFAULT_LIFETIME_MINUTES = 0;

	public static final int DEFAULT_LIFETIME_SECONDS = 0;

	public static final int DEFAULT_DELEGATION_PATH_LENGTH = 0;
	public static final String AUTHENTICATION_SERVICE_URL = "https://dorian.cagrid.org:6443/wsrf/services/cagrid/Dorian";
	public static final String DORIAN_URL = "https://dorian.cagrid.org:6443/wsrf/services/cagrid/Dorian";

	public CaGridAuthenticationManager() {
	}

	public Authentication authenticate(Credentials credentials)
			throws AuthenticationException {
		try {
			final String username = ((UsernamePasswordCredentials) credentials)
					.getUsername();
			final String password = ((UsernamePasswordCredentials) credentials)
					.getPassword();

			BasicAuthenticationCredential auth = new BasicAuthenticationCredential();
			auth.setUserId(username);
			auth.setPassword(password);
			Credential cred = new Credential();
			cred.setBasicAuthenticationCredential(auth);

			ProxyLifetime lifetime = new ProxyLifetime();
			lifetime.setHours(DEFAULT_LIFETIME_HOURS);
			lifetime.setMinutes(DEFAULT_LIFETIME_MINUTES);
			lifetime.setSeconds(DEFAULT_LIFETIME_SECONDS);

			System.out.print("Authenticating with the service "
					+ AUTHENTICATION_SERVICE_URL + ".....");

			AuthenticationClient client = new AuthenticationClient(
					AUTHENTICATION_SERVICE_URL, cred);
			SAMLAssertion saml = client.authenticate();
			System.out.println("SUCCESSFUL");
			System.out.print("Requesting a proxy from the Dorian "
					+ AUTHENTICATION_SERVICE_URL + ".....");

			IFSUserClient dorian = new IFSUserClient(DORIAN_URL);
			GlobusCredential proxy = dorian.createProxy(saml, lifetime,
					DEFAULT_DELEGATION_PATH_LENGTH);
			System.out.println("SUCCESSFUL");
			
			//TODO: Validate Proxy against Trust Fabric
			
			
			System.out.println();
			System.out.println("Grid Proxy Certificate Summary");
			System.out.println("==============================");
			System.out.println("Grid Identity: " + proxy.getIdentity());
			System.out.println("Issuer: " + proxy.getIssuer());
			Calendar c = new GregorianCalendar();
			c.add(Calendar.SECOND, (int) proxy.getTimeLeft());
			System.out.println("Expires: " + c.getTime().toString());
			System.out.println("Strength: " + proxy.getStrength() + " bits.");
			System.out.println();
			Principal p = new SimplePrincipal(proxy.getIdentity());
			return new MutableAuthentication(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw BadCredentialsAuthenticationException.ERROR;
		}
	}
}
