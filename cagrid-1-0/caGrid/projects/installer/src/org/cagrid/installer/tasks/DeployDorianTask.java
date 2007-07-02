/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DeployDorianTask extends DeployServiceTask {

	/**
	 * @param name
	 * @param description
	 * @param serviceName
	 * @param model
	 */
	public DeployDorianTask(String name, String description,
			CaGridInstallerModel model) {
		super(name, description, "dorian", model);

	}

	protected Object runAntTask(Map state, String target, Map env,
			Properties sysProps) throws Exception {
		super.runAntTask(state, target, env, sysProps);

		String antTarget = "deployTomcatEndorsedJars";
		if (this.model.getMessage("container.type.globus").equals(
				this.model.getState().get(Constants.CONTAINER_TYPE))) {
			antTarget = "deployGlobusEndorsedJars";
		}
		Map m = new HashMap(state);
		m.put(Constants.BUILD_FILE_PATH, state.get(Constants.CAGRID_HOME)
				+ "/projects/installer/deployer/build.xml");
		new AntTask("", "", antTarget, env, sysProps).execute(m);

		return null;
	}

}
