/*
 * Created on Mar 20, 2006
 */
package gov.nih.nci.cagrid.cql2xpath;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class CqlNodeParserTestCase 
	extends TestCase 
{
	public CqlNodeParserTestCase(String name) 
	{
		super(name);
	}
	
	protected void setUp() {
	}

	protected void tearDown() {
	}

	public void testParse() 
		throws ParserConfigurationException, SAXException, IOException
	{
		CqlNodeParser parser = new CqlNodeParser();
		CqlNode[] node = parser.parse(new File("test" + File.separator + "resources" + File.separator + "xml" + File.separator + "scanFeatures_map.xml"));
		assertNotNull(node);
		assertEquals(node.length, 1);

		// root
		CqlNode root = node[0];
		checkNode(root, "scanFeatures", "edu.duke.cabig.scanFeatures.ScanFeatures");
		assertEquals(root.properties.size(), 1);
		checkProperty((CqlNode.CqlProperty) root.properties.get(0), "lsid", "lsid");
		assertEquals(root.children.size(), 2);

		// attributes
		CqlNode attributes = (CqlNode) root.children.get(0);
		checkNode(attributes, "attributes", "edu.duke.cabig.scanFeatures.Attributes");
		assertEquals(attributes.properties.size(), 13);
		checkProperty((CqlNode.CqlProperty) attributes.properties.get(0), "project", "project");
		checkProperty((CqlNode.CqlProperty) attributes.properties.get(12), "scanStepSize", "scanStepSize");
		assertEquals(attributes.children.size(), 0);
		
		// feature
		CqlNode feature = (CqlNode) root.children.get(1);
		checkNode(feature, "feature", "edu.duke.cabig.scanFeatures.Feature");
		assertEquals(feature.properties.size(), 1);
		checkProperty((CqlNode.CqlProperty) feature.properties.get(0), "@name", "name");
		assertEquals(feature.children.size(), 1);
		
		// base64
		CqlNode base64 = (CqlNode) feature.children.get(0);
		checkNode(base64, "base64", "edu.duke.cabig.scanFeatures.Base64");
		assertEquals(base64.properties.size(), 3);
		checkProperty((CqlNode.CqlProperty) base64.properties.get(0), "@type", "type");
		checkProperty((CqlNode.CqlProperty) base64.properties.get(1), "@count", "count");
		checkProperty((CqlNode.CqlProperty) base64.properties.get(2), ".", "value");
		assertEquals(attributes.children.size(), 0);
		
	}
	
	protected void checkNode(CqlNode node, String xmlName, String cqlName)
	{
		assertEquals(node.xmlName, xmlName);
		assertEquals(node.cqlName, cqlName);
	}
	
	protected void checkProperty(CqlNode.CqlProperty node, String xmlName, String cqlName)
	{
		assertEquals(node.xmlName, xmlName);
		assertEquals(node.cqlName, cqlName);
	}

	public static void main(String args[]) 
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CqlNodeParserTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}