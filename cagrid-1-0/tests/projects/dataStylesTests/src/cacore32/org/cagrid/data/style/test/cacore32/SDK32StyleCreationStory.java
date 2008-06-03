package org.cagrid.data.style.test.cacore32;

import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.util.Vector;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;

/** 
 *  SDK32StyleCreationStory
 *  Tests creating a caGrid Data Service using the SDK32 service style
 * 
 * @author David Ervin
 * 
 * @created Jul 18, 2007 2:35:15 PM
 * @version $Id: SDK32StyleCreationStory.java,v 1.2 2008-06-03 18:24:26 dervin Exp $ 
 */
public class SDK32StyleCreationStory extends Story {
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";

    public SDK32StyleCreationStory() {
        setName("Data Service Creation with caCORE 3_2 / 3_2_1 Style");
    }


    public String getDescription() {
        return "A test for creating a caGrid data service using the caCORE 3.2 / 3.2.1 service style";
    }
    
    
    public String getName() {
        return "Data Service Creation with caCORE 3_2 / 3_2_1 Style";
    }
    
    
    private String getIntroduceBaseDir() {
        String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
        if (dir == null) {
            fail("Introduce base dir environment variable " + INTRODUCE_DIR_PROPERTY + " is required");
        }
        return dir;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector();
        DataTestCaseInfo tci = getTestCaseInfo();
        steps.add(new DeleteOldServiceStep(tci));
        steps.add(new CreateSDK32StyleServiceStep(
            tci, getIntroduceBaseDir()));
        return steps;
    }
    
    
    private DataTestCaseInfo getTestCaseInfo() {
        DataTestCaseInfo tci = new DataTestCaseInfo() {
            public String getServiceDirName() {
                return getName();
            }

            
            public String getName() {
                return "TestCaCORE32StyleService";
            }

            
            public String getNamespace() {
                return "http://" + getPackageName() + "/" + getName();
            }
            

            public String getPackageName() {
                return "gov.nih.nci.cagrid.data.style.test.cacore32";
            }
        };
        return tci;
        
    }
}
