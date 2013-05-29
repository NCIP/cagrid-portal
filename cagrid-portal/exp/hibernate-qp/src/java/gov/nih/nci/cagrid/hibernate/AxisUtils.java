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
package gov.nih.nci.cagrid.hibernate;

import org.apache.axis.MessageContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class AxisUtils {

	/**
	 * 
	 */
	public AxisUtils() {

	}
	
	public static String getOption(String param, String defaultValue) {
		String value = (String) MessageContext.getCurrentContext()
				.getAxisEngine().getOption(param);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

}
