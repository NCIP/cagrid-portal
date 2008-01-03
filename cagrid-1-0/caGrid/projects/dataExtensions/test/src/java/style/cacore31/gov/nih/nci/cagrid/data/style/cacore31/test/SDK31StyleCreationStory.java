package gov.nih.nci.cagrid.data.style.cacore31.test;

import gov.nih.nci.cagrid.data.creation.DataTestCaseInfo;
import gov.nih.nci.cagrid.data.creation.DeleteOldServiceStep;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.util.Vector;

/** 
 *  SDK31StyleCreationStory
 *  Tests creating a caGrid Data Service using the SDK31 service style
 * 
 * @author David Ervin
 * 
 * @created Jul 18, 2007 2:35:15 PM
 * @version $Id: SDK31StyleCreationStory.java,v 1.4 2007-12-03 16:27:20 hastings Exp $ 
 */
public class SDK31StyleCreationStory extends Story {
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";

    public SDK31StyleCreationStory() {
        setName("Data Service Creation with caCORE 3_1 Style");
    }


    public String getDescription() {
        return "A test for creating a caGrid data service using the caCORE 3.1 service style";
    }
    
    
    public String getName() {
        return "Data Service Creation with caCORE 3_1 Style";
    }
    
    
    private String getIntroduceBaseDir() {
        String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
        if (dir == null) {
            fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
        }
        return dir;
    }
    
    
    // used to make sure that if we are going to use a junit testsuite to 
    // test this that the test suite will not error out 
    // looking for a single test......
    public void testDummy() throws Throwable {
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector();
        DataTestCaseInfo tci = getTestCaseInfo();
        steps.add(new DeleteOldServiceStep(tci));
        steps.add(new CreateSDK31StyleServiceStep(
            tci, getIntroduceBaseDir()));
        return steps;
    }
    
    
    private DataTestCaseInfo getTestCaseInfo() {
        DataTestCaseInfo tci = new DataTestCaseInfo() {
            public String getDir() {
                return (new File("..")).getAbsolutePath() + File.separator + "dataExtensions" 
                    + File.separator + "test" + File.separator + "TestCaCORE31StyleService";
            }

            
            public String getName() {
                return "TestCaCORE31StyleService";
            }

            
            public String getNamespace() {
                return "http://" + getPackageName() + "/" + getName();
            }
            

            public String getPackageName() {
                return "gov.nih.nci.cagrid.data.style.test.cacore31";
            }
        };
        return tci;
        
    }
}