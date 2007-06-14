/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.pointofcontact;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import message.MessageHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectPointOfContactController extends AbstractController{
	
	private static final Log logger = LogFactory.getLog(SelectPointOfContactController.class);
	
	private String successAction;
	
	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		String spocIdStr = request.getParameter("spoc_id");
		logger.debug("spocIdStr = " + spocIdStr);
		Integer spocId = null;
		try {
			spocId = Integer.parseInt(spocIdStr);
		} catch (Exception ex) {
			// This should never happen.
			String msg = "Error parsing selected pointOfContact ID = " + spocIdStr
					+ ": " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		
		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);
		
		logger.debug("########## publishing selectedPointOfContactId = " + spocId + " #########");
		
		helper.send("selectedPointOfContactId", spocId);

		logger.debug("setting action to " + getSuccessAction());
		response.setRenderParameter("action", getSuccessAction());
	}

	public String getInstanceID(PortletRequest request) {
		return "SelectPointOfContact" + MessageHelper.getPortletID(request);
	}

}
