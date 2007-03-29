/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.steps.WorkflowConfigureStep;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.message.addressing.EndpointReferenceType;

import com.atomicobject.haste.framework.Story;


// TODO: does this test anything?
/**
 * This is an integration test that tests the functionality of the
 * WorkflowFactoryService. It deploys the service, deploys sample services,
 * invokes a number of bpel docs, checks the results, and then tears it all
 * down.
 * 
 * @testType integration
 * @steps GlobusCreateStep, GlobusDeployServiceStep, WorkflowConfigureStep
 * @steps GlobusStartStep, ServiceInvokeStep, GlobusStopStep, GlobusCleanupStep
 * @author Patrick McConnell
 */
public class WorkflowTest extends Story {
    private GlobusHelper globus;
    private GlobusHelper secureGlobus;

    private File serviceDir;
    private File sampleServicesDir;
    private EndpointReferenceType endpoint;


    public WorkflowTest() {
        super();
    }


    @Override
    public String getName() {
        return "Workflow Story";
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

        if (this.secureGlobus != null) {
            this.secureGlobus.stopGlobus();
            this.secureGlobus.cleanupTempGlobus();
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Vector steps() {
        this.globus = new GlobusHelper();
        this.secureGlobus = new GlobusHelper(true);

        this.serviceDir = new File(System.getProperty("wfs.dir", ".." + File.separator + ".." + File.separator + ".."
            + File.separator + "caGrid" + File.separator + "projects" + File.separator + "workflow" + File.separator
            + "WorkflowFactoryService"));
        this.sampleServicesDir = new File(System.getProperty("wokflow.samples.dir", ".." + File.separator
            + "workflow-services"));

        File methodsDir = new File("test", "resources" + File.separator + "WorkflowTest" + File.separator + "methods");

        Vector steps = new Vector();
        steps.add(new GlobusCreateStep(this.globus));

        // deploy wms
        steps.add(new GlobusDeployServiceStep(this.secureGlobus, this.serviceDir));
        steps.add(new WorkflowConfigureStep(this.secureGlobus));

        // deploy workflow sample services
        File[] sampleServiceDirs = this.sampleServicesDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() & file.getName().matches("\\d+_\\w+");
            }
        });
        for (File sampleServiceDir : sampleServiceDirs) {
            steps.add(new GlobusDeployServiceStep(this.globus, this.secureGlobus, this.serviceDir));
        }

        // start globus and invoke services
        steps.add(new GlobusStartStep(this.globus));
        steps.add(new GlobusStartStep(this.secureGlobus));
        try {
            AbstractServiceTest.addInvokeSteps(steps, this.serviceDir, this.serviceDir, methodsDir, this.endpoint);
        } catch (Exception e) {
            throw new RuntimeException("unable to add invoke steps", e);
        }

        // cleanup
        steps.add(new GlobusStopStep(this.globus));
        steps.add(new GlobusStopStep(this.secureGlobus));
        steps.add(new GlobusCleanupStep(this.globus));
        steps.add(new GlobusCleanupStep(this.secureGlobus));
        return steps;
    }


    @Override
    public String getDescription() {
        return "WorfklowTest";
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
        TestResult result = runner.doRun(new TestSuite(WorkflowTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
