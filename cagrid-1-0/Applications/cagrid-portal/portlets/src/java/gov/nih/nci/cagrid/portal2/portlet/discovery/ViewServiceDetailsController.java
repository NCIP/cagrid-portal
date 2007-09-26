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
import org.springframework.web.portlet.util.PortletUtils;

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
		}
		
		if (selectedGridServiceId != null) {

			setSelectedGridServiceId(request, selectedGridServiceId);
			response.setWindowState(WindowState.MAXIMIZED);

		}

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		logger.debug("Handling render request");

		ModelAndView mav = new ModelAndView(getSuccessView());

		GridService gridService = null;
		Integer gridServiceId = getSelectedGridServiceId(request);
		if (gridServiceId != null) {

			logger.debug("Found selectedGridServiceId = " + gridServiceId);

			/*
			 * NOTE: The object has to be fetched from Hibernate instead of the
			 * message box so that the Hibernate session is available so enable
			 * lazy loading of associated objects during the render phase.
			 */
			gridService = getGridServiceDao().getById(gridServiceId);
		}

		if (!PortalUtils.isEmpty(request.getParameter("gridServiceUrl"))) {
			mav.addObject("gridServiceUrl", request
					.getParameter("gridServiceUrl"));
		}
		
		//See if we need to maximize this window
		if(isMaximize(request)){
			clearMaximize(request);
			logger.debug("Setting maximize to true");
			mav.addObject("maximize", true);
		}else{
			logger.debug("Setting maximize to false");
			mav.addObject("maximize", false);
		}
		
		if (gridService != null) {
			mav.addObject("gridService", gridService);
			mav.addObject("gridServiceUrl", gridService.getUrl());
		} else {
			logger.debug("No grid service found");
		}
		return mav;
	}
	
	private void clearMaximize(RenderRequest request) {
		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);

		StringBuilder sb = new StringBuilder();
		String portletName = request.getPreferences().getValue("portletName", null);
		String maximizedPortlets = (String) helper.get("maximizedPortlets");
		if(!PortalUtils.isEmpty(maximizedPortlets)){
			String[] names = maximizedPortlets.split(",");
			for(int i = 0; i < names.length; i++){
				String name = names[i];
				if(!name.trim().equals(portletName.trim())){
					sb.append(name);
					if(i + 1 < names.length){
						sb.append(",");
					}
				}
			}
		}
		
		helper.send("maximizedPortlets", sb.toString());
	}

	private boolean isMaximize(RenderRequest request) {
		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);

		String portletName = request.getPreferences().getValue("portletName", null);
		String maximizedPortlets = (String) helper.get("maximizedPortlets");
		if(!PortalUtils.isEmpty(maximizedPortlets)){
			for(String name : maximizedPortlets.split(",")){
				if(name.trim().equals(portletName.trim())){
					return true;
				}
			}
		}
		
		return false;
	}

	private void setSelectedGridServiceId(PortletRequest request, Integer sgsId){
		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);
		
		logger.debug("Publishing selectedGridServiceId: " + sgsId);
		
		helper.send("selectedGridServiceId", sgsId);
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
		gridServiceId = (Integer) helper.get("selectedGridServiceId");
		

		logger.debug("selectedGridServiceId = " + gridServiceId);

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
