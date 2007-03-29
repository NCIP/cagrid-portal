package gov.nih.nci.cagrid.bdt.test.steps;

import gov.nih.nci.cagrid.bdt.test.unit.CreationTest;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


/**
 * CreationStep TODO:DOCUMENT ME
 * 
 * 
 * @created Aug 22, 2006
 * @version $Id: CreationStep.java,v 1.3 2007-03-29 17:03:56 dervin Exp $
 */
public class CreationStep extends Step {
    private String introduceDir;


    public CreationStep(String introduceDir) {
        super();
        this.introduceDir = introduceDir;
    }


    public void runStep() throws Throwable {
        System.out.println("Creating service...");

        String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir, CreationTest.SERVICE_NAME,
            CreationTest.SERVICE_DIR, CreationTest.PACKAGE_NAME, CreationTest.SERVICE_NAMESPACE, "bdt");
        Process p = CommonTools.createAndOutputProcess(cmd);
        p.waitFor();
        assertTrue("Creating new data service failed", p.exitValue() == 0);
        
        addBdtStartMethod();

        System.out.println("Invoking post creation processes...");
        cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, CreationTest.SERVICE_NAME,
            CreationTest.SERVICE_DIR, CreationTest.PACKAGE_NAME, CreationTest.SERVICE_NAMESPACE, "bdt");
        p = CommonTools.createAndOutputProcess(cmd);
        p.waitFor();
        assertTrue("Service post creation process failed", p.exitValue() == 0);

        System.out.println("Building created service...");
        cmd = CommonTools.getAntAllCommand(CreationTest.SERVICE_DIR);
        p = CommonTools.createAndOutputProcess(cmd);
        p.waitFor();
        assertTrue("Build process failed", p.exitValue() == 0);
    }


    private void addBdtStartMethod() throws Throwable {
        // verify the service model exists
        System.out.println("Verifying the service model file exists");
        File serviceModelFile = new File(CreationTest.SERVICE_DIR + File.separator
            + IntroduceConstants.INTRODUCE_XML_FILE);
        assertTrue("Service model file did not exist: " + serviceModelFile.getAbsolutePath(), 
            serviceModelFile.exists());
        assertTrue("Service model file cannot be read: " + serviceModelFile.getAbsolutePath(), 
            serviceModelFile.canRead());

        // deserialize the service model
        System.out.println("Deserializing service description from introduce.xml");
        ServiceDescription serviceDesc = (ServiceDescription) Utils.deserializeDocument(
            serviceModelFile.getAbsolutePath(), ServiceDescription.class);

        // get the extensions, verify BDT exists
        ExtensionType[] extensions = serviceDesc.getExtensions().getExtension();
        ExtensionType bdtExtension = null;
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i].getName().equals("bdt")) {
                bdtExtension = extensions[i];
                break;
            }
        }
        assertNotNull("BDT extension was not found in the service model", bdtExtension);
        
        // create the BDT start
        ServiceType mainService = serviceDesc.getServices().getService(0);
        MethodType bdtQueryMethod = new MethodType();
        bdtQueryMethod.setName("bdtStart");
        bdtQueryMethod.setDescription("Starts a BDT operation");
        // output of BDT client handle
        MethodTypeOutput bdtHandleOutput = new MethodTypeOutput();
        QName handleQname = new QName(
            mainService.getNamespace() + "BDT/types",
            mainService.getName() + "BulkDataHandlerReference"
        );
        bdtHandleOutput.setQName(handleQname);
        bdtHandleOutput.setIsArray(false);
        bdtHandleOutput.setIsClientHandle(Boolean.TRUE);
        String clientHandleClass = mainService.getPackageName() 
            + ".bdt.client." + mainService.getName() + "BulkDataHandlerClient";
        bdtHandleOutput.setClientHandleClass(clientHandleClass);
        bdtQueryMethod.setOutput(bdtHandleOutput);
        
        // add the method to the service
        mainService.getMethods().setMethod(
            (MethodType[]) Utils.appendToArray(
                mainService.getMethods().getMethod(), bdtQueryMethod));
        
        // save the model back to disk for the post creation process
        Utils.serializeDocument(serviceModelFile.getAbsolutePath(), serviceDesc,
            IntroduceConstants.INTRODUCE_SKELETON_QNAME);
    }
}
