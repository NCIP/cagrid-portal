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
