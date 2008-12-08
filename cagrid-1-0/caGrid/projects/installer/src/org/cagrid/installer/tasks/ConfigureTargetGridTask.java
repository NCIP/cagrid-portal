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
public class ConfigureTargetGridTask extends CaGridAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public ConfigureTargetGridTask(String name, String description) {
		super(name, description, "configure");
	}
	
	@Override
	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String, String> env, Properties sysProps) throws Exception {
		return new AntTask("", "", model.getProperty(Constants.CAGRID_HOME) + "/build.xml",target, env, sysProps).execute(model);
	}

}
