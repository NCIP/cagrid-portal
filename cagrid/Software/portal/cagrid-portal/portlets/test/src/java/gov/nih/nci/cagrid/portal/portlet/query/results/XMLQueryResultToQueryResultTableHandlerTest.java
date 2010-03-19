package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.service.PortalFileService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class XMLQueryResultToQueryResultTableHandlerTest {

    QueryResultTableToDataTableMetadataBuilder builder;

    @Before
      public void setup() {
          builder = new QueryResultTableToDataTableMetadataBuilder();
          QueryResultTableDao mockResultTableDao = mock(QueryResultTableDao.class);
          when(mockResultTableDao.getRowCountForColumn(anyInt(), anyString())).thenReturn(2);

          builder.setQueryResultTableDao(mockResultTableDao);

      }


    @Test
    public void testRun() throws Exception{
        try{
            
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

}
