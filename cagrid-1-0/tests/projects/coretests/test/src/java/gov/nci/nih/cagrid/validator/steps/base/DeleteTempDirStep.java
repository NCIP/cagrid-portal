package gov.nci.nih.cagrid.validator.steps.base;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteTempDirStep
 *  Step deletes a temporary directory
 * 
 * @author David Ervin
 * 
 * @created Aug 28, 2007 11:31:24 AM
 * @version $Id: DeleteTempDirStep.java,v 1.1 2007-08-28 16:02:59 dervin Exp $ 
 */
public class DeleteTempDirStep extends Step {
    
    File tempDir;

    public DeleteTempDirStep(File tempDir) {
        this.tempDir = tempDir;
    }
    
    
    public String getName() {
        return "Delete temp dir " + tempDir.getAbsolutePath();
    }


    public void runStep() throws Throwable {
        if (tempDir.exists()) {
            Utils.deleteDir(tempDir);
        }
    }
}
