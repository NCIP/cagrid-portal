package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddNotificationMethodImplStep extends BaseStep {
    private TestCaseInfo tci;
    private String methodName;


    public AddNotificationMethodImplStep(TestCaseInfo tci, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;
        this.methodName = methodName;
    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = new File(this.getClass().getResource(File.separator +"gold" + File.separator
            + "notification" + File.separator + tci.getName() + "Client.java").getFile());
        File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "client" + File.separator + tci.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);

        File inFileImpl = new File(this.getClass().getResource(File.separator +"gold" + File.separator + "notification" + File.separator + tci.getName() + "Impl.java").getFile());
        File outFileImpl = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir() + File.separator + "service" + File.separator  + tci.getName() + "Impl.java");
        
        SourceUtils.modifyImpl(inFileImpl, outFileImpl, "setBook");

        buildStep();
    }

}
