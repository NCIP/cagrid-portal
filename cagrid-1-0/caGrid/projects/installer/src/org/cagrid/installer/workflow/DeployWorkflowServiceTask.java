package org.cagrid.installer.workflow;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.tasks.DeployServiceTask;

public class DeployWorkflowServiceTask extends DeployServiceTask{

	public DeployWorkflowServiceTask(String name, String description, String serviceName) {
		super(name, description, serviceName);
	}
	
	protected String getBuildFilePath(CaGridInstallerModel model) {
		return model.getServiceDestDir() + "/" + this.serviceName + "/WorkflowFactoryService"
				+ "/build.xml";
	}
}
