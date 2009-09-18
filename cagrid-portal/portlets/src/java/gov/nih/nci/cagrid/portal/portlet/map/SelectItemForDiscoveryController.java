/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractCommandController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectItemForDiscoveryController extends AbstractCommandController {

	private InterPortletMessageSender interPortletMessageSender;
	private GridServiceDao gridServiceDao;
	private ParticipantDao participantDao;
	private PersonDao personDao;
	private String redirectUrlPreferenceName;

	/**
	 * 
	 */
	public SelectItemForDiscoveryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectItemForDiscoveryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectItemForDiscoveryController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {

		SelectItemCommand command = (SelectItemCommand) obj;
		Object item = null;
		if (DiscoveryType.SERVICE.equals(command.getType())) {
			item = getGridServiceDao().getById(command.getSelectedId());
		} else if (DiscoveryType.PARTICIPANT.equals(command.getType())) {
			item = getParticipantDao().getById(command.getSelectedId());
		} else if (DiscoveryType.POC.equals(command.getType())) {
			item = getPersonDao().getById(command.getSelectedId());
		} else {
			throw new Exception("Invalid discovery type: " + command.getType());
		}

		handleSend(request, response, item);
	}

	protected void handleSend(ActionRequest request, ActionResponse response,
			Object item) throws Exception {
		getInterPortletMessageSender().send(request, item);
		String redirectUrl = request.getPreferences().getValue(
				getRedirectUrlPreferenceName(), null);
		if (redirectUrl == null) {
			throw new Exception("No redirect URL preference provided.");
		}
		response.sendRedirect(redirectUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest arg0,
			RenderResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		throw new IllegalArgumentException(getClass().getName()
				+ " doesn't handle render requests.");
	}

	@Required
	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	@Required
	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	@Required
	public String getRedirectUrlPreferenceName() {
		return redirectUrlPreferenceName;
	}

	public void setRedirectUrlPreferenceName(String redirectUrl) {
		this.redirectUrlPreferenceName = redirectUrl;
	}

	@Required
	public InterPortletMessageSender getInterPortletMessageSender() {
		return interPortletMessageSender;
	}

	public void setInterPortletMessageSender(
			InterPortletMessageSender interPortletMessageSender) {
		this.interPortletMessageSender = interPortletMessageSender;
	}

	@Required
	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

}
