/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.query.results.XMLQueryResultToQueryResultTableHandler;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class XMLToQueryResultTableTest {

	@Test
	public void testParseDCQL() {
		try {
			List<String> colNames = new ArrayList<String>();
			colNames.add("id");
			colNames.add("lsidAuthority");
			colNames.add("lsidNamespace");
			colNames.add("lsidObjectId");
			colNames.add("name");
			colNames.add("assayType");
			colNames.add("version");
			DCQLQuery query = new DCQLQuery();
			DCQLQueryInstance queryInstance = new DCQLQueryInstance();
			queryInstance.setResult("some data");
			queryInstance.setQuery(query);
			XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
			handler.setPersist(false);
			handler.setColumnNames(colNames);
			handler.getTable().setQueryInstance(queryInstance);
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			SAXParser parser = fact.newSAXParser();
			parser.parse(new FileInputStream(
					"test/data/caarray_dcql_results.xml"), handler);
			QueryResultTable table = handler.getTable();
			assertEquals(
					"http://array.nci.nih.gov:80/wsrf/services/cagrid/CaArraySvc",
					table.getRows().get(0).getServiceUrl());
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Error encountered: " + ex.getMessage();
			fail(msg);
		}
	}

	@Test
	public void testCountQuery() {
		try {
			CQLQuery query = new CQLQuery();
			CQLQueryInstance queryInstance = new CQLQueryInstance();
			queryInstance.setResult("some data");
			queryInstance.setQuery(query);
			XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
			handler.setPersist(false);
			handler.setDataServiceUrl("http://service");
			handler.getTable().setQueryInstance(queryInstance);
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			SAXParser parser = fact.newSAXParser();
			parser.parse(new FileInputStream("test/data/count_results.xml"),
					handler);
			QueryResultTable table = handler.getTable();
			assertEquals(1, table.getColumns().size());
			assertEquals("count", table.getColumns().get(0).getName());
			assertEquals(1, table.getRows().size());
			assertEquals("1208", table.getRows().get(0).getCells().get(0)
					.getValue());
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Error encountered: " + ex.getMessage();
			fail(msg);
		}
	}

	@Test
	public void testSelectedAttributesQuery() {
		try {
			CQLQuery query = new CQLQuery();
			CQLQueryInstance queryInstance = new CQLQueryInstance();
			queryInstance.setResult("some data");
			queryInstance.setQuery(query);
			XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
			handler.setPersist(false);
			handler.setDataServiceUrl("http://service");
			handler.getTable().setQueryInstance(queryInstance);
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			SAXParser parser = fact.newSAXParser();
			parser.parse(new FileInputStream(
					"test/data/selected_attribute_results.xml"), handler);
			QueryResultTable table = handler.getTable();
			assertEquals(2, table.getColumns().size());
			assertEquals(1000, table.getRows().size());
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Error encountered: " + ex.getMessage();
			fail(msg);
		}
	}

}
