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
public class CompileCaGridTask extends CaGridAntTask {


	/**
	 * @param name
	 * @param description
	 */
	public CompileCaGridTask(String name, String description) {
		super(name, description, "all");
	}
	
	protected String getBuildFilePath(Map state){
		return state.get(Constants.CAGRID_HOME) + "/build.xml";
	}

}
