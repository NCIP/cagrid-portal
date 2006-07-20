package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.cql.validation.CQLValidator;
import gov.nih.nci.cagrid.data.cql.validation.ObjectWalkingCQLValidator;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

/** 
 *  InvalidDomainTestCase
 *  Tests invalid cql against a valid domain model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 26, 2006 
 * @version $Id$ 
 */
public class InvalidDomainTestCase extends TestCase {
	public static final String DOMAIN_MODEL_FILE = "test/resources/domainModel.xml";
	public static final String DOMAIN_MODEL_XSD = "ext/xsd/cagrid/types/data/data.xsd";
	
	private CQLValidator validator = null;
	private DomainModel domainModel = null;
	private String cqlDocsDir = null;
	
	public InvalidDomainTestCase(String name) {
		super(name);
		validator = new ObjectWalkingCQLValidator();
		cqlDocsDir = System.getProperty("cql.docs.dir");
	}
	
	
	private CQLQuery getQuery(String filename) {
		try {
			System.out.println("Validating CQL " + filename + " against domain model");
			InputSource queryInput = new InputSource(new FileReader(filename));
			CQLQuery query = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
			return query;
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
		return null;
	}
	
	
	private DomainModel getDomainModel() {
		if (domainModel == null) {
			try {
				FileInputStream configStream = new FileInputStream(new File("client-config.wsdd"));
				domainModel = (DomainModel) Utils.deserializeObject(new FileReader(DOMAIN_MODEL_FILE), 
					DomainModel.class, configStream);
			} catch (Exception ex) {
				ex.printStackTrace();
				fail("Could not load domain model");
			}
		}
		return domainModel;
	}
	
	
	private void checkQuery(String filename) {
		CQLQuery query = getQuery(cqlDocsDir + File.separator + "invalid" + File.separator + "domain" + File.separator + filename);
		try {
			validator.validateDomain(query, getDomainModel());
		} catch (MalformedQueryException ex) {
			System.out.println("The query is invalid: " + ex.getMessage());
			assertTrue("The query is invalid: " + ex.getMessage(), true);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Query validation failed for some other reason: " + ex.getMessage());
		}
	}
	
	
	public void testInvalidTarget() {
		checkQuery("invalidTarget.xml");
	}
	
	
	public void testInvalidAssociationClass() {
		checkQuery("objectWithInvalidAssociationClass.xml");
	}
	
	
	public void testInvalidAssociationRoleName() {
		checkQuery("objectWithInvalidAssociationRoleName.xml");
	}
	
	
	public void testDomainModelConformsToSchema() {
		try {
			SchemaValidator.validate(DOMAIN_MODEL_XSD, new File(DOMAIN_MODEL_FILE));
			assertTrue(DOMAIN_MODEL_FILE + " appears valid against schema " + DOMAIN_MODEL_XSD, true);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(DOMAIN_MODEL_FILE + " not valid against schema " + DOMAIN_MODEL_XSD + "\n\t" + ex.getMessage());
		}
	}
	
	
	/*
	 * Until the caDSR accuratly returns target and source association edges, 
	 * this test is turned off.  Presently it returns the same class for both
	 * edges, which obviously isn't right
	public void testValidObjectWithAssociation() {
		checkQuery("objectWithAssociation.xml");
	}
	*/
	
	
	public static void main(String[] args) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(InvalidDomainTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
