/*
 * Created on Aug 1, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.GlobusCleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;

import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class BasicDataServiceTest extends AbstractServiceTest {
    public BasicDataServiceTest() {
        super();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Vector steps() {
        super.init("BasicDataService");

        Vector steps = new Vector();
        steps.add(getCreateServiceStep());
        steps.add(new GlobusCreateStep(getGlobus()));
        steps.add(new GlobusDeployServiceStep(getGlobus(), getCreateServiceStep().getServiceDir()));

        // add gme/cadsr deploy/configure steps
        // steps.add(new GlobusDeployServiceStep(globus, gmeServiceDir));
        // steps.add(new GMEConfigureStep(globus));
        // steps.add(new GlobusDeployServiceStep(globus, cadsrServiceDir));
        // steps.add(new CaDSRServiceConfigStep(globus));

        steps.add(new GlobusStartStep(getGlobus()));
        try {
            addInvokeSteps(steps);
        } catch (Exception e) {
            throw new IllegalArgumentException("could not add invoke steps", e);
        }
        // steps.add(new CheckServiceMetadataStep(endpoint, metadataFile));
        steps.add(new GlobusStopStep(getGlobus()));
        steps.add(new GlobusCleanupStep(getGlobus()));
        return steps;
    }


    @Override
    public String getDescription() {
        return "BasicDataServiceTest";
    }


    /**
     * Convenience method for running all the Steps in this Story.
     */
    public static void main(String args[]) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(BasicDataServiceTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
