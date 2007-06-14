/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.pointofcontact;

import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;


import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import message.MessageHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PointOfContactDiscoveryController extends AbstractController {

	private static final Log logger = LogFactory.getLog(PointOfContactDiscoveryController.class);
	
	private PointOfContactDao pointOfContactDao;
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
		List<PointOfContact> pointOfContacts = getPointOfContactDao().getAll();
		List<Integer> pointOfContactIds = new ArrayList<Integer>();
		for(PointOfContact poc : pointOfContacts){
			pointOfContactIds.add(poc.getId());
		}

		MessageHelper.loadPrefs(request);
		MessageHelper helper = new MessageHelper(request);
		logger.debug("############## Sending list of " + pointOfContactIds.size() + " pointOfContactIds. #############");
		helper.send("pointOfContactIds", pointOfContactIds);
		
		logger.debug("setting action to " + getSuccessAction());
		response.setRenderParameter("action", getSuccessAction());
	}


	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}
	
}
