package org.cagrid.fqp.test.remote;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.fqp.test.common.AggregationStory;
import org.cagrid.fqp.test.common.DataServiceDeploymentStory;
import org.cagrid.fqp.test.common.FederatedQueryProcessorHelper;
import org.cagrid.fqp.test.common.QueryStory;
import org.cagrid.fqp.test.common.ServiceContainerSource;
import org.junit.After;
import org.junit.Test;

/** 
 *  RemoteFqpSystemTests
 *  System tests for FQP using the remote Federated Query API
 * 
 * @author David Ervin
 * 
 * @created Jul 10, 2008 10:57:40 AM
 * @version $Id: RemoteFqpSystemTests.java,v 1.13 2008-11-12 23:36:11 jpermar Exp $ 
 */
public class RemoteFqpSystemTests {
    
    public static final Log logger = LogFactory.getLog(RemoteFqpSystemTests.class);
    
    public static final String FQP_DIR_PROPERTY = "fqp.service.dir";
    public static final String TRANSFER_SERVICE_DIR_PROPERTY = "transfer.service.dir";
    
    private DataServiceDeploymentStory[] dataServiceDeployments;
    private FQPServiceDeploymentStory fqpDeployment;
    
    /*
    public RemoteFqpSystemTests() {
        super();
        setName("Remote Federated Query Service System Tests");
    }
    */
    
    
    @Test
    public void remoteFqpSystemTests() {
        // deploy two example SDK data services which pull from slightly different data
        DataServiceDeploymentStory exampleService1Deployment = 
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService1.zip"), false);
        DataServiceDeploymentStory exampleService2Deployment =
            new DataServiceDeploymentStory(new File("resources/services/ExampleSdkService2.zip"), false);
        dataServiceDeployments = new DataServiceDeploymentStory[] {
            exampleService1Deployment, exampleService2Deployment
        };
        
        exampleService1Deployment.run();
        exampleService2Deployment.run();
        
        // sources of data service containers.  This allows stories to grab
        // service containers after they've been created in the order of execution
        ServiceContainerSource[] containerSources = new ServiceContainerSource[] {
            exampleService1Deployment, exampleService2Deployment
        };
        
        // deploy the FQP service
        fqpDeployment = new FQPServiceDeploymentStory(getFqpDir(), false);
        
        fqpDeployment.run();
        
        FederatedQueryProcessorHelper queryHelper = 
            new FederatedQueryProcessorHelper(fqpDeployment);
        
        // initialize ws-notification client side
        NotificationClientSetupStory notificationSetupStory = new NotificationClientSetupStory();
        notificationSetupStory.run();
        
        // run standard queries
        QueryStory queryTests = new QueryStory(containerSources, queryHelper);
        queryTests.run();
        
        // run the aggregation queries
        AggregationStory aggregationTests = new AggregationStory(containerSources, queryHelper);
        aggregationTests.run();
        
        // run asynchronous queries
        AsynchronousExecutionStory asynchronousStory = 
            new AsynchronousExecutionStory(containerSources, fqpDeployment);
        asynchronousStory.run();

        // run enumeration queries
        EnumerationExecutionStory enumerationStory = 
            new EnumerationExecutionStory(containerSources, fqpDeployment);
        enumerationStory.run();

        // run partial results queries
        PartialResultsStory partialResultsStory = 
            new PartialResultsStory(containerSources, fqpDeployment);
        partialResultsStory.run();
        
        // deploy transfer to the FQP container
        TransferServiceDeploymentStory transferDeployStory = 
            new TransferServiceDeploymentStory(getTransferDir(), fqpDeployment);
        transferDeployStory.run();
        
        // run transfer queries
        TransferExecutionStory transferStory = 
            new TransferExecutionStory(containerSources, fqpDeployment);
		transferStory.run();
        
    }
    
    
    private File getFqpDir() {
        String value = System.getProperty(FQP_DIR_PROPERTY);
        Assert.assertNotNull("System property " + FQP_DIR_PROPERTY + " was not set!", value);
        File dir = new File(value);
        return dir;
    }
    
    private File getTransferDir() {
        String value = System.getProperty(TRANSFER_SERVICE_DIR_PROPERTY);
        Assert.assertNotNull("System property " + TRANSFER_SERVICE_DIR_PROPERTY + " was not set!", value);
        File dir = new File(value);
        return dir;
    }
    
    @After
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
