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
package gov.nih.nci.cagrid.portal.portlet.discovery.search;

import gov.nih.nci.cagrid.portal.portlet.ActionResponseHandler;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.mvc.SimpleFormController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class KeywordSearchController extends SimpleFormController {

	private KeywordSearchService keywordSearchService;
	private ActionResponseHandler actionResponseHandler;
	private DiscoveryModel discoveryModel;
	
	/**
	 * 
	 */
	public KeywordSearchController() {

	}
	
	protected void onSubmitAction(ActionRequest request,
			ActionResponse response, Object command, BindException errors)
			throws Exception {
		
		DiscoveryResults results = getKeywordSearchService().search((KeywordSearchBean)command);
		getDiscoveryModel().getResults().add(results);
		getDiscoveryModel().selectResults(results.getId());
		
		getActionResponseHandler().handle(request, response);
	}

	@Required
	public KeywordSearchService getKeywordSearchService() {
		return keywordSearchService;
	}

	public void setKeywordSearchService(KeywordSearchService keywordSearchService) {
		this.keywordSearchService = keywordSearchService;
	}

	@Required
	public ActionResponseHandler getActionResponseHandler() {
		return actionResponseHandler;
	}

	public void setActionResponseHandler(ActionResponseHandler actionResponseHandler) {
		this.actionResponseHandler = actionResponseHandler;
	}

	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

}
