package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.ZipUtilities;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  UnzipStep
 *  Step unpacks a zip file
 * 
 * @author David Ervin
 * 
 * @created Nov 8, 2007 10:56:21 AM
 * @version $Id: UnzipStep.java,v 1.1 2007-11-08 20:28:07 dervin Exp $ 
 */
public class UnzipStep extends Step {
    private String zipLocation;
    private String unzipLocation;
    
    public UnzipStep(String zip, String unzipDir) {
        this.zipLocation = zip;
        this.unzipLocation = unzipDir;
    }
    

    public void runStep() throws Throwable {
        File zipFile = new File(zipLocation);
        File outDir = new File(unzipLocation);
        assertTrue("Zip file (" + zipFile.getAbsolutePath() + ") not found", 
            zipFile.exists());
        if (outDir.exists()) {
            Utils.deleteDir(outDir);
        }
        ZipUtilities.unzip(zipFile, outDir);
    }
}
