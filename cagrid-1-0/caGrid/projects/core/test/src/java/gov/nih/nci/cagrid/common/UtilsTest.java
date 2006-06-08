package gov.nih.nci.cagrid.common;

import junit.framework.TestCase;


public class UtilsTest extends TestCase {

	String a1 = "a";
	String a2 = "a";
	String b = "b";
	String aws = "a  ";
	String ws = "  ";


	public void testClean() {
		assertEquals(a1, Utils.clean(a1));
		assertEquals(aws, Utils.clean(aws));
		assertNull(Utils.clean(ws));
		assertNull(Utils.clean(new String()));
		assertNull(Utils.clean(null));
	}


	public void testEqualsObjectObject() {

		assertTrue(Utils.equals(null, null));
		assertTrue(Utils.equals(a1, a1));
		assertTrue(Utils.equals(a1, a2));
		assertTrue(Utils.equals(a2, a1));

		assertFalse(Utils.equals(a1, b));
		assertFalse(Utils.equals(b, a1));
		assertFalse(Utils.equals(null, a1));
		assertFalse(Utils.equals(a1, null));
	}

}
