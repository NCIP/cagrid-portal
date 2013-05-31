/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.junit.Test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

/**
 *
 * Test using the spring app context
 * 
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class XMLQueryResultToQueryResultTableHandlerSystemTest extends PortalPortletIntegrationTestBase {



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
               XMLQueryResultToQueryResultTableHandler handler = (XMLQueryResultToQueryResultTableHandler)getBean("xmlQueryResultToQueryResultTableHandlerPrototype");
               handler.setPersist(false);
               handler.setDataServiceUrl("http://service");
               handler.getTable().setQueryInstance(queryInstance);
               SAXParserFactory fact = SAXParserFactory.newInstance();
               fact.setNamespaceAware(true);
               SAXParser parser = fact.newSAXParser();
               parser.parse(new FileInputStream("test/data/count_results.xml"),
                       handler);
               

           } catch (Exception ex) {
               ex.printStackTrace();
               String msg = "Error encountered: " + ex.getMessage();
               fail(msg);
           }
       }

   }
