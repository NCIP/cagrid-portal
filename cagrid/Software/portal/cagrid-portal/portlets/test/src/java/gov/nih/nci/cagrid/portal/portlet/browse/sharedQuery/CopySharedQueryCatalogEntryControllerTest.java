package gov.nih.nci.cagrid.portal.portlet.browse.sharedQuery;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.SharedQueryCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.dataservice.Query;
import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import static org.mockito.Mockito.*;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CopySharedQueryCatalogEntryControllerTest extends PortalPortletIntegrationTestBase {
    protected MockRenderRequest request = new MockRenderRequest();
    protected MockRenderResponse response = new MockRenderResponse();


    public void testSharedQuery() throws Exception {
        Query mockQuery = mock(Query.class);
        when(mockQuery.getXml()).thenReturn("<xml/>");

        SharedQueryCatalogEntry ce = (SharedQueryCatalogEntry) Class.forName("gov.nih.nci.cagrid.portal.domain.catalog.SharedQueryCatalogEntry").newInstance();
        ce.setAbout(mockQuery);
        CatalogEntryDao mockDao = mock(CatalogEntryDao.class);
        when(mockDao.getById(anyInt())).thenReturn(ce);

        CopySharedQueryCatalogEntryController controller = (CopySharedQueryCatalogEntryController) getApplicationContext().getBean("copySharedQueryCatalog");
        controller.setCatalogEntryDao(mockDao);
        UserModel mockUserModel = mock(UserModel.class);
        when(mockUserModel.getCurrentCatalogEntry()).thenReturn(ce);
        controller.setUserModel(mockUserModel);

        request.setParameter("entryId", "1");

        ModelAndView mv = controller.handleRenderRequestInternal(request, response);
        assertNotNull(mv);
        assertNotNull(mv.getModel().containsKey(controller.getQueryCopyParam()));

    }

}
