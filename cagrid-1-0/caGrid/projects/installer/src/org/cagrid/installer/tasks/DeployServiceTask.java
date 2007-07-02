/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DeployServiceTask extends CaGridAntTask {

	private String serviceName;

	protected CaGridInstallerModel model;

	/**
	 * @param name
	 * @param description
	 */
	public DeployServiceTask(String name, String description,
			String serviceName, CaGridInstallerModel model) {
		super(name, description, "deployTomcat");
		this.model = model;
		this.serviceName = serviceName;
	}

	protected Object runAntTask(Map state, String target, Map env,
			Properties sysProps) throws Exception {
		String antTarget = target;
		if(this.model.getMessage("container.type.globus").equals(this.model.getState().get(Constants.CONTAINER_TYPE))){
			antTarget = "deployGlobus"; 
		}
		new AntTask("", "", antTarget, env, sysProps).execute(state);

		return null;
	}

	protected String getBuildFilePath(Map state) {
		return state.get(Constants.CAGRID_HOME) + "/projects/"
				+ this.serviceName + "/build.xml";
	}

}
