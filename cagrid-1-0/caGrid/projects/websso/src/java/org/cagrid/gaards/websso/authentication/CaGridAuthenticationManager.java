package org.cagrid.gaards.websso.authentication;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.AuthenticationManager;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;


/**
 * CaGridAuthenticationManager
 * 
 * @author oster
 * @created Oct 2, 2007 12:40:18 PM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class CaGridAuthenticationManager implements AuthenticationManager {

    public CaGridAuthenticationManager() {
    }


    public Authentication authenticate(Credentials arg0) throws AuthenticationException {
        throw BadCredentialsAuthenticationException.ERROR;
    }
}
