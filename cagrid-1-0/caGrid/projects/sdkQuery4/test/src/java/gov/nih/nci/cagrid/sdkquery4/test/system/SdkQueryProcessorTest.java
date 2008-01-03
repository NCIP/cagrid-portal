package gov.nih.nci.cagrid.sdkquery4.test.system;

import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.cagrid.testing.system.haste.Story;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  SdkQueryProcessorTest
 *  Tests the SDK 4.0 Query Processor
 * 
 * @author David Ervin
 * 
 * @created Jan 3, 2008 11:21:33 AM
 * @version $Id: SdkQueryProcessorTest.java,v 1.1 2008-01-03 17:53:29 dervin Exp $ 
 */
public class SdkQueryProcessorTest extends Story {
    
    public static final String PROCESSOR_CONFIG_FILE = "processor.config.file";
    
    private SDK4QueryProcessor queryProcessor;

    public SdkQueryProcessorTest() {
        super();
    }
    
    
    public String getName() {
        return "SDK 4.0 Query Processor Test";
    }


    public String getDescription() {
        return "Tests the SDK 4.0 Query Processor";
    }
    
    
    public boolean storySetUp() {
        try {
            Properties config = loadProcessorProperties();
            queryProcessor = new SDK4QueryProcessor();
            // TODO: will we need a wsdd?
            queryProcessor.initialize(config, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error initializing query processor: " + ex.getMessage());
        }
        return true;
    }


    protected Vector<Step> steps() {
        Vector<Step> steps = new Vector<Step>();
        
        return steps;
    }
    
    
    private Properties loadProcessorProperties() throws IOException {
        String propertiesFilename = System.getProperty(PROCESSOR_CONFIG_FILE);
        FileInputStream fis = new FileInputStream(propertiesFilename);
        Properties props = new Properties();
        props.load(fis);
        return props;
    }


    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(SdkQueryProcessorTest.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
