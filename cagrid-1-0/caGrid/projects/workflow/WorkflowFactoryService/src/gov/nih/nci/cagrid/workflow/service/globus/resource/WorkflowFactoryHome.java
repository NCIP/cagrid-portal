package gov.nih.nci.cagrid.workflow.service.globus.resource;

import gov.nih.nci.cagrid.workflow.stubs.types.WMSInputType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.SimpleResourceKey;
import org.globus.wsrf.impl.SingletonResourceHome;
import org.globus.wsrf.jndi.Initializable;


/**
 * This class implements a resource home
 */

public class WorkflowFactoryHome extends SingletonResourceHome implements Initializable {

	static final Log logger = LogFactory.getLog(WorkflowFactoryHome.class);


	public Resource findSingleton() {
		logger.info("Creating a single resource.");
		try {
			gov.nih.nci.cagrid.workflow.service.globus.resource.WorkflowFactoryResource 
				resource = new gov.nih.nci.cagrid.workflow.service.globus.resource.WorkflowFactoryResource();
			resource.initialize();
			return resource;
		} catch (Exception e) {
			logger.error("Exception when creating the resource: " + e);
			return null;
		}
	}


	public Resource find(ResourceKey key) throws ResourceException {
		gov.nih.nci.cagrid.workflow.service.globus.resource.WorkflowFactoryResource 
		resource = (gov.nih.nci.cagrid.workflow.service.globus.resource.WorkflowFactoryResource) super.find(key);
		// each time the resource is looked up, do a lazy refreash of
		// registration.
		resource.refreshRegistration(false);
		return resource;
	}
	
	
	/**
	 * Initialze the singleton resource, when the home is initialized.
	 */
	public void initialize() throws Exception {
		logger.info("Attempting to initialize resource.");
		Resource resource = find(null);
		if (resource == null) {
			logger.error("Unable to initialize resource!");
		} else {
			logger.info("Successfully initialized resource.");
		}
	}
}