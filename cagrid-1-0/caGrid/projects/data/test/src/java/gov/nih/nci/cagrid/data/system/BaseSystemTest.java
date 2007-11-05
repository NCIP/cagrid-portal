package gov.nih.nci.cagrid.data.system;

import com.atomicobject.haste.framework.Story;

/** 
 *  BaseSystemTest
 *  The base system test
 * 
 * @author David Ervin
 * 
 * @created Mar 14, 2007 2:21:41 PM
 * @version $Id: BaseSystemTest.java,v 1.2 2007-11-05 21:33:55 dervin Exp $ 
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
