/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.domain.Participant;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewParticipantDetailsController extends
		AbstractDiscoveryViewObjectController {
	
	private ParticipantDao participantDao;
	
	

	/**
	 * 
	 */
	public ViewParticipantDetailsController() {

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
