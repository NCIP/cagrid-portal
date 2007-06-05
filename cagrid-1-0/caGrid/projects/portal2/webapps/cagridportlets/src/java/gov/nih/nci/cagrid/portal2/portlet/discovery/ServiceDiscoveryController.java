/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;

import java.util.ArrayList;
import java.util.List;

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
public class ServiceDiscoveryController extends AbstractController {

	private static final Log logger = LogFactory.getLog(ServiceDiscoveryController.class);
	
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

		String keyword = request.getParameter("keyword");
		logger.debug("keyword = " + keyword);
		
		//Just get them all
		List<GridService> gridServices = getGridServiceDao().getAll();
		List<Integer> gridServiceIds = new ArrayList<Integer>();
		for(GridService svc : gridServices){
			gridServiceIds.add(svc.getId());
		}
		
		PortletSession portletSession = request.getPortletSession(true);
		String id = getInstanceID(request);
		String msgSessionId = MessageHelper.getSessionID(request);
		MessageHelper.loadPrefs(request, id, msgSessionId);
		MessageHelper helper = new MessageHelper(portletSession, id,
				msgSessionId);
		logger.debug("############## Sending list of " + gridServiceIds.size() + " gridServiceIds. #############");
		helper.send("gridServiceIds", gridServiceIds);
		
		logger.debug("setting action to " + getSuccessAction());
		response.setRenderParameter("action", getSuccessAction());
	}

	public String getInstanceID(PortletRequest request) {
		return "DiscoverGridServices" + MessageHelper.getPortletID(request);
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}
	
}
