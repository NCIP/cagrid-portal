/**
 * 
 */
package org.cagrid.installer.gridgrouper;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.AntTask;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureGridGrouperTask extends CaGridInstallerAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public ConfigureGridGrouperTask(String name, String description
			) {
		super(name, description, null);

	}
	
	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {

		//Configure grouper.hibernate.properties
		model.setProperty(Constants.BUILD_FILE_PATH, getBuildFilePath(model));
		
		new AntTask("", "", "configure-gridgrouper-hibernate", env, sysProps).execute(model);
		
		
		model.setProperty(Constants.BUILD_FILE_PATH, model.getServiceDestDir() + "/gridgrouper/build.xml");
		
		//Run grouperInit
		new AntTask("", "", "grouperInit", env, sysProps).execute(model);
		
		//Run addAdmin
		model.setProperty("gridId.input", model.getProperty(Constants.GRID_GROUPER_ADMIN_IDENT));
		new AntTask("", "", "addAdmin", env, sysProps).execute(model);

		return null;
	}

}
