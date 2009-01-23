package gov.nih.nci.cagrid.workflow.test.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.CopyServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.story.ServiceStoryBase;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import gov.nih.nci.cagrid.workflow.test.system.steps.CheckTavernaWorkflowServiceStep;

public class TavernaWorkflowServiceStory extends ServiceStoryBase {

    private static final String SERVICE_TEMP_PATH = "tmp/TempGME";
    private static final String RESULTS_TEMP_PATH = "tmp/results";
    private static final String TWS_URL_PATH = "cagrid/TavernaWorkflowService";
    private static final String PATH_TO_MMS_PROJECT = "../../../caGrid/projects/TavernaWorkflowServices";
    public static final String TWS_DIR_PROPERTY = "tws.service.dir";


    public TavernaWorkflowServiceStory(ServiceContainer container) {
        super(container);
    }

    public TavernaWorkflowServiceStory() {

        // init the container
        try {
            this.setContainer(ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
    }


    @Override
    public String getName() {
        return getDescription();
    }


    @Override
    public String getDescription() {
        return "Taverna Workflow Service Test";
    }


    protected File getTWSDir() {
        String value = System.getProperty(TWS_DIR_PROPERTY, PATH_TO_MMS_PROJECT);
        assertNotNull("System property " + TWS_DIR_PROPERTY + " was not set!", value);
        File dir = new File(value);
        return dir;
    }
    
    private class ChangeServicePropertyFile extends Step {
    	
    	public ChangeServicePropertyFile (){
    		
    	}
    	
		@Override
		public void runStep() throws Throwable {

			Properties props = new Properties();
	        try {
	        	File path = new File (SERVICE_TEMP_PATH);
	        	String absPath = path.getAbsolutePath();
				props.load(new FileInputStream(absPath + "/service.properties"));
	        	System.out.println(" >> Service Properites File : \n >> " + absPath + "/service.properties");
				props.setProperty("tavernaDir", absPath + "/taverna");
				props.setProperty("baseRepositoryDir", absPath + "/taverna/target/repository");
				props.store(new FileOutputStream(absPath + "/service.properties"), null);
	
				System.out.println(" >> File Changed to:\n");
				System.out.println(" >> " + props.getProperty("tavernaDir"));
				System.out.println(" >> " + props.getProperty("baseRepositoryDir"));
				
				Thread.sleep(5000);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    	
    }



	@Override
	protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        File tempGMEServiceDir = new File(SERVICE_TEMP_PATH);

        // SETUP
        steps.add(new UnpackContainerStep(getContainer()));
        steps.add(new CopyServiceStep(getTWSDir(), tempGMEServiceDir));


        
        // CONFIGURE
        // steps.add(new SetDatabasePropertiesStep(tempGMEServiceDir));
        // steps.add(new CreateDatabaseStep(tempGMEServiceDir));
        steps.add (new ChangeServicePropertyFile());

        
        
        DeployServiceStep deployStep = new DeployServiceStep(getContainer(), tempGMEServiceDir.getAbsolutePath(),
            Arrays.asList(new String[]{"-Dno.deployment.validation=true"}));
        steps.add(deployStep);
        steps.add(new StartContainerStep(getContainer()));

        EndpointReferenceType epr = null;
        try {
            epr = getContainer().getServiceEPR(TWS_URL_PATH);
        } catch (MalformedURIException e) {
            e.printStackTrace();
            fail("Error constructing client:" + e.getMessage());
        }

        // TEST
        steps.add(new CheckTavernaWorkflowServiceStep(epr));

        // retrieve failures

        return steps;
	}

}
