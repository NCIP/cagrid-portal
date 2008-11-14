package org.cagrid.fqp.test.remote.secure;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import org.cagrid.fqp.test.common.ServiceContainerSource;


/**
 * SecureQueryStory Invokes FQP queries using a delegated credential
 * 
 * @author David
 */
public class SecureQueryStory extends Story {
    public static final String SERVICE_NAME_BASE = "cagrid/ExampleSdkService";

    private ServiceContainerSource[] dataContainerSources = null;
    private ServiceContainerSource cdsContainerSource = null;
    private ServiceContainerSource fqpContainerSource = null;


    public SecureQueryStory(ServiceContainerSource[] dataContainers, ServiceContainerSource cdsContainer,
        ServiceContainerSource fqpContainer) {
        this.dataContainerSources = dataContainers;
        this.cdsContainerSource = cdsContainer;
        this.fqpContainerSource = fqpContainer;
    }


    public String getName() {
        return "Secure FQP with CDS Query Story";
    }


    public String getDescription() {
        return "Invokes FQP queries using a delegated credential";
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        // figure out the URLs of the test services
        String[] serviceUrls = new String[dataContainerSources.length];
        for (int i = 0; i < dataContainerSources.length; i++) {
            ServiceContainer container = dataContainerSources[i].getServiceContainer();
            try {
                String base = container.getContainerBaseURI().toString();
                serviceUrls[i] = base + SERVICE_NAME_BASE + String.valueOf(i + 1);
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Error creating data service URL: " + ex.getMessage());
            }
        }
        
        ServiceContainer fqpContainer = fqpContainerSource.getServiceContainer();
        
        ServiceContainer cdsContainer = cdsContainerSource.getServiceContainer();
        
        // delegate a credential to CDS
        

        return steps;
    }
}
