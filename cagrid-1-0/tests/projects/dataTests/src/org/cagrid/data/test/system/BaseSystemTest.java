package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.testing.system.haste.Story;

/** 
 *  BaseSystemTest
 *  The base system test
 * 
 * @author David Ervin
 * 
 * @created Mar 14, 2007 2:21:41 PM
 * @version $Id: BaseSystemTest.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public abstract class BaseSystemTest extends Story {
	public static final String INTRODUCE_DIR_PROPERTY = "introduce.base.dir";
    
    public BaseSystemTest() {
        super();
    }
	

	protected String getIntroduceBaseDir() {
		String dir = System.getProperty(INTRODUCE_DIR_PROPERTY);
		if (dir == null) {
			fail("Introduce base dir environment variable "
					+ INTRODUCE_DIR_PROPERTY + " is required");
		}
		return dir;
	}
	

	// used to make sure that if we are going to use a junit testsuite to
	// test this that the test suite will not error out
	// looking for a single test......
	public void testDummy() throws Throwable {
	}
}
