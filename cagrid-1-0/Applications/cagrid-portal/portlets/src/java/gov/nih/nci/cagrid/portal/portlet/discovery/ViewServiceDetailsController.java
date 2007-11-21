/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery;

import java.util.HashMap;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal.portlet.discovery.details.ServiceMetadataTreeNodeListener;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

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
	
	private TreeFacade treeFacade;
	
	private ServiceMetadataTreeNodeListener serviceMetadataTreeNodeListener;

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
		
		if (gridService != null) {
			TreeNode rootNode = getTreeFacade().getRootNode();
			if(rootNode == null){
				logger.debug("Creating new tree for gridService:" + gridService.getId());
				rootNode = createRootNode(gridService);
				
			}
			GridService s = (GridService)rootNode.getContent();
			if(!s.getId().equals(gridService.getId())){
				logger.debug("Changing tree from gridService:" + s.getId() + " to gridService:" + gridService.getId());
				rootNode = createRootNode(gridService);
			}
			getTreeFacade().setRootNode(rootNode);
			mav.addObject("rootNode", rootNode);
			//mav.addObject("gridService", gridService);
			mav.addObject("gridServiceUrl", gridService.getUrl());
		} else {
			logger.debug("No grid service found");
		}
		return mav;
	}
	
	private TreeNode createRootNode(GridService gridService) {
		TreeNode rootNode = new TreeNode(null, "gridService");
		rootNode.setContent(gridService);
		
		getServiceMetadataTreeNodeListener().handleGridServiceNode(rootNode, new HashMap(), gridService);
		
		return rootNode;
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

	public TreeFacade getTreeFacade() {
		return treeFacade;
	}

	public void setTreeFacade(TreeFacade treeFacade) {
		this.treeFacade = treeFacade;
	}

	public ServiceMetadataTreeNodeListener getServiceMetadataTreeNodeListener() {
		return serviceMetadataTreeNodeListener;
	}

	public void setServiceMetadataTreeNodeListener(
			ServiceMetadataTreeNodeListener serviceMetadataTreeNodeListener) {
		this.serviceMetadataTreeNodeListener = serviceMetadataTreeNodeListener;
	}

}
