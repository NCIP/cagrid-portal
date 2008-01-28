package gov.nih.nci.cagrid.testing.core;

/** 
 *  TestingConstants
 *  Constants used in testing
 * 
 * @author David Ervin
 * 
 * @created Oct 31, 2007 10:40:12 AM
 * @version $Id: TestingConstants.java,v 1.3 2008-01-28 20:15:22 hastings Exp $ 
 */
public class TestingConstants {

    // the default min and max testing ports
    public static final Integer TEST_PORT_LOWER_BOUND = Integer.valueOf(44444);
    public static final Integer TEST_PORT_UPPER_BOUND = Integer.valueOf(TEST_PORT_LOWER_BOUND.intValue() + 1000);

    public static final String TEST_TEMP_DIR = "tmp";
    
    private TestingConstants() {
        // prevents instantiation
    }
}
