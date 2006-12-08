/**
 * $Id: CaBIOClientTestCase.java,v 1.5 2006-12-08 12:48:42 joshua Exp $
 *
 */
package gov.nih.nci.cagrid.cabio.test;

import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cagrid.cabio.client.CaBIOSvcClient;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.common.domain.DatabaseCrossReference;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

/**
 * 
 * @version $Revision: 1.5 $
 * @author Joshua Phillips
 * 
 */
public class CaBIOClientTestCase extends TestCase {

	private CaBIOSvcClient gridSvcClient;

	private ApplicationService appSvcClient;

	public CaBIOClientTestCase() {

	}

	public CaBIOClientTestCase(String name) {
		super(name);
	}

	public void setUp() {
		String appSvcUrl = System.getProperty("appSvcUrl",
				"http://cabio.nci.nih.gov/cacore31/http/remoteService");
		String gridSvcUrl = System.getProperty("gridSvcUrl",
				"http://localhost:8080/wsrf/services/cagrid/CaBIOSvc");
		try {
			gridSvcClient = new CaBIOSvcClient(gridSvcUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed to instantiate grid service client: "
					+ ex.getMessage());
		}
		try {
			appSvcClient = ApplicationServiceProvider
					.getRemoteInstance(appSvcUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed to instantiate app service client: " + ex.getMessage());
		}
	}

	/**
	 * Compares Java client with Grid client.
	 * 
	 */
	public void testGetGenesByDBXRef() {

		// Get gene from app service
		List aGeneList = null;
		try {
			List xrefs = new ArrayList();
			DatabaseCrossReference xref = new DatabaseCrossReference();
			xref.setDataSourceName("OMIM_ID");
			xref.setCrossReferenceId("243400");
			xrefs.add(xref);
			Gene qGene = new Gene();
			qGene.setDatabaseCrossReferenceCollection(xrefs);
			aGeneList = appSvcClient.search(Gene.class, qGene);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed to query app service for genes: " + ex.getMessage());
		}

		// Get gene from grid service
		List gGeneList = new ArrayList();
		try {
			CQLQuery query = (CQLQuery) Utils.deserializeDocument(
					"test/resources/gene1.xml", CQLQuery.class);
			CQLQueryResults results = gridSvcClient.query(query);
			CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(
					results,
					new FileInputStream(
							"src/gov/nih/nci/cagrid/cabio/client/client-config.wsdd"));
			while (iterator.hasNext()) {
				gGeneList.add(iterator.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed to query grid service for genes: " + ex.getMessage());
		}
		int numAGenes = aGeneList.size();
		int numGGenes = gGeneList.size();
		assertTrue("No genes retrieved", numAGenes + numGGenes > 0);
		assertTrue("Got different number of genes; # from app service = "
				+ aGeneList.size() + ", # from grid service = "
				+ gGeneList.size(), aGeneList.size() == gGeneList.size());
		Map map = new HashMap();
		for (Iterator i = aGeneList.iterator(); i.hasNext();) {
			Gene g = (Gene) i.next();
			System.out.println("A Gene: " + g.getId());
			map.put(g.getId(), g);
		}
		for (Iterator i = gGeneList.iterator(); i.hasNext();) {
			Gene g = (Gene) i.next();
			System.out.println("G Gene: " + g.getId());
			map.remove(g.getId());
		}
		assertTrue("Different genes retrieved", map.size() == 0);
	}

	public void testGetDBXRefsByGene() {
		// Get xref from app service
		List aXRefList = null;
		try {
			Gene gene = new Gene();
			gene.setId(Long.valueOf(4041));
			DatabaseCrossReference qXRef = new DatabaseCrossReference();
			qXRef.setGene(gene);
			aXRefList = appSvcClient
					.search(DatabaseCrossReference.class, qXRef);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed to query app service for xrefs: " + ex.getMessage());
		}

		// Get gene from grid service
		List gXRefList = new ArrayList();
		try {
			CQLQuery query = (CQLQuery) Utils.deserializeDocument(
					"test/resources/dbxref1.xml", CQLQuery.class);
			CQLQueryResults results = gridSvcClient.query(query);
			CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(
					results,
					new FileInputStream(
							"src/gov/nih/nci/cagrid/cabio/client/client-config.wsdd"));
			while (iterator.hasNext()) {
				gXRefList.add(iterator.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed to query grid service for xrefs: " + ex.getMessage());
		}
		int numAXRefs = aXRefList.size();
		int numGXRefs = gXRefList.size();
		assertTrue("No xrefs retrieved", numAXRefs + numGXRefs > 0);
		assertTrue("Got different number of xrefs; # from app service = "
				+ aXRefList.size() + ", # from grid service = "
				+ gXRefList.size(), aXRefList.size() == gXRefList.size());
		Map map = new HashMap();
		for (Iterator i = aXRefList.iterator(); i.hasNext();) {
			DatabaseCrossReference x = (DatabaseCrossReference) i.next();
			System.out.println("A xref: " + x.getId());
			map.put(x.getId(), x);
		}
		for (Iterator i = gXRefList.iterator(); i.hasNext();) {
			DatabaseCrossReference x = (DatabaseCrossReference) i.next();
			System.out.println("G xref: " + x.getId());
			map.remove(x.getId());
		}
		assertTrue("Different xrefs retrieved", map.size() == 0);
	}

	public void testValidXml() {
		try {
			CQLQuery query = (CQLQuery) Utils.deserializeDocument(
					"test/resources/query_1.xml", CQLQuery.class);
			CQLQueryResults results = this.gridSvcClient.query(query);
			CQLQueryResultsIterator iterator = new CQLQueryResultsIterator(
					results,
					new FileInputStream(
							"src/gov/nih/nci/cagrid/cabio/client/client-config.wsdd"));
			while (iterator.hasNext()) {
				Gene gene = (Gene) iterator
						.next();
				StringWriter w2 = new StringWriter();
				Utils
						.serializeObject(
								gene,
								new QName(
										"gme://caCORE.caCORE/3.1/gov.nih.nci.cabio.domain",
										"Gene"),
								w2,
								new FileInputStream(
										"src/gov/nih/nci/cagrid/cabio/client/client-config.wsdd"));
				DocumentBuilder parser = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document document = parser.parse(new ByteArrayInputStream(w2
						.getBuffer().toString().getBytes()));
				document.getDocumentElement().setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
				document.getDocumentElement().setAttribute("xsi:schemaLocation", "gme://caCORE.caCORE/3.1/gov.nih.nci.cabio.domain file:/Users/joshua/dev/ew_cagrid3/cagrid-1-0/Applications/cabio/schema/CaBIOSvc/gov.nih.nci.cabio.domain.xsd");
				
				SchemaFactory factory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Source schemaFile = new StreamSource(new File(
						"schema/CaBIOSvc/gov.nih.nci.cabio.domain.xsd"));
				Schema schema = factory.newSchema(schemaFile);
				Validator validator = schema.newValidator();
				validator.validate(new DOMSource(document));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error encountered: " + ex.getMessage());
		}
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		 suite.addTest(new CaBIOClientTestCase("testGetGenesByDBXRef"));
		 suite.addTest(new CaBIOClientTestCase("testGetDBXRefsByGene"));
		suite.addTest(new CaBIOClientTestCase("testValidXml"));
		return suite;
	}
}
