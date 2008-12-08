/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DeployServiceTask extends CaGridAntTask {

	protected String serviceName;

	/**
	 * @param name
	 * @param description
	 */
	public DeployServiceTask(String name, String description,
			String serviceName) {
		super(name, description, null);
		this.serviceName = serviceName;
	}

	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {
		String antTarget = getDeployTomcatTarget();
		if (!model.isTomcatContainer()) {
			antTarget = getDeployGlobusTarget();
		}
		new AntTask("", "", getBuildFilePath(model), antTarget, env, sysProps).execute(model);

		return null;
	}

	protected String getDeployTomcatTarget() {
		return "deployTomcat";
	}

	protected String getDeployGlobusTarget() {
		return "deployGlobus";
	}

	protected String getBuildFilePath(CaGridInstallerModel model) {
		return model.getServiceDestDir() + "/" + this.serviceName
				+ "/build.xml";
	}

}
