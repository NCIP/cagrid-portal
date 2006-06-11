/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core;

import gov.nci.nih.cagrid.tests.core.util.IntroduceServiceInfo;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class IntroduceServiceInfoTest
	extends TestCase
{
	public IntroduceServiceInfoTest(String name)
	{
		super(name);
	}
	
	public void testServiceInfo() 
		throws ParserConfigurationException, SAXException, IOException
	{
		IntroduceServiceInfo info = new IntroduceServiceInfo(new File(System.getProperty(
			"IntroduceServiceInfoTest.file",
			"test" + File.separator + "resources" + File.separator + "IntroduceServiceInfoTest" + File.separator + "introduce.xml"
		)));
		
		assertEquals("http://tests.cagrid.nci.nih.gov/BasicAnalyticalService", info.getNamespace());
		assertEquals("gov.nih.nci.cagrid.tests", info.getPackageName());
		assertEquals("BasicAnalyticalService", info.getServiceName());
		String[] methodNames = info.getMethodNames();
		assertEquals(1, methodNames.length);
		assertEquals("reverseTranslate", methodNames[0]);
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(SourceUtilsTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
