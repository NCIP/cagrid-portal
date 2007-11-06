/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.dao.PersonDao;
import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.domain.Person;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewPocDetailsController extends
		AbstractDiscoveryViewObjectController {
	
	private PersonDao personDao;	

	/**
	 * 
	 */
	public ViewPocDetailsController() {

	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.discovery.details.AbstractViewDetailsController#doHandle(javax.portlet.RenderRequest, javax.portlet.RenderResponse, org.springframework.web.portlet.ModelAndView)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		Person p = getDiscoveryModel().getSelectedPointOfContact();
		if(p != null){
			p = getPersonDao().getById(p.getId());
		}
		return p;
	}
	
	@Required
	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	

}
