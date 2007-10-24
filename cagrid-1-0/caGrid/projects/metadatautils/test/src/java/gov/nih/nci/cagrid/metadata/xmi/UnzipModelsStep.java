package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.common.ZipUtilities;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UnzipModelsStep
 *  Unpacks the XMIs and Gold Domain Models
 * 
 * @author David Ervin
 * 
 * @created Oct 24, 2007 11:50:38 AM
 * @version $Id: UnzipModelsStep.java,v 1.1 2007-10-24 16:43:23 dervin Exp $ 
 */
public class UnzipModelsStep extends Step {
    
    private String modelsZipFile;
    private String destinationDir;
    
    public UnzipModelsStep(String modelsZipFile, String destinationDir) {
        this.modelsZipFile = modelsZipFile;
        this.destinationDir = destinationDir;
    }
    

    public void runStep() throws Throwable {
        File zipFile = new File(modelsZipFile);
        assertTrue("Model zip file not found", zipFile.exists());
        File destDir = new File(destinationDir);
        destDir.mkdirs();
        ZipUtilities.unzip(zipFile, destDir);
    }
}
