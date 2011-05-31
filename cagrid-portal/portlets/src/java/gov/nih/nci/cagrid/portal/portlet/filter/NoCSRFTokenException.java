/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.filter;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class NoCSRFTokenException extends Exception {

	/**
	 * 
	 */
	public NoCSRFTokenException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoCSRFTokenException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoCSRFTokenException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoCSRFTokenException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
