package org.cagrid.fqp.test.remote;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestResult;

import org.cagrid.fqp.test.common.AggregationStory;
import org.cagrid.fqp.test.common.DataServiceDeploymentStory;
import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;
import org.cagrid.fqp.test.common.ServiceContainerSource;

/** 
 *  RemoteFqpSystemTests
 *  System tests for FQP using the remote Federated Query API
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 10:57:40 AM
 * @version $Id: RemoteFqpSystemTests.java,v 1.1 2008-07-16 17:02:19 dervin Exp $ 
 */
public class RemoteFqpSystemTests extends StoryBook {
    
    public static final String FQP_DIR_PROPERTY = "fqp.service.dir";
    
    private DataServiceDeploymentStory[] dataServiceDeployments;
    private FQPServiceDeploymentStory fqpDeployment;
    
    public RemoteFqpSystemTests() {
        super();
    }
    

    protected void stories() {
        // deploy two example SDK data services which pull from slightly different data
        DataServiceDeploymentStory exampleService1Deployment = 
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService1.zip"));
        DataServiceDeploymentStory exampleService2Deployment =
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService2.zip"));
        dataServiceDeployments = new DataServiceDeploymentStory[] {
            exampleService1Deployment, exampleService2Deployment
        };
        addStory(exampleService1Deployment);
        addStory(exampleService2Deployment);
        
        // deploy the FQP service
        fqpDeployment = new FQPServiceDeploymentStory(getFqpDir());
        addStory(fqpDeployment);
        
        // run the aggregation queries
        ServiceContainerSource[] containerSources = new ServiceContainerSource[] {
            exampleService1Deployment, exampleService2Deployment
        };
        
        FederatedQueryProcessorHelper queryHelper = 
            new FederatedQueryProcessorHelper(fqpDeployment);
        AggregationStory aggregationTests = new AggregationStory(containerSources, queryHelper);
        addStory(aggregationTests);
    }
    
    
    private File getFqpDir() {
        String value = System.getProperty(FQP_DIR_PROPERTY);
        Assert.assertNotNull("System property " + FQP_DIR_PROPERTY + " was not set!", value);
        File dir = new File(value);
        return dir;
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
        for (DataServiceDeploymentStory deployment : dataServiceDeployments) {
            ServiceContainer container = deployment.getServiceContainer();
            try {
                new StopContainerStep(container).runStep();
                new DestroyContainerStep(container).runStep();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        try {
            ServiceContainer fqpContainer = fqpDeployment.getServiceContainer();
            new StopContainerStep(fqpContainer).runStep();
            new DestroyContainerStep(fqpContainer).runStep();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
