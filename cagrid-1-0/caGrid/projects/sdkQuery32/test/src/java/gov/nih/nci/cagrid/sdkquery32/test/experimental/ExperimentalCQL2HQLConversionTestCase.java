package gov.nih.nci.cagrid.sdkquery32.test.experimental;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.sdk32query.experimental.hql313.CQL2HQL;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  ExperimentalCQL2HQLConversionTestCase
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Mar 8, 2007 3:09:20 PM
 * @version $Id: ExperimentalCQL2HQLConversionTestCase.java,v 1.1 2007-03-08 20:22:52 dervin Exp $ 
 */
public class ExperimentalCQL2HQLConversionTestCase extends TestCase {

	public ExperimentalCQL2HQLConversionTestCase(String name) {
		super(name);
	}
	
	
	private CQLQuery deserializeQuery(String filename) {
		CQLQuery query = null;
		try {
			query = (CQLQuery) Utils.deserializeDocument(filename, CQLQuery.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error deserializing CQL query: " + ex.getMessage());
		}
		return query;
	}
	
	
	private void translateQuery(String filename) {
		CQLQuery query = deserializeQuery(filename);
		String criteria = null;
		try {
			criteria = CQL2HQL.convertToHql(query, false, false);
			System.out.println(criteria);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error translating query: " + ex.getMessage());
		}
	}
	
	
	public void testReturnAllQuery() {
		translateQuery("ext/resources/returnAllOfType.xml");
	}
	
	
	public void testAttributePredicates() {
		translateQuery("ext/resources/attributePredicates.xml");
	}
	
	
	public void testObjectWithAssociation() {
		translateQuery("ext/resources/objectWithAssociation.xml");
	}
	
	
	public void testObjectWithAssociationNoRoleName() {
		translateQuery("ext/resources/objectWithAssociationNoRoleName.xml");
	}
	
	
	public void testObjectWithAttribute() {
		translateQuery("ext/resources/objectWithAttribute.xml");
	}
	
	
	public void testObjectWithGroup() {
		translateQuery("ext/resources/objectWithGroup.xml");
	}
	
	
	public void testObjectWithNestedGroup() {
		translateQuery("ext/resources/objectWithNestedGroup.xml");
	}
	

	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(
			new TestSuite(ExperimentalCQL2HQLConversionTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
