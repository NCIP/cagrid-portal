package gov.nih.nci.cagrid.data.creation;

import java.io.File;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;

/** 
 *  DataTestCaseInfo
 *  Test case info for use in data service testing
 * 
 * @author David Ervin
 * 
 * @created Jun 12, 2007 11:46:16 AM
 * @version $Id: DataTestCaseInfo.java,v 1.3 2007-11-29 18:04:59 hastings Exp $ 
 */
public abstract class DataTestCaseInfo extends TestCaseInfo {

    public String getPackageDir() {
        return getPackageName().replace('.',File.separatorChar);
    }
    

    public String getResourceFrameworkType() {
        return IntroduceConstants.INTRODUCE_MAIN_RESOURCE + "," + IntroduceConstants.INTRODUCE_SINGLETON_RESOURCE + "," + IntroduceConstants.INTRODUCE_IDENTIFIABLE_RESOURCE;
    }
    
    
    /**
     * If creating an enumeration or BDT service, override this appropriatly
     * @return
     *      The extensions to use
     */
    public String getExtensions() {
        return "data";
    }
    
    
    
}
