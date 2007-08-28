/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class InitPortalDBTask extends CaGridAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public InitPortalDBTask(String name, String description, String targetName) {
		super(name, description, targetName);
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.CaGridAntTask#getBuildFilePath(org.cagrid.installer.model.CaGridInstallerModel)
	 */
	@Override
	protected String getBuildFilePath(CaGridInstallerModel model) {
		return model.getServiceDestDir() + "/portal/build.xml";
	}

	/* (non-Javadoc)
	 * @see org.cagrid.installer.tasks.CaGridAntTask#runAntTask(org.cagrid.installer.model.CaGridInstallerModel, java.lang.String, java.util.Map, java.util.Properties)
	 */
	@Override
	protected Object runAntTask(CaGridInstallerModel model, String target,
			Map<String, String> env, Properties sysProps) throws Exception {

		
		
		return null;
	}

}
