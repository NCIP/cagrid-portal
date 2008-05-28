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
