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
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ViewQueryHistoryController extends AbstractQueryRenderController {

	private PortalUserDao portalUserDao;

	/**
     *
     */
	public ViewQueryHistoryController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject
	 * (javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		return getQueryService().getSubmittedQueries();
	}

	@Required
	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

}
