package gov.nih.nci.cagrid.introduce.codegen.services.methods;

import junit.framework.TestCase;

public class SyncSourceTest extends TestCase {

	public void testRemoveMultiNewLines(){
		assertEquals("abc", SyncSource.removeMultiNewLines("abc"));
		assertEquals("abc\n\n", SyncSource.removeMultiNewLines("abc\n\n"));
		assertEquals("\n\na\nb\n\nc\n\n", SyncSource.removeMultiNewLines("\n\na\nb\n\nc\n\n"));
		assertEquals("\n\na\nb\n\nc\n\n", SyncSource.removeMultiNewLines("\n\na\nb\n\nc\n\n\n"));
		assertEquals("\n\na\nb\n\nc\n\n", SyncSource.removeMultiNewLines("\n\na\nb\n\nc\n\n\n\n"));
		assertEquals("\n\na\nb\n\nc\n\n", SyncSource.removeMultiNewLines("\n\na\nb\n\nc\n\n\n\n\n"));
		assertEquals("\n\na\nb\n\nc\n\n", SyncSource.removeMultiNewLines("\n\na\nb\n\nc\n\n\n\n\n\n"));
		assertEquals("\n\na\nb\n\nc\n\n", SyncSource.removeMultiNewLines("\n\n\n\n\na\nb\n\nc\n\n\n\n\n\n"));
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(SyncSourceTest.class);
	}
}
