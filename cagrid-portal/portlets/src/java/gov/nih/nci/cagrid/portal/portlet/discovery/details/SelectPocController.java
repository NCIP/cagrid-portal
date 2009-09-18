/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectPocController extends AbstractDiscoverySelectDetailsController {

	private PersonDao personDao;
	
	/**
	 * 
	 */
	public SelectPocController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectPocController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectPocController(Class commandClass, String commandName) {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.discovery.details.AbstractSelectDetailsController#doSelect(gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel, java.lang.Integer)
	 */
	@Override
	protected void doSelect(DiscoveryModel model, Integer selectedId) {
		model.setSelectedPointOfContact(getPersonDao().getById(selectedId));

	}

	@Required
	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

}
