package gov.nih.nci.cagrid.sdkquery4.test.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.sdkquery4.test.system.steps.SDK4StyleCreationStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  Sdk4ServiceStyleCreationTest
 *  Tests ability to create a SDK 4 style data service
 * 
 * @author David Ervin
 * 
 * @created Jan 29, 2008 9:09:34 AM
 * @version $Id: Sdk4ServiceStyleCreationTest.java,v 1.2 2008-01-31 20:29:09 dervin Exp $ 
 */
public class Sdk4ServiceStyleCreationTest extends Story {
    // system property to locate the Introduce base directory
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
    public static final String SERVICE_PACKAGE = "org.cagrid.sdkquery4.test";
    public static final String SERVICE_NAME = "TestSDK4StyleDataService";

    
    private DataTestCaseInfo styleTestCaseInfo = null;

    public Sdk4ServiceStyleCreationTest() {
        super();
    }


    public String getDescription() {
        return "Tests ability to create a SDK 4 style data service";
    }
    
    
    public String getName() {
        return "SDK 4 Style Creation Test";
    }
    
    
    public boolean storySetUp() throws Throwable {
        File tmpDir = new File("tmp");
        tmpDir.mkdirs();
        final File tempServiceDir = File.createTempFile(SERVICE_NAME, "temp", tmpDir);
        // make the file a directory
        tempServiceDir.delete();
        tempServiceDir.mkdirs();
        
        boolean success = tempServiceDir.exists() && tempServiceDir.isDirectory();
        
        styleTestCaseInfo = new DataTestCaseInfo() {
            public String getDir() {
                return tempServiceDir.getAbsolutePath();
            }
            
            
            public String getName() {
                return SERVICE_NAME;
            }
            
            
            public String getNamespace() {
                return "http://" + SERVICE_PACKAGE + "/" + getName();
            }
            
            
            public String getPackageName() {
                return SERVICE_PACKAGE;
            }
            
            public String getExtensions() {
                return "cagrid_metadata," + super.getExtensions();
            }
        };
        return success;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        // steps.add(new CreationStep(styleTestCaseInfo, getIntroduceBaseDir()));
        // steps.add(new SDK4StyleConfigurationStep(new File(styleTestCaseInfo.getDir())));
        steps.add(new SDK4StyleCreationStep(styleTestCaseInfo, getIntroduceBaseDir()));
        return steps;
    }
    
    
    public void storyTearDown() throws Throwable {
        Utils.deleteDir(new File(styleTestCaseInfo.getDir()));
    }
    
    
    private String getIntroduceBaseDir() {
        String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
        if (dir == null) {
            fail("Introduce base dir system property " + INTRODUCE_DIR_PROPERTY + " is required");
        }
        return dir;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(Sdk4ServiceStyleCreationTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
