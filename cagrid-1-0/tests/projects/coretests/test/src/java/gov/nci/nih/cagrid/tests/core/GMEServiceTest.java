/*
 * Created on Jun 13, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.steps.GMECleanupStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEConfigureStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEGetSchemaListStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEGetSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.GMEPublishSchemaStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusCreateStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusDeployServiceStep;
import gov.nci.nih.cagrid.tests.core.steps.GlobusStartStep;
import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.NoAvailablePortException;

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

import com.atomicobject.haste.framework.Story;


/**
 * This is an integration test that tests the functionality of the GME grid
 * service. It deploys the service, adds some schemas, retrieves the schemas,
 * and lists the schemas.
 * 
 * @testType integration
 * @steps ServiceCreateStep,
 * @steps GlobusCreateStep, GlobusDeployServiceStep, GMEConfigureStep,
 *        GlobusStartStep
 * @steps GMEPublishSchemaStep, GMEGetSchemaStep, GMEGetSchemaListStep
 * @steps GlobusStopStep, GMECleanupStep, GlobusCleanupStep
 * @author Patrick McConnell
 */
public class GMEServiceTest extends Story {
    private GlobusHelper globus;
    private File serviceDir;


    public GMEServiceTest() {
        super();
    }


    @Override
    public String getName() {
        return "GME Story";
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

        new GMECleanupStep().runStep();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Vector steps() {
        this.globus = new GlobusHelper();
        this.serviceDir = new File(System.getProperty("gme.dir", ".." + File.separator + ".." + File.separator + ".."
            + File.separator + "caGrid" + File.separator + "projects" + File.separator + "gme"));

        File schemaRoot = new File("test", "resources" + File.separator + "GMEServiceTest" + File.separator + "schema");

        Vector steps = new Vector();
        steps.add(new GlobusCreateStep(this.globus));
        try {
            List<String> deployArgs = new ArrayList<String>();
            deployArgs.add("-Dservice.deployment.host=\"localhost\"");
            deployArgs.add("-Dservice.deployment.port=\"" + String.valueOf(this.globus.getPort()) + "\"");
            deployArgs.add("-Dservice.deployment.protocol=\"" + (this.globus.isSecure() ? "https" : "http") + "\"");

            GlobusDeployServiceStep globusDeployServiceStep = new GlobusDeployServiceStep(this.globus, this.serviceDir);
            globusDeployServiceStep.setArgs(deployArgs);
            steps.add(globusDeployServiceStep);
        } catch (NoAvailablePortException e) {
            throw new IllegalArgumentException("unable to instantiate GMEStep", e);
        }

        steps.add(new GMEConfigureStep(this.globus));
        steps.add(new GlobusStartStep(this.globus));

        try {
            EndpointReferenceType gmeEPR = this.globus.getServiceEPR("cagrid/GlobalModelExchange");
            File[] schemaDirs = schemaRoot.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory() && !file.getName().equals("CVS");
                }
            });

            for (File schemaDir : schemaDirs) {
                File[] schemaFiles = schemaDir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.getName().endsWith(".xsd");
                    }
                });

                for (File schemaFile : schemaFiles) {
                    steps.add(new GMEPublishSchemaStep(gmeEPR, schemaFile));
                }
                for (File schemaFile : schemaFiles) {
                    steps.add(new GMEGetSchemaStep(gmeEPR, schemaFile));
                }
                for (File schemaFile : schemaFiles) {
                    steps.add(new GMEGetSchemaListStep(gmeEPR, schemaFile));
                }
            }
        } catch (MalformedURIException e) {
            throw new IllegalArgumentException("unable to instantiate GMEStep", e);
        }

        return steps;
    }


    @Override
    public String getDescription() {
        return "GMEServiceTest";
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
        TestResult result = runner.doRun(new TestSuite(GMEServiceTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }

}
