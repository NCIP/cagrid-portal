/**
 * 
 */
package org.cagrid.installer.dorian;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.AntTask;
import org.cagrid.installer.tasks.DeployServiceTask;
import org.cagrid.installer.util.InstallerUtils;

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
		super(name, description, "dorian");

	}

	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {
		super.runAntTask(model, target, env, sysProps);

		String antTarget = "deployTomcatEndorsedJars";
		if (model.getMessage("container.type.globus").equals(
				model.getProperty(Constants.CONTAINER_TYPE))) {
			antTarget = "deployGlobusEndorsedJars";
		}
		
		model.setProperty(Constants.BUILD_FILE_PATH, InstallerUtils.getScriptsBuildFilePath());
		sysProps.setProperty("service.name", "dorian");
		new AntTask("", "", antTarget, env, sysProps).execute(model);

		return null;
	}

}
