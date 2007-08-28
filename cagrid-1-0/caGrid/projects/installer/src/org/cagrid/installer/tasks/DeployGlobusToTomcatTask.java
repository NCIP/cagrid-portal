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
public class DeployGlobusToTomcatTask extends CaGridInstallerAntTask {

	/**
	 * 
	 */
	public DeployGlobusToTomcatTask(String name, String description) {
		super(name, description, null);
	}

	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {

		boolean secure = model.isTrue(Constants.USE_SECURE_CONTAINER);

		setStepCount(1);
		if (!secure) {
			new AntTask("", "", "globus-deploy-tomcat", env, sysProps)
					.execute(model);
		} else {
			new AntTask("", "", "globus-deploy-secure-tomcat", env, sysProps)
					.execute(model);
		}
		

		return null;
	}

}
