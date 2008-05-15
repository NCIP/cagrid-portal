package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

import java.io.File;


public class ModifyPersistenceClientStep extends BaseStep {
    private TestCaseInfo tci;
    private String methodName;


    public ModifyPersistenceClientStep(TestCaseInfo tci, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;
    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = new File(this.getClass().getResource(File.separator +"gold" + File.separator
            + "persistence" + File.separator + tci.getName() + "ClientGet.java").getFile());
        File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "client" + File.separator + tci.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);

        buildStep();
    }

}
