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
