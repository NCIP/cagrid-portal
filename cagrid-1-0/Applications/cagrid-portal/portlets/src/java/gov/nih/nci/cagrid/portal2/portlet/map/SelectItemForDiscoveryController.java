/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.dao.PointOfContactDao;
import gov.nih.nci.cagrid.portal2.portlet.InterPortletMessageManager;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryType;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractCommandController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectItemForDiscoveryController extends AbstractCommandController {
	
	private InterPortletMessageManager interPortletMessageManager;
	private GridServiceDao gridServiceDao;
	private ParticipantDao participantDao;
	private PointOfContactDao pointOfContactDao;
	private String redirectUrl;

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
	public SelectItemForDiscoveryController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		
		SelectItemCommand command = (SelectItemCommand)obj;
		Object item = null;
		if(DiscoveryType.SERVICE.equals(command.getType())){
			item = getGridServiceDao().getById(command.getSelectedId());
		}else if(DiscoveryType.PARTICIPANT.equals(command.getType())){
			item = getParticipantDao().getById(command.getSelectedId());
		}else if(DiscoveryType.POC.equals(command.getType())){
			item = getPointOfContactDao().getById(command.getSelectedId());
		}else{
			throw new Exception("Invalid discovery type: "+ command.getType());
		}
		
		String outputQueueName = request.getPreferences().getValue("selectedDiscoveryItemOutputQueueName", "selectedDiscoveryItem");
		getInterPortletMessageManager().send(request, outputQueueName, item);
		
		response.sendRedirect(getRedirectUrl());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest arg0,
			RenderResponse arg1, Object arg2, BindException arg3)
			throws Exception {
		throw new IllegalArgumentException(getClass().getName() + " doesn't handle render requests.");
	}

	public InterPortletMessageManager getInterPortletMessageManager() {
		return interPortletMessageManager;
	}

	public void setInterPortletMessageManager(
			InterPortletMessageManager interPortletMessageManager) {
		this.interPortletMessageManager = interPortletMessageManager;
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

	public PointOfContactDao getPointOfContactDao() {
		return pointOfContactDao;
	}

	public void setPointOfContactDao(PointOfContactDao pointOfContactDao) {
		this.pointOfContactDao = pointOfContactDao;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
