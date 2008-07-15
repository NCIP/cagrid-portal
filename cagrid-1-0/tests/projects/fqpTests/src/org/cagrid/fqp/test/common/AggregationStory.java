package org.cagrid.fqp.test.common;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.fqp.test.common.steps.AggregationStep;

/** 
 *  AggregationStory
 *  Tests the aggregation capabilities of the Federated Query Engine
 * 
 * @author David Ervin
 * 
 * @created Jun 30, 2008 12:48:50 PM
 * @version $Id: AggregationStory.java,v 1.2 2008-07-15 19:44:13 dervin Exp $ 
 */
public class AggregationStory extends Story {
    
    public static final String QUERIES_LOCATION = "resources" + File.separator + "queries";
    public static final String GOLD_LOCATION = "resources" + File.separator + "gold" + File.separator + "aggregation";
    
    public static final String SERVICE_NAME_BASE = "cagrid/ExampleSdkService";
    
    private ServiceContainerSource[] dataContainers = null;
    private FederatedQueryProcessorHelper queryHelper = null;
    
    public AggregationStory(ServiceContainerSource[] dataServiceContainers,
        FederatedQueryProcessorHelper queryHelper) {
        this.dataContainers = dataServiceContainers;
        this.queryHelper = queryHelper;
    }
    

    public String getDescription() {
        return "Tests the aggregation capabilities of the Federated Query Engine";
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
        
        steps.add(new AggregationStep(QUERIES_LOCATION + File.separator + "exampleAggregation1.xml",
            GOLD_LOCATION + File.separator + "exampleAggregation1_gold.xml",
            queryHelper, serviceUrls));
        return steps;
    }
}
