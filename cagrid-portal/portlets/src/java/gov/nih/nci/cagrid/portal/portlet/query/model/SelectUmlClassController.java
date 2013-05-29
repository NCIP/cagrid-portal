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
package gov.nih.nci.cagrid.portal.portlet.query.model;

import gov.nih.nci.cagrid.portal.dao.UMLClassDao;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

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
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void doHandleAction(ActionRequest request,
			ActionResponse response, Object obj, BindException errors)
			throws Exception {
		SelectUmlClassCommand command = (SelectUmlClassCommand)obj;
		UMLClass umlClass = getUmlClassDao().getById(command.getUmlClassId());
		getUserModel().setSelectedUmlClass(umlClass);
	}

	public UMLClassDao getUmlClassDao() {
		return umlClassDao;
	}

	public void setUmlClassDao(UMLClassDao umlClassDao) {
		this.umlClassDao = umlClassDao;
	}

	

}
