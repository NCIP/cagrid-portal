/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewPocDetailsController extends
		AbstractDiscoveryViewObjectController {
	
	private ParticipantDao participantDao;

	/**
	 * 
	 */
	public ViewPocDetailsController() {

	}

	@Override
	protected Object getObject(RenderRequest request) {
		Participant p = getDiscoveryModel().getSelectedParticipant();
		if(p != null){
			p = getParticipantDao().getById(p.getId());
		}
		return p;
	}

	@Required
	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

}
