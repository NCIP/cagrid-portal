package org.cagrid.fqp.test.remote;

import gov.nih.nci.cagrid.fqp.client.FederatedQueryProcessorClient;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.fqp.test.common.ServiceContainerSource;
import org.cagrid.fqp.test.remote.steps.AsynchronousQueryExecutionStep;

/** 
 *  AsynchronousExecutionStory
 *  Tests the asynchronous execution capabilities of the remote FQP service
 * 
 * @author David Ervin
 * 
 * @created Jun 30, 2008 12:48:50 PM
 * @version $Id: AsynchronousExecutionStory.java,v 1.1 2008-08-26 15:02:16 dervin Exp $ 
 */
public class AsynchronousExecutionStory extends Story {
    
    public static final String QUERIES_LOCATION = 
        "resources" + File.separator + "queries" + File.separator;
    public static final String GOLD_LOCATION = 
        "resources" + File.separator + "gold" + File.separator;
    
    public static final String SERVICE_NAME_BASE = "cagrid/ExampleSdkService";
    
    private ServiceContainerSource[] dataContainers = null;
    private ServiceContainerSource fqpContainerSource = null;

    public AsynchronousExecutionStory(ServiceContainerSource[] dataServiceContainers, ServiceContainerSource fqpContainerSource) {
        this.dataContainers = dataServiceContainers;
        this.fqpContainerSource = fqpContainerSource;
    }
    
    
    public String getName() {
        return "FQP Asynchronous Execution";
    }


    public String getDescription() {
        return "Tests FQP service's asynchronous query execution";
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        // figure out the URLs of the test services
        String[] serviceUrls = new String[dataContainers.length];
        for (int i = 0; i < dataContainers.length; i++) {
            ServiceContainer container = dataContainers[i].getServiceContainer();
            try {
                String base = container.getContainerBaseURI().toString();
                serviceUrls[i] = base + SERVICE_NAME_BASE + String.valueOf(i + 1);
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Error creating data service URL: " + ex.getMessage());
            }
        }
        
        // create a new FQP client from the FQP service container
        FederatedQueryProcessorClient fqpClient = null;
        try {
            fqpClient = new FederatedQueryProcessorClient(
                fqpContainerSource.getServiceContainer().getContainerBaseURI().toString() + 
                "cagrid/FederatedQueryProcessor");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error creating FQP client: " + ex.getMessage());
        }

        steps.add(new AsynchronousQueryExecutionStep(QUERIES_LOCATION + File.separator + "exampleDistributedJoin1.xml",
            GOLD_LOCATION + File.separator + "exampleDistributedJoin1_gold.xml", 
            fqpClient, serviceUrls));
        return steps;
    }
}
