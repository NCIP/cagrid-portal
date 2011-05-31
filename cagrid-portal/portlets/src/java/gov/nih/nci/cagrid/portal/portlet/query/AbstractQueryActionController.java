/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class AbstractQueryActionController extends
		AbstractActionResponseHandlerCommandController {

	private UserModel userModel;
	private QueryService queryService;

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
	public AbstractQueryActionController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public QueryService getQueryService() {
		return queryService;
	}

	public void setQueryService(QueryService queryService) {
		this.queryService = queryService;
	}

}
