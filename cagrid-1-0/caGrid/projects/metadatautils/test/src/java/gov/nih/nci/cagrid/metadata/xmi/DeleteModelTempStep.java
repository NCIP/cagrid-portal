package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  DeleteModelTempStep
 *  Deletes the temporary model directory
 * 
 * @author David Ervin
 * 
 * @created Oct 24, 2007 12:03:38 PM
 * @version $Id: DeleteModelTempStep.java,v 1.1 2007-10-24 16:43:23 dervin Exp $ 
 */
public class DeleteModelTempStep extends Step {
    
    private String tempDir;
    
    public DeleteModelTempStep(String tempDir) {
        this.tempDir = tempDir;
    }
    

    public void runStep() throws Throwable {
        File dir = new File(tempDir);
        Utils.deleteDir(dir);
    }
}
