/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.map.SelectItemCommand;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectItemController extends
		AbstractActionResponseHandlerCommandController {

	private DiscoveryModel discoveryModel;
	private GridServiceDao gridServiceDao;
	private ParticipantDao participantDao;
	private PersonDao personDao;

	/**
	 * 
	 */
	public SelectItemController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param commandClass
	 */
	public SelectItemController(Class commandClass) {
		super(commandClass);
	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectItemController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectItemCommand command = (SelectItemCommand) obj;
		Object item = null;
		if (DiscoveryType.SERVICE.equals(command.getType())) {
			item = getGridServiceDao().getById(command.getSelectedId());
			getDiscoveryModel().setSelectedService((GridService) item);
		} else if (DiscoveryType.PARTICIPANT.equals(command.getType())) {
			item = getParticipantDao().getById(command.getSelectedId());
			getDiscoveryModel().setSelectedParticipant((Participant) item);
		} else if (DiscoveryType.POC.equals(command.getType())) {
			item = getPersonDao().getById(command.getSelectedId());
			getDiscoveryModel().setSelectedPointOfContact((Person) item);
		} else {
			throw new Exception("Invalid discovery type: " + command.getType());
		}
	}

	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

}
