package gov.nih.nci.cagrid.caarray;

import gov.nih.nci.cagrid.caarray.client.CaArraySvcClient;
import gov.nih.nci.cagrid.caarray.encoding.MGEDCubeHandler;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.mageom.domain.BioAssayData.BioDataCube;
import gov.nih.nci.mageom.domain.BioAssayData.MeasuredBioAssayData;
import gov.nih.nci.mageom.domain.Description.Description;
import gov.nih.nci.mageom.domain.Experiment.Experiment;
import gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class QueryTestCase extends TestCase {

	private static Log LOG = LogFactory.getLog(QueryTestCase.class);

	private static final String DEFAULT_SERVICE_URL = "http://localhost:8081/wsrf/services/cagrid/CaArraySvc";

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
		// suite.addTest(new QueryTestCase("testExperimentAttsQuery"));
		// suite.addTest(new QueryTestCase("testExperimentObjectQuery"));
		// suite.addTest(new QueryTestCase("testExperimentDistinctAttQuery"));
		// suite.addTest(new QueryTestCase("testExperimentObjectNestedQuery"));
		// suite.addTest(new
		// QueryTestCase("testExperimentObjectGroupingQuery"));
		// suite.addTest(new QueryTestCase("testUnmarshall"));
		suite.addTest(new QueryTestCase("testMeasuredBioAssayDataObjectQuery"));
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
			results
					.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
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
			results
					.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
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
			results
					.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
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
			results
					.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
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
			results
					.setTargetClassname("gov.nih.nci.mageom.domain.Experiment.impl.ExperimentImpl");
			printResults(results);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}

	public void testUnmarshall() {
		try {
			String xmlFile = "test/resources/experiment-1015897558050098-1.xml";
			String mappingFile = "src/gov/nih/nci/cagrid/caarray/common/caarray-xml-mapping.xml";
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Element el = builder.parse(new File(xmlFile)).getDocumentElement();
			Mapping mapping = getMapping(mappingFile);
			Unmarshaller u = new Unmarshaller(ExperimentImpl.class);
			u.setMapping(mapping);
			Experiment exp = (Experiment) u.unmarshal(el);
			Description[] descs = exp.getDescriptions();
			System.out.println("identifier=" + exp.getIdentifier());
			for (int i = 0; i < descs.length; i++) {
				System.out.println(descs[i].getText());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error unmarshalling: " + ex.getMessage());
		}
	}

	public void testMeasuredBioAssayDataObjectQuery() {

		LOG.debug("Howdy!");
		// if(true) return;

		String queryFileName = "test/resources/bioassaydata_object_1.xml";
		try {
			System.out.println("Querying....");
			CQLQueryResults results = runQuery(queryFileName);
			System.out.println("...done");

			// CQLQueryResults results = null;
			// FileReader reader = new FileReader("out.xml");
			// FileInputStream wsdd = new FileInputStream(
			// "src/gov/nih/nci/cagrid/caarray/client/client-config.wsdd");
			// results = (CQLQueryResults) Utils.deserializeObject(reader,
			// CQLQueryResults.class, wsdd);

			results
					.setTargetClassname("gov.nih.nci.mageom.domain.BioAssayData.impl.MeasuredBioAssayDataImpl");
			printResults(results);
			CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(
					results,
					new FileInputStream(
							"src/gov/nih/nci/cagrid/caarray/client/client-config.wsdd"));

			while (iterator.hasNext()) {
				MeasuredBioAssayData mbad = (MeasuredBioAssayData) iterator
						.next();
				BioDataCube bdc = (BioDataCube) mbad.getBioDataValues();
				Object[][][] cube = bdc.getCube();

				if (cube == null) {
					fail("Cube is null");
				} else {

					System.out.println("dim1.length = " + cube.length);
					if (cube.length == 0) {
						fail("Cube is empty");
					} else {
						System.out.println("dim2.length = " + cube[0].length);
						if (cube[0].length > 0) {
							System.out.println("dim3.length = "
									+ cube[0][0].length);
						}
						String str = MGEDCubeHandler.getCubeAsString(cube,
								"\n", "\t");
						// System.out.println(str);
						FileWriter w = new FileWriter("out.txt");
						w.write(str);
						w.flush();
						w.close();
					}
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error querying experiment atts: " + ex.getMessage());
		}
	}

	private void printResults(CQLQueryResults results) {
		try {
			// StringWriter w = new StringWriter();
			FileWriter w = new FileWriter("out.xml");
			Utils.serializeObject(results, new QName(
					"http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet",
					"CQLResultSet"), w);
			w.flush();
			w.close();
			// System.out.println(w.getBuffer());
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
		org.xml.sax.InputSource mappIS = new org.xml.sax.InputSource(
				new FileInputStream(fileName));
		Mapping mapping = new Mapping();
		mapping.setEntityResolver(resolver);
		mapping.loadMapping(mappIS);
		return mapping;
	}

}
