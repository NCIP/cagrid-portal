/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.domain.Participant;

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
