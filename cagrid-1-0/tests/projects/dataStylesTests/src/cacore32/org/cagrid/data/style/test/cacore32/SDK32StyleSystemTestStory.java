package org.cagrid.data.style.test.cacore32;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.ZipUtilities;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.cagrid.data.test.creation.DataTestCaseInfo;
import org.cagrid.data.test.creation.DeleteOldServiceStep;
import org.cagrid.data.test.system.RebuildServiceStep;

/** 
 *  SDK32StyleSystemTestStory
 *  Tests creating and invoking a caGrid Data Service using the SDK32 service style
 * 
 * @author David Ervin
 * 
 * @created Jul 18, 2007 2:35:15 PM
 * @version $Id: SDK32StyleSystemTestStory.java,v 1.1 2008-11-06 16:32:15 dervin Exp $ 
 */
public class SDK32StyleSystemTestStory extends Story {
    public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
    private DataTestCaseInfo tci = null;
    private File sdkPackageDir = null;

    public SDK32StyleSystemTestStory() {
        setName("Data Service System Test with caCORE 3_2 / 3_2_1 Style");
    }


    public String getDescription() {
        return "A test for creating and invoking a caGrid data service using the caCORE 3.2 / 3.2.1 service style";
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
    
    
    public boolean storySetUp() {
        this.tci = new DataTestCaseInfo() {
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
        
        try {
            sdkPackageDir = File.createTempFile("SDK321Package", "dir");
            sdkPackageDir.delete();
            sdkPackageDir.mkdirs();
        } catch (IOException ex) {
            ex.printStackTrace();
            fail("Error creating temp directory: " + ex.getMessage());
        }
        File sdkPackageZip = new File(Sdk32TestConstants.SDK_PACKAGE_ZIP);
        try {
            System.out.println("Unzipping SDK 3.2.1 package zip " + sdkPackageZip.getAbsolutePath());
            ZipUtilities.unzip(sdkPackageZip, sdkPackageDir);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error unzipping SDK package: " + ex.getMessage());
        }

        return true;
    }


    protected Vector steps() {
        Vector<Step> steps = new Vector<Step>();
        steps.add(new DeleteOldServiceStep(tci));
        steps.add(new CreateSDK32StyleServiceStep(
            tci, getIntroduceBaseDir(), sdkPackageDir));
        steps.add(new EditServiceDescriptionStep(tci));
        steps.add(new AddCabioSchemaStep(tci, sdkPackageDir));
        steps.add(new CopyEtcFilesStep(tci));
        steps.add(new CopySdkLibrariesStep(tci, sdkPackageDir));
        steps.add(new RebuildServiceStep(tci, getIntroduceBaseDir()));
        return steps;
    }
    
    
    protected void storyTearDown() throws Throwable {
        // Step destroyStep = new DeleteOldServiceStep(tci);
        Step deleteTempSdkStep = new Step() {
            public void runStep() throws Throwable {
                Utils.deleteDir(sdkPackageDir);
            }
        };
        // destroyStep.runStep();
        deleteTempSdkStep.runStep();
    }
}
