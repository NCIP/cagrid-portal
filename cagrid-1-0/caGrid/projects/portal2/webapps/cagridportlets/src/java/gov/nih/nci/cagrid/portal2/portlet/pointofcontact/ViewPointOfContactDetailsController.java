/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.pointofcontact;

import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;


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
public class ViewPointOfContactDetailsController extends AbstractController {
	
	private static final Log logger = LogFactory.getLog(ViewPointOfContactDetailsController.class);
	
	private PointOfContactDao pointOfContactDao;
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
		Integer pointOfContactId = (Integer) helper.get("selectedPointOfContactId");
		if(pointOfContactId != null){
			
			logger.debug("########### Found pointOfContactId = " + pointOfContactId + " ##########");
			
			/*
			 * NOTE: The object has to be fetched from Hibernate instead of
			 * the message box so that the Hibernate session is available so
			 * enable lazy loading of associated objects during the render
			 * phase.
			 */
			PointOfContact pointOfContact = getPointOfContactDao().getById(pointOfContactId);
			mav.addObject("pointOfContact", pointOfContact);
		}else{
			logger.debug("######### No selectPointOfContactsId found #########");
		}
		return mav;
	}
	
	public String getInstanceID(PortletRequest request)
    {
        return "ViewPointOfContactDetails" + MessageHelper.getPortletID(request);
    }

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

}
