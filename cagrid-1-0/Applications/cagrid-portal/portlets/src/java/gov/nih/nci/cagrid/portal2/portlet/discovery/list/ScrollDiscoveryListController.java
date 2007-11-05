/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.list;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal2.portlet.util.ScrollCommand;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ScrollDiscoveryListController extends
		AbstractActionResponseHandlerCommandController {
	
	private String listBeanSessionAttributeName;

	/**
	 * 
	 */
	public ScrollDiscoveryListController() {

	}

	/**
	 * @param commandClass
	 */
	public ScrollDiscoveryListController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ScrollDiscoveryListController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		ScrollCommand command = (ScrollCommand)obj;
		ListBean bean = (ListBean)request.getPortletSession().getAttribute(getListBeanSessionAttributeName());
		bean.getScroller().scroll(command);
	}

	public String getListBeanSessionAttributeName() {
		return listBeanSessionAttributeName;
	}

	public void setListBeanSessionAttributeName(String listBeanSessionAttributeName) {
		this.listBeanSessionAttributeName = listBeanSessionAttributeName;
	}

}
