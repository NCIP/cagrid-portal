/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GridServiceExistsException extends RuntimeException {

	/**
	 * 
	 */
	public GridServiceExistsException() {

	}

	/**
	 * @param message
	 */
	public GridServiceExistsException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public GridServiceExistsException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public GridServiceExistsException(String message, Throwable cause) {
		super(message, cause);

	}

}
