package org.cagrid.fqp.test.remote;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.StoryBook;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.test.common.AggregationStory;
import org.cagrid.fqp.test.common.DataServiceDeploymentStory;
import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;
import org.cagrid.fqp.test.common.QueryStory;
import org.cagrid.fqp.test.common.ServiceContainerSource;

/** 
 *  RemoteFqpSystemTests
 *  System tests for FQP using the remote Federated Query API
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 10:57:40 AM
 * @version $Id: RemoteFqpSystemTests.java,v 1.8 2008-10-10 18:49:05 dervin Exp $ 
 */
public class RemoteFqpSystemTests extends StoryBook {
    
    public static final Log logger = LogFactory.getLog(RemoteFqpSystemTests.class);
    
    public static final String FQP_DIR_PROPERTY = "fqp.service.dir";
    
    private DataServiceDeploymentStory[] dataServiceDeployments;
    private FQPServiceDeploymentStory fqpDeployment;
    
    public RemoteFqpSystemTests() {
        super();
        setName("Remote Federated Query Service System Tests");
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
        
        // sources of data service containers.  This allows stories to grab
        // service containers after they've been created in the order of execution
        ServiceContainerSource[] containerSources = new ServiceContainerSource[] {
            exampleService1Deployment, exampleService2Deployment
        };
        
        // deploy the FQP service
        fqpDeployment = new FQPServiceDeploymentStory(getFqpDir());
        addStory(fqpDeployment);
        FederatedQueryProcessorHelper queryHelper = 
            new FederatedQueryProcessorHelper(fqpDeployment);
        
        // run standard queries
        QueryStory queryTests = new QueryStory(containerSources, queryHelper);
        addStory(queryTests);
        
        // run the aggregation queries
        AggregationStory aggregationTests = new AggregationStory(containerSources, queryHelper);
        addStory(aggregationTests);
        
        // run asynchronous queries
        AsynchronousExecutionStory asynchronousStory = 
            new AsynchronousExecutionStory(containerSources, fqpDeployment);
        addStory(asynchronousStory);
        
        // run enumeration queries
        EnumerationExecutionStory enumerationStory = 
            new EnumerationExecutionStory(containerSources, fqpDeployment);
        addStory(enumerationStory);
        
        // run partial results queries
        PartialResultsStory partialResultsStory = 
            new PartialResultsStory(containerSources, fqpDeployment);
        addStory(partialResultsStory);
    }
    
    
    private File getFqpDir() {
        String value = System.getProperty(FQP_DIR_PROPERTY);
        Assert.assertNotNull("System property " + FQP_DIR_PROPERTY + " was not set!", value);
        File dir = new File(value);
        return dir;
    }
    
    
    public void run(TestResult result) {
        logger.debug("Starting Remote FQP Tests");
        super.run(result);
        cleanUp();
    }
    
    
    private void cleanUp() {
        logger.debug("Cleaning Up Remote FQP Tests");
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
