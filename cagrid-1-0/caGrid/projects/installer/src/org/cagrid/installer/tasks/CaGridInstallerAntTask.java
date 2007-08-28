/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.util.InstallerUtils;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CaGridInstallerAntTask extends CaGridAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public CaGridInstallerAntTask(String name, String description,
			String targetName) {
		super(name, description, targetName);
	}

	@Override
	protected String getBuildFilePath(CaGridInstallerModel model) {
		return InstallerUtils.getScriptsBuildFilePath();
	}
	
	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {
		return new AntTask("", "", target, env, sysProps).execute(model);
	}
}
