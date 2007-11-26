/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

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
public class SelectItemsForDiscoveryController extends
		SelectItemForDiscoveryController {

	/**
	 * 
	 */
	public SelectItemsForDiscoveryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectItemsForDiscoveryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectItemsForDiscoveryController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}
	
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {

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
		
		handleSend(request, response, results);
	}
	
	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(List.class, "selectedIds", new SelectedItemsEditor());
	}

}
