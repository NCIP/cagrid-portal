/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureTomcatTask extends CaGridInstallerAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public ConfigureTomcatTask(String name, String description) {
		super(name, description, null);
	}
	
	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {

		boolean secure = model.isTrue(Constants.USE_SECURE_CONTAINER);

		if (!secure) {
			setStepCount(3);
			new AntTask("", "", getBuildFilePath(), "fix-web-xml", env, sysProps).execute(model);
			setLastStep(1);
			new AntTask("", "", getBuildFilePath(), "configure-tomcat-server-config", env, sysProps)
			.execute(model);
		} else {
			setStepCount(5);
			new AntTask("", "", getBuildFilePath(), "insert-secure-connector", env, sysProps)
					.execute(model);
			setLastStep(1);
			new AntTask("", "", getBuildFilePath(), "insert-valve", env, sysProps).execute(model);
			setLastStep(2);
			new AntTask("", "", getBuildFilePath(), "set-global-cert-and-key-paths", env, sysProps)
					.execute(model);
			setLastStep(3);
			new AntTask("", "", getBuildFilePath(), "fix-secure-web-xml", env, sysProps)
					.execute(model);
			setLastStep(4);
			new AntTask("", "", getBuildFilePath(), "configure-tomcat-server-config", env, sysProps)
					.execute(model);
		}
		
		return null;
	}

}
