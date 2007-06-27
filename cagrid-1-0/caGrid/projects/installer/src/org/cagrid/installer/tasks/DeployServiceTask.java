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
public class DeployServiceTask extends CaGridAntTask {

	private String serviceName;

	/**
	 * @param name
	 * @param description
	 */
	public DeployServiceTask(String name, String description, String serviceName) {
		super(name, description, "deployTomcat");
		this.serviceName = serviceName;
	}

	protected String getBuildFilePath(Map state) {
		return state.get(Constants.CAGRID_HOME) + "/projects/"
				+ this.serviceName + "/build.xml";
	}

}
