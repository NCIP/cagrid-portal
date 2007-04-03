package gov.nih.nci.cagrid.bdt.test.steps;

import gov.nih.nci.cagrid.bdt.test.unit.CreationTest;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.atomicobject.haste.framework.Step;

/** 
 *  VerifyBDTImplAddedStep
 *  Step to verify the BDT method was implemented
 * 
 * @author David Ervin
 * 
 * @created Mar 29, 2007 2:28:04 PM
 * @version $Id: VerifyBDTImplAddedStep.java,v 1.3 2007-04-03 15:06:49 dervin Exp $ 
 */
public class VerifyBDTImplAddedStep extends Step {

    public void runStep() throws Throwable {
        // get the service model
        File serviceModelFile = new File(CreationTest.SERVICE_DIR 
            + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
        assertTrue("Service model file did not exist", serviceModelFile.exists());
        assertTrue("Service model file was not readable", serviceModelFile.canRead());
        ServiceDescription desc = (ServiceDescription) Utils.deserializeDocument(
            serviceModelFile.getAbsolutePath(), ServiceDescription.class);
        Properties introduceProperties = new Properties();
        introduceProperties.load(new FileInputStream(new File(
            CreationTest.SERVICE_DIR + File.separator + IntroduceConstants.INTRODUCE_PROPERTIES_FILE)));
        
        // verify the bdtStart method exists
        ServiceType mainService = desc.getServices().getService(0);
        MethodType bdtStartMethod = null;
        for (MethodType method : mainService.getMethods().getMethod()) {
            if (method.getName().equals("bdtStart")) {
                bdtStartMethod = method;
                break;
            }
        }
        assertNotNull("No BDT Start method found in the service", bdtStartMethod);
        
        // read in the main service's impl source file
        File mainServiceImplFile = new File(CreationTest.SERVICE_DIR + File.separator 
            + "src" + File.separator + CommonTools.getPackageDir(mainService)
            + File.separator + "service" + File.separator 
            + mainService.getName() + "Impl.java");
        assertTrue("Main service implementation file did not exist", mainServiceImplFile.exists());
        assertTrue("Main service implementation file could not be read", mainServiceImplFile.canRead());
        
        String mainServiceImplSource = readStream(new FileInputStream(mainServiceImplFile));
        
        // read in the gold method body
        InputStream methodBodyInputStream = getClass().getResourceAsStream(
            "/bdtStartMethodImpl.gold");
        String methodBodySource = readStream(methodBodyInputStream);
        
        
        assertTrue("BDT implementation not found in source", 
            mainServiceImplSource.indexOf(methodBodySource) != -1);
    }
    
    
    private String readStream(InputStream in) throws IOException {
        BufferedInputStream inStream = new BufferedInputStream(in);
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
