package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.FQPAsynchronousQueryImmediateTerminationStep;
import gov.nci.nih.cagrid.tests.core.steps.FQPAsynchronousQueryScheduledTerminationStep;
import gov.nci.nih.cagrid.tests.core.steps.FQPConfigStep;
import gov.nci.nih.cagrid.tests.core.steps.FQPQueryStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusInstallSecurityDescriptorStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStopStep;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.PortPreference;
import gov.nih.nci.cagrid.dcql.DCQLQuery;
import gov.nih.nci.cagrid.dcql.Object;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.axis.types.URI.MalformedURIException;


/**
 * This is an integration test that tests the deployment of a data service, fqp
 * service, and issues DCQL queries to fqp which access the data service.
 * 
 * @testType integration
 * @author Scott Oster
 */
public class FQPServiceTest extends AbstractServiceTest {
    GlobusHelper fqpGlobus = null;


    public FQPServiceTest() {
        super();
    }


    @Override
    protected void storyTearDown() throws Throwable {
        if (this.fqpGlobus != null) {
            this.fqpGlobus.stopGlobus();
            this.fqpGlobus.cleanupTempGlobus();
        }
        super.storyTearDown();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Vector steps() {
        super.init("BasicDataService");

        File fqpServiceDir = new File(System.getProperty("fqp.dir", ".." + File.separator + ".." + File.separator
            + ".." + File.separator + "caGrid" + File.separator + "projects" + File.separator + "fqp"));

        // find a port for FQP that isn't what the data service is going to use
        PortPreference fqpPortPref = null;
        try {
            fqpPortPref = new PortPreference(GlobusHelper.getDefaultPortRangeMinimum(), GlobusHelper
                .getDefaultPortRangeMaximum(), new Integer[]{getGlobus().getPort()});
        } catch (Exception e1) {
            e1.printStackTrace();
            fail("Problem getting fqp port:" + e1.getMessage());
        }
        this.fqpGlobus = new GlobusHelper(false, fqpPortPref);
        String fqpURL = null;
        try {
            fqpURL = this.fqpGlobus.getServiceEPR("cagrid/FederatedQueryProcessor").getAddress().toString();
        } catch (MalformedURIException e) {
            fail("Problem getting FQP URL:" + e.getMessage());
        }

        // 5 seconds (how often resource will be cleaned up)
        long duration = 5000;

        Vector steps = new Vector();

        // stand up FQP
        steps.add(new GlobusCreateStep(this.fqpGlobus));
        if (this.fqpGlobus.isSecure()) {
            steps.add(new GlobusInstallSecurityDescriptorStep(this.fqpGlobus));
        }
        steps.add(new GlobusDeployServiceStep(this.fqpGlobus, fqpServiceDir, "deployGlobus"));
        steps.add(new FQPConfigStep(this.fqpGlobus, duration));
        steps.add(new GlobusStartStep(this.fqpGlobus));

        // stand up the data service
        steps.add(getCreateServiceStep());
        steps.add(new GlobusCreateStep(getGlobus()));
        steps.add(new GlobusDeployServiceStep(getGlobus(), getCreateServiceStep().getServiceDir()));
        steps.add(new GlobusStartStep(getGlobus()));

        // issue DCQL queries to FQP
        DCQLQuery dcql1 = new DCQLQuery();
        Object targetObject = new Object();
        targetObject.setName("java.lang.String");
        dcql1.setTargetObject(targetObject);
        dcql1.setTargetServiceURL(new String[]{getEndpoint().getAddress().toString()});
        steps.add(new FQPQueryStep(fqpURL, dcql1));
        steps.add(new FQPAsynchronousQueryImmediateTerminationStep(fqpURL, dcql1));
        steps.add(new FQPAsynchronousQueryScheduledTerminationStep(fqpURL, dcql1, duration));

        // shutdown the services
        steps.add(new GlobusStopStep(getGlobus()));
        steps.add(new GlobusStopStep(this.fqpGlobus));

        return steps;
    }


    @Override
    public String getDescription() {
        return "FQPServiceTest";
    }


    @Override
    public String getName() {
        return "FQP Service Story";
    }


    /**
     * Convenience method for running all the Steps in this Story.
     */
    public static void main(String args[]) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(FQPServiceTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
