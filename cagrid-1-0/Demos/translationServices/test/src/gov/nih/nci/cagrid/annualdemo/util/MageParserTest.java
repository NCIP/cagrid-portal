/*
 * Created on Feb 6, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.File;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class MageParserTest
	extends TestCase
{
	public MageParserTest(String name)
	{
		super(name);
	}
	
	public void testParse()
		throws Exception
	{
		MageParser parser = new MageParser();
		
		parser.parseMicroarray(GeWorkbenchParserTest.readText(
			new File("test", "resources" + File.separator + "mage3_bioassaydata.xml")
		));
		parser.parseGeneNames(GeWorkbenchParserTest.readText(
			new File("test", "resources" + File.separator + "mage3b_features.xml")
		));
		
		MicroarrayData data = parser.getMicroarrayData();
		assertEquals(1, data.arrayNames.size());
		assertEquals(1936, data.geneNames.size());
		assertEquals(1936, data.data.size());
		assertEquals(1, data.data.get(0).length);
	}

	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(MageParserTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
