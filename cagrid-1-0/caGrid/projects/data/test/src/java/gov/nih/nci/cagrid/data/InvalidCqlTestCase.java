package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.cql.validation.ObjectWalkingCQLValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  InvalidCqlTestCase
 *  Negative testing for CQL validation.  All documents loaded here
 *  should fail the validator.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 23, 2006 
 * @version $Id$ 
 */
public class InvalidCqlTestCase extends TestCase {
	private ObjectWalkingCQLValidator validator;
	private String cqlDocsDir;
	
	public InvalidCqlTestCase(String name) {
		super(name);
	}
	
	
	protected void setUp() {
		validator = new ObjectWalkingCQLValidator();
		cqlDocsDir = System.getProperty("cql.docs.dir") + File.separator + "invalid";
	}
	
	
	protected void tearDown() {
		validator = null;
	}
	
	
	private CQLQuery getQuery(String filename) {
		try {
			System.out.println("Validating structure of CQL: " + filename);
			InputSource queryInput = new InputSource(new FileReader(filename));
			CQLQuery query = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
			return query;
		} catch (FileNotFoundException ex) {
			System.out.println("File not found: " + filename);
			fail(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private void checkQuery(String filename) {
		CQLQuery query = getQuery(cqlDocsDir + File.separator + filename);
		try {
			validator.validateStructure(query);
			fail("Query should have been invalid, was not");
		} catch (Exception ex) {
			// System.out.println("Query verified invalid: " + ex.getMessage());
			assertTrue("Query verified invalid: " + ex.getMessage(), true);
		}
	}
	
	
	public void testMalformedTarget() {
		checkQuery("malformedRoot.xml");
	}
	
	
	public void testMalformedAttribute() {
		checkQuery("malformedAttribute.xml");
	}
	
	
	public void testMultipleChildrenOnTarget() {
		checkQuery("tooManyTargetChildren.xml");
	}
	
	
	public void testOneChildOnGroup() {
		checkQuery("oneGroupChild.xml");
	}
	
	
	public void testNoChildrenOnGroup() {
		checkQuery("noGroupChildren.xml");
	}
	
	
	public void testGroupWithoutLogic() {
		checkQuery("groupWithoutLogic.xml");
	}
	
	
	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(InvalidCqlTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
