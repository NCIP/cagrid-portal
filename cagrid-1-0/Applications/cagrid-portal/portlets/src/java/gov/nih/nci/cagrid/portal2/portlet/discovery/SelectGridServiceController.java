/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

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
public class SelectGridServiceController extends AbstractController {

	private static final Log logger = LogFactory
			.getLog(SelectGridServiceController.class);

	private GridServiceDao gridServiceDao;

	private String successAction;

	public String getSuccessAction() {
		return successAction;
	}

	public void setSuccessAction(String successAction) {
		this.successAction = successAction;
	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		Integer sgsId = null;
		String sgsUrl = request.getParameter("sgs_url");
		if (!PortalUtils.isEmpty(sgsUrl)) {
			logger.debug("Selecting grid service from url " + sgsUrl);
			GridService service = getGridServiceDao().getByUrl(sgsUrl);
			if (service == null) {
				logger.warn("No grid service found for url: " + sgsUrl);
			} else {
				sgsId = service.getId();
			}
		}
		if (sgsId == null) {
			String sgsIdStr = request.getParameter("sgs_id");
			try {
				sgsId = Integer.parseInt(sgsIdStr);
			} catch (Exception ex) {
				// This should never happen.
				String msg = "Error parsing selected gridService ID = "
						+ sgsIdStr + ": " + ex.getMessage();
				logger.error(msg, ex);
				throw new RuntimeException(msg, ex);
			}
		}

		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);

		logger.debug("Publishing selectedGridServiceId: " + sgsId);
		helper.send("selectedGridServiceId", sgsId);

		logger.debug("setting action to " + getSuccessAction());
		response.setRenderParameter("action", getSuccessAction());

		String category = request.getParameter("category");
		if (!PortalUtils.isEmpty(category)) {
			response.setRenderParameter("category", category);
		}

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
