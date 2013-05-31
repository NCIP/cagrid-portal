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
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.dao.news.NewsItemDao;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.springframework.web.portlet.ModelAndView;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewNewsSummaryControllerTest extends TestCase {


    public void test() {
        ViewNewsSummaryController controller = new ViewNewsSummaryController();

        NewsItemDao dao = mock(NewsItemDao.class);
        controller.setNewsItemDao(dao);

        try {
            ModelAndView _view = controller.handleRenderRequestInternal(null, null);
            assertNotNull(_view);
            assertNotNull(_view.getModel().get("items"));

            when(dao.getLatestNewsItems(anyInt())).thenThrow(new RuntimeException());

            _view = controller.handleRenderRequestInternal(null, null);
            assertNotNull(_view);
            assertNull("RuntimeException should have been thrown and model should not have this", _view.getModel().get("items"));

        } catch (Exception e) {
            fail("Controller Should not throw any exception");
        }
    }

}
