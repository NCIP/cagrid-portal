/**
 * $Id: AuthenticationProviderException.java,v 1.1 2006-09-15 10:52:46 joshua Exp $
 *
 */
package gov.nih.nci.cagrid.authentication.common;

/**
 *
 * @version $Revision: 1.1 $
 * @author Joshua Phillips
 *
 */
public class AuthenticationProviderException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -161046673913742365L;

    /**
     * 
     */
    public AuthenticationProviderException() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public AuthenticationProviderException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public AuthenticationProviderException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public AuthenticationProviderException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

}
