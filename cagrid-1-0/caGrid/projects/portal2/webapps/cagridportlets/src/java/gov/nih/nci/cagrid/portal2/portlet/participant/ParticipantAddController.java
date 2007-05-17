/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.participant;

import gov.nih.nci.cagrid.portal2.dao.PersonDao;
import gov.nih.nci.cagrid.portal2.domain.Person;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractWizardFormController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ParticipantAddController extends AbstractWizardFormController {

	private PersonDao personDao;

	protected void processFinish(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		getPersonDao().save((Person) command);
		response.setRenderParameter("action", "listParticipants");
	}

	protected void processCancel(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		response.setRenderParameter("action", "listPartipants");
	}

	protected void validatePage(Object command, Errors errors, int page,
			boolean finish) {
		if (finish) {
			getValidator().validate(command, errors);
		}
		Person person = (Person) command;
		ParticipantValidator personValidator = (ParticipantValidator) getValidator();
		switch (page) {
		case 0:
			personValidator.validateName(person, errors);
			break;
		case 1:
			personValidator.validateContactInfo(person, errors);
			break;
		}
	}

	protected ModelAndView renderInvalidSubmit(RenderRequest request,
			RenderResponse response) throws Exception {
		return null;
	}

	protected void handleInvalidSubmit(ActionRequest request,
			ActionResponse response) throws Exception {
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
