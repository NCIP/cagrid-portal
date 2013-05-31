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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import org.cagrid.gme.client.GlobalModelExchangeClient;
import org.cagrid.gme.domain.XMLSchemaNamespace;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class XMLSchemaUtilsTest {
    private String badUrl;
    private XMLSchemaNamespace ns;
    XMLSchemaUtils schemaUtils;


    @Before
    public void setup() throws Exception {
        badUrl = "http://www.yahoo.com";
        ns = new XMLSchemaNamespace("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.common");

        schemaUtils = new XMLSchemaUtils();
        schemaUtils.setGmeUrl("badURL");
        CaDSRClient caDSRClient = new StaticCaDSRClient();
        schemaUtils.setCaDSRClient(caDSRClient);
    }

    @Test
    public void testGME() {
        try {

            GlobalModelExchangeClient client = new GlobalModelExchangeClient(badUrl);
            org.cagrid.gme.domain.XMLSchema schema = client.getXMLSchema(ns);
            String content = schema.getRootDocument().getSchemaText();
            fail("Schema should be null");
        } catch (Exception e) {
            assertNotNull(e);
        }


    }

    @Test
    public void getXMLSchemas() throws Exception {
        DomainModel _model = mock(DomainModel.class);
        List<XMLSchema> _schemas = null;

        _schemas = schemaUtils.getXMLSchemas(_model);
        assertEquals("Schemas returned for bad URL", _schemas.size(), 0);

    }


    @Test
    public void getXmlSchemaContent() {
        schemaUtils.getXmlSchemaContent(ns.toString());
    }
}
