/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class UnknownIndexServiceException extends RuntimeException {

	/**
	 * 
	 */
	public UnknownIndexServiceException() {

	}

	/**
	 * @param arg0
	 */
	public UnknownIndexServiceException(String arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 */
	public UnknownIndexServiceException(Throwable arg0) {
		super(arg0);

	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UnknownIndexServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);

	}

}
