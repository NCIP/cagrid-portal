/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.domain.GridService;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import message.MessageHelper;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewServiceDetailsController extends AbstractController {
	
	private String successView;

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		ModelAndView mav = new ModelAndView(getSuccessView());
		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);

		// load this portlet's inputs and outputs from preferences
		MessageHelper.loadPrefs(request, id, msgSessionId);

		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);
		GridService gridService = (GridService) helper.get("selectedGridService");
		if(gridService != null){
			mav.addObject("gridService", gridService);
		}
		return mav;
	}
	
	public String getInstanceID(PortletRequest request)
    {
        return "ViewServiceDetails" + MessageHelper.getPortletID(request);
    }

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

}
