/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import javax.portlet.RenderRequest;

import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewSharedQueriesSearchResultsController extends
		AbstractQueryRenderController {
	
	private String listBeanSessionAttributeName;

	/**
	 * 
	 */
	public ViewSharedQueriesSearchResultsController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		return request.getPortletSession().getAttribute(getListBeanSessionAttributeName());
	}

	public String getListBeanSessionAttributeName() {
		return listBeanSessionAttributeName;
	}

	public void setListBeanSessionAttributeName(String listBeanSessionAttributeName) {
		this.listBeanSessionAttributeName = listBeanSessionAttributeName;
	}

}
