/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractQueryRenderController extends
		AbstractViewObjectController {
	
	private QueryModel queryModel;

	/**
	 * 
	 */
	public AbstractQueryRenderController() {

	}


	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
