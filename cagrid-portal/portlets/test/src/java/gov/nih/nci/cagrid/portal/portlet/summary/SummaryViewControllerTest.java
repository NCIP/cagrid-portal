package gov.nih.nci.cagrid.portal.portlet.summary;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SummaryViewControllerTest {
    protected MockRenderRequest request = new MockRenderRequest();
    protected MockRenderResponse response = new MockRenderResponse();

    @Test
    public void handleRenderRequestInternal() throws Exception {
        SummaryViewController controller = new SummaryViewController();

        controller.setSolrServiceUrl("url");
        ModelAndView mav = controller.handleRenderRequest(request, response);

        assertNotNull(response.getContentAsString());
        assertTrue(mav.getModel().size() > 0);

    }

}
