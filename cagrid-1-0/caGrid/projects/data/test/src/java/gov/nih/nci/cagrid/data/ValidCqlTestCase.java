package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.cql.validation.ObjectWalkingCQLValidator;

import java.io.File;
import java.io.FileReader;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

/** 
 *  ValidCqlTestCase
 *  Tests CQL documents for validity with the Object Walking CQL Validator 
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 23, 2006 
 * @version $Id$ 
 */
public class ValidCqlTestCase extends TestCase {
	private ObjectWalkingCQLValidator validator;
	private Exception exception;
	private String cqlDocsDir;
	
	public ValidCqlTestCase(String name) {
		super(name);
	}
	
	
	protected void setUp() {
		validator = new ObjectWalkingCQLValidator();
		cqlDocsDir = System.getProperty("cql.docs.dir");	
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
		} catch (Exception ex) {
			exception = ex;
		}
		return null;
	}
	
	
	public void testReturnAllCql() {
		assertNull(exception);
		try {
			CQLQuery query = getQuery(cqlDocsDir + File.separator + "returnAllOfType.xml");
			validator.validateStructure(query);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			ex.printStackTrace();
			exception = ex;
		}
	}
	
	
	public void testObjectWithAttribute() {
		assertNull(exception);
		try {
			CQLQuery query = getQuery(cqlDocsDir + File.separator + "objectWithAttribute.xml");
			validator.validateStructure(query);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			ex.printStackTrace();
			exception = ex;
		}
	}
	
	
	public void testObjectWithAssociation() {
		assertNull(exception);
		try {
			CQLQuery query = getQuery(cqlDocsDir + File.separator + "objectWithAssociation.xml");
			validator.validateStructure(query);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			ex.printStackTrace();
			exception = ex;
		}
	}
	
	
	public void testObjectWithGroup() {
		assertNull(exception);
		try {
			CQLQuery query = getQuery(cqlDocsDir + File.separator + "objectWithGroup.xml");
			validator.validateStructure(query);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			ex.printStackTrace();
			exception = ex;
		}
	}
	
	
	public void testObjectWithNestedGroup() {
		assertNull(exception);
		try {
			CQLQuery query = getQuery(cqlDocsDir + File.separator + "objectWithNestedGroup.xml");
			validator.validateStructure(query);
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			ex.printStackTrace();
			exception = ex;
		}
	}

	
	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(ValidCqlTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
