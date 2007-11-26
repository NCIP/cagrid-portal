/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.map.SelectItemsCommand;
import gov.nih.nci.cagrid.portal.portlet.map.SelectedItemsEditor;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectItemsController extends
		AbstractActionResponseHandlerCommandController {
	
	private GridServiceDao gridServiceDao;
	private ParticipantDao participantDao;
	private PersonDao personDao;
	private DiscoveryModel discoveryModel;

	/**
	 * 
	 */
	public SelectItemsController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectItemsController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectItemsController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectItemsCommand command = (SelectItemsCommand) obj;
		
		logger.debug("selected ids size: " + command.getSelectedIds().size());
		
		List<DomainObject> objects = new ArrayList<DomainObject>();
		
		if (DiscoveryType.SERVICE.equals(command.getType())) {
			for(Integer id : command.getSelectedIds()){
				objects.add(getGridServiceDao().getById(id));
			}
		} else if (DiscoveryType.PARTICIPANT.equals(command.getType())) {
			for(Integer id : command.getSelectedIds()){
				objects.add(getParticipantDao().getById(id));
			}
		} else if (DiscoveryType.POC.equals(command.getType())) {
			for(Integer id : command.getSelectedIds()){
				objects.add(getPersonDao().getById(id));
			}
		} else {
			throw new Exception("Invalid discovery type: " + command.getType());
		}
		
		logger.debug("objects.siz() = " + objects.size());
		DiscoveryResults results = new DiscoveryResults();
		results.setObjects(objects);
		results.setType(command.getType());
		
		logger.debug("results.id = " + results.getId());
		getDiscoveryModel().getResults().add(results);
		getDiscoveryModel().selectResults(results.getId());
		
	}
	
	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(List.class, "selectedIds", new SelectedItemsEditor());
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

	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

}
