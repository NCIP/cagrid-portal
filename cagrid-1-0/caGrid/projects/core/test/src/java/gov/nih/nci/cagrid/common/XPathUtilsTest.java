package gov.nih.nci.cagrid.common;

import junit.framework.TestCase;

import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.NamespaceManager;


public class XPathUtilsTest extends TestCase {

	private NamespaceManager namespaces;


	public static void main(String[] args) {
		junit.textui.TestRunner.run(XPathUtilsTest.class);
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			namespaces = new NamespaceManager();
			namespaces.addNamespace("a", new Namespace("http://foo/d"));
			namespaces.setDefaultNamespace(new Namespace("gme://abc/def"));
		} catch (MalformedNamespaceException e) {
			e.printStackTrace();
			fail("Error setting up namespaces:" + e.getMessage());
		}
	}


	public void testNoUse() {
		assertNoModification(null);
		assertNoModification("");
		assertNoModification("/");
		assertNoModification("/a");
		assertNoModification("/a/b/c");
		assertNoModification("/a[@b!='foo']");
		assertNoModification("/a[@b!='foo']/*[namespace-uri()='http://somedomain.com/200/foobar' and local-name()='SomeElementName']");
	}


	public void assertNoModification(String xpath) {
		try {
			String translated = XPathUtils.translateXPath(xpath, namespaces);
			assertEquals(translated, xpath);
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}


	public void assertRepeatable(String xpath) {
		try {
			String translated = XPathUtils.translateXPath(xpath, namespaces);
			System.out.println();
			System.out.println("ORIGINAL:"+xpath);
			System.out.println("TRANSLATED:"+translated);
			assertEquals(translated, XPathUtils.translateXPath(translated, namespaces));
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
		}
	}


	public void testValidPaths() {
		
		assertRepeatable("/a:B");
		assertRepeatable("/a:B/a:C");
		assertRepeatable("/a:B[@b!='foo']");
		assertRepeatable("/a:B[@b!='foo']/a:C");
		assertRepeatable("/a:B[@b!='foo' and a:C]/a:C");
	}
}
