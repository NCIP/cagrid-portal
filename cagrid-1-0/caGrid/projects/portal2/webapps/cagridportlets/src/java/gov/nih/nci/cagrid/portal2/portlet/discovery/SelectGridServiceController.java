/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import message.MessageHelper;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectGridServiceController extends AbstractController {
	
	private String successAction;
	private GridServiceDao gridServiceDao;
	
	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		String sgsIdStr = request.getParameter("sgs_id");
		Integer sgsId = null;
		try {
			sgsId = Integer.parseInt(sgsIdStr);
		} catch (Exception ex) {
			// This should never happen.
			String msg = "Error parsing selected gridService ID = " + sgsIdStr
					+ ": " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		
		//Retrieve the grid service
		GridService selectedGridService = getGridServiceDao().getById(sgsId);

		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);
		helper.send("selectedGridService", selectedGridService);

		response.setRenderParameter("action", getSuccessAction());
	}

	public String getInstanceID(PortletRequest request) {
		return "SelectGridService" + MessageHelper.getPortletID(request);
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}
}
