/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractQueryActionController extends
		AbstractActionResponseHandlerCommandController {
	
	private QueryModel queryModel;

	/**
	 * 
	 */
	public AbstractQueryActionController() {

	}

	/**
	 * @param commandClass
	 */
	public AbstractQueryActionController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public AbstractQueryActionController(Class commandClass,
			String commandName) {
		super(commandClass, commandName);

	}
	
	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
