package gov.nih.nci.cagrid.caarray;

import java.io.StringWriter;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.caarray.client.CaArraySvcClient;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class QueryTestCase extends TestCase {

	private static final String DEFAULT_SERVICE_URL = "http://localhost:8080/wsrf/services/cagrid/CaArraySvc";

	private CaArraySvcClient client;

	public QueryTestCase() {
		// TODO Auto-generated constructor stub
	}

	public QueryTestCase(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new QueryTestCase("testExperimentAttsQuery"));
		suite.addTest(new QueryTestCase("testExperimentObjectQuery"));
		suite.addTest(new QueryTestCase("testExperimentDistinctAttQuery"));
		return suite;
	}

	public void setUp() {
		String url = System.getProperty("test.serviceUrl", DEFAULT_SERVICE_URL);
		try {
			this.client = new CaArraySvcClient(url);
		} catch (Exception ex) {
			throw new RuntimeException("Error instantiating client: "
					+ ex.getMessage(), ex);
		}
	}

	public void testExperimentAttsQuery() {
		String queryFileName = "test/resources/experiment_atts_1.xml";
		try {
			CQLQueryResults results = runQuery(queryFileName);
			results.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
			printResults(results);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}
	
	public void testExperimentObjectQuery() {
		String queryFileName = "test/resources/experiment_object_1.xml";
		try {
			CQLQueryResults results = runQuery(queryFileName);
			results.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
			printResults(results);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}
	
	public void testExperimentDistinctAttQuery() {
		String queryFileName = "test/resources/experiment_distinct_att_1.xml";
		try {
			CQLQueryResults results = runQuery(queryFileName);
			results.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
			printResults(results);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}

	private void printResults(CQLQueryResults results) {
		try {
			StringWriter w = new StringWriter();
			Utils.serializeObject(results, new QName(
					"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet",
					"CQLResultSet"), w);
			System.out.println(w.getBuffer());
		} catch (Exception ex) {
			throw new RuntimeException("Error printing results: "
					+ ex.getMessage(), ex);
		}
	}

	private CQLQueryResults runQuery(String queryFileName) {
		CQLQueryResults results = null;
		try {
			CQLQuery query = (CQLQuery) Utils.deserializeDocument(
					queryFileName, CQLQuery.class);
			results = this.client.query(query);
		} catch (Exception ex) {
			throw new RuntimeException("Error running query: "
					+ ex.getMessage(), ex);
		}
		return results;
	}

}
