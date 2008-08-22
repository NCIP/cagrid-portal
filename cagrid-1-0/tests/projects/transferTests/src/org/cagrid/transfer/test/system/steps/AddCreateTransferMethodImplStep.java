package org.cagrid.transfer.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.steps.BaseStep;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddCreateTransferMethodImplStep extends BaseStep {
    private TestCaseInfo tci;


    public AddCreateTransferMethodImplStep(TestCaseInfo tci, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;

    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = new File(".." + File.separator + ".." + File.separator + ".."
            + File.separator + "tests" + File.separator + "projects" + File.separator + "transferTests"
            + File.separator + "resources" + File.separator + tci.getName() + "Client.java");
        File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "client" + File.separator + tci.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);

        File inFileImpl = new File(".." + File.separator + ".." + File.separator + ".."
            + File.separator + "tests" + File.separator + "projects" + File.separator + "transferTests"
            + File.separator + "resources" + File.separator + tci.getName() + "Impl.java");
        File outFileImpl = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "service" + File.separator + tci.getName() + "Impl.java");

        SourceUtils.modifyImpl(inFileImpl, outFileImpl, "createTransferMethodStep");

        buildStep();
    }

}
