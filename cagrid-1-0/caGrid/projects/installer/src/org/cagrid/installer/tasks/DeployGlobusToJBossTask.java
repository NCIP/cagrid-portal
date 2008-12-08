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
public class DeployGlobusToJBossTask extends CaGridAntTask {

	/**
	 * 
	 */
	public DeployGlobusToJBossTask(String name, String description) {
		super(name, description, null);
	}

	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {

		boolean secure = model.isTrue(Constants.USE_SECURE_CONTAINER);
		sysProps.put("jboss.dir", model.getProperty(Constants.JBOSS_HOME));
		
		setStepCount(1);
		if (!secure) {
			new AntTask("", "", model.getProperty(Constants.CAGRID_HOME) + "/share/jboss/jboss.xml", "deployJBoss", env, sysProps)
					.execute(model);
		} else {
			new AntTask("", "", model.getProperty(Constants.CAGRID_HOME) + "/share/jboss/jboss.xml", "deploySecureJBoss", env, sysProps)
					.execute(model);
		}
		

		return null;
	}

}
