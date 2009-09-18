package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultData;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.query.shared.XMLSchemaValidatorFactory;
import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ExportQueryResultTableToXMLControllerTest extends PortalAggrIntegrationTestBase {
    SchemaValidator cqlResultsValidator;

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();    //To change body of overridden methods use File | Settings | File Templates.
        cqlResultsValidator = XMLSchemaValidatorFactory.initialize("1_gov.nih.nci.cagrid.CQLResultSet-1.3.xsd");

    }

    public void testExport() throws Exception {
        ExportQueryResultTableToXMLController controller = new ExportQueryResultTableToXMLController();

        String cqlResult = PortalTestUtils.readFileASString("test/data/cabioGeneQueryResults.xml");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream bufos = new GZIPOutputStream(bos);
        bufos.write(cqlResult.getBytes());
        bufos.close();


        QueryResultTableDao mockDao = mock(QueryResultTableDao.class);
        QueryResultTable mockTable = mock(QueryResultTable.class);
        QueryResultData mockData = mock(QueryResultData.class);

        when(mockDao.getByQueryInstanceId(anyInt())).thenReturn(mockTable);
        when(mockTable.getData()).thenReturn(mockData);
        when(mockData.getData()).thenReturn(bos.toByteArray());

        controller.setQueryResultTableDao(mockDao);

        request.setParameter("instanceId", "1");
        controller.handleRequestInternal(request, response);

        String outputCQLXML = response.getContentAsString();
        try {
            cqlResultsValidator.validate(outputCQLXML);
        } catch (SchemaValidationException e) {
            fail("CQL results are not valid");
        }


    }


}
