package gov.nih.nci.cagrid.portal;

import junit.framework.TestCase;

/**
 * Base class that will run tests and make sure
 * they complete in an acceptable time limit
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractTimeSensitiveTest extends TestCase {
    private Long start;

    /**
     * Subclass need to tell what is an acceptable time limit
     * <p/>
     * Tests will fail if they don't complete in the acceptable
     * time limit
     *
     * @return time in milli seconds
     */
    protected abstract Long getAcceptableTime();

    @Override
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        start = PortalTestUtils.getTimestamp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Long _timeTaken = PortalTestUtils.getTimestamp() - start;
        System.out.println("Test completed in " + _timeTaken / 1000 + " seconds.");
        assertTrue("Test did not complete in acceptable time",
                _timeTaken < getAcceptableTime());
    }
}
