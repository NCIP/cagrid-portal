/**
 * 
 */
package org.cagrid.installer.workflow;

import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.tasks.AntTask;
import org.cagrid.installer.tasks.CaGridInstallerAntTask;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigureBPELAdminSecurityTask extends CaGridInstallerAntTask {

	/**
	 * @param name
	 * @param description
	 * @param targetName
	 */
	public ConfigureBPELAdminSecurityTask(String name, String description,
			String targetName) {
		super(name, description, targetName);

	}
	
	protected Object runAntTask(CaGridInstallerModel model, String target, Map<String,String> env,
			Properties sysProps) throws Exception {
		
		//Unpack war
		new AntTask("", "", "unpack-bpel-admin-war", env, sysProps).execute(model);
		
		//Modify web.xml
		new AntTask("", "", "configure-bpel-admin-web-xml", env, sysProps).execute(model);
		
		//Create context config
		new AntTask("", "", "create-bpel-admin-context", env, sysProps).execute(model);
		
		//Create users file
		new AntTask("", "", "create-bpel-admin-users", env, sysProps).execute(model);
		
		return null;
	}

}
