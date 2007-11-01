/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import org.springframework.beans.factory.annotation.Required;

import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectPocController extends AbstractDiscoverySelectDetailsController {

	private PointOfContactDao pointOfContactDao;
	
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

		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.discovery.details.AbstractSelectDetailsController#doSelect(gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryModel, java.lang.Integer)
	 */
	@Override
	protected void doSelect(DiscoveryModel model, Integer selectedId) {
		model.setSelectedPointOfContact(getPointOfContactDao().getById(selectedId));

	}

	@Required
	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

}
