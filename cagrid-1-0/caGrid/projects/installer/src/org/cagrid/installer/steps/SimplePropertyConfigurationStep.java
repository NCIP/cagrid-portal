/**
 * 
 */
package org.cagrid.installer.steps;

import java.util.Map;

import org.cagrid.common.wizard.steps.BasePropertyConfigureStep;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SimplePropertyConfigurationStep extends BasePropertyConfigureStep {

	/**
	 * @param globalMap
	 */
	public SimplePropertyConfigurationStep(Map<String,String> globalMap, Map<String,String> localMap) {
		super(globalMap);

		for(String propName : localMap.keySet()){
			String propDescription = localMap.get(propName);
			String defaultValue = globalMap.get(propName);
			addTextOption(propName, defaultValue, propDescription);
		}
		setComplete(true);
		
	}

}
