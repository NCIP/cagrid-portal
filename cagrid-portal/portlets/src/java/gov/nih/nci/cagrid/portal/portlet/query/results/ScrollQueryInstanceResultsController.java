/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryInstanceResultsBean;
import gov.nih.nci.cagrid.portal.portlet.util.ScrollCommand;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ScrollQueryInstanceResultsController extends
		AbstractQueryActionController {

	private String resultsBeanSessionAttributeName;

	/**
	 * 
	 */
	public ScrollQueryInstanceResultsController() {

	}

	/**
	 * @param commandClass
	 */
	public ScrollQueryInstanceResultsController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public ScrollQueryInstanceResultsController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		ScrollCommand command = (ScrollCommand) obj;
		CQLQueryInstanceResultsBean resultsBean = (CQLQueryInstanceResultsBean) request
				.getPortletSession().getAttribute(
						getResultsBeanSessionAttributeName());
		if(resultsBean == null){
			logger.warn("No results bean found in session. Session is probably expired.");
		}else{
			resultsBean.getTableScroller().scroll(command);
		}
	}

	@Required
	public String getResultsBeanSessionAttributeName() {
		return resultsBeanSessionAttributeName;
	}

	public void setResultsBeanSessionAttributeName(
			String resultsBeanSessionAttributeName) {
		this.resultsBeanSessionAttributeName = resultsBeanSessionAttributeName;
	}

}
