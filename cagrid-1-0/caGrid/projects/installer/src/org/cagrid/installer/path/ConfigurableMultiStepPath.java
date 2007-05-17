/**
 * 
 */
package org.cagrid.installer.path;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigurableMultiStepPath extends MultiStepPath {
	
	private Map<String,String> globalProperties = new HashMap<String,String>();

	public Map<String, String> getGlobalProperties() {
		return globalProperties;
	}

	public void setGlobalProperties(Map<String, String> globalProperties) {
		this.globalProperties = globalProperties;
	} 

}
