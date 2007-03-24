/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.EvsCheckServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.EvsServiceConfigStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;


/**
 * This is an integration test that tests the functionality of the EVS grid
 * service. It deploys the service and then performs a number of client calls.
 * 
 * @testType integration
 * @steps ServiceCreateStep,
 * @steps GlobusCreateStep, GlobusDeployServiceStep, EvsServiceConfigStep,
 *        GlobusStartStep
 * @steps EvsCheckServiceStep
 * @steps GlobusStopStep, GlobusCleanupStep
 * @author Avinash Shanbhag
 * @author Patrick McConnell
 */
public class EvsServiceTest extends Story {
    private GlobusHelper globus;
    private File serviceDir;


    public EvsServiceTest() {
        super();
    }


    @Override
    protected boolean storySetUp() throws Throwable {
        return true;
    }


    @Override
    protected void storyTearDown() throws Throwable {
        if (this.globus != null) {
            this.globus.stopGlobus();
            this.globus.cleanupTempGlobus();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Vector steps() {

        this.globus = new GlobusHelper();

        this.serviceDir = new File(System.getProperty("evs.dir", ".." + File.separator + ".." + File.separator + ".."
            + File.separator + "caGrid" + File.separator + "projects" + File.separator + "evs"));

        Vector steps = new Vector();
        steps.add(new GlobusCreateStep(this.globus));
        steps.add(new GlobusDeployServiceStep(this.globus, this.serviceDir));
        steps.add(new EvsServiceConfigStep(this.globus));
        steps.add(new GlobusStartStep(this.globus));
        try {
            steps.add(new EvsCheckServiceStep(this.globus.getServiceEPR(EvsCheckServiceStep.SERVICE_DEPLOYMENT_PATH)));
        } catch (MalformedURIException e) {
            throw new RuntimeException("unable to instantiate EvsCheckServiceStep", e);
        }
        steps.add(new GlobusStopStep(this.globus));
        steps.add(new GlobusCleanupStep(this.globus));
        return steps;
    }


    @Override
    public String getDescription() {
        return "EvsServiceTest";
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
        TestResult result = runner.doRun(new TestSuite(EvsServiceTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
