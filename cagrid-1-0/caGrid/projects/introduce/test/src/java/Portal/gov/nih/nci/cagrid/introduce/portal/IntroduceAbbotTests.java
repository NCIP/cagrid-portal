package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;

public class IntroduceAbbotTests extends TestCase {
	
	private static final String testDir = "TestService";
	private	static final String tempDir = "C:/TempIntroduceTestFiles";
	private static final String testSchema = "test/resources/abbot/1_TestString.xsd";
	private static final String tempTestSchema = tempDir + "/1_TestString.xsd";
	private String baseDir;
	
	public void testCreateService(){	
		baseDir = System.getProperty("basedir");
		if (baseDir == null) {
			System.err.println("basedir system property not set");
			fail();
		}
		
		System.out.println(baseDir);
		
		//create ScriptFixture to test script
//		ScriptFixture tester = new ScriptFixture("/test/resources/abbot/TestIntroduce_2.xml");
//		
//		//create TestResult to hold the result of the test
//		TestResult tr = null;
//		
//		try {
//			//run test
//			tr = tester.run();
//		} catch (Throwable e) {
//			e.printStackTrace();
//			fail("The test failed.");
//		}
		
		//add more tests here to check the file system...talk to shannon about this
		
	}
	

	protected void setUp() throws Exception {
		super.setUp();
		
		File tempDirectory = new File(tempDir);
		File testServiceDir = new File(testDir);
		if(tempDirectory.exists()){
			if(!(Utils.deleteDir(tempDirectory)))
				fail("Unable to delete C:/TempIntroduceTestFiles");
		}
	    
	    boolean success = (tempDirectory).mkdir();
	    if (!success) {
	        fail("Error creating temporary test directory.");
	    }
	    
	    this.copy(new File(testSchema), new File (tempTestSchema));

	}

	protected void tearDown() throws Exception {
		super.tearDown();
//		if(!(Utils.deleteDir(new File(tempDir))))
//			fail("Unable to delete C:/TempIntroduceTestFiles");
	}
	
	private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
    
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
