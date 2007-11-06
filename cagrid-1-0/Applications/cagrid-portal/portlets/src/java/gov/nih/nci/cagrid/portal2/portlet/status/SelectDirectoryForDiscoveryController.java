/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.status;

import gov.nih.nci.cagrid.portal2.portlet.InterPortletMessageManager;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.AbstractDirectoryBean;

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
public class SelectDirectoryForDiscoveryController extends
		AbstractCommandController {
	
	private InterPortletMessageManager interPortletMessageManager;
	private String redirectUrl;

	/**
	 * 
	 */
	public SelectDirectoryForDiscoveryController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectDirectoryForDiscoveryController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectDirectoryForDiscoveryController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		AbstractDirectoryBean dirBean = (AbstractDirectoryBean)obj;
		
		String outputQueueName = request.getPreferences().getValue("selectedDiscoveryDirectoryOutputQueueName", "selectedDiscoveryDirectory");
		getInterPortletMessageManager().send(request, outputQueueName, dirBean);
		
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

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
