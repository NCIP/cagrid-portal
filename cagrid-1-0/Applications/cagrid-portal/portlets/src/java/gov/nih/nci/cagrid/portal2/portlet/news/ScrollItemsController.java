/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.news;

import gov.nih.nci.cagrid.portal2.portlet.util.ScrollCommand;

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
public class ScrollItemsController extends AbstractCommandController {
	
	private String objectName;
	private String successView;
	private String sessionAttributeName;

	/**
	 * 
	 */
	public ScrollItemsController() {

	}

	/**
	 * @param commandClass
	 */
	public ScrollItemsController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ScrollItemsController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest arg0, ActionResponse arg1,
			Object arg2, BindException arg3) throws Exception {
		throw new IllegalArgumentException(getClass().getName() + " doesn't handle action requests.");

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest request,
			RenderResponse response, Object obj, BindException errors)
			throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		ScrollCommand command = (ScrollCommand)obj;
		ItemsListBean itemsListBean = (ItemsListBean) request.getPortletSession().getAttribute(getSessionAttributeName());
		itemsListBean.getScroller().scroll(command);
		mav.addObject(getObjectName());
		return mav;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public String getSessionAttributeName() {
		return sessionAttributeName;
	}

	public void setSessionAttributeName(String sessionAttributeName) {
		this.sessionAttributeName = sessionAttributeName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

}
