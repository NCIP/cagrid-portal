/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.content;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.PortletRequestUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ContentEditController extends AbstractContentViewController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleActionRequest(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void handleActionRequest(ActionRequest request,
			ActionResponse response) throws Exception {
		
		String viewName = PortletRequestUtils.getStringParameter(request,
				"viewName");
		if (viewName != null) {
			PortletPreferences preferences = request.getPreferences();
			preferences.setValue(getViewNameParam(), viewName);
			preferences.store();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.Controller#handleRenderRequest(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		return new ModelAndView("edit", "viewName", request
				.getPreferences().getValue(getViewNameParam(),
						getViewNameDefault()));
	}

}
