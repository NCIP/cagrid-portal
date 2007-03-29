/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.CaDSRCheckServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.CaDSRServiceConfigStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.util.CaDSRExtractUtils;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Story;


/**
 * This is an integration test that tests the functionality of the caDSR grid
 * service. It deploys the service and then compares a number of domain models
 * against their cached XML extracts.
 * 
 * @testType integration
 * @steps ServiceCreateStep,
 * @steps GlobusCreateStep, GlobusDeployServiceStep, CaDSRServiceConfigStep,
 *        GlobusStartStep
 * @steps CaDSRCheckServiceStep
 * @steps GlobusStopStep, GlobusCleanupStep
 * @author Patrick McConnell
 */
public class CaDSRServiceTest extends Story {
    private GlobusHelper globus;
    private File serviceDir;


    public CaDSRServiceTest() {
        super();
    }


    @Override
    public String getName() {
        return "caDSR Story";
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

        this.serviceDir = new File(System.getProperty("cadsr.dir", ".." + File.separator + ".." + File.separator + ".."
            + File.separator + "caGrid" + File.separator + "projects" + File.separator + "cadsr"));
        CaDSRExtractUtils.setAxisConfig(new File("etc", "cadsr" + File.separator + "client-config.wsdd"));

        Vector steps = new Vector();
        steps.add(new GlobusCreateStep(this.globus));
        steps.add(new GlobusDeployServiceStep(this.globus, this.serviceDir));
        steps.add(new CaDSRServiceConfigStep(this.globus));
        steps.add(new GlobusStartStep(this.globus));
        try {
            File[] files = new File("test", "resources" + File.separator + "CheckCaDSRServiceStep")
                .listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile() && file.getName().endsWith(".xml");
                    }
                });
            for (File file : files) {
                steps.add(new CaDSRCheckServiceStep(this.globus
                    .getServiceEPR(CaDSRCheckServiceStep.SERVICE_DEPLOYMENT_PATH), file));
            }
        } catch (MalformedURIException e) {
            throw new RuntimeException("unable to instantiate CheckCaDSRStep", e);
        }
        steps.add(new GlobusStopStep(this.globus));
        steps.add(new GlobusCleanupStep(this.globus));
        return steps;
    }


    @Override
    public String getDescription() {
        return "CaDSRServiceTest";
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
        TestResult result = runner.doRun(new TestSuite(CaDSRServiceTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
