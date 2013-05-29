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
package gov.nih.nci.cagrid.portal.portlet.tools;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ViewToolsController extends AbstractController {

    private String viewName;
    private String remoteViewName;


    public ModelAndView handleRenderRequestInternal(
            RenderRequest request,
            RenderResponse response) throws Exception {
        ModelAndView mav = new ModelAndView(getViewName(),"remoteView",getRemoteViewName());
        return mav;

    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getRemoteViewName() {
        return remoteViewName;
    }

    public void setRemoteViewName(String remoteViewName) {
        this.remoteViewName = remoteViewName;
    }
}
