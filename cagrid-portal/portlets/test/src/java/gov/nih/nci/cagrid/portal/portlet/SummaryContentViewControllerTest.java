/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet;

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
public class SummaryContentViewControllerTest {
    protected MockRenderRequest request = new MockRenderRequest();
    protected MockRenderResponse response = new MockRenderResponse();

    @Test
    public void handleRenderRequestInternal() throws Exception {
        SummaryContentViewController controller = new SummaryContentViewController();

        ModelAndView mav = controller.handleRenderRequest(request, response);

        assertNotNull(response.getContentAsString());
//        assertTrue(mav.getModel().size() > 0);

    }

}
