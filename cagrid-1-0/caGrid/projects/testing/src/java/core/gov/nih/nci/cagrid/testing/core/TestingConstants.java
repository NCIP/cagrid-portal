package gov.nih.nci.cagrid.testing.core;

/** 
 *  TestingConstants
 *  Constants used in testing
 * 
 * @author David Ervin
 * 
 * @created Oct 31, 2007 10:40:12 AM
 * @version $Id: TestingConstants.java,v 1.1 2007-10-31 19:27:55 dervin Exp $ 
 */
public class TestingConstants {

    public static final Integer TEST_PORT_LOWER_BOUND = Integer.valueOf(44444);
    public static final Integer TEST_PORT_UPPER_BOUND = Integer.valueOf(TEST_PORT_LOWER_BOUND.intValue() + 200);
    
    public static final String TEST_TEMP_DIR = "tmp";
    
    private TestingConstants() {
        // prevents instantiation
    }
}
