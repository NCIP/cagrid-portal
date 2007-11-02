/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.results;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;

import gov.nih.nci.cagrid.portal2.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal2.portlet.query.cql.CQLQueryService;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewQueryHistoryController extends AbstractQueryRenderController {

	private CQLQueryService cqlQueryService;
	
	/**
	 * 
	 */
	public ViewQueryHistoryController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		return getCqlQueryService().getSubmittedCqlQueries();
	}

	@Required
	public CQLQueryService getCqlQueryService() {
		return cqlQueryService;
	}

	public void setCqlQueryService(CQLQueryService cqlQueryService) {
		this.cqlQueryService = cqlQueryService;
	}

}
