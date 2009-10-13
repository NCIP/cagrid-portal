package gov.nih.nci.cagrid.portal.portlet.latestcontent;

import gov.nih.nci.cagrid.portal.portlet.SpringPortletContextTestBase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;

import javax.portlet.PortletMode;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LatestContentContextInitTest extends SpringPortletContextTestBase<LatestContextContext> {

    public LatestContentContextInitTest() {
        super(LatestContextContext.class);
    }

    /**
     * Make sure we get a view back
     *
     * @throws Exception
     */
    @Test
    public void testView() throws Exception {

        request.setPortletMode(PortletMode.VIEW);
        doRender(request, response);
        assertNotNull(response.getIncludedUrl());

    }

}
