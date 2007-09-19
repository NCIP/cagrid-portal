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
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import message.MessageHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewServiceDetailsController extends AbstractController {

	private static final Log logger = LogFactory
			.getLog(ViewServiceDetailsController.class);

	private GridServiceDao gridServiceDao;

	private String successView;

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		Integer selectedGridServiceId = null;
		String gridServiceUrl = request.getParameter("gridServiceUrl");
		if (!PortalUtils.isEmpty(gridServiceUrl)) {

			response.setRenderParameter("gridServiceUrl", gridServiceUrl);
			
			logger.debug("Looking for " + gridServiceUrl);
			GridService gridService = getGridServiceDao().getByUrl(
					gridServiceUrl);
			if (gridService != null) {
				selectedGridServiceId = gridService.getId();
			}
		} else {
			selectedGridServiceId = getSelectedGridServiceId(request);
		}

		if (selectedGridServiceId != null) {

			response.setRenderParameter("selectedGridServiceId", String
					.valueOf(selectedGridServiceId));
			response.setWindowState(WindowState.MAXIMIZED);

		}
		
		
	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		logger.debug("Handling render request");

		ModelAndView mav = new ModelAndView(getSuccessView());

		GridService gridService = null;
		Integer gridServiceId = null;
		if (!PortalUtils.isEmpty(request.getParameter("selectedGridServiceId"))) {
			try {
				gridServiceId = Integer.valueOf(request
						.getParameter("selectedGridServiceId"));
			} catch (Exception ex) {
				logger.error("Error parsing selectedGridServiceId", ex);
			}
		}
		if (gridServiceId != null) {

			logger.debug("Found selectedGridServiceId = " + gridServiceId);

			/*
			 * NOTE: The object has to be fetched from Hibernate instead of the
			 * message box so that the Hibernate session is available so enable
			 * lazy loading of associated objects during the render phase.
			 */
			gridService = getGridServiceDao().getById(gridServiceId);
		}

		if (gridService != null) {
			mav.addObject("gridService", gridService);
		} else {
			logger.debug("No grid service found");
		}

		if (!PortalUtils.isEmpty(request.getParameter("gridServiceUrl"))) {
			mav.addObject("gridServiceUrl", request
					.getParameter("gridServiceUrl"));
		}

		return mav;
	}

	private Integer getSelectedGridServiceId(PortletRequest request) {
		Integer gridServiceId = null;

		logger.debug("Looking for selectedGridServiceId in session");

		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);

		logger.debug("selectedGridServiceId = " + gridServiceId);

		gridServiceId = (Integer) helper.get("selectedGridServiceId");
		return gridServiceId;
	}

	public String getInstanceID(PortletRequest request) {
		return "ViewServiceDetails" + MessageHelper.getPortletID(request);
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
