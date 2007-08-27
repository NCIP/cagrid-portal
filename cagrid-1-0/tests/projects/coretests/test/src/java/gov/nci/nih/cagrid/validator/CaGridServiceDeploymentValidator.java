                                             
/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.validator;

import gov.nci.nih.cagrid.tests.core.steps.GMECleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEConfigureStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEGetSchemaListStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEGetSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEPublishSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.ServiceCheckMetadataStep;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.NoAvailablePortException;
import gov.nci.nih.cagrid.validator.steps.base.TestServiceMetaData;
import gov.nci.nih.cagrid.validator.steps.base.TestServiceUpStep;
import gov.nih.nci.cagrid.metadata.service.Service;
import org.apache.log4j.*;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.ws.security.trust2.serialization.TestSerialization;

import com.atomicobject.haste.framework.Story;


public class CaGridServiceDeploymentValidator extends Story {
    public CaGridServiceDeploymentValidator() {
        super();
    }

    @Override
    public String getName() {
        return "Validator";
    }

    @Override
    protected boolean storySetUp() throws Throwable {
        return true;
    }

    @Override
    protected void storyTearDown() throws Throwable {
       
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Vector steps() {
    	/*
    	String indexService  = "http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService";
    	String gmeService  = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/GlobalModelExchange";
    	String evsService = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/EVSGridService";
    	String cadsrService = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/CaDSRService";
    	String authService = "https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/AuthenticationService";
    	//String dorianService = "https://cagrid-dorian.nci.nih.gov:8443/wsrf/services/cagrid/Dorian"; 
    	String dorianService = "https://training.cagrid.org:8443/wsrf/services/cagrid/Dorian";
    	String ggService = "https://cagrid-auth.nci.nih.gov:8443/wsrf/services/cagrid/GridGrouper";
    	String gtsMService = "https://cagrid-gts-master.nci.nih.gov:8443/wsrf/services/cagrid/GTS";
    	String gtsSService = "https://cagrid-gts-slave.nci.nih.gov:8443/wsrf/services/cagrid/GTS";
    	String fqpService = "http://cagrid-portal.nci.nih.gov:8080/wsrf/services/cagrid/FederatedQueryProcessor";
    	String workflowService = "https://cagrid-workflow.nci.nih.gov:8443/wsrf/services/cagrid/WorkflowFactoryService";
    	*/
    	

    	String indexService  = "http://training.cagrid.org:8080/wsrf/services/DefaultIndexService";
    	String gmeService  = "http://training.cagrid.org:8080/wsrf/services/cagrid/GlobalModelExchange";
    	String cadsrService = "http://training.cagrid.org:8080/wsrf/services/cagrid/CaDSRService";    	
    	String dorianService = "https://training.cagrid.org:8443/wsrf/services/cagrid/Dorian";
    	String ggService = "https://training.cagrid.org:8443/wsrf/services/cagrid/GridGrouper";
    	String gtsService ="https://training.cagrid.org:7443/wsrf/services/cagrid/GTS";
       
        Vector steps = new Vector();
        //Testing Services Up/Down

        
        System.out.println("Checking whether Service is UP...");
        
        //Index Service
        steps.add(new TestServiceUpStep(indexService));
        //GME Service
        steps.add(new TestServiceUpStep(gmeService));
        //EVS
        //steps.add(new TestServiceUpStep(evsService));
        //caDSR
        steps.add(new TestServiceUpStep(cadsrService));
        //Authentication
        //steps.add(new TestServiceUpStep(authService));        								 
        //Dorian        
        steps.add(new TestServiceUpStep(dorianService));
        //Grid Grouper
        steps.add(new TestServiceUpStep(ggService));
        //GTS
        steps.add(new TestServiceUpStep(gtsService));
        //GTS Master
        //steps.add(new TestServiceUpStep(gtsMService));
        //GTS Slave
        //steps.add(new TestServiceUpStep(gtsSService));
        //FQP
        //steps.add(new TestServiceUpStep(fqpService));
        //Workflow Service
        //steps.add(new TestServiceUpStep(workflowService));
        //Test bogus URL
        //steps.add(new TestServiceUpStep("http://blah.com"));
        
        System.out.println("Service UP test over...");
        System.out.println("..............");

        //Testing Service MetaData
       
        System.out.println("Checking whether Service MetaData can be fetched..."); 

        //GME Service
        steps.add(new TestServiceMetaData(gmeService));
        //EVS
        //steps.add(new TestServiceMetaData(evsService));
        //caDSR
        steps.add(new TestServiceMetaData(cadsrService));        
        //Authentication
        //steps.add(new TestServiceMetaData(authService));
        //Dorian        
        steps.add(new TestServiceMetaData(dorianService));
        //Grid Grouper
        steps.add(new TestServiceMetaData(ggService));
        //GTS 
        steps.add(new TestServiceMetaData(gtsService));
        //GTS Master
        //steps.add(new TestServiceMetaData(gtsMService));        
        //GTS Slave
        //steps.add(new TestServiceMetaData(gtsSService));        
        //FQP
        //steps.add(new TestServiceMetaData(fqpService));        
        //Workflow Service
        //steps.add(new TestServiceMetaData(workflowService));
        //Test Bogus
        //steps.add(new TestServiceMetaData("http://blah.com"));

        System.out.println("Service MetaData test over...");
        
        //steps.add(new SpecificTests(""));
        System.out.println("Start Specific Service tests...");
        
        
        
        
        System.out.println("Specific Service tests over...");

        return steps;
    }

    @Override
    public String getDescription() {
        return "Validator";
    }


    /**
     * used to make sure that if we are going to use a junit testsuite to test
     * this that the test suite will not error out looking for a single
     * test......
     */
    public void testDummy() throws Throwable {
    }


    /**
     * Convenience method for running all the Steps in this Story.
     */
    public static void main(String args[]) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(CaGridServiceDeploymentValidator.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
