/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import org.springframework.beans.factory.annotation.Required;

import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectParticipantController extends
		AbstractDiscoverySelectDetailsController {
	
	private ParticipantDao participantDao;

	/**
	 * 
	 */
	public SelectParticipantController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectParticipantController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectParticipantController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.discovery.details.AbstractSelectDetailsController#doSelect(gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel, java.lang.Integer)
	 */
	@Override
	protected void doSelect(DiscoveryModel model, Integer selectedId) {
		model.setSelectedParticipant(getParticipantDao().getById(selectedId));
	}

	@Required
	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

}
