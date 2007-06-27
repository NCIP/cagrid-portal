/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;

import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GenerateCATask extends CaGridAntTask {

	/**
	 * @param name
	 * @param description
	 */
	public GenerateCATask(String name, String description) {
		super(name, description, "generate-ca");
	}
	
	protected String getBuildFilePath(Map state){
		return state.get(Constants.CAGRID_HOME) + "/projects/installer/deployer/build.xml";
	}
}
