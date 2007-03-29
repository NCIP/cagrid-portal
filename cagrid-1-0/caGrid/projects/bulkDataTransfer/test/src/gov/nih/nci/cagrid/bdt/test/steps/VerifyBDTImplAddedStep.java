package gov.nih.nci.cagrid.bdt.test.steps;

import gov.nih.nci.cagrid.bdt.templates.BDTResourceCreatorTemplate;
import gov.nih.nci.cagrid.bdt.test.unit.CreationTest;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.atomicobject.haste.framework.Step;

/** 
 *  VerifyBDTImplAddedStep
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Mar 29, 2007 2:28:04 PM
 * @version $Id: VerifyBDTImplAddedStep.java,v 1.1 2007-03-29 19:13:48 dervin Exp $ 
 */
public class VerifyBDTImplAddedStep extends Step {

    public void runStep() throws Throwable {
        File serviceModelFile = new File(CreationTest.SERVICE_DIR + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
        assertTrue("Service model file did not exist", serviceModelFile.exists());
        assertTrue("Service model file was not readable", serviceModelFile.canRead());
        ServiceDescription desc = (ServiceDescription) Utils.deserializeDocument(
            serviceModelFile.getAbsolutePath(), ServiceDescription.class);
        Properties introduceProperties = new Properties();
        introduceProperties.load(new FileInputStream(
            new File(CreationTest.SERVICE_DIR + File.separator + IntroduceConstants.INTRODUCE_PROPERTIES_FILE)));
        
        ServiceInformation info = new ServiceInformation(desc, introduceProperties, new File(CreationTest.SERVICE_DIR));        
        
        ServiceType mainService = desc.getServices().getService(0);
        File mainServiceImplFile = new File(CreationTest.SERVICE_DIR + File.separator 
            + "src" + File.separator + CommonTools.getPackageDir(mainService)
            + File.separator + "service" + File.separator 
            + mainService.getName() + "Impl.java");
        assertTrue("Main service implementation file did not exist", mainServiceImplFile.exists());
        assertTrue("Main service implementation file could not be read", mainServiceImplFile.canRead());
        // System.out.println("Examining main service impl: " + mainServiceImplFile);
        
        String mainServiceImplSource = readFile(mainServiceImplFile);
        
        // System.out.println("MAIN SERVICE IMPL SOURCE:");
        // System.out.println(mainServiceImplSource);
        
        BDTResourceCreatorTemplate template = new BDTResourceCreatorTemplate();
        String methodTemplate = template.generate(
            new SpecificServiceInformation(info, mainService));
        
        // System.out.println("LOOKING FOR METHOD BODY:");
        // System.out.println(methodTemplate);
        
        assertTrue("BDT implementation not found in source", mainServiceImplSource.indexOf(methodTemplate) != -1);
    }
    
    
    private String readFile(File in) throws IOException {
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(in));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] tmp = new byte[8192];
        int len = -1;
        while ((len = inStream.read(tmp)) != -1) {
            bytes.write(tmp, 0, len);
        }
        inStream.close();
        return bytes.toString();
    }
}
