/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.participant;

import gov.nih.nci.cagrid.portal2.dao.PersonDao;
import gov.nih.nci.cagrid.portal2.domain.Person;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.util.Assert;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ParticipantDeleteController extends AbstractController {
	private PersonDao personDao;

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		Integer id = new Integer(request.getParameter("participant"));
		Person person = getPersonDao().getById(id);
		getPersonDao().delete(person);
		response.setRenderParameter("action", "listParticipants");
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.personDao, "The personDao property is required.");
	}

}
