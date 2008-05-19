package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddGetPersistenceResourceMethodImplStep extends BaseStep {
    private TestCaseInfo tci;
    private TestCaseInfo tci2;
    private String methodName;


    public AddGetPersistenceResourceMethodImplStep(TestCaseInfo tci, TestCaseInfo tci2, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;
        this.tci2 = tci2;
    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = new File(this.getClass().getResource("/gold/persistence/" + tci.getName() + "ClientGetPersistentResource.java").getFile());
        File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "client" + File.separator + tci.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);

        buildStep();
    }

}
