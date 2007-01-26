/*
 * Created on Jan 25, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.StringWriter;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.common.Utils;
import gridextensions.Data;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class StatmlHelperTest
	extends TestCase
{
	public StatmlHelperTest(String name)
	{
		super(name);
	}
	
	public void testCreateStatmlData()
		throws Exception
	{
		String[] rowNames = {"gene 1", "gene 2"};
		String[] rowDescriptions = {"gene 1 description", "gene 2 description"};
		String[] columnNames = {"Sample 1", "Sample 2", "Sample 3"};
		double[][] values = {
			{1.1, 2.1, 3.1},
			{1.2, 2.2, 3.2}
		};
		StatmlHelper helper = new StatmlHelper();
		Data data = helper.createStatmlData(rowNames, rowDescriptions, columnNames, values);
		
		StringWriter sw = new StringWriter();
		Utils.serializeObject(data, new QName("data"), sw);
		System.out.println(sw.toString());
	}
	
	public static void main(String[] args) throws Exception
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(StatmlHelperTest.class));
		System.exit(result.errorCount() + result.failureCount());
	}

}
