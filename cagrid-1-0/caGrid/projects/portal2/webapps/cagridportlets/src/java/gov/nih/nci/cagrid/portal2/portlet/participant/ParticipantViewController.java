/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.participant;

import gov.nih.nci.cagrid.portal2.dao.PersonDao;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ParticipantViewController extends AbstractController implements InitializingBean {
	
	private PersonDao personDao;
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response)
	throws Exception{
		Integer id = new Integer(request.getParameter("participant"));
		return new ModelAndView("participantView", "participant", getPersonDao().getById(id));
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
