/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.domain.Participant;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewPocDetailsController extends
		AbstractViewDetailsController {
	
	private ParticipantDao participantDao;
	private String requestAttributeName = "participant";

	/**
	 * 
	 */
	public ViewPocDetailsController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.discovery.details.AbstractViewDetailsController#doHandle(javax.portlet.RenderRequest, javax.portlet.RenderResponse, org.springframework.web.portlet.ModelAndView)
	 */
	@Override
	protected void doHandle(RenderRequest request, RenderResponse reponse,
			ModelAndView mav) {
		Participant p = getDiscoveryModel().getSelectedParticipant();
		if(p != null){
			mav.addObject(getRequestAttributeName(), getParticipantDao().getById(p.getId()));
		}
	}

	@Required
	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public String getRequestAttributeName() {
		return requestAttributeName;
	}

	public void setRequestAttributeName(String requestAttributeName) {
		this.requestAttributeName = requestAttributeName;
	}

}
