package gov.nih.nci.cagrid.introduce.test.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.test.util.SourceUtils;

import java.io.File;


public class AddPersistenceNotificationResourceMethodImplStep extends BaseStep {
    private TestCaseInfo tci;
    private TestCaseInfo tci2;
    private String methodName;


    public AddPersistenceNotificationResourceMethodImplStep(TestCaseInfo tci, TestCaseInfo tci2, boolean build) throws Exception {
        super(tci.getDir(), build);
        this.tci = tci;
        this.tci2 = tci2;
    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = new File("test" + File.separator + "resources" + File.separator + "gold" + File.separator
            + "persistentnotification" + File.separator + tci.getName() + "SetClient.java");
        File outFileClient = new File(tci.getDir() + File.separator + "src" + File.separator + tci.getPackageDir()
            + File.separator + "client" + File.separator + tci.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);
        
        inFileClient = new File("test" + File.separator + "resources" + File.separator + "gold" + File.separator
            + "persistentnotification" + File.separator + tci2.getName() + "Client.java");
        outFileClient = new File(tci2.getDir() + File.separator + "src" + File.separator + tci2.getPackageDir()
            + File.separator + "client" + File.separator + tci2.getName() + "Client.java");

        Utils.copyFile(inFileClient, outFileClient);
        

        File inFileImpl = new File("test" + File.separator + "resources" + File.separator + "gold" + File.separator + "persistentnotification" + File.separator + tci2.getName() + "Impl.java");
        File outFileImpl = new File(tci2.getDir() + File.separator + "src" + File.separator + tci2.getPackageDir() + File.separator + "service" + File.separator  + tci2.getName() + "Impl.java");
        
        SourceUtils.modifyImpl(inFileImpl, outFileImpl, "setBook");
        
        inFileImpl = new File("test" + File.separator + "resources" + File.separator + "gold" + File.separator + "persistentnotification" + File.separator + tci2.getName() + "Impl.java");
        outFileImpl = new File(tci2.getDir() + File.separator + "src" + File.separator + tci2.getPackageDir() + File.separator + "service" + File.separator  + tci2.getName() + "Impl.java");
        
        SourceUtils.modifyImpl(inFileImpl, outFileImpl, "getBook");

        buildStep();
    }

}
