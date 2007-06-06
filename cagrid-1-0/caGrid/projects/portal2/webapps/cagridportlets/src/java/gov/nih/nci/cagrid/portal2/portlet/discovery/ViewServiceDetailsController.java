/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridService;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
	
	private static final Log logger = LogFactory.getLog(ViewServiceDetailsController.class);
	
	private GridServiceDao gridServiceDao;
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
		Integer gridServiceId = (Integer) helper.get("selectedGridServiceId");
		if(gridServiceId != null){
			
			logger.debug("########### Found selectedGridServiceId = " + gridServiceId + " ##########");
			
			/*
			 * NOTE: The object has to be fetched from Hibernate instead of
			 * the message box so that the Hibernate session is available so
			 * enable lazy loading of associated objects during the render
			 * phase.
			 */
			GridService gridService = getGridServiceDao().getById(gridServiceId);
			mav.addObject("gridService", gridService);
		}else{
			logger.debug("######### No selectedGridServiceId found #########");
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

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
