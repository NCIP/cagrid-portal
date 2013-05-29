/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.news;

import gov.nih.nci.cagrid.portal.portlet.util.ScrollCommand;

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
	
	private String successOperation;
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
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		ScrollCommand command = (ScrollCommand)obj;
		ItemsListBean itemsListBean = (ItemsListBean) request.getPortletSession().getAttribute(getSessionAttributeName());
		itemsListBean.getScroller().scroll(command);
		response.setRenderParameter("operation", getSuccessOperation());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView handleRender(RenderRequest request,
			RenderResponse response, Object obj, BindException errors)
			throws Exception {
		throw new IllegalArgumentException(getClass().getName() + " doesn't handle render requests.");
	}

	public String getSuccessOperation() {
		return successOperation;
	}

	public void setSuccessOperation(String successView) {
		this.successOperation = successView;
	}

	public String getSessionAttributeName() {
		return sessionAttributeName;
	}

	public void setSessionAttributeName(String sessionAttributeName) {
		this.sessionAttributeName = sessionAttributeName;
	}

}
