package gov.nih.nci.cagrid.workflow.tests.sample2.service.globus.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.globus.wsrf.impl.SingletonResourceHome;
import org.globus.wsrf.jndi.Initializable;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.ResourceContextException;


/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements the resource home for the resource type represented
 * by this service.
 * 
 * @created by Introduce Toolkit version 1.1
 * 
 */
public class BaseResourceHome extends SingletonResourceHome implements Initializable {

	static final Log logger = LogFactory.getLog(BaseResourceHome.class);


	public Resource findSingleton() {
		logger.info("Creating a single resource.");
		try {
			WorkflowTestService2Resource resource = new WorkflowTestService2Resource();
			resource.initialize();
			return resource;
		} catch (Exception e) {
			logger.error("Exception when creating the resource: " + e);
			return null;
		}
	}
	
	public WorkflowTestService2Resource getResource(){
		WorkflowTestService2Resource serviceBaseResource;
		try {
			serviceBaseResource = (WorkflowTestService2Resource)ResourceContext.getResourceContext().getResource();
		} catch (ResourceContextException e) {
			return null;
		} catch (ResourceException e) {
			return null;
		}
		return serviceBaseResource;
	}


	public Resource find(ResourceKey key) throws ResourceException {
		WorkflowTestService2Resource resource = (WorkflowTestService2Resource) super.find(key);
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
	
    /**
     * Get the resouce that is being addressed in this current context
     */
    public WorkflowTestService2Resource getAddressedResource() throws Exception {
        WorkflowTestService2Resource thisResource;
        thisResource = (WorkflowTestService2Resource) ResourceContext.getResourceContext().getResource();
        return thisResource;
    }
}