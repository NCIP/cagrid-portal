/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.content;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ContentViewController extends AbstractContentViewController {

	private static final Log logger = LogFactory
			.getLog(ContentViewController.class);

	public void handleActionRequest(ActionRequest arg0, ActionResponse arg1)
			throws Exception {
		// nothing to do
	}

	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {

		String viewNameParam = getViewNameParam();
		String viewNameDefault = getViewNameDefault();
		logger.debug("Looking for preference " + viewNameParam
				+ ", with default of '" + viewNameDefault + "'");
		String viewName = viewNameDefault;
		try {
			viewName = request.getPreferences().getValue(viewNameParam,
					viewNameDefault);
			logger.debug("Got preference " + viewNameParam + " = " + viewName);
		} catch (Exception ex) {
			logger.error("Error getting viewName from preferences: "
					+ ex.getMessage() + ". Using default: " + viewNameDefault,
					ex);
		}
		return new ModelAndView(viewName);
	}

}
