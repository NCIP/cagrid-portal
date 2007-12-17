package gov.nih.nci.cagrid.testing.core;

/** 
 *  TestingConstants
 *  Constants used in testing
 * 
 * @author David Ervin
 * 
 * @created Oct 31, 2007 10:40:12 AM
 * @version $Id: TestingConstants.java,v 1.2 2007-12-17 17:38:02 dervin Exp $ 
 */
public class TestingConstants {

    // the default min and max testing ports
    public static final Integer TEST_PORT_LOWER_BOUND = Integer.valueOf(44444);
    public static final Integer TEST_PORT_UPPER_BOUND = Integer.valueOf(TEST_PORT_LOWER_BOUND.intValue() + 1000);
    
    // system property; if == 'true', the testing port assignment mode will be used
    public static final String USE_TESTING_ASSIGNMENT_MODE = "use.testing.port.assignment";
    
    public static final String TEST_TEMP_DIR = "tmp";
    
    private TestingConstants() {
        // prevents instantiation
    }
}
