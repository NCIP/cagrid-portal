/*
 * Created on Mar 20, 2006
 */
package gov.nih.nci.cagrid.cql2xpath;

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

	public void testBlah()
	{
	}
	
	protected void setUp() {
	}

	protected void tearDown() {
	}

	public static void main(String args[]) 
	{
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(CqlNodeParserTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}