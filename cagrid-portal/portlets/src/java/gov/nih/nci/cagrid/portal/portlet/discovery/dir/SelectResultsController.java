/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectResultsController extends SelectDirectoryController {

	/**
	 * 
	 */
	public SelectResultsController() {

	}

	/**
	 * @param commandClass
	 */
	public SelectResultsController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public SelectResultsController(Class commandClass, String commandName) {
		super(commandClass, commandName);

	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected void handleAction(ActionRequest request, ActionResponse response,
			Object obj, BindException errors) throws Exception {
		AbstractDirectoryBean dirBean = (AbstractDirectoryBean)obj;
		String resultsId = dirBean.getSelectedResults();
		if(!"----".equals(resultsId)){
			getDiscoveryModel().selectResults(dirBean.getSelectedResults());
		}
		getActionResponseHandler().handle(request, response);
	}

}
