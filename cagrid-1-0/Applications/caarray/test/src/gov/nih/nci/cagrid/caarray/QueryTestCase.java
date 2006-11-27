package gov.nih.nci.cagrid.caarray;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import gov.nih.nci.cagrid.caarray.client.CaArraySvcClient;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.mageom.domain.Description.Description;
import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl;
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
		suite.addTest(new QueryTestCase("testExperimentObjectNestedQuery"));
		suite.addTest(new QueryTestCase("testExperimentObjectGroupingQuery"));
		suite.addTest(new QueryTestCase("testUnmarshall"));
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
	
	public void testExperimentObjectNestedQuery() {
		String queryFileName = "test/resources/experiment_object_1_nested.xml";
		try {
			CQLQueryResults results = runQuery(queryFileName);
			results.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
			printResults(results);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}
	
	public void testExperimentObjectGroupingQuery() {
		String queryFileName = "test/resources/experiment_object_1_grouping.xml";
		try {
			CQLQueryResults results = runQuery(queryFileName);
			results.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
			printResults(results);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}
	
	public void testUnmarshall(){
		try{
			String xmlFile = "test/resources/experiment-1015897558050098-1.xml";
			String mappingFile = "src/gov/nih/nci/cagrid/caarray/common/caarray-xml-mapping.xml";
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element el = builder.parse(new File(xmlFile)).getDocumentElement();
			Mapping mapping = getMapping(mappingFile);
			Unmarshaller u = new Unmarshaller(ExperimentImpl.class);
			u.setMapping(mapping);
			Experiment exp = (Experiment)u.unmarshal(el);
			Description[] descs = exp.getDescriptions();
			System.out.println("identifier=" + exp.getIdentifier());
			for(int i = 0; i < descs.length; i++){
				System.out.println(descs[i].getText());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error unmarshalling: " + ex.getMessage());
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
	
	
	
	private Mapping getMapping(String fileName) throws Exception {
		EntityResolver resolver = new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) {
				if (publicId
						.equals("-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN")) {
					InputStream in = Thread.currentThread()
							.getContextClassLoader().getResourceAsStream(
									"mapping.dtd");
					return new InputSource(in);
				}
				return null;
			}
		};
		org.xml.sax.InputSource mappIS = new org.xml.sax.InputSource(new FileInputStream(fileName));
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		mapping.loadMapping(mappIS);
		return mapping;
	}

}
