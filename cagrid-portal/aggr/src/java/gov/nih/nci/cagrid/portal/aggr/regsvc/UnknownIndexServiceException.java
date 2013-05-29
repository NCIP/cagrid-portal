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
