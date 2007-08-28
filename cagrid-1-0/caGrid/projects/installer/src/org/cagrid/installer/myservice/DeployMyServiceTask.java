/**
 * 
 */
package org.cagrid.installer.myservice;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.tasks.DeployServiceTask;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DeployMyServiceTask extends DeployServiceTask {

	/**
	 * @param name
	 * @param description
	 * @param serviceName
	 * @param model
	 */
	public DeployMyServiceTask(String name, String description,
			String serviceName) {
		super(name, description, serviceName);
	}

	protected String getBuildFilePath(CaGridInstallerModel model) {
		return model.getProperty(Constants.MY_SERVICE_DIR) + "/build.xml";
	}

}
