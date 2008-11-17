package org.cagrid.index.tests;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DestroyContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StopContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.data.test.creation.CreationStep;
import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.index.tests.steps.ChangeIndexSweeperDelayStep;
import org.cagrid.index.tests.steps.DeployIndexServiceStep;
import org.cagrid.index.tests.steps.ServiceDiscoveryStep;
import org.cagrid.index.tests.steps.SetAdvertisementUrlStep;
import org.cagrid.index.tests.steps.SetServiceMetadataStep;

public class IndexServiceSystemTest extends Story {
    
    private static Log log = LogFactory.getLog(IndexServiceSystemTest.class);
    
    public static final String INTRODUCE_SERVICEMETADATA_FILENAME = "serviceMetadata.xml";
    public static final String INDEX_SERVICE_DIR_PROPERTY = "index.dir";
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    public static final String DEFAULT_INDEX_SERVICE_DIR = ".." + File.separator + ".." + File.separator
            + ".." + File.separator + "caGrid" + File.separator + "projects" + File.separator + "index";
    public static final String SERVICE_NAME = "TestDataService";
    public static final String PACKAGE_NAME = "gov.nih.nci.cagrid.testds";
    public static final String SERVICE_NAMESPACE = "http://" + PACKAGE_NAME + "/" + SERVICE_NAME;
    
    private ServiceContainer indexServiceContainer = null;
    private ServiceContainer dataServiceContainer = null;
    private TestDataServiceInfo testDataServiceInfo = null;
    
    
    public String getName() {
        return "Index Service System Test";
    }
    

    public String getDescription() {
        return "Tests the Index Service";
    }
    
    
    public boolean storySetUp() {
        testDataServiceInfo = new TestDataServiceInfo();
        
        // set up the index service container
        try {
            log.debug("Creating container for index service");
            indexServiceContainer = ServiceContainerFactory.createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER);
            new UnpackContainerStep(indexServiceContainer).runStep();
        } catch (Throwable ex) {
            String message = "Error creating container for index service: " + ex.getMessage();
            log.error(message, ex);
            fail(message);
        }
        
        // set up a data service container
        try {
            log.debug("Creating container for data service");
            dataServiceContainer = ServiceContainerFactory.createContainer(ServiceContainerType.SECURE_TOMCAT_CONTAINER);
            new UnpackContainerStep(dataServiceContainer).runStep();
        } catch (Throwable ex) {
            String message = "Error creating container for data service: " + ex.getMessage();
            log.error(message, ex);
            fail(message);
        }
        return true;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        
        // get the EPR of the Index service
        EndpointReferenceType indexEPR = null;
        try {
            indexEPR = indexServiceContainer.getServiceEPR("DefaultIndexService");
        } catch (MalformedURIException ex) {
            String message = "Error obtaining EPR of the Index Service: " + ex.getMessage();
            log.error(message, ex);
            fail(message);
        }
        
        // create a data service
        steps.add(new CreationStep(testDataServiceInfo, getIntroduceBaseDir()));
        // set the service's expected metadata
        StringBuffer testMetadata = getTestingServiceMetadata();
        steps.add(new SetServiceMetadataStep(new File(testDataServiceInfo.getDir()), testMetadata));
        // make that service register to our testing index service
        steps.add(new SetAdvertisementUrlStep(new File(testDataServiceInfo.getDir()), indexEPR));
        
        // deploy the index service
        String indexServiceLocation = System.getProperty(INDEX_SERVICE_DIR_PROPERTY, DEFAULT_INDEX_SERVICE_DIR);
        log.debug("Index service dir: " + indexServiceLocation);
        steps.add(new DeployIndexServiceStep(indexServiceContainer, new File(indexServiceLocation)));
        // change the sweeper delay of the index service
        steps.add(new ChangeIndexSweeperDelayStep(indexServiceContainer));
        // start the index service
        steps.add(new StartContainerStep(indexServiceContainer));
        
        // deploy the data service
        steps.add(new DeployServiceStep(dataServiceContainer, new TestDataServiceInfo().getDir()));
        // start the data service
        steps.add(new StartContainerStep(dataServiceContainer));
        
        // sleep long enough to allow the data service to register itself
        steps.add(new SleepStep(ChangeIndexSweeperDelayStep.DEFAULT_SWEEPER_DELAY * 5));
        
        // get the EPR of the data service
        EndpointReferenceType dataEPR = null;
        try {
            dataEPR = dataServiceContainer.getServiceEPR("cagrid/" + testDataServiceInfo.getName());
        } catch (MalformedURIException ex) {
            String message = "Error obtaining EPR of data service: " + ex.getMessage();
            log.error(message, ex);
            fail(message);
        }
        
        // find the data service in the index service
        steps.add(new ServiceDiscoveryStep(indexEPR, dataEPR, testMetadata, true));
        
        // make sure the sweeper has run and its still there
        steps.add(new SleepStep(ChangeIndexSweeperDelayStep.DEFAULT_SWEEPER_DELAY * 2));
        steps.add(new ServiceDiscoveryStep(indexEPR, dataEPR, testMetadata, true));
        
        // shut down the data service
        steps.add(new StopContainerStep(dataServiceContainer));
        
        // make sure the sweeper has run and the service is gone
        steps.add(new SleepStep(ChangeIndexSweeperDelayStep.DEFAULT_SWEEPER_DELAY * 2));
        steps.add(new ServiceDiscoveryStep(indexEPR, dataEPR, testMetadata, false));
        
        return steps;
    }
    
    
    public void storyTearDown() throws Throwable {
        if (dataServiceContainer.isStarted()) {
            new StopContainerStep(dataServiceContainer).runStep();
        }
        new DestroyContainerStep(dataServiceContainer).runStep();
        new StopContainerStep(indexServiceContainer).runStep();
        new DestroyContainerStep(indexServiceContainer).runStep();
        new DeleteOldServiceStep(testDataServiceInfo);
    }
    
    
    private StringBuffer getTestingServiceMetadata() {
        InputStream metadataInput = getClass().getResourceAsStream("/" + INTRODUCE_SERVICEMETADATA_FILENAME);
        StringBuffer metadata = null;
        try {
            metadata = Utils.inputStreamToStringBuffer(metadataInput);
            metadataInput.close();
        } catch (IOException ex) {
            String message = "Error reading default metadata document: " + ex.getMessage();
            log.error(message, ex);
            fail(message);
        }
        return metadata;
    }
    
    
    private String getIntroduceBaseDir() {
        String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
        if (dir == null) {
            fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
        }
        return dir;
    }
    
    
    private static class TestDataServiceInfo extends DataTestCaseInfo {
        public String getName() {
            return SERVICE_NAME;
        }


        public String getServiceDirName() {
            return SERVICE_NAME;
        }


        public String getNamespace() {
            return SERVICE_NAMESPACE;
        }


        public String getPackageName() {
            return PACKAGE_NAME;
        }
    }
    
    
    private static class SleepStep extends Step {
        
        private long sleep;
        
        public SleepStep(long sleep) {
            this.sleep = sleep;
        }
        
        
        public void runStep() throws Throwable {
            Thread.sleep(sleep);
        }
    }
}
