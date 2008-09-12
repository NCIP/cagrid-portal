package org.cagrid.data.test.upgrades.from1pt0.system;

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

import java.util.Vector;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.system.AddBookstoreStep;
import org.cagrid.data.test.system.EnableValidationStep;
import org.cagrid.data.test.system.InvokeDataServiceStep;
import org.cagrid.data.test.system.RebuildServiceStep;
import org.cagrid.data.test.system.SetQueryProcessorStep;
import org.cagrid.data.test.upgrades.UpgradeTestConstants;
import org.cagrid.data.test.upgrades.from1pt0.UpgradeFrom1pt0Tests;

/** 
 *  UpgradedServiceSystemTest
 *  Tests the upgraded data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 21, 2007 
 * @version $Id: UpgradedServiceSystemTest.java,v 1.4 2008-09-12 14:44:31 dervin Exp $ 
 */
public class UpgradedServiceSystemTest extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
    private DataTestCaseInfo info;
    private ServiceContainer container;
    
    public UpgradedServiceSystemTest() {
        super();
    }
    
    
    public String getName() {
        return "Data Service 1_0 to " 
            + UpgradeTestConstants.DATA_CURRENT_VERSION.replace(".", "_") 
            + " Upgraded System Tests";
    }
    
	
	protected boolean storySetUp() {
        info = new UpgradeFrom1pt0Tests.Upgrade1pt0to1pt1TestServiceInfo();
        // obtain a new container instance
        try {
            container = ServiceContainerFactory.createContainer(ServiceContainerType.GLOBUS_CONTAINER);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Failed to create container: " + ex.getMessage());
        }
        
		// unpack container
        Step unpack = new UnpackContainerStep(container);
        try {
            unpack.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
		return true;
	}


	public String getDescription() {
		return "Deploys and invokes the upgraded data service";
	}


	protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
		// steps to invoke the upgraded service 
		// by the data service creation tests
		// 1) Add the bookstore schema to the data service
		steps.add(new AddBookstoreStep(info));
		// 2) change out query processor
		steps.add(new SetQueryProcessorStep(info.getDir()));
		// 3) remove anything thats not the query method
		// steps.add(new RemoveNonQueryMethodsStep(info.getDir()));
		// 4) Turn on query validation
		steps.add(new EnableValidationStep(info.getDir()));
		// 5) Rebuild the service to pick up the bookstore beans
		steps.add(new RebuildServiceStep(info, getIntroduceBaseDir()));
		// 6) deploy data service
		steps.add(new DeployServiceStep(container, info.getDir()));
		// 7) start globus
		steps.add(new StartContainerStep(container));
		// 8) test data service
		steps.add(new InvokeDataServiceStep(container, info.getName()));
		return steps;
	}
	
	
	protected void storyTearDown() throws Throwable {
		super.storyTearDown();
        // 10) stop globus
		Step stopStep = new StopContainerStep(container);
		try {
			stopStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		// 11) throw away globus
		Step destroyStep = new DestroyContainerStep(container);
		try {
			destroyStep.runStep();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
        // 12) throw away service
        Step destroyServiceStep = new DeleteOldServiceStep(info);
        try {
            destroyServiceStep.runStep();
        } catch (Throwable th) {
            th.printStackTrace();
        }
	}
	
	
	private String getIntroduceBaseDir() {
		String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
		if (dir == null) {
			fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
		}
		return dir;
	}
}
