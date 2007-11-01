/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewParticipantDetailsController extends
		AbstractDiscoveryViewObjectController {
	
	private PointOfContactDao pointOfContactDao;

	/**
	 * 
	 */
	public ViewParticipantDetailsController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.discovery.details.AbstractViewDetailsController#doHandle(javax.portlet.RenderRequest, javax.portlet.RenderResponse, org.springframework.web.portlet.ModelAndView)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		PointOfContact p = getDiscoveryModel().getSelectedPointOfContact();
		if(p != null){
			p = getPointOfContactDao().getById(p.getId());
		}
		return p;
	}
	
	@Required
	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

}
