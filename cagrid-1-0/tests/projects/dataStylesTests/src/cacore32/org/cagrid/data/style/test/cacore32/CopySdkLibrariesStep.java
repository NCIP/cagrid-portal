package org.cagrid.data.style.test.cacore32;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

import org.cagrid.data.test.creation.DataTestCaseInfo;

public class CopySdkLibrariesStep extends Step {
    
    private DataTestCaseInfo testInfo = null;
    private File sdkPackageDir = null;
    
    public CopySdkLibrariesStep(DataTestCaseInfo testInfo, File sdkPackageDir) {
        this.testInfo = testInfo;
        this.sdkPackageDir = sdkPackageDir;
    }
    

    public void runStep() throws Throwable {
        File serviceLibDir = new File(testInfo.getDir(), "lib");
        File clientLibDir = new File(sdkPackageDir, "sdk3.2.1" + File.separator + "client" + File.separator + "lib");
        File[] clientJars = clientLibDir.listFiles(new FileFilters.JarFileFilter());
        for (File jar : clientJars) {
            Utils.copyFile(jar, new File(serviceLibDir, jar.getName()));
        }
    }
}
