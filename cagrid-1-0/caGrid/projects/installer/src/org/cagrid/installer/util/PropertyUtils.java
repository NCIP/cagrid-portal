/**
 * 
 */
package org.cagrid.installer.util;

import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PropertyUtils {
	
	public static String getRequiredProperty(Map state, String name) {
		String value = (String) state.get(name);
		if(value == null){
			throw new IllegalStateException("Required property '" + name + "' not found in state.");
		}
		return value;
	}

}
