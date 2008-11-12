package org.cagrid.fqp.test.local.secure;

import gov.nih.nci.cagrid.fqp.processor.FederatedQueryEngine;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.test.common.DataServiceDeploymentStory;
import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;
import org.cagrid.fqp.test.common.QueryStory;
import org.junit.After;
import org.junit.Test;

/**
 * SecureLocalFQPSystemTests
 * Runs tests against the local Federated Query Engine using secure data services
 * and delegated credentials
 * 
 * @author David
 */
public class SecureLocalFQPSystemTests {
    
    private static Log logger = LogFactory.getLog(SecureLocalFQPSystemTests.class);
       
    private DataServiceDeploymentStory[] dataServiceDeployments;
    
    /*
    public SecureLocalFQPSystemTests() {
        super();
        setName("Secure Local FQP System Tests");
    }
    */
    
    @Test
    protected void secureLocalFQPSystemTests() {
        // deploy two example SDK data services with security enabled
        // which pull from slightly different data
        DataServiceDeploymentStory exampleService1Deployment = 
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService1.zip"), true);
        DataServiceDeploymentStory exampleService2Deployment =
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService2.zip"), true);
        // sources of data service containers.  This allows stories to grab
        // service containers after they've been created in the order of execution
        dataServiceDeployments = new DataServiceDeploymentStory[] {
            exampleService1Deployment, exampleService2Deployment
        };
        exampleService1Deployment.run();
        exampleService2Deployment.run();
        
        FederatedQueryEngine fqpEngine = new FederatedQueryEngine(null, null);
        FederatedQueryProcessorHelper fqpHelper = new FederatedQueryProcessorHelper(fqpEngine);
        
        QueryStory queryStory = new QueryStory(dataServiceDeployments, fqpHelper);
        queryStory.run();
    }
    

    /*
    public void run(TestResult result) {
        logger.debug("Starting Secure Local FQP Tests");
        super.run(result);
        cleanUp();
    }
    */
    
    @After
    private void cleanUp() {
        logger.debug("Cleaning Up Secure Local FQP Tests");
        for (DataServiceDeploymentStory deployment : dataServiceDeployments) {
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
