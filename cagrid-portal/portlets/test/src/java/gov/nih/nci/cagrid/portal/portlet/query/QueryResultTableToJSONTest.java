/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultTableToDataTableMetadataBuilder;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultTableToJSONObjectBuilder;
import gov.nih.nci.cagrid.portal.portlet.query.results.XMLQueryResultToQueryResultTableHandler;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.service.PortalFileService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class QueryResultTableToJSONTest {

    /**
     *
     */

    QueryResultTableToDataTableMetadataBuilder builder;

    public QueryResultTableToJSONTest() {


    }


    @Before
    public void setup() {
        builder = new QueryResultTableToDataTableMetadataBuilder();
        QueryResultTableDao mockResultTableDao = mock(QueryResultTableDao.class);
        when(mockResultTableDao.getRowCountForColumn(anyInt(), anyString())).thenReturn(2);

        builder.setQueryResultTableDao(mockResultTableDao);

    }

    @Test
    public void testCountQueryJSONMeta() {
        try {

            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new FileReader(
                    "test/data/count_query.xml"));
            String line = null;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String cql = sb.toString();
            assertTrue(PortletUtils.isCountQuery(cql));

            CQLQuery query = new CQLQuery();
            query.setXml(cql);
            CQLQueryInstance queryInstance = new CQLQueryInstance();
            queryInstance.setResult("some data");
            queryInstance.setQuery(query);
            XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
            handler.setPersist(false);
            handler.setDataServiceUrl("http://service");
            handler.getTable().setQueryInstance(queryInstance);

            PortalFileService mockFileService = mock(PortalFileService.class);
            File mockFile = mock(File.class);
            when(mockFile.getName()).thenReturn(anyString());
            when(mockFileService.write(new byte[]{})).thenReturn(mockFile);

            when(mockFileService.read(anyString())).thenReturn(sb.toString().getBytes());
            handler.setPortalFileService(mockFileService);

            SAXParserFactory fact = SAXParserFactory.newInstance();
            fact.setNamespaceAware(true);
            SAXParser parser = fact.newSAXParser();
            parser.parse(new FileInputStream("test/data/count_results.xml"),
                    handler);
            QueryResultTable table = handler.getTable();
            String expected = "{\"responseSchema\":{\"metaFields\":{\"totalRecords\":\"numRows\"},\"resultsList\":\"rows\",\"fields\":[\"count\",\"dataServiceUrl\"]},\"columnDefs\":[{\"resizeable\":true,\"sortable\":true,\"key\":\"count\"},{\"resizeable\":true,\"sortable\":true,\"key\":\"dataServiceUrl\"}]}";
            assertEquals(expected, builder.build(table).toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = "Error encountered: " + ex.getMessage();
            fail(msg);
        }
    }

    @Test
    public void testCountQueryJSON() {
        try {

            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new FileReader(
                    "test/data/count_query.xml"));
            String line = null;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String cql = sb.toString();
            assertTrue(PortletUtils.isCountQuery(cql));

            CQLQuery query = new CQLQuery();
            query.setXml(cql);
            CQLQueryInstance queryInstance = new CQLQueryInstance();
            queryInstance.setResult("some data");
            queryInstance.setQuery(query);
            XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
            handler.setDataServiceUrl("http://service");
            handler.setPersist(false);
            handler.getTable().setQueryInstance(queryInstance);

            PortalFileService mockFileService = mock(PortalFileService.class);
            File mockFile = mock(File.class);
            when(mockFile.getName()).thenReturn(anyString());
            when(mockFileService.write(new byte[]{})).thenReturn(mockFile);

            when(mockFileService.read(anyString())).thenReturn(sb.toString().getBytes());
            handler.setPortalFileService(mockFileService);

            SAXParserFactory fact = SAXParserFactory.newInstance();
            fact.setNamespaceAware(true);
            SAXParser parser = fact.newSAXParser();
            parser.parse(new FileInputStream("test/data/count_results.xml"),
                    handler);
            QueryResultTable table = handler.getTable();
            QueryResultTableToJSONObjectBuilder builder = new QueryResultTableToJSONObjectBuilder();
            String expected = "{\"numRows\":1,\"rows\":[{\"count\":\"1208\",\"dataServiceUrl\":\"http://service\"}]}";
            assertEquals(expected, builder.build(table.getRows(), 1).toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = "Error encountered: " + ex.getMessage();
            fail(msg);
        }
    }

    @Test
    public void testSelectedAttributesQueryJSONMeta() {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new FileReader(
                    "test/data/selected_attribute_query.xml"));
            String line = null;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String cql = sb.toString();
            assertTrue(!PortletUtils.isCountQuery(cql));

            CQLQuery query = new CQLQuery();
            query.setXml(cql);
            CQLQueryInstance queryInstance = new CQLQueryInstance();
            queryInstance.setResult("some data");
            queryInstance.setQuery(query);
            XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
            handler.setPersist(false);
            handler.setDataServiceUrl("http://service");
            handler.getTable().setQueryInstance(queryInstance);

            PortalFileService mockFileService = mock(PortalFileService.class);
            File mockFile = mock(File.class);
            when(mockFile.getName()).thenReturn(anyString());
            when(mockFileService.write(new byte[]{})).thenReturn(mockFile);

            when(mockFileService.read(anyString())).thenReturn(sb.toString().getBytes());
            handler.setPortalFileService(mockFileService);


            SAXParserFactory fact = SAXParserFactory.newInstance();
            fact.setNamespaceAware(true);
            SAXParser parser = fact.newSAXParser();
            parser.parse(new FileInputStream(
                    "test/data/selected_attribute_results.xml"), handler);
            QueryResultTable table = handler.getTable();

            String expected = "{\"responseSchema\":{\"metaFields\":{\"totalRecords\":\"numRows\"},\"resultsList\":\"rows\",\"fields\":[\"id\",\"ageOfOnset\",\"dataServiceUrl\"]},\"columnDefs\":[{\"resizeable\":true\"}]}";

        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = "Error encountered: " + ex.getMessage();
            fail(msg);
        }
    }

    @Test
    public void testSelectedAttributesQueryJSON() {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new FileReader(
                    "test/data/selected_attribute_query.xml"));
            String line = null;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String cql = sb.toString();
            assertTrue(!PortletUtils.isCountQuery(cql));

            CQLQuery query = new CQLQuery();
            query.setXml(cql);
            CQLQueryInstance queryInstance = new CQLQueryInstance();
            queryInstance.setResult("some data");
            queryInstance.setQuery(query);
            XMLQueryResultToQueryResultTableHandler handler = new XMLQueryResultToQueryResultTableHandler();
            handler.setPersist(false);
            handler.setDataServiceUrl("http://service");
            handler.getTable().setQueryInstance(queryInstance);
            PortalFileService mockFileService = mock(PortalFileService.class);
            File mockFile = mock(File.class);
            when(mockFile.getName()).thenReturn(anyString());
            when(mockFileService.write(new byte[]{})).thenReturn(mockFile);

            when(mockFileService.read(anyString())).thenReturn(sb.toString().getBytes());
            handler.setPortalFileService(mockFileService);

            SAXParserFactory fact = SAXParserFactory.newInstance();
            fact.setNamespaceAware(true);
            SAXParser parser = fact.newSAXParser();
            parser.parse(new FileInputStream(
                    "test/data/selected_attribute_results.xml"), handler);
            QueryResultTable table = handler.getTable();

            QueryResultTableToJSONObjectBuilder builder = new QueryResultTableToJSONObjectBuilder();
            JSONObject out = builder.build(table.getRows(), 1000);
            assertEquals(1000, out.get("numRows"));

        } catch (Exception ex) {
            ex.printStackTrace();
            String msg = "Error encountered: " + ex.getMessage();
            fail(msg);
        }
    }

}
