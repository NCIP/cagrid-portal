/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
