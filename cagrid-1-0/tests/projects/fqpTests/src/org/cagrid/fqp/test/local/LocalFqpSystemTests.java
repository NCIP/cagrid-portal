package org.cagrid.fqp.test.local;

import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.File;

import junit.framework.TestResult;

import org.cagrid.fqp.test.common.AggregationStory;
import org.cagrid.fqp.test.common.ServiceContainerSource;
import org.cagrid.fqp.test.common.DataServiceDeploymentStory;
import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;

/** 
 *  LocalFqpSystemTests
 *  System tests for FQP using the local Federated Query API
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 10:57:40 AM
 * @version $Id: LocalFqpSystemTests.java,v 1.2 2008-07-15 19:44:14 dervin Exp $ 
 */
public class LocalFqpSystemTests extends StoryBook {
    
    private DataServiceDeploymentStory[] deployments;
    
    public LocalFqpSystemTests() {
        super();
    }
    

    protected void stories() {
        // deploy two example SDK data services which pull from slightly different data
        DataServiceDeploymentStory exampleService1Deployment = 
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService1.zip"));
        DataServiceDeploymentStory exampleService2Deployment =
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService2.zip"));
        deployments = new DataServiceDeploymentStory[] {
            exampleService1Deployment, exampleService2Deployment
        };
        addStory(exampleService1Deployment);
        addStory(exampleService2Deployment);
        
        // run the local aggregation queries
        ServiceContainerSource[] containerSources = new ServiceContainerSource[] {
            exampleService1Deployment, exampleService2Deployment
        };
        FederatedQueryProcessorHelper queryHelper = new FederatedQueryProcessorHelper(new FederatedQueryEngine());
        AggregationStory aggregationTests = new AggregationStory(containerSources, queryHelper);
        addStory(aggregationTests);
    }
    
    
    public void run(TestResult result) {
        System.out.println("RUNNING TESTS");
        System.out.println("RUNNING TESTS");
        System.out.println("RUNNING TESTS");
        super.run(result);
        cleanUp();
    }
    
    
    private void cleanUp() {
        System.out.println("CLEANING UP TESTING RESOURCES");
        System.out.println("CLEANING UP TESTING RESOURCES");
        System.out.println("CLEANING UP TESTING RESOURCES");
        for (DataServiceDeploymentStory deployment : deployments) {
            ServiceContainer container = deployment.getServiceContainer();
            try {
                new StopContainerStep(container).runStep();
                new DestroyContainerStep(container).runStep();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}
