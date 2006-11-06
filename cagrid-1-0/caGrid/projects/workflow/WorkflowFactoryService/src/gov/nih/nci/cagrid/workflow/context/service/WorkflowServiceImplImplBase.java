package gov.nih.nci.cagrid.workflow.context.service;

import gov.nih.nci.cagrid.workflow.service.ServiceConfiguration;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.ResourceHome;
/** 
 *  TODO:DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public abstract class WorkflowServiceImplImplBase {
    private ServiceConfiguration configuration;
	
	public WorkflowServiceImplImplBase() throws RemoteException {
	
	}
	
	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return this.configuration;
	}
	
	
	public gov.nih.nci.cagrid.workflow.context.service.globus.resource.WorkflowServiceHome getResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("home");
		return (gov.nih.nci.cagrid.workflow.context.service.globus.resource.WorkflowServiceHome)resource;
	}

	
	
	
	public gov.nih.nci.cagrid.workflow.service.globus.resource.WorkflowFactoryHome getWorkflowFactoryServiceResourceHome() throws Exception {
		ResourceHome resource = getResourceHome("workflowFactoryServiceHome");
		return (gov.nih.nci.cagrid.workflow.service.globus.resource.WorkflowFactoryHome)resource;
	}
	
	
	protected ResourceHome getResourceHome(String resourceKey) throws Exception {
		MessageContext ctx = MessageContext.getCurrentContext();

		ResourceHome resourceHome = null;
		
		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/" + resourceKey;
		try {
			javax.naming.Context initialContext = new InitialContext();
			resourceHome = (ResourceHome) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate resource home. : " + resourceKey, e);
		}

		return resourceHome;
	}
	
		
	
	


}

