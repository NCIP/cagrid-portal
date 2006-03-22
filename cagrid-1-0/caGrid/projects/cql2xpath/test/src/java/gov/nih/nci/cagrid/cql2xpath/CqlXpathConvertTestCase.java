/*
 * Created on Mar 20, 2006
 */
package gov.nih.nci.cagrid.cql2xpath;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cql.CQLQueryType;

import java.io.File;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class CqlXpathConvertTestCase 
	extends TestCase 
{
	public CqlXpathConvertTestCase(String name) 
	{
		super(name);
	}
	
	protected void setUp() {
	}

	protected void tearDown() {
	}

	public void testParse1() 
		throws Exception
	{
		CqlNodeParser parser = new CqlNodeParser();
		CqlNode[] rootNodes = parser.parse(new File("test" + File.separator + "resources" + File.separator + "xml" + File.separator + "scanFeatures_map.xml"));

		CQLQueryType query = (CQLQueryType) Utils.deserializeDocument(
			new File("test" + File.separator + "resources" + File.separator + "xml" + File.separator + "scanFeatures_query1.xml").toString(), 
			CQLQueryType.class
		);
		
		CqlXpathConverter converter = new CqlXpathConverter(rootNodes);
		String xpath = converter.toXpath(query);
		//System.out.println(xpath);
		assertEquals(xpath,
			"/scanFeatures[lsid/text()=\"urn:lsid:rproteomics.cabig.duhs.duke.edu:scanfeatures:773b780a-c8f5-481b-a468-061ce06d7e5d\" and attributes/project/text()=\"MyProject\" and attributes/processingStep/text()=\"load\" and feature/@name/text()=\"mz\" and base64/./text()=\"SJFDHASIUDHDA==\"]"
		);
	}

	public void testParse2() 
		throws Exception
	{
		CqlNodeParser parser = new CqlNodeParser();
		CqlNode[] rootNodes = parser.parse(new File("test" + File.separator + "resources" + File.separator + "xml" + File.separator + "scanFeatures_map.xml"));

		CQLQueryType query = (CQLQueryType) Utils.deserializeDocument(
			new File("test" + File.separator + "resources" + File.separator + "xml" + File.separator + "scanFeatures_query2.xml").toString(), 
			CQLQueryType.class
		);
		
		CqlXpathConverter converter = new CqlXpathConverter(rootNodes);
		String xpath = converter.toXpath(query);
		//System.out.println(xpath);
		assertEquals(xpath,
			"/scanFeatures[lsid/text()=\"urn:lsid:rproteomics.cabig.duhs.duke.edu:scanfeatures:773b780a-c8f5-481b-a468-061ce06d7e5d\"]"
		);
	}

	public static void main(String args[]) 
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CqlXpathConvertTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}