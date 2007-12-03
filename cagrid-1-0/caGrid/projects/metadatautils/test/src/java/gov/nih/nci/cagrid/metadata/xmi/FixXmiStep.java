package gov.nih.nci.cagrid.metadata.xmi;

import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileFilter;

/** 
 *  FixXmiStep
 *  Step to run the Fix-xmi executor
 * 
 * @author David Ervin
 * 
 * @created Nov 8, 2007 11:18:10 AM
 * @version $Id: FixXmiStep.java,v 1.2 2007-12-03 16:27:18 hastings Exp $ 
 */
public class FixXmiStep extends Step {
    private String sdkDir;
    private String modelsDir;
    
    public FixXmiStep(String sdkDir, String modelsDir) {
        this.sdkDir = sdkDir;
        this.modelsDir = modelsDir;
    }
    

    public void runStep() throws Throwable {
        File[] xmiFiles = listXmiFiles();
        for (File xmi : xmiFiles) {
            System.out.println("fix-xmi for " + xmi.getName());
            File out = FixXmiExecutor.fixEaXmiModel(xmi, new File(sdkDir));
            assertTrue("FIX-XMI did not return a fixed model file", out != null && out.exists());
        }
    }
    
    
    private File[] listXmiFiles() {
        File dir = new File(modelsDir);
        assertTrue("Models dir (" + dir.getAbsolutePath() + ") does not exist", dir.exists());
        File[] xmis = dir.listFiles(new FileFilter() {
            public boolean accept(File path) {
                return path.getName().toLowerCase().endsWith(".xmi");
            }
        });
        assertTrue("No XMIs found in " + dir.getAbsolutePath(), xmis != null && xmis.length != 0);
        return xmis;
    }
}
