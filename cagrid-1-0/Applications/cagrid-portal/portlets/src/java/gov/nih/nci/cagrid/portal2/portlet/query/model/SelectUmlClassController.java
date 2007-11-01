/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.model;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

import gov.nih.nci.cagrid.portal2.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal2.portlet.query.QueryModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectUmlClassController extends
		AbstractQueryActionController {

	private UMLClassDao umlClassDao;
	
	
	/**
	 * 
	 */
	public SelectUmlClassController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectUmlClassController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectUmlClassController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectUmlClassCommand command = (SelectUmlClassCommand)obj;
		UMLClass umlClass = getUmlClassDao().getById(command.getUmlClassId());
		getQueryModel().setSelectedUmlClass(umlClass);
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

	

}
