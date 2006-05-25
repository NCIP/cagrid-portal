package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.SchemaValidator;

import java.io.File;

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
public class JaxPInvalidCqlTestCase extends TestCase {
	private SchemaValidator validator;
	private String cqlDocsDir;
	
	public JaxPInvalidCqlTestCase(String name) {
		super(name);
	}
	
	
	protected void setUp() {
		try {
			validator = new SchemaValidator("schema/Data/1_gov.nih.nci.cagrid.CQLQuery.xsd");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		cqlDocsDir = System.getProperty("cql.docs.dir") + File.separator + "invalid";
	}
	
	
	protected void tearDown() {
		validator = null;
	}
	
	
	
	private void checkQuery(String filename) {
		File queryFile = new File(cqlDocsDir + File.separator + filename);
		try {
			System.out.println("Validating structure of CQL: " + queryFile.getCanonicalPath());
			validator.validate(queryFile);
			fail("Query should have been invalid, was not");
		} catch (Exception ex) {
			System.out.println("Query verified invalid: " + ex.getMessage());
			assertTrue("Query verified invalid: " + ex.getMessage(), true);
		}
	}
	
	
	public void testTargetWithoutName() {
		checkQuery("targetWithoutName.xml");
	}
	
	
	public void testAttributeWithoutValue() {
		checkQuery("attributeWithoutValue.xml");
	}
	
	
	public void testAttributeWithoutName() {
		checkQuery("attributeWithoutName.xml");
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
	
	
	public void testAssociationWithoutName() {
		checkQuery("associationWithoutName.xml");
	}
	
	
	public void testMultipleChildrenOnAssociation() {
		checkQuery("associationWithTooManyChildren.xml");
	}
	
	
	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(JaxPInvalidCqlTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
