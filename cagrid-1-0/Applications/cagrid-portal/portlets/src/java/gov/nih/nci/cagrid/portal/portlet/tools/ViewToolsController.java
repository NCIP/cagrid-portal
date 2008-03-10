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


    public ModelAndView handleRenderRequestInternal(
            RenderRequest request,
            RenderResponse response) throws Exception {
        ModelAndView mav = new ModelAndView(getViewName());


        return mav;

    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
