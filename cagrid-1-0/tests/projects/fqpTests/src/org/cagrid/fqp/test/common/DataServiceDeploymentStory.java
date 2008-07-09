package org.cagrid.fqp.test.common;

import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerFactory;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainerType;
import gov.nih.nci.cagrid.testing.system.deployment.steps.DeployServiceStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.StartContainerStep;
import gov.nih.nci.cagrid.testing.system.deployment.steps.UnpackContainerStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import org.cagrid.fqp.test.common.steps.DeleteDirectoryStep;
import org.cagrid.fqp.test.common.steps.UnzipServiceStep;

/** 
 *  DataServiceDeploymentStory
 *  Deploys a data service to a local service container and starts it up
 * 
 * @author David Ervin
 * 
 * @created Jul 9, 2008 11:46:02 AM
 * @version $Id: DataServiceDeploymentStory.java,v 1.1 2008-07-09 21:04:08 dervin Exp $ 
 */
public class DataServiceDeploymentStory extends Story {
    
    private File dataServiceZip;
    private File tempDir;
    
    private ServiceContainer dataServiceContainer;
    private boolean complete;
    
    public DataServiceDeploymentStory(File dataServiceZip) {
        this.dataServiceZip = dataServiceZip;
        complete = false;
    }
    

    public String getDescription() {
        return "Deploys a data service to a local service container and starts it up";
    }
    
    
    public boolean storySetUp() {
        try {
            dataServiceContainer = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        File temp = null;
        try {
            temp = File.createTempFile("FQPTestDataService", "Temp", 
                new File(System.getProperty("java.io.tmpdir")));
            temp.delete();
            temp.mkdirs();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if (!temp.isDirectory() || !temp.canWrite()) {
            System.err.println("Failed to create writable temp directory: " + temp.getAbsolutePath());
            return false;
        }
        return true;
    }
    
    
    /**
     * Overridden to run tests, and on successful completion set 
     * the 'complete' flag to true so the service container can be returned
     */
    protected void runTest() throws Throwable {
        super.runTest();
        complete = true;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        steps.add(new UnpackContainerStep(dataServiceContainer));
        steps.add(new UnzipServiceStep(dataServiceZip, tempDir));
        steps.add(new DeployServiceStep(dataServiceContainer, tempDir.getAbsolutePath()));
        steps.add(new DeleteDirectoryStep(tempDir));
        steps.add(new StartContainerStep(dataServiceContainer));
        return steps;
    }
    
    
    public ServiceContainer getDataServiceContainer() {
        if (dataServiceContainer == null || !complete) {
            throw new IllegalStateException("Deployment Story has not completed to create a working service container!");
        }
        return dataServiceContainer;
    }
}
