package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.AbstractTimeSensitiveTest;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import static org.mockito.Mockito.mock;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.Namespace;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.projectmobius.protocol.gme.SchemaNode;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalUtilsTest extends AbstractTimeSensitiveTest {
    private String badUrl;
    private Namespace ns;


    public PortalUtilsTest() throws Exception {
        badUrl = "http://www.yahoo.com";
        ns = new Namespace("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata");

    }

    protected Long getAcceptableTime() {
        return new Long("2500");
    }

    public void testGME() throws Exception {

        GridServiceResolver.getInstance().setDefaultFactory(
                new GlobusGMEXMLDataModelServiceFactory());

        XMLDataModelService handle = (XMLDataModelService) GridServiceResolver
                .getInstance().getGridService(badUrl);
        SchemaNode schema = handle.getSchema(ns, false);

    }

    public void testGetXMLSchemas() throws Exception {
        DomainModel _model = mock(DomainModel.class);
        List<XMLSchema> _schemas = PortalUtils.getXMLSchemas(_model, badUrl, badUrl);

    }

    public void testGetXmlSchemaContent() {
        PortalUtils.getXmlSchemaContent(ns.toString(), badUrl);
    }
}
